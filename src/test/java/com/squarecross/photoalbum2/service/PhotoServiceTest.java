package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.dto.PhotoDto;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoServiceTest {

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    PhotoService photoService;

    @Test
    void getPhoto() {
        Photo photo = new Photo();
        photo.setPhotoId(1L);
        Photo savedPhoto = photoRepository.save(photo);

        PhotoDto resPhoto = photoService.getPhoto(savedPhoto.getPhotoId());
        assertEquals(1L, resPhoto.getPhotoId());

    }
}