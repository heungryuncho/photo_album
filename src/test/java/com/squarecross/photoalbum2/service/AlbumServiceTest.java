package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.mapper.AlbumMapper;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest

class AlbumServiceTest {

    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    AlbumService albumService;
    @Autowired
    PhotoRepository photoRepository;


    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        assertEquals("테스트", resAlbum.getAlbumName());
    }

    @Test
    void albumIdNotFind() {
        try {
            Album album = new Album();
            album.setAlbumId(-1L);
            Album savedAlbum = albumRepository.save(album);
            fail("An Exception was Expected");
        } catch (Exception e) {
            assertEquals(1, e.getMessage());
        }

    }

    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);
        //사진을 생성하고, setAlbum을 통해 앨범을 지정해준 이후, repository에 사진을 저장한다

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);
        Photo photo2 = new Photo();
        photo2.setFileName("사진2");
        photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);
        Photo photo3 = new Photo();
        photo3.setFileName("사진3");
        photo3.setAlbum(savedAlbum);
        photoRepository.save(photo3);
        // 가짜 사진 추가

        int expectedCount = 3;
        int actualCount = photoRepository.countByAlbum_AlbumId(savedAlbum.getAlbumId());
        assertThat(actualCount).isEqualTo(expectedCount);

        // 기대값 3, 실제값 3

    }


    @Test
    void testAlbumCreate() throws IOException {
        AlbumDto album = new AlbumDto();
        album.setAlbumName("테스트 앨범");

        AlbumDto createdAlbum = albumService.createAlbum(album);

        assertEquals(album.getAlbumName(), createdAlbum.getAlbumName());

    }

    @Test
    void makingFolderDelete() throws IOException {
        Path folderPath = Paths.get("테스트 폴더");
        Files.createDirectory(folderPath);

        Files.delete(folderPath);

        assertFalse(Files.exists(folderPath));
    }



    @Test
    @Transactional
    void testAlbumRepository() throws InterruptedException {
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaaa");
        album2.setAlbumName("aaab");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1); //시간차를 벌리기위해 두번째 앨범 생성 1초 딜레이
        albumRepository.save(album2);

        //최신순 정렬, 두번째로 생성한 앨범이 먼저 나와야합니다
        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        assertEquals("aaab", resDate.get(0).getAlbumName()); // 0번째 Index가 두번째 앨범명 aaab 인지 체크
        assertEquals("aaaa", resDate.get(1).getAlbumName()); // 1번째 Index가 첫번째 앨범명 aaaa 인지 체크
        assertEquals(2, resDate.size()); // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크

        //앨범명 정렬, aaaa -> aaab 기준으로 나와야합니다
        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        assertEquals("aaaa", resName.get(0).getAlbumName()); // 0번째 Index가 두번째 앨범명 aaaa 인지 체크
        assertEquals("aaab", resName.get(1).getAlbumName()); // 1번째 Index가 두번째 앨범명 aaab 인지 체크
        assertEquals(2, resName.size()); // aaa 이름을 가진 다른 앨범이 없다는 가정하에, 검색 키워드에 해당하는 앨범 필터링 체크
    }



    @Test
    @Transactional
    void testChangeAlbumName() throws IOException {
        //앨범 생성
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경전");
        AlbumDto res = albumService.createAlbum(albumDto);

        Long albumId = res.getAlbumId(); // 생성된 앨범 아이디 추출
        AlbumDto updateDto = new AlbumDto();
        updateDto.setAlbumName("변경후"); // 업데이트용 Dto 생성
        albumService.changeName(albumId, updateDto);

        AlbumDto updatedDto = albumService.getAlbum(albumId);

        //앨범명 변경되었는지 확인
        assertEquals("변경후", updatedDto.getAlbumName());
    }

    //삭제API 테스트는 조금 더 공부해보고 해보기
//    @Test
//    public void deleteAlbumTest() {
//        Long albumId = 1L;
//        AlbumDto albumDto = new AlbumDto();
//        albumDto.setAlbumId(1L);
//        albumDto.setAlbumName("Test Album");
//        Album album = new Album();
//        album.setAlbumId(albumId);
//        album.setAlbumName(albumDto.getAlbumName());
//
//        when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
//        doNothing().when(albumRepository).delete(album);
//
//        AlbumDto deletedAlbum = albumService.deleteAlbum(albumId, albumDto);
//        assertNotNull(deletedAlbum);
//        assertEquals(albumId, deletedAlbum.getAlbumId());
//        assertEquals("Test Album", deletedAlbum.getAlbumName());
//    }

}
