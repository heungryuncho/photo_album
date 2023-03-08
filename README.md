# :pushpin: webPhotoAlbum
>Rest API를 구현하여 백엔드 API를 완성했습니다. 기존의 평범한 게시판 API에서 벗어나,  
>디자인이 주어진 상황을 가정하여 더욱 개선된 API를 만들었습니다.  
>더 나은 사용자 경험을 제공하기 위해 새로운 디자인 요소를 추가하였습니다.  
>이 API를 사용하여 사용자들은 더욱 쉽게 앨범과 사진을 관리할 수 있게 되었습니다.

</br>

## 1. 제작 기간 & 참여 인원
- 2023년 1월 24일 ~ 2023년 2월 23일
- 개인 프로젝트

</br>

## 2. 사용 기술
#### `Back-end`
  - Java 11
  - Spring Boot 2.7.8
  - Gradle
  - Spring Data JPA
  - MySQL 8

</br>

## 3. ERD 설계
![photoAlbumERDjpeg](https://user-images.githubusercontent.com/74303992/220884695-ffc0523c-09a2-417c-b56d-dd821d32f5ee.jpeg)

</br>

## 3-1. 앨범 API 문서
![화면 캡처 2023-02-23 194737](https://user-images.githubusercontent.com/74303992/220887075-80ae60ba-345f-4a38-b67d-9a133df1e59c.jpg)

자세한 API 문서확인 (https://mulberry-willow-962.notion.site/API-5526bd2805394edf87b5b71a9c896d7c) 참고

</br>

## 4. 기능
 - 앨범: 생성, 삭제, 이름 변경, 목록 조회, 검색
 - 사진: 업로드, 상세정보 조회, 목록 조회, 다운로드, 삭제, 다른앨범으로 사진옮기기

</br>

## 5. 코드

<details>
<summary><b>Controller</b></summary>
<div markdown="1">
  - AlbumController (https://github.com/heungryuncho/photo_album/blob/eefcf11b55322de3fe172d9f0335d1b367d3136a/src/main/java/com/squarecross/photoalbum2/controller/AlbumController.java#L17)
  - PhotoController https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/controller/PhotoController.java#L24
</div>
</details>

<details>
<summary><b>Domain</b></summary>
<div markdown="1">
  - Album https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/domain/Album.java#L15
  - Photo https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/domain/Photo.java#L14
</div>
</details> 
  
<details>
<summary><b>Dto</b></summary>
<div markdown="1">
  - AlbumDto https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/dto/AlbumDto.java#L11
  - PhotoDto https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/dto/PhotoDto.java#L11
</div>
</details> 

<details>
<summary><b>Mapper</b></summary>
<div markdown="1">
  - AlbumMapper https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/mapper/AlbumMapper.java#L9
  - PhotoMapper https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/mapper/PhotoMapper.java#L11
</div>
</details> 

<details>
<summary><b>Repository</b></summary>
<div markdown="1">
  - AlbumRepository https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/repository/AlbumRepository.java#L12
  - PhotoRepository https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/repository/PhotoRepository.java#L13
</div>
</details> 

<details>
<summary><b>Service</b></summary>
<div markdown="1">
  - AlbumService https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/service/AlbumService.java#L29
  - PhotoService https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/service/PhotoService.java#L31
</div>
</details> 

</br>

## 6.트러블 슈팅
> 앨범엔티티를 지우면서 연관된 사진엔티티가 남아있어서 삭제가 진행이 안되어서 CascadeType.ALL을 추가 하여서 해결하였습니다.
> 테스트코드의 경우 만들어가는 과정에서 익숙치않아 간단한 것을 만드는데에도 초반에는 시간이 걸렸지만 구글링을 통해 해결하겠습니다.

</br>

## 7. 회고 / 느낀점
>자바와 스프링에 대해 공부를 진행하다가 이제는 프로젝트를 만들어서 공부해본 것을 적용해봐야겠다는 생각으로 프로젝트를 진행해보았습니다. 
>그러나 프로젝트를 만들다보면 빠르고 쉬운 방법만을 찾아보게되었습니. 그래서 강의를 통한 클론코딩을 진행하였는데 만들면서도 뭔가 남는 것이 없다고 느꼈습니다.
>감사하게도 이번 프로젝트를 통해 앞으로 어떤식으로 방향을 잡고 만들어야하는지 알아가는 시간이었습니다.
>이번 프로젝트에서는 Spring Security 같은 보안부분이나 성능부분에서도 추후에 공부를 해서 보완해야할 점이 남아있습니다.
  
