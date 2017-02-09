# IoT기술을 이용한 산업재해 예방 솔루션

## 1. 서론
### 1.1 개요
 ○ 산업재해를 예방하기 위해 IoT(Internet Over Things)기술을 적용하여 산업 환경을 관측하고
  데이터를 수집 및 경보 기능을 가진 산업재해 예방 솔루션을 개발하여 제시한다.
### 1.2 연구 환경
|  구분  | 항목 | 적용내역 |
|----|----|---|
|  S/W  | OS     | Windows 7 64Bit |
|       | IDE    | Android Studio  |
|       | IDE    | Arduino IDE     |
|       | Lang   | Android Java    |
|       | Lang   | C++             |
|  H/W  | Device | Arduino Mega    |
|       | Device | Genuino 101     |
|       | Device | Bluetooth 2.1 Shield |
|       | Sensor | WinsenO2Sensor  |
|       | Sensor | HT-01DV  온습도|
|       | Sensor | MQ7  일산화탄소|


### 1.3 연구 기간 및 수행 일정
○ 기간 : 2016년 03월 02일 ~ 2016년 11월 12일



<table class="tg">
  <tr>
    <th class="tg-hgcj" rowspan="2">구분</th>
    <th class="tg-hgcj" rowspan="2">추진내용</th>
    <th class="tg-amwm" colspan="9">수행일정</th>
  </tr>
  <tr>
    <td class="tg-yw4l">4월</td>
    <td class="tg-yw4l">5월</td>
    <td class="tg-yw4l">6월</td>
    <td class="tg-yw4l">7월</td>
    <td class="tg-yw4l">8월</td>
    <td class="tg-yw4l">9월</td>
    <td class="tg-yw4l">10월</td>
    <td class="tg-yw4l">11월</td>
    <td class="tg-yw4l">12월</td>
  </tr>
  <tr>
    <td class="tg-hgcj">계획</td>
    <td class="tg-s6z2">통합센서기기구성 및 App 개발 방안</td>
    <td class="tg-nii2">O</td>
    <td class="tg-v7o9"></td>
    <td class="tg-v7o9"></td>
    <td class="tg-2usb"></td>
    <td class="tg-2usb"></td>
    <td class="tg-2usb"></td>
    <td class="tg-v7o9"></td>
    <td class="tg-2usb"></td>
    <td class="tg-v7o9"></td>
  </tr>
  <tr>
    <td class="tg-amwm">분석</td>
    <td class="tg-baqh">Arduino 활용방법, 센서원리 및 BT통신</td>
    <td class="tg-lwkq">O</td>
    <td class="tg-lwkq">O</td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-hgcj" rowspan="2">설계</td>
    <td class="tg-s6z2">통합형센서기기</td>
    <td class="tg-baqh"></td>
    <td class="tg-lwkq">O</td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-s6z2">Android App</td>
    <td class="tg-baqh"></td>
    <td class="tg-lwkq">O</td>
    <td class="tg-lwkq">O</td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-amwm" rowspan="3">개발</td>
    <td class="tg-baqh">Arduino보드 활용 통합 센서기기 구현</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-lwkq">O</td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-baqh">통합 센서기기에서 BT를 통한 Data전송</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-lwkq">O</td>
    <td class="tg-52dw">O</td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-baqh">스마트기기용 Data처리 App.개발</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-52dw">O</td>
    <td class="tg-52dw">O</td>
    <td class="tg-52dw">O</td>
    <td class="tg-lwkq">O</td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-amwm">테스트</td>
    <td class="tg-baqh">통합 센서기기와 스마트기기 App. 연동</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-lwkq">O</td>
    <td class="tg-52dw">O</td>
    <td class="tg-baqh"></td>
  </tr>
  <tr>
    <td class="tg-amwm">종료</td>
    <td class="tg-baqh">프로젝트 완료 보고서 준비</td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-baqh"></td>
    <td class="tg-yw4l"></td>
    <td class="tg-lwkq">O</td>
  </tr>
</table>

### 1.4 연구 배경
- 사고방지를 위한 시설투자 대비 반복적인 사고 발생
- 사고 발생시 막대한 사고 처리비용 필요
- 사고를 미연에 방지할 수 있는 새로운 안전 시스템 구축 필요



### 1.5 관련 연구
- 인텔 & 하니웰 : Connected Worker Solution
> http://www.intel.com/content/www/us/en/events/corporate/cebit-safer-industrial-workers.html

- IJIRCCE : 야간운전자를 위한 IoT기반 사고 예방 시스템
> http://www.ijircce.com/upload/2015/april/101_45_AN.pdf

- FUJITSU : IoT기반 사고 예방 및 근로자 동선 개선 솔루션
> http://journal.jp.fujitsu.com/en/2016/01/22/01/


## 2. 본론
### 2.1 기본이론
○ ADC
일반적인 센서는 ADC (Analog to Digital Converter – 아날로그 디지털 변환기)를 의미한다. 자연에 존재하는 아날로그 신호는 컴퓨터가 처리하지 못하므로 이를 해결하기 위해 아닐로그 신호를 디지털 신호로 처리하는 것이다.

ADC의 동작과정은 표본화(Sampling), 양자화(Quantizing), 부호화(Binary Encoding)을 거친다. 표본화는 시간축 방향에서 일정 간격으로 신호를 추출하여 이산신호로 변환시키는 과정을 의미하며 양자화는 표본화된 진폭치를 특정 대푯값으로 바꾸는 과정이다. 마지막으로 부호화는 양자화된 신호를 디지털 코드 형태로 변환하는 과정을 의미한다.

ADC의 가장 중요한 요인은 분해능(Resolution)으로 아날로그 신호를 디지털 신호로 변환할 수 있는 범위를 뜻한다. (입력 범위와는 별개)

예를 들어 8bit의 경우 0~255 단계(-128~127)를 나타낼 수 있다. 분해능이 높을수록 범위가 넓어지고 정확도가 높아진다. 센서중에는 출력이 아날로그(전압)인 경우도 있으며 이때는 분해능이 높을수록 전압 단계가 세세하게 나누어진다. Arduino의 분해능은 10bit이며 0~1023 단계만큼 처리가 가능하다.


○ Bluetooth Classic / BLE(Bluetooth Low Energy)
개인 근거리 무선 통신을 위한 산업 표준. 2.4 GHz ISM(Industry-Science-Medical)대역을 사용하며 2.1Mbps의 대역폭을 가진다. Classic의 경우 v2.x를 의미하며 초창기 스마트기기에 적용되어있다. BLE의 경우 v4.x를 의미하며 Classic에 비해 저전력에 초점을 두어 만들어졌다. 프로토콜 스택이 거의 다르기 때문에 사실상 별도의 프로토콜로 볼 수 있다.


Bluetooth Classic의 경우 연결을 위해 Socket 연결이 필요하며 BLE(Bluetooth Low Energy)연결시 GATT 설정을 변경하는 것으로 연결이 가능하다.

본 솔루션에는 센서 기기에서 스마트기기로 데이터를 전송할 때 사용되며 센서 기기에 부 착된 Bluetooth 통신 모듈에 따라 Classic, BLE로 나누어진다. 스마트 기기에서는 Classic, BLE 둘다 연결이 가능하다.



### 2.2 시스템 구성
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/solution_pic.JPG?token=ASX2deRLjV8g0H5zrZN79yKUqDSXNEN6ks5YpUJ6wA%3D%3D">

솔루션은 크게 센서 디바이스, 스마트디바이스로 구성된다. 센서 디바이스는 작업환경을 센서를 통해 측정하며 측정된 데이터는 Bluetooth를 통해 스마트 디바이스로 전송된다. 스마트 디바이스에는 Android Application이 구동되어 수신받은 데이터를 바탕으로 수치를 관측, 및 DB에 저장한다. 관측도중 이상이 있을 경우 사용자에게 경고를 실시한다.

<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/device_pic.JPG?token=ASX2dQyzUeWwVbtN0o3XOE039wPOpVTDks5YpUNWwA%3D%3D">
Sensor Device : 온도, 습도, 일산화탄소, 산소센서, Bluetooth 통신 모듈이 하나로 결합되어 산업 현장의 환경 데이터를 측정한다. 측정한 데이터는 Bluetooth 통신 모듈을 통해 1초에 한번씩 스마트 기기로 전송된다.


Smart Device : 센서 기기에서 보내오는 환경 데이터를 전달받아 처리하는 Android Application이 구동된다. 데이터를 DB에 저장하고 관측되는 데이터를 바탕으로 이상이 있을 경우 작업자에게 경고를 실시한다. 센서 기기는 최대 7개까지 연결 가능하다.

### 2.3 하드웨어 구성

하드웨어는 센서 기기와 Android Application을 구동하는 스마트 기기로 나누어진다.

<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/sensor_pic.JPG?token=ASX2dVXh9O5MrmAw-ql7zXKUzNv7YGPrks5YpUOUwA%3D%3D">

명칭 : Sensor Device

전원 : 5v~12v 2A USB Type-B

|         명칭         	|                                                                                 역할                                                                                 	|
|:--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|     Arduino Mega     	| 센서에서 읽어 들이는 Data 처리 및 Bluetooth 모듈과 센서 연결.                                                                                                        	|
|         MQ-7         	| 일산화탄소 센서. 센서 표면의 일산화탄소 농도를 측정정상적인 조건하에서 20ppm~2000ppm 까지 측정가능https://www.sparkfun.com/datasheets/Sensors/Biometric/MQ-7.pdf     	|
|        HT-01D        	| 온습도 센서. 온도와 습도를 측정.정상적인 조건하에서 온도 –40~120 / 습도 0~100% 까지 측정가능http://www.micosnp.com/data/HT-01D-140403-DATASHEET.pdf                  	|
| Winsen Oxygen Sensor 	| 산소농도 센서. 센서 표면의 산소 농도를 측정정상적인 조건하에서 0~25% 측정이 가능http://eleparts.co.kr/data/design/product_file/SENSOR/gas/ME2-O2-20Manual_ver1_2.pdf 	|


### 2.4 소프트웨어 구성 및 주요 알고리즘
소프트웨어에는 센서 기기 프로그램과 스마트 기기에서 구동되는 Android Application이 있다.
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/connection_flow.JPG?token=ASX2dUmFqVvYTeO7jhnCmMtMEASr8X9Nks5YpUR5wA%3D%3D">

센서 기기는 스마트 기기로 최소 8Byte에서 최대 20Byte 길이의 환경 데이터를 전송한다. 전송 주기는 미리 1초로 설정되어있다.
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/sensor_protocol.JPG?token=ASX2dQ-vAPE-Q9J8pbJlkTJzOox8Ul16ks5YpUSXwA%3D%3D">



| 명칭         	| 최소    	| 최대      	| 설명                                       	|
|--------------	|---------	|-----------	|--------------------------------------------	|
| Protocol Ver 	| 0x00(0) 	| 0xFE(254) 	| 프로토콜 버전을 나타낸다                   	|
| Board Ver    	| 0x00(0) 	| 0xFE(254) 	| 보드 버전을 나타낸다.                      	|
| Sensor Count 	| 0x01(1) 	| 0x04(4)   	| 센서 디바이스의 장착 센서 개수를 나타낸다. 	|
| Sensor Id    	| 0x01(1) 	| 0x04(4)   	| 센서 종류를 나타낸다.                      	|
|              	|         	|           	| 0x00 : 일산화탄소                          	|
|              	|         	|           	| 0x01 : 온도                                	|
|              	|         	|           	| 0x02 : 습도                                	|
|              	|         	|           	| 0x03 : 산소                                	|
| Sensor Data  	|         	|           	| 센서 데이터를 나타낸다.                    	|


○ 스마트 기기 – Android Application
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/whole_flow.JPG?token=ASX2dbQ0Masl2HXodrXUh-5AJgdg0VLxks5YpUlHwA%3D%3D">

| 항목                      	| 설명                                                                                                                                               	|
|---------------------------	|----------------------------------------------------------------------------------------------------------------------------------------------------	|
| Sensor Device Scan        	| 스마트 기기 근처의 센서 기기를 탐색한다. 약 18초동안 검색을 실시하며 검색된 기기는 화면에 표시한다.                                                	|
| Set Connection            	| 센서 기기와 연결을 시도한다. Bluetooth Classic, BLE(Bluetooth Low Energy) 각각 별도의 연결 과정을 거친다.                                          	|
| Monitoring & Data Logging 	| 센서 기기에서 보내오는 데이터 (Byte Array)를 해석한다. 해석한 데이터를 DB에 기록하며 수치에 이상이 있을 경우 Warning Service에 경고 요청을 보낸다. 	|
| Warning Service           	| 경고 요청을 받은 Warning Service는 초기 설정에 따라 Notification 혹은 음성 경고를 실시한다.                                                        	|
| Visualization             	| DB에 기록된 데이터를 표, 그래프로 표현한다.                                                                                                        	|


○ 센서 기기 Scan & 연결 수립
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/bluetooth_flow.JPG?token=ASX2ddVp6eM5FKqEvrML-QPcJ7D7EAIDks5YpUkzwA%3D%3D">

| 항목                     	| 설명                                                                                                                          	|
|--------------------------	|-------------------------------------------------------------------------------------------------------------------------------	|
| Scan Start               	| 주변의 센서 기기를 탐색한다.                                                                                                  	|
| Device Select            	| 사용자가 화면에 표시된 센서 기기를 선택한다.                                                                                  	|
| Socket Connection        	| 선택 기기가 Bluetooth Classic인 경우 Bluetooth Socket Connection을 시도한다.                                                  	|
| GATT Notification Listen 	| 선택 기기가 BLE(Bluetooth Low Energy)인 경우 GATT Notification을 활성화시켜 해당 기기가 보내는 데이터를 수신 상태로 전환한다. 	|
| Byte Process             	| Byte Array를 Data로 변환한다.

○ Data Process & Warning Service
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/data_flow.JPG?token=ASX2dRmCOiBTjANO6tdxDWfuA3yXgY4Xks5YpUl0wA%3D%3D">

| 항목            	| 설명                                                                                                                                            	|
|-----------------	|-------------------------------------------------------------------------------------------------------------------------------------------------	|
| Processed Data  	| Byte Array가 Data로 변환된 상태. 바로 DB에 저장된다.                                                                                            	|
| Check Condition 	| 사전에 설정된 조건들을 검사하여 Data가 조건들을 넘어가는지 검사한다. 만약 넘어갈시 Warning Service에 경고를 요청한다.                           	|
| Warning Service 	| 요청에 따라 경고를 실시한다. 사전 설정에 따라 Android의 Notification을 할 것인지 TTS(Text to Speech) 알림을 할 것인지 판단하고 경고를 실행한다. 	|

## 3. 결과
### 3.1 동작결과




<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/init_pic.JPG?token=ASX2dVHV-xBP04bqpwxYFhBAw0EQlF0Hks5YpUmMwA%3D%3D">

(좌) Application 초기 화면 (우) Navi Menu

Application을 초기 구동하면 좌측의 화면이 가장 먼저 표시된다. 각 기능을 이용하기 위해서는 좌측 상단의 매뉴 버튼을 눌러 Navi 메뉴를 호출한다. 우측의 메뉴를 통해 각 기능을 사용할 수 있다.


○ Connect Sensor

<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/connect_1.png?token=ASX2dTvebzcNL3_cGV2yym1hiRLJFoZgks5YpUmswA%3D%3D" width="270" height="480" >
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/connect_2.png?token=ASX2dWnsqzTHruIxa1k9dBM4z-ZXti9Wks5YpUm2wA%3D%3D" width="270" height="480" >
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/connect_3.png?token=ASX2dRDVRKO8SjFriKP3KM95BhUU_j8Jks5YpUnDwA%3D%3D" width="270" height="480" >

센서 기기 연결 과정.


스마트 기기의 Bluetooth를 ON/OFF 할 수 있는 스위치를 통해 현재 스마트 기기의 Bluetooth 작동유무를 확인할 수 있다.(좌) SCAN 버튼을 누르면 주변의 센서 기기를 스캔하여 화면에 표시한다.(중) 기기 명을 누르면 해당기기와 연결 및 페어링을 할 것인지 선택할수 있다. 단 연결을 위해서는 페어링이 선행되어야한다. (우) BLE 모듈을 사용하는 센서 기기는 페어링 유무와 상관없이 연결된다.


<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/connected.png?token=ASX2dZJ-rBmcxhiHouwuh3b7NgtPgHPVks5YpUnawA%3D%3D" width="270" height="480" >

센서 기기와
연결이 완료된 상태


메인 화면에는 연결된 센서 기기 목록과 해당 데이터가 실시간으로 출력된다.
○ Device Info

<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/device_info_1.png?token=ASX2dfi1yiXGaSlYEwqeyAPXwgPB2XCHks5YpUn5wA%3D%3D" width="270" height="480" >
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/device_info_2.png?token=ASX2dSIpjECCiojf8P6yzOfJe1IOpUhhks5YpUjowA%3D%3D" width="270" height="480" >

Device Info 실행 화면

Device Info를 통해 연결되었던 센서 기기의 로그를 확인할수 있다.

○ Stats

<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/stats_pic.JPG?token=ASX2dRQlXycbO95Ta-stJRF6gFWZR92Rks5YpUoKwA%3D%3D">

Stats 선택 화면
(좌) Chart 옵션 (우) 실제 표시된 Chart


DB에 저장된 데이터를 바탕으로 시각화를 할 수 있다. 그래프 라이브러리를 이용하여 구현하였으며 https://github.com/PhilJay/MPAndroidChart
 그래프로 확인하고자 하는 요소와 센서 기기, 기간을 설정하면(좌) 그래프로 표시해준다.(우)


○ AlertConfig

<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/alert_2.png?token=ASX2dfXNfLINPcBOXOZAYIBjR-26htzzks5YpUoswA%3D%3D" width="270" height="480" >
<img src="https://raw.githubusercontent.com/momoci99/smartDeviceApp/master/READMEpic/alert_1.png?token=ASX2dd2-uXZwcorqQXfCJB173hfaLUGsks5YpUocwA%3D%3D" width="270" height="480" >

(좌) 경고 설정 (우) Notification이 동작한 화면

TTS(Text to Speech) 경고 및 Notification 경고 둘 다 선택하거나 별도로 선택가능하다. (좌)각 항목별로 임계치를 설정해 줄 수 있으며 필요에 따라 요소에 대한 모니터링을 활성화 / 비활성화가 가능하다. 임계치를 넘게 되면 (우)경고가 동작된다.


### 3.2 결과분석

○ 목표한 기능은 대부분 제대로 작동하는 것을 확인하였다. 다만 개선해야할 사항이 많이 발견됨. 추후에 상용화를 한다면 반드시 개선되어야할 부분이다.

○ 본래 Bluetooth 특성상 1 기기에 최대 7대의 기기가 연결이 가능하나 3대 이상의 센서 기기를 연결할 경우 Android Application의 처리 성능이 저하된다. 이는 Android Application의 성능 개선이 필요한 부분이다.

○ 센서 기기에 현재 동작중인지 어떤 상태인지 알려주는 표시장치가 없어서 테스트할 때 어려움이 있었음. Android Application에는 연결된 센서 기기의 상태만을 알 수 있기 때문에 상태 확인에 제한이 있다.

○ Android Application 전체적인 UI가 텍스트 위주고 사용자에 대한 배려가 부족하여 사용하기가 어렵다. UI를 고칠 필요성이 있다.

○ Android Application을 장시간 구동시 Battery가 빠르게 소모되는 현상이 관측되었다. 이는 Android Application의 내부 처리 알고리즘에 문제가 있다고 판단된다.




## 4. 결론

본 솔루션을 통해 산업현장에서 일어날 수 있는 위험들을 예지하고 예방하는데에 도움이 될수 있다고 판단된다. 또한 산업 현장뿐만 아니라 솔루션에 적용된 IoT 기술을 이용하여 식당, 학교, 병원 등에서도 활용이 가능할 것이다.


### 4.1 앞으로의 연구방향


본 솔루션을 통해 수집된 데이터는 RAW 데이터이기 때문에 의미가 있는 데이터는 아니다. 빅 데이터 처리 기술을 활용하여 장기간 데이터를 축적 및 분석할 필요가 있다. 또한 UX를 연구하여 사용하는데에 최대한 불편함이 없게끔 UI를 구성하는 것도 필요하다.

