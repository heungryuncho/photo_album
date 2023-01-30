package com.squarecross.photoalbum2.controller;

import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumRepository albumRepository;


    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId")final Long albumId){
        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/query")
    public String getAlbumByQuery(
            @RequestParam(value = "albumId") Long albumId){
        AlbumDto album = albumService.getAlbum((albumId));
        return "albumId: " + albumId  ;
    }


    // 이거 맞나 질문하기
    @PostMapping("/json_body")
    public int getAlbumByJSON(@RequestBody Map<String, Integer> body) {
        int album = body.get("albumId");
        return album;
    }

    @PostMapping("")
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }

}
