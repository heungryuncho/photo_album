package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.mapper.AlbumMapper;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

@Service
public class AlbumService {
    @Autowired private AlbumRepository albumRepository;
    @Autowired private PhotoRepository photoRepository;

    public AlbumDto getAlbum(Long albumId){

        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isPresent()){
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다", albumId));
        }
    }

    public Album getAlbumName(String albumName){
        Optional<Album> res2 = albumRepository.findByAlbumName(albumName);
        if(res2.isPresent()){
            return res2.get();
        } else {
            throw new EntityNotFoundException(String.format("앨범 이름 %d으로 조회되지 않았습니다", albumName));
        }
    }


}
