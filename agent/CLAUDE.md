# SaeSSac 프로젝트 개발 문서

> **프로젝트명**: SaeSSac (새싹)  
> **설명**: 일기 작성 및 할 일 관리 기능을 제공하는 Android 다이어리 애플리케이션  
> **개발 언어**: Java 11  
> **최소 SDK**: 31 (Android 12)  
> **목표 SDK**: 34 (Android 14)

---

## 📋 목차

1. [기술 스택](#-기술-스택)
2. [프로젝트 구조](#-프로젝트-구조)
3. [코딩 컨벤션](#-코딩-컨벤션)
4. [아키텍처](#️-아키텍처)
5. [주요 기능](#-주요-기능)

---

## 🛠 기술 스택

### 플랫폼 & 빌드

```gradle
Platform: Android
Language: Java 11
Compile SDK: 34 (Android 14)
Min SDK: 31 (Android 12)
Target SDK: 34
Build Tool: Gradle 8.x
```

### 핵심 라이브러리

#### AndroidX 컴포넌트

```gradle
androidx.appcompat:appcompat:1.6.1
androidx.core:core:1.12.0
androidx.core:core-splashscreen:1.0.1
androidx.activity:activity:1.8.0
androidx.constraintlayout:constraintlayout:2.1.4
androidx.navigation:navigation-fragment:2.7.7
androidx.navigation:navigation-ui:2.7.7
```

#### UI & Material Design

```gradle
com.google.android.material:material:1.11.0
```

#### 데이터 & 네트워크

```gradle
com.google.code.gson:gson:2.10.1          // JSON 파싱
commons-net-3.10.0.jar                    // FTP 통신
org.jsoup:jsoup:1.17.2                    // HTML 파싱
SQLite                                     // 로컬 DB (내장)
```

#### 테스트

```gradle
junit:junit:4.13.2
androidx.test.ext:junit:1.1.5
androidx.test.espresso:espresso-core:3.5.1
```

---

## 📁 프로젝트 구조

### 패키지 구조

```
com.saessac/
├── activities/                    # UI 레이어 (15개 액티비티)
│   ├── auth/
│   │   └── AuthActivity          # 사용자 인증 및 UUID 관리
│   ├── diary/
│   │   ├── DiaryViewActivity     # 일기 조회
│   │   └── DiaryEditorActivity   # 일기 작성/편집
│   ├── event/
│   │   ├── EventActivity         # 이벤트 메인
│   │   ├── EventDiscountActivity # 할인 공지
│   │   └── GoodsRequestActivity  # 굿즈 신청
│   ├── main/
│   │   ├── MainActivity          # 메인 화면 (캘린더)
│   │   └── SplashActivity        # 스플래시 스크린
│   ├── settings/
│   │   ├── SettingActivity       # 설정 메인
│   │   ├── MyPageActivity        # 마이페이지
│   │   ├── PrivacyPolicyActivity # 개인정보 처리방침
│   │   └── CustomerServiceActivity # 고객 서비스
│   ├── sharing/
│   │   ├── BabyInfoActivity      # 아기 정보 입력
│   │   ├── FamilySharingActivity # 가족 공유 (UUID)
│   │   └── ThemeSelectionActivity # 테마 선택
│   └── todo/
│       └── WriteTodoListActivity # 할 일 작성/편집
│
├── data/                          # 데이터 레이어
│   ├── TodoDBHelper.java         # SQLite DB 헬퍼 (CRUD)
│   ├── TodoItem.java             # Todo 데이터 모델
│   └── TodoListAdapter.java     # ListView 어댑터
│
└── utils/                         # 유틸리티 레이어
    ├── Extensions.java           # 파일 입출력 유틸
    ├── SaessacFTP.java          # FTP 통신 관리
    ├── SaessacUI.java           # UI 헬퍼 (화면 전환, Toast 등)
    └── SaessacUserData.java     # 사용자 데이터 관리
```

### 리소스 구조

```
res/
├── drawable/        # 이미지 리소스 및 벡터 드로어블
├── layout/          # XML 레이아웃 (30+ 레이아웃)
├── values/          # 문자열, 색상, 스타일 정의
├── raw/             # BGM 파일 (background_bgm.mp3)
├── mipmap/          # 앱 아이콘
└── anim/            # 화면 전환 애니메이션
```

---

## 📝 코딩 컨벤션

### 네이밍 규칙

#### 클래스명

```java
// 액티비티: [기능명]Activity
public class AuthActivity extends AppCompatActivity { }
public class DiaryEditorActivity extends AppCompatActivity { }

// 유틸리티: Saessac[기능명]
public class SaessacUI { }
public class SaessacFTP { }

// 데이터 모델: [이름]Item / [이름]Adapter
public class TodoItem { }
public class TodoListAdapter extends BaseAdapter { }

// DB 헬퍼: [이름]DBHelper
public class TodoDBHelper extends SQLiteOpenHelper { }
```

#### 변수명

```java
// camelCase 사용
private SharedPreferences sharedPreferences;
private ExecutorService executorService;
private FTPClient ftpClient;

// 상수: UPPER_SNAKE_CASE
private static final String DATABASE_NAME = "TodoDatabase.db";
private static final int DATABASE_VERSION = 1;
public static final String DIARY_DIR = "/user_diary/";
```

#### 메서드명

```java
// camelCase, 동사로 시작
public void openActivity(Context context, Class<?> activityClass) { }
public static void showText(Context context, String message) { }
private UUID loadUUID() { }
private void saveUUID(UUID uuid) { }

// Boolean 반환: is/has 접두사
public boolean isCompleted() { }
public static Future<Boolean> isValid(String uuid) { }
```

### 코드 스타일

#### Javadoc 주석

```java
/**
 * 투두 항목을 표현하는 데이터 모델 클래스
 * 데이터베이스의 todo_table 테이블과 매핑됩니다.
 */
public class TodoItem {
    /**
     * 모든 필드를 초기화하는 생성자
     *
     * @param id        투두 항목 ID
     * @param date      날짜
     * @param task      할 일 내용
     * @param completed 완료 여부
     */
    public TodoItem(int id, String date, String task, boolean completed) { }
}
```

#### 들여쓰기 & 포맷

```java
// 4칸 들여쓰기
public void method() {
    if (condition) {
        doSomething();
    }
}

// 중괄호는 같은 줄에
public class Example {
    public void method() {
        // code
    }
}
```

#### 리소스 ID 규칙

```xml
<!-- [타입]_[화면]_[기능] -->
<Button android:id="@+id/btn_login_submit" />
<TextView android:id="@+id/tv_main_title" />
<EditText android:id="@+id/et_diary_content" />
<ImageButton android:id="@+id/imgbtn_theme_select" />
```

---

## 🏗️ 아키텍처

### MVC 패턴

```
┌─────────────────────────────────────┐
│           View (Activities)          │
│  - UI 이벤트 처리                     │
│  - 사용자 입력 수집                   │
│  - 화면 렌더링                        │
└───────────┬─────────────────────────┘
            │
            ↓
┌─────────────────────────────────────┐
│        Controller (Utils)            │
│  - SaessacUI: 화면 전환 로직         │
│  - 비즈니스 로직 처리                 │
└───────────┬─────────────────────────┘
            │
            ↓
┌─────────────────────────────────────┐
│          Model (Data)                │
│  - TodoItem: 데이터 모델             │
│  - TodoDBHelper: 데이터베이스        │
│  - SaessacFTP: 원격 데이터           │
└─────────────────────────────────────┘
```

### 데이터 흐름

#### 1. SQLite (로컬 저장)

```java
TodoDBHelper db = new TodoDBHelper(context);

// Create
db.addTodo(new TodoItem(date, task));

// Read
List<TodoItem> todos = db.getTodosByDate(date);

// Update
db.updateTodo(todoItem);

// Delete
db.deleteTodo(id);
```

#### 2. FTP (원격 저장)

```java
// 비동기 처리 (ExecutorService)
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    Future<Boolean> result = SaessacFTP.uploadFile(filePath, remotePath);
    boolean success = result.get();
});
```

#### 3. SharedPreferences (설정)

```java
SharedPreferences prefs = getSharedPreferences("com.saessac.preferences", MODE_PRIVATE);

// 저장
prefs.edit()
    .putString("uuid", uuid.toString())
    .putInt("theme", themeId)
    .apply();

// 조회
String uuid = prefs.getString("uuid", null);
int theme = prefs.getInt("theme", 0);
```

### 비동기 처리

```java
// ExecutorService 사용
private ExecutorService executorService = Executors.newSingleThreadExecutor();

executorService.execute(() -> {
    // 백그라운드 작업
    runOnUiThread(() -> {
        // UI 업데이트
    });
});
```

---

## 🎯 주요 기능

### 1. 사용자 인증 (UUID 기반)

```java
// UUID 생성 및 저장
UUID uuid = UUID.randomUUID();
SharedPreferences.Editor editor = sharedPreferences.edit();
editor.putString("uuid", uuid.toString());
editor.apply();
```

### 2. 일기 관리 (FTP)

- FTP 서버에 일기 텍스트 파일 업로드
- UUID 기반 파일명으로 관리
- 날짜별 조회 및 편집

### 3. 할 일 관리 (SQLite)

```sql
-- 테이블 구조
CREATE TABLE todo_table (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT NOT NULL,
    task TEXT NOT NULL,
    completed INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4. 테마 시스템

- 2가지 테마 (기본, 현대)
- SharedPreferences에 테마 ID 저장
- 모든 액티비티에서 동적 테마 적용

### 5. BGM 재생

```java
MediaPlayer player = MediaPlayer.create(context, R.raw.background_bgm);
player.setLooping(true);
player.start();
```

### 6. 화면 전환 애니메이션

```java
ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
    context,
    R.anim.slide_in_right,
    R.anim.slide_out_left
);
context.startActivity(intent, options.toBundle());
```

---

## 🔐 권한 & 보안

### 필수 권한

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 네트워크 보안

```xml
<!-- Cleartext 트래픽 허용 (FTP용) -->
<application android:usesCleartextTraffic="true">
```

### FTP 인증 정보

```java
// 보안 주의: 실제 배포 시 암호화 필요
private static final String SERVER = "saessak.dothome.co.kr";
private static final String USER = "saessak";
private static final String PASSWORD = "xodnjs6923!";
```

---

## 📱 화면 구성

### 앱 플로우

```
SplashActivity
    ↓
AuthActivity (UUID 생성/로드)
    ↓
MainActivity (캘린더)
    ├→ DiaryEditorActivity (일기 작성)
    ├→ WriteTodoListActivity (할 일 작성)
    ├→ EventActivity (이벤트)
    ├→ SettingActivity (설정)
    │   ├→ MyPageActivity
    │   ├→ CustomerServiceActivity
    │   └→ PrivacyPolicyActivity
    ├→ BabyInfoActivity (아기 정보)
    └→ ThemeSelectionActivity (테마)
```

---

## 🐛 디버깅 & 로그

### 로그 태그 규칙

```java
private static final String TAG = "AuthActivity";
Log.d(TAG, "UUID loaded: " + uuid);
Log.e(TAG, "Error: " + e.getMessage());
```

### 주요 로그 포인트

- FTP 연결 성공/실패
- DB CRUD 작업
- 화면 전환 이벤트
- 에러 발생 시점

---

## 📦 빌드 & 배포

### 빌드 명령어

```bash
# Debug 빌드
./gradlew assembleDebug

# Release 빌드
./gradlew assembleRelease

# 전체 빌드
./gradlew build
```

### ProGuard 설정

```gradle
buildTypes {
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

---

## 🔄 버전 관리

### 버전 정보

```gradle
versionCode 1
versionName "1.0"
```

### Git 브랜치

- `main`: 안정 버전
- `forExam`: 시험용 브랜치 (현재)
- Repository: `feralshining/SaeSSack`

---

## 📚 참고 문서

- [프로젝트 구조 상세](./docs/project_structure.md)
- [채점 기준](./docs/채점기준.md)
- Android Developer Guide: https://developer.android.com
- Commons Net FTP: https://commons.apache.org/proper/commons-net/
