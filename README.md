## 서울여자대학교 programming guru2 Android
<br>
네이버지도API 기반타임캡슐서비스

<hr>

![1](https://github.com/user-attachments/assets/296f5119-2408-4601-b032-5b1eb3d4efb3)
![2](https://github.com/user-attachments/assets/cf61fe14-b0e4-4f93-ac54-8fbfde9f550a)
![3](https://github.com/user-attachments/assets/d3664075-669d-4ccd-9e9c-d86bd7da380f)
![4](https://github.com/user-attachments/assets/2f95b63f-07c9-4e0f-ba36-003a2541ea42)

## 위치 기반 지도 조회 (ElasticSearch)

### 로컬 실행 준비

#### 1) MySQL (3306)
- DB: `guru2`
- 환경변수(파워쉘):

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/guru2?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="비밀번호"
```

#### 2) ElasticSearch (Docker, 9200)

```powershell
docker compose up -d
$env:ES_URIS="http://localhost:9200"
```

### API

#### 지도에서 현재 위치 기반 타임캡슐 조회
- **Endpoint**: `GET /api/timecapsules/map`
- **Query**
  - `latitude` / `longitude` (필수)
  - `radiusMeters` (기본 2000)
  - `scope`: `ME` | `FOLLOWING` | `BOTH` (기본 BOTH)
  - `category` (기본 전체)
  - `page` (기본 0), `size` (기본 50)

예시:

```text
GET /api/timecapsules/map?latitude=37.5665&longitude=126.9780&radiusMeters=1500&scope=BOTH&category=전체&page=0&size=50
```

### 테스트
테스트는 외부 MySQL 없이도 실행되도록 `test` 프로필에서 H2(in-memory)를 사용합니다.
