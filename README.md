<div align="center">
  <a href="https://www.numble.it/b21bf666-045f-478e-8643-927417a448d2"><img src="https://user-images.githubusercontent.com/53372971/175571684-0927f3a5-860d-4d4d-ac42-42179fd971a0.png" height="128px" alt="badge42 logo" ></a>
  <h1>Whatz</h1>
  <p>🏆디자인-프론트-백엔드 연계 모바일용 웹 서비스 만들기 챌린지 TOP10%🏆</p>
</div>

<div align="center">
<img width="80%" alt="스크린샷 2022-06-25 오전 12 15 02" src="https://user-images.githubusercontent.com/53372971/175565326-5afc61f3-835e-4d1e-8343-e8d182fc4d89.png">
  </div>

## 목차

- [팀원](#팀원)
- [프로젝트 목표](#프로젝트-목표)
- [동작 방식](#동작-방식)
- [개발 및 배포환경](#개발-및-배포환경)
- [동작 테스트](#동작-테스트)




## 팀원✨

<table>
  <tr>
    <td align="center"><a href="https://github.com/Lee-seungju"><img src="https://avatars.githubusercontent.com/u/53372971?v=4" width="100px;" alt=""/><br /><sub><b>Lee-seungju(slee2)</b></sub></a><br /><a>Back End</a></td>
    <td align="center"><a href="https://github.com/bluewow"><img src="https://avatars.githubusercontent.com/u/16996054?v=4" width="100px;" alt=""/><br /><sub><b>bluewow(Ki Hyun Kim)</b></sub></a><br /><a>Back End</a></td>
    <td align="center"><a href="https://github.com/pjainxido"><img src="https://avatars.githubusercontent.com/u/55627999?v=4" width="100px;" alt=""/><br /><sub><b>pjainxido(Park Jein)</b></sub></a><br /><a>Front End</a></td>
    <td align="center"><a href="https://github.com/kks2139"><img src="https://avatars.githubusercontent.com/u/17682954?v=4" width="100px;" alt=""/><br /><sub>kks2139(kwangsun)</b></sub></a><br/><a>Front End</a></td>
    <td align="center"><a href="https://www.numble.it/9b3cfe5c-f052-4584-b202-1d0093eb640f"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d5/No_sign.svg/1200px-No_sign.svg.png" width="100px;" alt=""/><br/><sub><b>장지현</b></sub></a><br/><a>Designer</a></td>
  </tr>
</table>


## 프로젝트 목표
2022.04.15-2022.05.15 1달의 기간 동안, 숏츠를 이용한 영상 스트리밍 웹 사이트 제작

## 동작 방식


<div align="center">
<img width="30%" src="https://user-images.githubusercontent.com/53372971/175761008-8d506f0c-a1cc-4bf0-bab7-7ec21d08ed02.png">
 </div>

- 영상을 업로드 하기위해 서버로 MultipartFile을 제공
- 서버에서 해당 파일을 FFmpeg 외부 프로그램을 이용하여 .m3u8과 .ts파일 생성 후 로컬에 저장
- 로컬의 파일을 S3로 전송 후 로컬 파일 삭제
- S3에 연동된 CloudFront 경로 + 파일 경로를 DB에 저장
- 이후에 HLS 기술을 이용하여 프론트는 서버에서 보내준 DB에 저장된 경로를 CloudFront로 요청하여 영상 스트리밍

<div align="center">
<img width="80%" src="https://user-images.githubusercontent.com/53372971/175760998-6eca49f2-e989-4c85-8ba6-399660f5d1f3.png">
 </div>

- 소셜 로그인 기능

## 개발 및 배포환경

<div align="center">
<img width="60%" src="https://user-images.githubusercontent.com/53372971/175760859-6286e5dd-9ef5-4040-b423-757291587fbf.png">
 </div>
 
- Back End
  - Spring Boot, Spring Security, Spring Rest Docs, FFmpeg, HLS
  - MySQL, H2
  - AWS EC2, S3
- Front End
  - React, HLS
  - Typescript, NextJS, styled-jsx, Redux, Axios

<div align="center">
<img width="80%" src="https://user-images.githubusercontent.com/53372971/175760724-974273ff-63bc-4a82-a159-3a9b64fd1e11.png">
 </div>
 
 ## 동작 테스트

<div align="center">
<img width="80%" alt="스크린샷 2022-06-25 오후 3 54 14" src="https://user-images.githubusercontent.com/53372971/175762293-2e25d2cd-1d3e-4926-b933-9f3c5805c79c.png">
  <img width="80%" alt="스크린샷 2022-06-25 오후 3 54 50" src="https://user-images.githubusercontent.com/53372971/175762320-67862064-c9a4-4719-8c83-15cc0030a035.png">
  <img width="80%" alt="스크린샷 2022-06-25 오후 3 55 29" src="https://user-images.githubusercontent.com/53372971/175762351-5c1ae77a-1ecc-4724-b39c-ffed72dc6e06.png">
  <img width="80%" alt="스크린샷 2022-06-25 오후 3 56 08" src="https://user-images.githubusercontent.com/53372971/175762390-411f88b0-512b-4885-8e8b-d4cdedb00585.png">

  </div>


