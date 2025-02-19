# Online Course Management System

## 기능 설명

### 학생 관리
- **학생 생성**: 새로운 학생을 생성하고 등록합니다.
- **학생 삭제**: 특정 학생을 삭제합니다.
- **로그인한 학생 삭제**: 현재 로그인한 학생을 삭제합니다.
- **학생 강의 등록**: 학생이 여러 강의를 한 번에 등록할 수 있습니다.

### 강의 관리
- **강의 생성**: 새로운 강의를 생성하고 등록합니다.
- **강의 조회**: 특정 강의의 상세 정보를 조회합니다.
- **강의 목록 조회**: 모든 강의를 정렬 기준에 따라 조회합니다.
- **강의 업데이트**: 기존 강의의 정보를 업데이트합니다.
- **강의 삭제**: 특정 강의를 삭제합니다.
- **강의 공개**: 특정 강의를 공개 상태로 전환합니다.
- **강의 비공개**: 특정 강의를 비공개 상태로 전환합니다.
- **강의 검색**: 강의 제목과 카테고리, 강사 이름과 카테고리로 강의를 검색합니다.
- **학생 수 기준 강의 정렬**: 학생 수를 기준으로 강의를 정렬하여 페이지네이션된 결과를 조회합니다.

### 수강 관리
- **강의 수강 신청**: 학생이 특정 강의를 수강 신청합니다.
- **강의 수강 취소**: 학생이 특정 강의의 수강을 취소합니다.
- **좋아하는 강의**: 학생이 특정 강의를 좋아요로 표시합니다.
- **좋아하는 강의 목록 조회**: 학생이 좋아요로 표시한 강의 목록을 조회합니다.

### 보안 및 권한 관리
- **관리자 권한 확인**: 관리자만 특정 작업을 수행할 수 있도록 권한을 확인합니다.
- **로그인한 사용자 이메일 조회**: 현재 로그인한 사용자의 이메일을 조회합니다.

## 기술 스택
- **언어**: Java
- **프레임워크**: Spring Boot
- **빌드 도구**: Gradle
- **데이터베이스**: H2 (또는 다른 데이터베이스로 변경 가능)
- **보안**: Spring Security

## 설치 및 실행 방법
1. 저장소를 클론합니다:
    ```bash
    git clone https://github.com/your-repo/online-course-management.git
    ```
2. 프로젝트 디렉토리로 이동합니다:
    ```bash
    cd online-course-management
    ```
3. 필요한 의존성을 설치하고 애플리케이션을 빌드합니다:
    ```bash
    ./gradlew build
    ```
4. 애플리케이션을 실행합니다:
    ```bash
    ./gradlew bootRun
    ```
5. 브라우저에서 `http://localhost:8080`으로 접속하여 애플리케이션을 확인합니다.

## API 문서
  
  ### 학생 관리 API
  
  - **학생 생성**
    - **URL**: `/students`
    - **Method**: `POST`
    - **Request Body**: `StudentDTO`
    - **Response**: `StudentDTO`
    - **설명**: 새로운 학생을 생성하고 등록합니다.
  
  - **학생 삭제**
    - **URL**: `/students/{id}`
    - **Method**: `DELETE`
    - **설명**: 특정 학생을 삭제합니다.
  
  - **로그인한 학생 삭제**
    - **URL**: `/students/me`
    - **Method**: `DELETE`
    - **설명**: 현재 로그인한 학생을 삭제합니다.
  
  - **학생 강의 등록**
    - **URL**: `/students/lectures`
    - **Method**: `POST`
    - **Request Body**: `List<LectureDTO>`
    - **설명**: 학생이 여러 강의를 한 번에 등록할 수 있습니다.
  
  ### 강의 관리 API
  
  - **강의 생성**
    - **URL**: `/lectures`
    - **Method**: `POST`
    - **Request Body**: `LectureDTO`
    - **Response**: `LectureDTO`
    - **설명**: 새로운 강의를 생성하고 등록합니다.
  
  - **강의 조회**
    - **URL**: `/lectures/{id}`
    - **Method**: `GET`
    - **Response**: `LectureDTO`
    - **설명**: 특정 강의의 상세 정보를 조회합니다.
  
  - **강의 목록 조회**
    - **URL**: `/lectures`
    - **Method**: `GET`
    - **Response**: `List<LectureDTO>`
    - **설명**: 모든 강의를 정렬 기준에 따라 조회합니다.
  
  - **강의 업데이트**
    - **URL**: `/lectures/{id}`
    - **Method**: `PUT`
    - **Request Body**: `LectureDTO`
    - **Response**: `LectureDTO`
    - **설명**: 기존 강의의 정보를 업데이트합니다.
  
  - **강의 삭제**
    - **URL**: `/lectures/{id}`
    - **Method**: `DELETE`
    - **설명**: 특정 강의를 삭제합니다.
  
  - **강의 공개**
    - **URL**: `/lectures/{id}/public`
    - **Method**: `PUT`
    - **Response**: `LectureDTO`
    - **설명**: 특정 강의를 공개 상태로 전환합니다.
  
  - **강의 비공개**
    - **URL**: `/lectures/{id}/private`
    - **Method**: `PUT`
    - **Response**: `LectureDTO`
    - **설명**: 특정 강의를 비공개 상태로 전환합니다.
  
  - **강의 검색 (제목과 카테고리)**
    - **URL**: `/lectures/search/title`
    - **Method**: `GET`
    - **Request Params**: `title`, `category`
    - **Response**: `List<LectureDTO>`
    - **설명**: 강의 제목과 카테고리로 강의를 검색합니다.
  
  - **강의 검색 (강사 이름과 카테고리)**
    - **URL**: `/lectures/search/instructor`
    - **Method**: `GET`
    - **Request Params**: `instructorName`, `category`
    - **Response**: `List<LectureDTO>`
    - **설명**: 강사 이름과 카테고리로 강의를 검색합니다.
  
  - **학생 수 기준 강의 정렬**
    - **URL**: `/lectures/sorted`
    - **Method**: `GET`
    - **Request Params**: `page`, `size`
    - **Response**: `Page<LectureDTO>`
    - **설명**: 학생 수를 기준으로 강의를 정렬하여 페이지네이션된 결과를 조회합니다.
  
  ### 수강 관리 API
  
  - **강의 수강 신청**
    - **URL**: `/enrollments`
    - **Method**: `POST`
    - **Request Params**: `studentId`, `lectureId`
    - **Response**: `EnrollmentDTO`
    - **설명**: 학생이 특정 강의를 수강 신청합니다.
  
  - **강의 수강 취소**
    - **URL**: `/enrollments/{id}`
    - **Method**: `DELETE`
    - **설명**: 학생이 특정 강의의 수강을 취소합니다.
  
  - **좋아하는 강의**
    - **URL**: `/lectures/{id}/like`
    - **Method**: `POST`
    - **설명**: 학생이 특정 강의를 좋아요로 표시합니다.
  
  - **좋아하는 강의 목록 조회**
    - **URL**: `/students/{id}/likes`
    - **Method**: `GET`
    - **Response**: `List<LectureDTO>`
    - **설명**: 학생이 좋아요로 표시한 강의 목록을 조회합니다.