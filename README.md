알서포트 공지사항 API 제작 과제
---

과제는 다음 환경에서 진행되었습니다.

- Windows 10
- IntelliJ IDEA
- Java 17
- SpringBoot 3.2.8
- MySql
  - 실행시, `application.yml` 파일에서 DB 설정 변경 필요.

---

## 실행

1. git clone 하기.

```bash
git clone https://github.com/KINghyuk9/rsupport-resume-notice-board.git
cd rsupport-resume-notice-board
```

2. 프로젝트 빌드.

```bash
./gradlew build
```

3. 프로젝트 실행.

```bash
./gradle bootRun

or

cd build/libs
java -jar noticeboard.jar 
```

---

## API

- [ ]  공지사항 등록
- [ ]  공지사항 삭제
- [ ]  공지사항 조회
    - [ ]  공지사항 상세 조회
    - [ ]  공지사항 조건 조회
- [ ]  공지사항 수정

| API 명            | 상세                             | type  |
|-------------------|----------------------------------|-------|
| 공지사항 등록     | /board/notice/create             | POST  |
| 공지사항 조건 검색 | /board/notice/search-with-condition | GET   |
| 공지사항 상세 조회 | /board/notice/search-notice-detail/{noticeId} | GET   |
| 공지사항 삭제     | /board/notice/delete/{noticeId}/{userId} | DELETE |
| 공지사항 수정     | /board/notice/update             | PUT   |

---

## 상세
- 파일 처리:
    - `application.yml` 파일에서 `file-save-path`의 파일 저장 경로를 별도로 수정 필요.
    - 실제 서비스 운영 시: Load Balancer 환경의 경우, 별도의 FILE 서버를 구축하여 서버 간 파일 송수신 설정 진행.
    - 해당 프로젝트: 로컬 환경에서 호출하기 때문에 로컬 환경에서 직접 파일 송수신 설정 진행.

- API
    - 단순한 쿼리의 경우 → Query Method 활용.
    - 복잡도가 있다고 판단되는 경우 → queryDSL 활용.

### 공지사항 등록
- **`Request`**
- `POST`: localhost:8080/board/notice/create
```
{
  "title": "제목1",
  "content": "내용1",
  "author": "작성자",
  "userId": "userId",
  "startDate": "2024-07-17 00:00:00",
  "endDate": "2024-08-17 00:00:00",
  "files": [
    {
      "name": "첨부파일1"
    },
    {
      "name": "첨부파일2"
    }
  ]
}
```

- **`Response`**
```
{
  "success": true,
  "message": "success",
  "code": "200",
  "data": {
    "noticeId": 1,
    "author": "작성자",
    "userId": "userId",
    "title": "제목1",
    "content": "내용1",
    "createdDate": "2024-07-17 00:05:11",
    "startDate": "2024-07-17 00:00:00",
    "endDate": "2024-08-17 00:00:00",
    "views": 0,
    "files": [
      {
        "fileId": 1,
        "fileName": "첨부파일1"
      },
      {
        "fileId": 2,
        "fileName": "첨부파일2"
      }
    ]
  }
}
```

### 공지사항 조건 조회
- **`Request`**
- `GET`: localhost:8080/board/notice/search-with-condition
- **`param`**
    - `searchType`: title, content, author
    - `searchValue`: 검색어
    - `page`: 페이지 번호
    - `size`: 페이지 사이즈
    - `sort`: 정렬 조건
- **`Response`**
```
{
  "success": true,
  "message": "success",
  "code": "200",
  "data": [
    {
      "noticeId": 1,
      "author": "작성자",
      "userId": "userId",
      "title": "제목1",
      "content": "내용1",
      "createdDate": "2024-07-17 00:05:11",
      "startDate": "2024-07-17 00:00:00",
      "endDate": "2024-08-17 00:00:00",
      "views": 0,
      "files": [
        {
          "fileId": 1,
          "fileName": "첨부파일1"
        },
        {
          "fileId": 2,
          "fileName": "첨부파일2"
        }
      ]
    }
  ]
}
```

### 공지사항 상세 조회
- **`Request`**
- `GET`: localhost:8080/board/notice/search-notice-detail/{noticeId}
- **`param`**
    - `noticeId`: 공지사항 ID
- **`Response`**
```
{
  "success": true,
  "message": "success",
  "code": "200",
  "data": {
    "noticeId": 1,
    "author": "작성자",
    "userId": "userId",
    "title": "제목1",
    "content": "내용1",
    "createdDate": "2024-07-17 00:05:11",
    "startDate": "2024-07-17 00:00:00",
    "endDate": "2024-08-17 00:00:00",
    "views": 0,
    "files": [
      {
        "fileId": 1,
        "fileName": "첨부파일1"
      },
      {
        "fileId": 2,
        "fileName": "첨부파일2"
      }
    ]
  }
}
```

### 공지사항 삭제
- **`Request`**
- `POST`: localhost:8080/board/notice/delete/{noticeId}/{userId}
- **`param`**
    - `noticeId`: 공지사항 ID
    - `userId`: 사용자 ID
- **`Response`**
```
{
  "success": true,
  "message": "success",
  "code": "200",
  "data": "공지사항이 삭제되었습니다."
}
```

### 공지사항 수정
- **`Request`**
- `PUT`: localhost:8080/board/notice/update
```
{
  "noticeId": 1,
  "title": "제목1",
  "content": "내용1",
  "author": "작성자",
  "userId": "userId",
  "startDate": "2024-07-17 00:00:00",
  "endDate": "2024-08-17 00:00:00",
  "files": [
    {
      "name": "첨부파일1"
    },
    {
      "name": "첨부파일2"
    }
  ]
}
```

- **`Response`**
```
{
  "success": true,
  "message": "success",
  "code": "200",
  "data": {
    "noticeId": 1,
    "author": "작성자",
    "userId": "userId",
    "title": "제목1",
    "content": "내용1",
    "createdDate": "2024-07-17 00:05:11",
    "startDate": "2024-07-17 00:00:00",
    "endDate": "2024-08-17 00:00:00",
    "views": 0,
    "files": [
      {
        "fileId": 1,
        "fileName": "첨부파일1"
        },
        {
          "fileId": 2,
          "fileName": "첨부파일2"
        }
      ]
    }
  }
}
```

---

## 참고

- FILE DIRECTORY
    - file이 저장될 Local Directory 지정을 필요합니다.
        - `application.yml` 내 설정 변경을 진행해주세요.