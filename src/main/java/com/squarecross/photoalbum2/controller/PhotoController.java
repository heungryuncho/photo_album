package com.squarecross.photoalbum2.controller;

import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    public ResponseEntity<Photo> getPhotoInfo() {
        return null;
    }
}
