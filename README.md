# ForDiver
![indexPhoto1](https://github.com/kimhyeon622/ForDiver/assets/121088829/62bd7d26-dc92-4e7d-bc7a-10bdb7950645)
## For Diver
인천일보아카데미 마지막 프로젝트(파이널프로젝트) <br/>
개발 기간 : 2022.01.03 ~ 2022.02.24 (약 2달)

## 프로젝트 소개

## 개발 팀원
시작 인원 : 5명 <br/>
끝까지 같이한 인원 : 4명


|<img src="https://github.com/kimhyeon622/ForDiver/assets/121088829/c3d458e1-bcb6-499b-bfba-d646b5d27189" width="300" height="300">|<img src="https://github.com/kimhyeon622/ForDiver/assets/121088829/c4a84783-fe76-4d7c-867c-64bbbf7eeef3" width="300" height="300">|<img src="https://github.com/kimhyeon622/ForDiver/assets/121088829/3ae78422-0469-4aed-bce0-c5696df3b79d" width="300" height="200">|<img src="https://github.com/kimhyeon622/ForDiver/assets/121088829/243257a9-d0ec-4b25-9051-8ebd42c9da9d" width="300" height="200">
|:----:|:----:|:----:|:----:|
|오주현|강동근|김현|정창민|

### 담당 부분

#### 중간에 나간 팀원
 - **신대철** - 다이빙 장비 데이터 수집, 스킨/스쿠버 개요, 다이빙 명소, 다이빙 클럽

#### 기획 단계
 - **오주현(팀장)** - 기획안 작성, Job List 작성, DB 설계 및 수정, 스토리보드 작성, 발표문 및 PPT 작성
 - **강동근** - Job List 작성, DB 설계 및 수정, ERD 작성, 스토리보드 작성, PPT 제작
 - **김현** - ERD 작성 및 수정, DB 수정, 해양 생물 모션 제작
 - **정창민** - 다이빙 장비 데이터 수집, 템플릿 선정 및 배포, 멘치마킹 사이트 검색, 웹 디자인 검색

#### 개발 단계
 - **오주현(팀장)** - 자유/갤러리 게시판, index/header/footer 제작, 해양 위기정보, 해양 위기자가 진단, 나간 팀원이 만든 페이지 수정(스킨/스쿠버 개요, 다이빙 명소, 다이빙 클럽), 환경단체 목록, 프로젝트 초기 취합 
 - **강동근** - 회원 메뉴(회원가입, 로그인, 내 정보 보기, 문의하기 등). 관리자 메뉴(문의내역, 회원관리, 공지사항 작성등)
 - **김현** - API를 이용한 결제 기능, 커스템 예약(바다 및 수영장 예약페이지, 결제페이지등등), 장바구니 결제 및 장바구니 초기화, 퀵메뉴, 다른 팀원이 만든 페이지 해상도에 맞게 재수정, 포토샵을 이용한 이미지 제작(거북이), 프로젝트 취합, 테스트 및 디버깅, 회의부터 발표전 까지의 개발 히스토리 제작 
 - **정창민** - 다이빙 장비 샵, 장바구니, 웹소켓을 이용한 접속자 명당 애니메이션, Intro 페이지


## 개발 환경
 - `Java 11`
 - `JDK 11`
 - **IDE** : Intellij IDEA
 - **Database** : Oracle DB(11g)
 - **ORM** : Mybatis

## 주요 기능
 ### Intro
  - 처음 접속했을 때 보게 되는 웹 페이지 입니다 Shift키와 함께 횡 이동이 가능하며 사용자가 휠만 굴리 경우 화면에 보이는 거북이가 알려줍니다.
  - 움직이며 배경에 해양 생물들을 볼 수 있어 시각적으로 관심을 얻을 수 있습니다.

 ### 회원 관련
  - 회원 가입
  - 로그인
  #### 일반 회원
   - 내 정보 보기
   - 내 정보 수정
   - 내 구매내역
   - 내 문의내역
   - 비밀번호 변경
   - 탈퇴하기
 #### 관리자
  - 회원리스트
  - 공지 작성
  - 전체 문의내역
  - 회원 복구 신청내역
  - 공지메일 작성

 ### 다이빙 이모저모
  - 스킨/스쿠버 다이빙의 개요와 국내 다이빙의 명소와 클럽의 정보를 담고 있는 페이지입니다
 


 ### 다이버 샵
 
  #### 다이빙 장비
   - 해당하는 버튼을 클릭시 그 분류의 하위 분유에 해당하는 장비 버튼을 볼 수 있습니다
   - 장비를 선택하여 바로 구매 또는 장바구니 담기가 가능합니다
   - 바로 구매를 누르면 구매 페이지로 이동하고 장바구니 담기를 누르면 우측에 장비를 착용한 모습을 볼 수 있습니다
   ##### 장바구니
    - 장비를 구매 할 수 있는 구매 페이지입니다
    - 장비구니에서 장바구니 담기를 하여 담았던 물품들을 뺄 수 있으며, 초기화도 가능합니다
    - 결제는 카카오 API를 사용하였습니다
  #### 커스텀 예약
   - 커스컴 예약으로 이동하면 어떻게 선택할 수 있는지 간단하게 알려줌니다.
   - 다이빙 장소를 바다로 선택할지 수영장으로 선택할지 고르면 해당 예약 페이지로 이동됩니다
   ##### 바다 or 수영장 페이지
   - 바다 또는 수영장페이지로 이동되면, 지역에 따른 여러 다이빙 포인트(또는 수영장)가 간단하게 소개되며, 클릭시 해당 포인트(또는 수영장)를 예약 할 수 있는 페이지로 이동됩니다
   ##### 상세 페이지
   - 예약 하는 페이지로 이동시 해당 포인트(또는 수영장)에 관한 자세한 설명, 위치, 추천장비, 트레이닝 코스등등 을 볼 수 있습니다
   - 바다일 경우 예약일과 인원 수만 수영장 일경우 트레이닝 코스까지 선택하여 예약 가능합니다
   -  

   ##### 결제 페이지
    - 선택한 바다 또는 수영장이름과 예약일 인원 수등등이 표시되고 포인트 사용이 가능합니다
    - 결제 수단은 카카오페이, KG이니시스 두 가지의 API를 사용했기 때문에 간편결제도 가능하고 다양한 결제도 가능합니다
    
 ### 환경 운동
  #### 해양위기 현황
   - 해양위기 현황에서 해양의 위기와 오염시키는 원인등 현재 해양 생태계의 심각한 현황에 대해 알 수 있습니다
   - 해결방안에서 간단한 방법으로 해양 위기를 해결할 수 있는 방안을 알 수 있습니다
   - 실천사례에서 현재 해양위기를 해결하기 위해 실천하고 있는 프로그램들이 소개되었습니다
  #### 환경단체 목록
  - 환경을 위해 힘쓰고 있는 단체를 간단하게 소개되어있습니다
  - 국내뿐만 아니라 해외의 환경단체도 포함되어있습니다
 ### 커뮤니티
  게시판따라 다른 게시글을 작성할 수 있으며 기본적으로 댓글기능과 좋아요기능이 있습니다<br/>
  댓글은 무한적으로 작성할 수 있는 무한댓글을 구현시켰습니다
  #### 자유 게시판
   - 네이버 블로그식처럼 글과 사진을 함께 작성 할 수 있는 자유 게시판입니다
  #### 갤러리 게시판
   - 이미지와 영상을 등록하는 게시판입니다
   - 게시글이 올라 갔을 때 다른사람에게 보일 썸네일을 설정 할 수 있습니다

 ### 이벤트
  #### 해양위기 자가진단
   - 간단한 여러 질문을 통해 해양위기에 관하여 어느정도 관심이 있는지 자가진단을 할 수 있는 이벤트입니다
   - 해양위기 자가진단에 관심을 끌기 위해 설문 완료시 포인트를 지급합니다
  #### 해저 클럽
   - 현재 접속한 회원과 각 회원들이 어떤 페이지를 보고 있는지 확인이 가능합니다
   - 단, 로그인을 한 회원만 열람가능하며 비 로그인시에는 배경만 보입니다
  
  
