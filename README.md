# 메가커피 직원 관리 프로젝트 

이 프로젝트는 메가커피 매장들의 직원들 교육을 관리하는 프로젝트입니다.

**멀티모듈** 기술 기반으로 사이드 프로젝트 목적이 큰 프로젝트입니다.

핵심 기능은 매장 설립시 등록된 매장 내 직원들에게 교육 프로그램에 대한 알림 서비스를 제공하는 것이 핵심입니다.

## 요구사항

아래 요구사항 내용은 사이드 프로젝트 진행을 위해 가상으로 기획하여 정리한 내용입니다.

- [요구사항 문서](https://github.com/meloning/mega-coffee-employee-manage-project/wiki/v1.0%E2%80%90%EA%B8%B0%EB%8A%A5%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD%E2%80%90%EB%AC%B8%EC%84%9C)

## 구조

버전별 시스템 아키텍처와 프로젝트 모듈 구조를 wiki를 통해 정리하였습니다.

### <시스템 아키텍처>
- [v1.0 시스템 아키텍처](https://github.com/meloning/mega-coffee-employee-manage-project/wiki/v1.0%E2%80%90%EC%8B%9C%EC%8A%A4%ED%85%9C%E2%80%90%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98)

### <프로젝트 구조>
- [모듈 구조 설명](https://github.com/meloning/mega-coffee-employee-manage-project/wiki/%EB%AA%A8%EB%93%88%E2%80%90%EA%B5%AC%EC%A1%B0)

## 테스트
도메인별 유닛 테스트와 RestAssured + TestContainer를 조합한 API 통합 테스트를 중심으로 테스트 코드가 작성되어 있습니다.

TestContainer는 MySQL, RabbitMQ 모듈 각각에서 관리되도록 하여 응집력있는 테스트 설정을 구성하였고,
TestFixture를 통해 의존하는 타 모듈의 테스트 영역에 TestContainer를 적용할 수 있도록 구성하였습니다.

(조만간 Wiki 문서를 통해 정리하여 내용 공유할 예정입니다.)

## API 문서
REST Docs + epages + Redoc 조합으로 OAS를 만들어 API 문서를 생성하였습니다.

https://meloning.github.io/redoc-meloning-api/

## Reference
사이드 프로젝트 진행과정에서 학습 및 참고했던 레퍼런스입니다.

- [개발잼 유튜브](https://www.youtube.com/@devgem)
- [스프링러너의스프링아카데미:스프링부트로 구현하는 실전 멀티모듈프로젝트](https://fastcampus.co.kr/dev_academy_springrunner201) 
  - 수강 완료
