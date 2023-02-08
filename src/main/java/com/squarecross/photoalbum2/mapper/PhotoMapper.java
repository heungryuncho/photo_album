package com.squarecross.photoalbum2.mapper;

import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.dto.PhotoDto;

public class PhotoMapper {
    public static PhotoDto convertToDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setPhotoId(photo.getPhotoId());
        photoDto.setFileName(photo.getFileName());
        photoDto.setOriginalUrl(photo.getOriginalUrl());
        photoDto.setThumbUrl(photo.getThumbUrl());
        photoDto.setUploadedAt(photo.getUploadedAt());
        photoDto.setAlbumId(photoDto.getAlbumId());

        return photoDto;
    }
}
