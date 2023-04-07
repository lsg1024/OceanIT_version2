# 📆 2022년 10월 14일 ~ 2022년 11월 17일 -> 기능 추가 및 코트 리펙토링 진행 중

# 안드로이드 버전 

        Sdk - 21 ~ 33

# 대표 기능 구현

1. 실시간 모니터링 페이지
2. 그래프 페이지
3. 임계치 설정 페이지
4. 파이어베이스 알림
+ 추가 기능 webView를 이용한 수조 모니터링

# 오픈 소스 라이브러리
https://github.com/anastr/SpeedView
https://github.com/PhilJay/MPAndroidChart

1. 실시간 센서 데이터 -> 

        1. 소켓 연결해 실시간으로 갱신되는 데이터를 UI에 출력 실시간 소켓 데이터는 갱신까지 20초간의 간격이 있어 이전 데이터를 호출하는 소켓까지 이용
        2. speedView 라이브러리 이용


2. 그래프 -> DB에 있는 데이터를 호출해 30분 간격의 데이터를 보여준다

        1. 소켓 연결 -> API 연결 수정 
        2. MPAndroidChart 라이브러리 이용 기존 라이브러리에서 LineChart 이용

3. 설정 페이지 ->

        1. RangeSlider를 이용한 바 처리
        2. 로그아웃처리
        

4. 파이어베이스를 이용한 알림

        1. 파이어베이스를 이용해 임계치에 도달하면 디바이스로 알림을 전송
