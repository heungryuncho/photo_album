package com.squarecross.photoalbum2.repository;

import com.squarecross.photoalbum2.domain.Album;
import com.squarecross.photoalbum2.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    int countByAlbum_AlbumId(Long AlbumId);

    List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(Long AlbumId); // 최신 4장 이미지를 가져옴

    Optional<Photo> findByFileNameAndAlbum_AlbumId(String photoName, Long albumId);

    List<Photo> findByAlbum_AlbumId(Long albumId);

    List<Photo> findByPhotoNameContainingOrderByCreatedAtDesc(String keyword); // 앨범생성날짜 최신순
    List<Photo> findByPhotoNameContainingOrderByAlbumNameAsc(String keyword); // 앨범명으로 오름차순 검색

    List<Photo> findByPhotoNameContainingOrderByCreatedAtAsc(String keyword); // 앨범생성날짜 오름차순
    List<Photo> findByPhotoNameContainingOrderByAlbumNameDesc(String keyword); // 앨범명으로 최신순 검색

}
