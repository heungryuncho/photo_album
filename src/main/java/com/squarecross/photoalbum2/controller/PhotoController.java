package com.squarecross.photoalbum2.controller;


import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.dto.PhotoDto;
import com.squarecross.photoalbum2.service.PhotoService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired private PhotoService photoService;

    // 사진 상세정보 API
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable("photoId") final Long photoId) {
        PhotoDto photoDto = photoService.getPhoto(photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }

    // 사진 업로드 API
    @PostMapping("")
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable("albumId") final Long albumId,
                                                       @RequestParam("photos") MultipartFile[] files) throws IOException {
        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    // 사진 다운로드 API
    @GetMapping("/download")
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse response) {
        try {
            if (photoIds.length == 1) {
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();
            } else {
                // 여러 사진들을 zip파일로 묶어서 다운로드
                response.setContentType("application/zip"); // zip파일로 설정
                response.setHeader("Content-Dispositon", "attachment: filename=\"photos.zip\"");
                // "Content-Disposition" 헤더를 설정(컨텐츠의 성향)하여 다운로드할 파일 이름을 "photos.zip"으로 설정
                ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

                for (Long photoId : photoIds) {
                    File file = photoService.getImageFile(photoId);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(zipEntry);

                    FileInputStream fileInputStream = new FileInputStream(file);
                    IOUtils.copy(fileInputStream, zipOutputStream);
                    fileInputStream.close();
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.finish();
                zipOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 사진 목록 불러오기 API
    @GetMapping("")
    public ResponseEntity<List<PhotoDto>> getPhotoList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "byDate") final String sort) {
        List<PhotoDto> PhotoDtos = photoService.getPhotoList(keyword, sort);
        return new ResponseEntity<>(PhotoDtos, HttpStatus.OK);
    }


    // 사진 옮기기 API

    // 사진 삭제 API

}