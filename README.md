### 4.3. Controller
- **요청 처리** :pushpin: [코드 확인](https://github.com/Integerous/goQuality/blob/b2c5e60761b6308f14eebe98ccdb1949de6c4b99/src/main/java/goQuality/integerous/controller/PostRestController.java#L55)
  - Controller에서는 요청을 화면단에서 넘어온 요청을 받고, Service 계층에 로직 처리를 위임합니다.

- **결과 응답** :pushpin: [코드 확인]()
  - Service 계층에서 넘어온 로직 처리 결과(메세지)를 화면단에 응답해줍니다.

### 4.4. Service

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_service1.png)

- **Http 프로토콜 추가 및 trim()** :pushpin: [코드 확인]()
  - 사용자가 URL 입력 시 Http 프로토콜을 생략하거나 공백을 넣은 경우,  
  올바른 URL이 될 수 있도록 Http 프로토콜을 추가해주고, 공백을 제거해줍니다.

- **URL 접속 확인** :pushpin: [코드 확인]()
  - 화면단에서 모양새만 확인한 URL이 실제 리소스로 연결되는지 HttpUrlConnection으로 테스트합니다.
  - 이 때, 빠른 응답을 위해 Request Method를 GET이 아닌 HEAD를 사용했습니다.
  - (HEAD 메소드는 GET 메소드의 응답 결과의 Body는 가져오지 않고, Header만 확인하기 때문에 GET 메소드에 비해 응답속도가 빠릅니다.)

  ![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_service2.png)

- **Jsoup 이미지, 제목 파싱** :pushpin: [코드 확인]()
  - URL 접속 확인결과 유효하면 Jsoup을 사용해서 입력된 URL의 이미지와 제목을 파싱합니다.
  - 이미지는 Open Graphic Tag를 우선적으로 파싱하고, 없을 경우 첫 번째 이미지와 제목을 파싱합니다.
  - 컨텐츠에 이미지가 없을 경우, 미리 설정해둔 기본 이미지를 사용하고, 제목이 없을 경우 생략합니다.


### 4.5. Repository

![](https://zuminternet.github.io/images/portal/post/2019-04-22-ZUM-Pilot-integer/flow_repo.png)

- **컨텐츠 저장** :pushpin: [코드 확인]()
  - URL 유효성 체크와 이미지, 제목 파싱이 끝난 컨텐츠는 DB에 저장합니다.
  - 저장된 컨텐츠는 다시 Repository - Service - Controller를 거쳐 화면단에 송출됩니다.

</div>


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

## 4. 코드

<details>
<summary><b>4-1. Controller</b></summary>
<div markdown="1">
  - AlbumController [코드 확인](https://github.com/heungryuncho/photo_album/blob/eefcf11b55322de3fe172d9f0335d1b367d3136a/src/main/java/com/squarecross/photoalbum2/controller/AlbumController.java#L17)
  - PhotoController https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/controller/PhotoController.java#L24
</div>
</details>

<details>
<summary><b>4-2. Domain</b></summary>
<div markdown="1">
  - Album https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/domain/Album.java#L15
  - Photo https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/domain/Photo.java#L14
</div>
</details> 
  
<details>
<summary><b>4-3. Dto</b></summary>
<div markdown="1">
  - AlbumDto https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/dto/AlbumDto.java#L11
  - PhotoDto https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/dto/PhotoDto.java#L11
</div>
</details> 

<details>
<summary><b>4-4. Mapper</b></summary>
<div markdown="1">
  - AlbumMapper https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/mapper/AlbumMapper.java#L9
  - PhotoMapper https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/mapper/PhotoMapper.java#L11
</div>
</details> 

<details>
<summary><b>4-5. Repository</b></summary>
<div markdown="1">
  - AlbumRepository https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/repository/AlbumRepository.java#L12
  - PhotoRepository https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/repository/PhotoRepository.java#L13
</div>
</details> 

<details>
<summary><b>4-6. Service</b></summary>
<div markdown="1">
  - AlbumService https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/service/AlbumService.java#L29
  - PhotoService https://github.com/heungryuncho/photo_album/blob/70d42b68a7cdb75b199954717f39a0e353df79b2/src/main/java/com/squarecross/photoalbum2/service/PhotoService.java#L31
</div>
</details> 

</br>

## 5.트러블 슈팅
### 5.1. 컨텐츠 필터와 페이징 처리 문제
- 저는 이 서비스가 페이스북이나 인스타그램 처럼 가볍게, 자주 사용되길 바라는 마음으로 개발했습니다.  
때문에 페이징 처리도 무한 스크롤을 적용했습니다.


</br>

## 6. 회고 / 느낀점
>항상 프로젝트를 만들다보면 사람인지라 빠르고 쉬운 방법만을 강구해왔었습니다. 강의를 통한 클론코딩을 똑같이 만들면서 뭔가 남는 것이 없다고 느꼈습니다.
>감사하게도 어떤식으로 방향을 잡고 만들어야하는지 깨닫는 시간이었습니다.
  
