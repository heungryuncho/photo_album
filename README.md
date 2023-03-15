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

## 3-2. 디자인
자세한 디자인 확인  
(https://www.figma.com/file/qL8ju2d9mYvkqRz4LnumON/%ED%8F%AC%ED%86%A0%EC%95%A8%EB%B2%94?node-id=0%3A1&t=rX3oqTRLAecEgcFG-1) 참고

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
> 삭제 과정에서 발생한 문제는 데이터베이스의 앨범과 사진 항목 간에 관계가 있었습니다.  
> 앨범 삭제를 시도할 때 데이터 일관성을 유지하기 위해 연결된 모든 사진도 삭제해야 합니다.  
>
>이 문제를 해결하기 위해 CascadeType.ALL 옵션이 앨범과 사진 엔티티 간의 관계 매핑에 추가되었습니다.  
>이 옵션은 앨범 항목에 대한 모든 변경 사항을 삭제를 포함하여 관련 사진 항목에 캐스케이드하도록 응용 프로그램에 지시합니다.
>
>관련 엔티티의 적절한 삭제를 보장하기 위해 간단한 테스트 코드를 작성해야하는데  
>테스트코드 작성하는 것에 익숙하지 않은 않아 어려운 작업이 될 수 있었습니다.  
>그러나 구글링을 통해 이 에러를 수정하였습니다.

</br>

## 7. 회고 / 느낀점
>Java와 Spring을 공부하고 있었고 프로젝트를 만들어 배운것을 적용해야겠다는 생각을 했습니다.  
>하지만 이 앨범 프로젝트말고 다른 프로젝트를 만들고 진행하면서 지름길을 선택하고 튜토리얼(클론코딩)에  
>크게 의존하는 제 자신을 발견했고,  이로 인해 진정으로 아무것도 배우고 있지 않은 것 같은 느낌이 들었습니다.  
>
>고맙게도 이 웹앨범프로젝트는 제가 집중해야 할 것과 향후 프로젝트에 접근하는 방법에 대한 통찰력을 얻는 데 도움이 되었습니다.  
>아직 보안, 성능 등 개선이 필요한 부분이 있지만 이에대해 더 자세히 공부할 계획입니다.
  
