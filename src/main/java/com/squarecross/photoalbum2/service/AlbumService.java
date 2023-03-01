package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.Constants;
import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.mapper.AlbumMapper;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Service
public class AlbumService {

    @Autowired private AlbumRepository albumRepository;
    @Autowired private PhotoRepository photoRepository;

    // Album 정보 조회
    public AlbumDto getAlbum(Long albumId){
        Optional<Album> res = albumRepository.findById(albumId);
        if (res.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
    }

    // Album 이름으로 조회
    public AlbumDto getAlbumName(String albumName) {
        Optional<Album> res2 = albumRepository.findByAlbumName(albumName);
        if (res2.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res2.get());
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 이름 %d으로 조회되지 않았습니다", albumName));
        }
    }


    // 앨범 만들기
    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        this.albumRepository.save(album);
        this.createAlbumDirectories(album);
        return AlbumMapper.convertToDto(album);
    }

    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    // 앨범 목록 불러오기
    public List<AlbumDto> getAlbumList(String keyword, String sort) {
        List<Album> albums;
        if (Objects.equals(sort, "byName")){
            albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
        } else if (Objects.equals(sort, "byDate")) {
            albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword);
        } else if (Objects.equals(sort, "byNameDesc")) {
            albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameDesc(keyword);
        } else if (Objects.equals(sort, "byDateAsc")) {
            albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtAsc(keyword);
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다");
        }

        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);

        for (AlbumDto albumDto : albumDtos) {
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream()
                    .map(Photo::getThumbUrl)
                    .map(c -> Constants.PATH_PREFIX + c)
                    .collect(Collectors.toList()));
        }
        return albumDtos;

    }

    // 앨범명 바꾸기
    public AlbumDto changeName(Long albumId, AlbumDto albumDto){
        Optional<Album> album = this.albumRepository.findById(albumId);
        if (album.isEmpty()){
            throw new NoSuchElementException(String.format("Album ID '%d'가 존재하지 않습니다", albumId));
        }

        Album updateAlbum = album.get();
        updateAlbum.setAlbumName(albumDto.getAlbumName());
        Album savedAlbum = this.albumRepository.save(updateAlbum);
        return AlbumMapper.convertToDto(savedAlbum);
    }


    // 앨범 삭제
    public void deleteAlbum(Long albumId) throws IOException {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new NoSuchElementException(String.format("Album Id '%d'가 존재하지 않습니다", albumId));
        }
        Album deleteAlbum = album.get();
        List<Photo> deletePhotos = photoRepository.findByAlbum_AlbumId(deleteAlbum.getAlbumId());

        for (Photo photo : deletePhotos) {
            deletePhoto(photo);
        }
        deleteAlbumDirectories(deleteAlbum);

        albumRepository.deleteById(deleteAlbum.getAlbumId());
    }

    public void deletePhoto(Photo photo) throws IOException{
        Files.deleteIfExists(Paths.get(Constants.PATH_PREFIX+photo.getThumbUrl()));
        Files.deleteIfExists(Paths.get(Constants.PATH_PREFIX+photo.getOriginalUrl()));
    }
    public void deleteAlbumDirectories(Album album) throws IOException {
        FileUtils.cleanDirectory(new File(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        FileUtils.cleanDirectory(new File(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

}
