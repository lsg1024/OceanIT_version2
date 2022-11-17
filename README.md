2022년 10월 14일 ~ 2022년 11월 17일

양식장 수치 측정값 표현 어플리케이션

3가지 화면으로 구성

모니터링 페이지 / 이전 수치 그래프 페이지 / 설정 페이지 / 파이어 베이스를 이용한 알림 


1. 종합 수치 및 개별 수치 표현 ->

        1. 소켓 연결 / 레트로핏 연결하는 방식과 비슷하다 느낌
        2. speedView 라이브러리 이용 기존 라이브러리에서 수치와 측정 그래프 변경
https://github.com/anastr/SpeedView
        
        
        

        
2. 이전 수치 그래프 페이지 ->

        1. 소켓 연결
        2. MPAndroidChart 라이브러리 이용 기존 라이브러리에서 LineChart 이용  
https://github.com/PhilJay/MPAndroidChart
        
        
                진행 사항 ) 
                        1. 기존 계획은 소켓을 이용해 실시간 처리를 목적으로 만들었으나 시간 문제로 일정 범위의 데이터를 가져와 화면 전환 후 복귀 할 때마다 리프레쉬 하는 방식으로 처리 
                        2. 기존 차트에서 VallueFormatter 클래스를 생성해 차트에 들어오는 값에서 일정 부분 잘라내는 작업을 진행하였다
                        3. refreshContent를 이용해 차트 클릭시 말풍선을 생성하고 필요한 값을 넣어줬다 기존 라이브러리에는 해당 x 좌표 값을 보여주는데 수정했다
                
                아쉬운 점 )
                        1. 진행 계획에서는 6개의 차트만 만들면되서 리사이클러 뷰를 이용하지 않고 각각을 생성해서 입력하는 방식으로 진행했다 하지만 진행 할 수 록 반복되는 부분과 모듈화가 필요한 부분들이 발생해 다음 비슷한 일에서는 리사이클러 뷰를 사용할 예정이다 
                        2. 시간 관계상 진행이 될 수록 모듈화보다는 하드코딩이 일상화가 되어버려서 코드 길이가 굉장히 길어졌다 모듈화가 필요할거 같다
                        3. 기존 실시간 처리에서 소켓의 데이터를 한번에 (여러개 값이 있음) 가져와 그래프가 꼬이는 현상이 발생해 진행 할 수 없었다 이후 수정해볼 예정이다
                        
                        
3. 설정 페이지 -> 

        1. RangeSlider를 이용한 바 처리
        2. 로그아웃처리
        
                진행 사항 ) 
                        1. RangeSlider를 이용해 데이터 제한 수치를 표시해 주었다 -> RangeSlider에서 터지는 오류 해결 stepSize를 0으로 만들고 바 이동을 담당하는 함수에서 포멧 처리를 해주었더니 오류 해결 onStopTrackingTouch
                        2. 로그아웃시 디바이스 데이터 제거 요청과 캐쉬 삭제
                        
                아쉬운 점 ) 
                        1. RangeSlider에서 stepSize 설정과 데이터 처리시 포멧 설정에서 애먹었다 기존에는 stepSize를 설정해 두어 증가되는 수치를 정해주었다 하지만 float 형식이여서 데이터 를 받아오며 발생하는 오류 같다 
https://github.com/material-components/material-components-android/issues/704    


4. 파이어베이스를 이용한 알림
        
        1. 파이어베이스를 이용해 일정 값이 되면 디바이스로 알림을 보냄
        2. 로그인 시 디바이스 토큰을 서버로 디비에 저장 서버에서 보내는 소켓 값이 일정 제한 값을 벗어나면 서버에서 디바이스 토큰을 이용해 알림을 보낸다
        
        

느낀 점 )

        1. 생각보다 생명주기에 대한 중요점과 그에 대한 화면 처리 대응에 대해 생각해 보게 되었다
        2. float 값은 무섭다 
        3. 화면 처리 순서에 따른 데이터 호출과 제한된 값에서의 데이터 처리과정 
        4. 개발 시간보다 생각하며 오류잡는 디버깅 시간이 더 오래걸린거 같다 -> 한번에 잘 만들자
                
                



안드로이드 앱 개발 : 임재성
서버 및 데이터베이스 개발 : 김나경
 
