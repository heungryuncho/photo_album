package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.dto.PhotoDto;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    public PhotoDto getPhoto(Long PhotoId) {
        return null;
    }
}
