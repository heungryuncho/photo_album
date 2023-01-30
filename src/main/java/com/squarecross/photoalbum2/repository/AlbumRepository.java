package com.squarecross.photoalbum2.repository;

import com.squarecross.photoalbum2.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByAlbumName(String name);

    Optional<Album> findByAlbumId(String albumId);
}
