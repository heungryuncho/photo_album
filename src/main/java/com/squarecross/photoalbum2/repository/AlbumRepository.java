package com.squarecross.photoalbum2.repository;

import com.squarecross.photoalbum2.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByAlbumName(String name);


    List<Album> findByAlbumNameContainingOrderByCreatedAtDesc(String keyword); // 앨범생성날짜 최신순
    List<Album> findByAlbumNameContainingOrderByAlbumNameAsc(String keyword); // 앨범명으로 오름차순 검색

    List<Album> findByAlbumNameContainingOrderByCreatedAtAsc(String keyword); // 앨범생성날짜 오름차순
    List<Album> findByAlbumNameContainingOrderByAlbumNameDesc(String keyword); // 앨범명으로 최신순 검색


}
