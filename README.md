# 메가커피 직원 관리 프로젝트 

이 프로젝트는 메가커피 매장들의 직원들 교육을 관리하는 프로젝트입니다.

**멀티모듈** 기술 기반으로 사이드 프로젝트 목적이 큰 프로젝트입니다.

핵심 기능은 매장 설립시 등록된 매장 내 직원들에게 교육 프로그램에 대한 알림 서비스를 제공하는 것이 핵심입니다.

## 요구사항

아래 요구사항 내용은 사이드 프로젝트 진행을 위해 가상으로 기획하여 정리한 내용입니다.

- [요구사항 문서](https://github.com/meloning/mega-coffee-employee-manage-project/wiki)

## 구조

### [v0.5]
### <시스템 아키텍처>
![사이드_프로젝트_v0.5_아키텍처.png](images%2F%EC%82%AC%EC%9D%B4%EB%93%9C_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_v0.5_%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98.png)

RabbitMQ를 통해 Producer인 API Server는 메시지를 발급하고, 
Consumer인 Event Server는 받은 메시지 타입에 따라 Event를 결정해 알림을 발송합니다.  

### <프로젝트 구조>
```text
project
 ├── clients
     └── java-email
 ├── event-consumer
      └── event-api    
 ├── mega-coffee-api
 ├── mega-coffee-common
 ├── mega-coffee-core
 └── mega-coffee-infra
      └── database
          └── mysql
      └── message-queue
          └── rabbitmq
```
![사이드_프로젝트_v0.5_구조.png](images%2F%EC%82%AC%EC%9D%B4%EB%93%9C_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_v0.5_%EA%B5%AC%EC%A1%B0.png)


**[Application 영역]**
- 실제 실행을 담당하는 모듈
- mega-coffee-api
- controller를 통한 입출력/검증 역할을 수행합니다.


**[Core 영역]**
- 도메인, 비즈니스 로직을 담당하는 모듈
- mega-coffee-core
- 변할 수 있는 Repository부분을 인터페이스로 정의하여 db가 바뀌거나 내부적으로 캐싱처리등 구체적인 구현을
  알필요 없도록 하였습니다.


**[Infra 영역]**
- database, 미들웨어등 타 App에 의존도가 높은 모듈
- db/mysql, message-queue/rabbitmq
- core에서 정의한 인터페이스의 구체적인 구현을 정의하고 활용합니다.
- DB의 경우 root entity, vo등 도메인 영역을 객체로 분리하여 응집력을 높입니다.


**[Clients 영역]**
- 외부 API 연동등 외부 App에 의존도가 높은 모듈
- clients/java-email
- 실제 외부 API연동과 관련한 로직을 구현하는 곳입니다.

## API 문서
REST Docs + epages 조합으로 OAS를 만들어 API 문서 링크를 공유할 예정입니다.


## Next 목표
**[v1.0]**

- 교육 장소별 교육 시작시간 1시간전 대상자들에게 알림을 발송하는 기능을 추가
  - Quartz의 트리거와 job 체이닝을 통해 구현
- RestAssured를 이용한 API 통합 테스트 작성
- Rest Docs + epages를 활용한 OAS 산출 및 API Document 작성
