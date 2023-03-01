package com.squarecross.photoalbum2.controller;

import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired AlbumService albumService;


    // 앨범 불러오기 (하나)
    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") final long albumId) {
        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/query")
    public String getAlbumByQuery(@RequestParam(value = "albumId") Long albumId){
        return "albumId: " + albumId  ;
    }

   @PostMapping("/json_body")
    public ResponseEntity<AlbumDto> getAlbumByJSON(@RequestBody Map<String, Long> jsonbody) {
        Long albumId = jsonbody.get("albumId");
        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    // 앨범 만들기
    @PostMapping("")
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }

    // 앨범 리스트 불러오기
    @GetMapping("")
    public ResponseEntity<List<AlbumDto>> getAlbumList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "byDate") final String sort) {
        List<AlbumDto> albumDtos = albumService.getAlbumList(keyword, sort);
        return new ResponseEntity<>(albumDtos, HttpStatus.OK);
    }
    // required=false는 필수값은 아니라는 의미

    // 앨범명 변경하기
    @PutMapping("/{albumId}")
    public ResponseEntity<AlbumDto> updateAlbum(@PathVariable("albumId") final long albumId,
                                                @RequestBody final AlbumDto albumDto){
        AlbumDto res = albumService.changeName(albumId, albumDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // 앨범 삭제하기
    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable("albumId") final Long albumId) throws IOException {
        albumService.deleteAlbum(albumId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
