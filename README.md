## 🌱 새싹 프로젝트 소개 🌱
<img width="250" alt="Intro" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/intro2.png">
<br>
<br>

### 🌱 새싹이란?
>  투두리스트와 다이어리로 쌓아가는 육아일기 앱📝

<br>

### 🗓 개발 기간
- 1차: 2024.03.30 ~ 2024.06.01
- 리팩토링 : 2025.06.30 ~ 

<br>

### 👨‍👩‍👧‍👧 협업 사용 도구
- 회의 - 카카오톡, 디스코드

<br>

### 💻 기술 스택
- `Apache Commons Net`, `jsoup`, `gson`

<br>

### 프로젝트 기획 배경
> 현대 부모들의 바쁜 일상 속에서 육아와 관련된 소중한 순간들을 기록하고, 동시에 효율적으로 일정을 관리할 수 있는 도구가 필요하다는 생각에서 출발함.

- ✅ 기록의 필요성 : 아이의 성장 과정은 빠르게 지나가지만, 부모는 모든 순간을 기억하고 싶어한다.
- ✅ 체계적인 관리 : 단순한 메모가 아니라, 일정과 계획을 정리하며 효율적으로 육아를 관리하고 싶다.
- ✅ 편리한 사용성 : 바쁜 부모들도 쉽게 사용할 수 있는 직관적인 UX/UI가 필요하다.
- ✅ 추억의 공유 : 아이와의 소중한 순간을 나중에 되돌아볼 수 있어야 한다.
- ✅ 일상의 균형 : 육아뿐만 아니라 부모 자신의 일상과 감정도 기록하며 균형을 맞출 수 있어야 한다.

<br>

### 💡 핵심 기능
- `아이 프로필 작성`
- `원하는 날짜를 선택해 투두리스트 (할 일) 작성 가능`
- `원하는 날짜를 선택해 다이어리 작성 가능`
- `가족 간의 다이어리 공유`

<br>

### 🙋🏻‍♂️  타겟 유저
- 아이의 성장 기록을 체계적으로 관리하고 싶은 20~40대 부모들.

<br>

### 🌟 기대 효과
1. 아이의 성장 과정을 기록하며 부모에게 정서적 만족감을 제공합니다.
2. 부모의 육아 관련 할 일을 체계적으로 정리해 시간 관리 부담을 줄입니다.
3. 직관적인 UI로 모든 연령대의 사용자가 쉽게 접근 가능하며, 장기적인 사용을 유도합니다.

<br> 

### ➡️ 서비스 흐름도
<img width="1000" alt="flowChart" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/flowchart.png">
<br>

### 🎨 페이지 구현

---

**이벤트 페이지**

<img width="250" alt="Event" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page1.PNG">

---

**테마 선택 페이지 & 선택 이후 메인 화면** 
<table>
  <tr>
    <td>
<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page2.PNG">
    </td>
    <td>
<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page2-1.PNG">
    </td>
  </tr>
</table>

---
**메인 페이지 & 기능 선택 페이지**
<table>
  <tr>
    <td>
<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page3.PNG">
    </td>
    <td>
<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page3-1.PNG">
    </td>
  </tr>
</table>

---
**투두리스트 페이지 & 다이어리 페이지**
<table>
  <tr>
    <td>
<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page3-2.PNG">
    </td>
    <td>
<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page3-3.PNG">
    </td>
  </tr>
</table>

---

**아이 프로필 작성 페이지**

<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page4.PNG">

---

**설정 페이지**

<img width="250" alt="page1" src="https://raw.githubusercontent.com/feralshining/SaeSSack/main/assets/page5.PNG">

---


### 🚧 추가 개발 계획
- **유료 호스팅 전환** : `무료 웹 호스팅 서버에서 유료 호스팅으로 업그레이드하여 더 빠르고 안정적인 서비스 제공`
- **부드러운 애니메이션** : `페이지 전환 시 매끄러운 애니메이션을 추가해 시각적 즐거움과 몰입감 향상`
- **디자인 리뉴얼** : `전체 UI/UX를 현대적이고 직관적으로 전면 보수하여 사용자 편의성 및 시각적인 효과 극대화`
- **오프라인 모드 지원** : `인터넷 연결 없이도 기록과 투두리스트를 관리할 수 있는 기능 추가`
- **코드 리팩토링** : `모듈화와 주석 정비를 통해 가독성과 유지 보수성을 높이고, 확장 용이성 확보`
- **공유 기능 도입** : `다이어리 기록을 가족이나 친구와 손쉽게 공유할 수 있는 옵션 개발`

### 🖥️ 실행 환경
- `Android 12 이상 (API 31+)`

### 🚀 배포링크

- `현재 리팩토링 중. 출시 이후 구글 Play Store에서 다운로드 가능`
