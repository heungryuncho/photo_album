package com.squarecross.photoalbum2.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "photo", schema = "photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "photo_id")})
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private long photoId;

    @Column(name = "file_name", unique = false, nullable = true)
    private String fileName;

    @Column(name = "thumb_url", unique = false, nullable = true)
    private String thumbUrl;

    @Column(name = "original_url", unique = false, nullable = true)
    private String originalUrl;

    @Column(name = "file_size", unique = false, nullable = true)
    private long fileSize;

    @Column(name = "uploaded_at", unique = false, nullable = true)
    @CreationTimestamp // DB에 입력될 때 자동으로 현재시간 입력
    private Date uploadedAt;

    public Photo() {}

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩은 정보가 필요할 때만 불러옴
    @JoinColumn(name = "album_id")
    private Album album;

}
