package com.squarecross.photoalbum2.mapper;

import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.dto.AlbumDto;

public class AlbumMapper {
    public static AlbumDto convertToDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(album.getAlbumId());
        albumDto.setAlbumName(album.getAlbumName());
        albumDto.setCreatedAt(album.getCreatedAt());
        return albumDto;
    }
}
