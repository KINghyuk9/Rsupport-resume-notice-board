알서포트 공지사항 API 제작 과제
---

과제는 다음 환경에서 진행되었습니다.

- Windows 10
- IntelliJ IDEA
- Java 17
- SpringBoot 3.2.8
- MySql

---

## 실행

1. git clone 하기.

```bash
git clone https://github.com/KINghyuk9/rsupport-resume-notice-board.git
cd rsupport-resume-notice-board
```

1. 프로젝트 빌드.

```bash
./gradlew build
```

1. 프로젝트 실행.

```bash
./gradle bootRun
```

---

## API

- [ ]  공지사항 등록
- [ ]  공지사항 삭제
- [ ]  공지사항 조회
    - [ ]  공지사항 상세 조회
    - [ ]  공지사항 조건 조회
- [ ]  공지사항 수정
|API 명|상세|type|
|------|---|---|
|공지사항 등록|/board/notice/create|POST|
|공지사항 삭제|/board/notice/delete/{noticeId}/{userId}|DELETE|
|공지사항 조건 검색|/board/notice/search-with-condition|GET|
|공지사항 상세 검색|/board/notice/search-notice-detail/{noticeId}|GET|
|공지사항 수정|/board/notice/update|PUT|
