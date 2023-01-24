package com.squarecross.photoalbum2.repository;

import com.squarecross.photoalbum2.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByAlbumName(String name);
}
