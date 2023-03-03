package com.squarecross.photoalbum2.controller;


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

    //사진 업로드 API
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

    @GetMapping("/download")
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse response){
        try {
            if (photoIds.length == 1){
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();
            } else {
                // 새 임시 zip 파일 생성
                File zipFile = File.createTempFile("photos", ".zip");

                // zip 파일에 쓸 ZipOutputStream을 생성합니다.
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

                // 각 사진 ID를 반복하여 zip 파일에 추가합니다.
                for (Long photoId : photoIds) {
                    File photoFile = photoService.getImageFile(photoId);

                    // zip 파일에 새 항목 만들기
                    zipOut.putNextEntry(new ZipEntry(photoFile.getName()));

                    // 사진 파일을 zip 출력 스트림에 쓰기
                    IOUtils.copy(new FileInputStream(photoFile), zipOut);

                    // 현재 항목 닫기
                    zipOut.closeEntry();
                }

                // zip output stream 닫기
                zipOut.close();

                // zip 파일이 반환되고 있음을 나타내도록 응답 헤더를 설정합니다.
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"photos.zip\"");

                // 응답 출력 스트림에 zip 파일 쓰기
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(zipFile), outputStream);
                outputStream.close();

                // 임시 zip file을 삭제
                zipFile.delete();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}