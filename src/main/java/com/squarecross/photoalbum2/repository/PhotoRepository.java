package com.squarecross.photoalbum2.repository;

import com.squarecross.photoalbum2.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    int countByAlbum_AlbumId(Long AlbumId);
}
