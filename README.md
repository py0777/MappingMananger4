# MappingMananger4
## 1. 개요
## 2. 용도
## 3. 설정
### tomcat
![image](https://user-images.githubusercontent.com/7609848/129444161-6ff3b92d-a2af-4b5e-9731-47c370877d34.png)
톰캣을 다운받아 압축을 풀고 tomcat7w.exe 실행 후 
지정된 서비스가 설치된 서비스로는 없습니다. 문구 출력시
콘솔창 열기(cmd)
압축을 해제한 폴더에서 - 예) c:\tomcat7\bin  
서비스를 등록 :  
service.bat install tomcat7 입력 
(삭제: service.bat. remove tomcat7)
그 후 tomcat7w.exe를 실행한다. 
###접속
  url:8088/http://127.0.0.1:8088/MappingManager4/jsp/MappingManager.jsp
ex) http://127.0.0.1:8088/MappingManager4/jsp/MappingManager.jsp

### 오라클 테스트DB 생성
/*사용자생성*/
CREATE USER IMCUSR
IDENTIFIED BY 1
DEFAULT TABLESPACE USERS 
TEMPORARY TABLESPACE TEMP;

/*권한주기*/
GRANT CONNECT TO IMCUSR;
GRANT RESOURCE TO IMCUSR;
GRANT DBA TO IMCUSR;