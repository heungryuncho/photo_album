package com.squarecross.photoalbum2.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="album", schema="photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "album_id")}) // 반복되면 안되는 제약조건
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id를 +1씩 하며 생성
    @Column(name = "album_id", unique = true, nullable = false)
    private Long albumId;

    @Column(name = "album_name", unique = false, nullable = false)
    private String albumName;

    @Column(name = "created_at", unique = false, nullable = true)
    @CreationTimestamp // DB에 입력될 때 자동으로 현재시간 입력
    private Date createdAt;

    public Album() {}

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album", cascade = CascadeType.ALL)
    private List<Photo> photos;

}
