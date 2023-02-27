package com.squarecross.photoalbum2.service;

import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.domain.Photo;
import com.squarecross.photoalbum2.dto.AlbumDto;
import com.squarecross.photoalbum2.repository.AlbumRepository;
import com.squarecross.photoalbum2.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired AlbumRepository albumRepository;
    @Autowired AlbumService albumService;
    @Autowired PhotoRepository photoRepository;

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        assertEquals("테스트", resAlbum.getAlbumName());
    }

    @Test
    void getAlbumName() {
        String albumName = "테스트 앨범";
        Album album = new Album();
        album.setAlbumName(albumName);
        Album savedAlbumName = albumRepository.save(album);

        AlbumDto resAlbumName = albumService.getAlbumName(savedAlbumName.getAlbumName());
        assertEquals("테스트 앨범", resAlbumName.getAlbumName());
    }

    @Test
    void getAlbumException() {
        Long albumId = 1234L;
        Album album = new Album();
        album.setAlbumName("예외 테스트");
        album.setAlbumId(albumId);
        albumRepository.save(album);

        Optional<Album> optionalAlbum = albumRepository.findById(albumId);

        try {
            optionalAlbum.orElseThrow(NoSuchElementException::new);
            fail("Expected NoSuchElementException was not thrown.");
        } catch (NoSuchElementException e) {
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


    @Test
    public void testDeleteAlbum() throws IOException {
        String folder = "C:\\Temp\\testAlbum";
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        File.createTempFile("temp", ".txt", file);
        File.createTempFile("temp", ".jpg", file);

        Album album = new Album();
        albumRepository.delete(album);

        assertTrue(file.exists());
    }



}
