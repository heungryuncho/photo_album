package com.squarecross.photoalbum2.mapper;

import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.dto.AlbumDto;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumMapper {
    public static AlbumDto convertToDto(Album album){
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumId(album.getAlbumId());
        albumDto.setAlbumName(album.getAlbumName());
        albumDto.setCreatedAt(album.getCreatedAt());
        return albumDto;
    }

    public static Album convertToModel(AlbumDto albumDto) {
        Album album = new Album();
        album.setAlbumId(albumDto.getAlbumId());
        album.setAlbumName(albumDto.getAlbumName());
        album.setCreatedAt(albumDto.getCreatedAt());
        return album;
    }

    public static List<AlbumDto> convertToDtoList(List<Album> albums) {
        return albums.stream()
                .map(AlbumMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
