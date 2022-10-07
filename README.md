# 플레이리스트

[Playlist](https://playlist.webius.net)

[Playlist - portfolio](git/%ED%8F%AC%ED%8A%B8%ED%8F%B4%EB%A6%AC%EC%98%A4_%ED%94%8C%EB%A0%88%EC%9D%B4%EB%A6%AC%EC%8A%A4%ED%8A%B8.pdf)

## 프로젝트 소개
* 목표 - 유튜브 / 네이버TV / 카카오TV 등 다양한 미디어 플랫폼의 영상을 통합된 플랫폼에서 제공
* 주요 기능 - 플레이리스트를 만들어 플랫폼 상관 없이 Embed 형태로 영상 제공, 영상 필터링 재생, 반복 재생과 랜덤 재생
* 작업 기간 - 2020년 07월 ~ 2020년 08월 (약 1달)
* 작업 배분
	- 디자이너 이예슬:
		1) 전체적인 톤앤매너와 로고 기획
		2) 디바이스 별 반응형 디자인
		3) 모든 디자인 담당
	- 개발자 김예승:
		1) Bootstrap 라이브러리를 활용한 페이지 퍼블리싱
		2) 서버 매퍼 설정 및 서비스 구현
		3) 모든 개발 담당
		4) 서비스 배포 환경 구축 및 배포

## 프로젝트 디렉터리 구조
* `lib` - 서비스에 필요한 외부 라이브러리 파일
* `src` - 서비스를 구현하는 Java 파일
* `war` - 배포 WAR
* `web` - 서비스를 구현하는 View 파일

## 웹 사이트 구조
* `/` - Playlist APP 메인
* `/play` - 나의 플레이리스트
* `/search` - 나의 플레이리스트 내 검색 기능
* `/notice` - 플랫폼 공지사항 게시판

## 기술 스택

### Server
* Spring Framework
* MyBatis
* Lombok
* Jackson JSON Parser
* MariaDB
* Maven

### Client
* Bootstrap
* FontAwesome
* jQuery

### Deploy
* Apache2 + Tomcat9 (JK Module)
