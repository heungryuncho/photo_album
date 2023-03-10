package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.Constants;
import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.dto.PhotoDto;
import com.squarecross.photoalbum2.mapper.AlbumMapper;
import com.squarecross.photoalbum2.mapper.PhotoMapper;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.mapping.Constraint;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhotoService {
    @Autowired private PhotoRepository photoRepository;
    @Autowired private AlbumRepository albumRepository;

    private final String original_path = Constants.PATH_PREFIX + "/photos/original";
    private final String thumb_path = Constants.PATH_PREFIX + "/photos/thumb";

    public PhotoDto getPhoto(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isPresent()){
            PhotoDto photoDto = PhotoMapper.convertToDto(res.get());
            return photoDto;
        } else {
            throw new EntityNotFoundException(String.format("사진 아이디 %d로 조회되지 않았습니다", photoId));
        }
    }

    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isEmpty()){
            throw new EntityNotFoundException("앨범이 존재하지 않습니다");
        }
        String fileName = file.getOriginalFilename(); // 파일 이름을 가져옴
        int fileSize = (int)file.getSize(); // int는 32바이트 -> int로 나타낼 수 있는 크기는 최대 2Gb

        // 확장자 확인
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif"}; // 허용 가능한 확장자 목록
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!Arrays.asList(allowedExtensions).contains(fileExtension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다.");
        }

        // 실제 이미지 파일인지 확인
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException("이미지 파일이 아닙니다.");
        }

        fileName = getNextFileName(fileName, albumId);
        saveFile(file, albumId, fileName);

        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName);
        photo.setFileSize(fileSize);
        photo.setAlbum(res.get());
        Photo createdPhoto = photoRepository.save(photo);
        return PhotoMapper.convertToDto(createdPhoto);
    }

    private String getNextFileName(String fileName, Long albumId){
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName); // 확장자 추출
        String ext = StringUtils.getFilenameExtension(fileName); // 추출한 확장자를 제거

        Optional<Photo> res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);

        int count = 2;
        while(res.isPresent()){
            fileName = String.format("%s (%d).%s", fileNameNoExt, count, ext);
            res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
            count++;
        }

        return fileName;
    }

    private void saveFile(MultipartFile file, Long AlbumId, String fileName) throws IOException {
        try {
            String filePath = AlbumId + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(original_path + "/" + filePath));

            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);

            File thumbFile = new File(thumb_path + "/" + filePath);
            String ext = StringUtils.getFilenameExtension(fileName);
            if (ext == null) {
                throw new IllegalArgumentException("No Extention");
            }
            ImageIO.write(thumbImg, ext, thumbFile);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    // 이미지(사진) 파일 확인
    public File getImageFile(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isEmpty()){
            throw new EntityNotFoundException(String.format("사진을 ID %d를 찾을 수 없습니다", photoId));
        }
        return new File(Constants.PATH_PREFIX + res.get().getOriginalUrl());
    }

    // 사진 목록 불러오기
    public List<PhotoDto> getPhotoList(String keyword, String sort) {
        List<Photo> photos;
        if (Objects.equals(sort, "byName")){
            photos = photoRepository.findByPhotoNameContainingOrderByAlbumNameAsc(keyword);
        } else if (Objects.equals(sort, "byDate")) {
            photos = photoRepository.findByPhotoNameContainingOrderByCreatedAtDesc(keyword);
        } else if (Objects.equals(sort, "byNameDesc")) {
            photos = photoRepository.findByPhotoNameContainingOrderByAlbumNameDesc(keyword);
        } else if (Objects.equals(sort, "byDateAsc")) {
            photos = photoRepository.findByPhotoNameContainingOrderByCreatedAtAsc(keyword);
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다");
        }

        List<PhotoDto> photoDtos = PhotoMapper.convertToDtoList(photos);

        for (PhotoDto photoDto : photoDtos) {
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(photoDto.getPhotoId());
            photoDto.setThumbUrls(top4.stream()
                    .map(Photo::getThumbUrl)
                    .map(c -> Constants.PATH_PREFIX + c)
                    .collect(Collectors.toList()));
        }
        return photoDtos;

    }

    // 사진 옮기기
    public PhotoDto movePhoto(Long photoId, Long albumId){
        Optional<Photo> photo = this.photoRepository.findById(photoId);
        if (photo.isEmpty()){
            throw new NoSuchElementException(String.format("Photo ID '%d'가 존재하지 않습니다", photoId));
        }

        Optional<Album> album = this.albumRepository.findById(albumId);
        if (album.isEmpty()){
            throw new NoSuchElementException(String.format("Album ID '%d'가 존재하지 않습니다", albumId));
        }

        Photo updatePhoto = photo.get();
        updatePhoto.setAlbum(album.get());
        Photo savedPhoto = this.photoRepository.save(updatePhoto);
        return PhotoMapper.convertToDto(savedPhoto);
    }

    // 사진 삭제
    public void deletePhoto(Long photoId) throws IOException {
        Optional<Photo> photo = photoRepository.findById(photoId);
        if (photo.isEmpty()) {
            throw new NoSuchElementException(String.format("사진 Id '%d'가 존재하지 않습니다", photoId));
        }
        Photo deletePhoto = photo.get();
        Files.deleteIfExists(Paths.get(Constants.PATH_PREFIX + deletePhoto.getThumbUrl()));
        Files.deleteIfExists(Paths.get(Constants.PATH_PREFIX + deletePhoto.getOriginalUrl()));
        // 사진과 연관된 썸네일 파일과 원본 파일을 삭제

        photoRepository.deleteById(deletePhoto.getPhotoId());

    }
}
