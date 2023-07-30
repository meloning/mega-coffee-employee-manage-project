# Event Consumer API 모듈

## 실행 방법
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: melon8372@gmail.com
    password: ${GMAIL_APP_PASSWORD}
```

- GMAIL_APP_PASSWORD 값을 Environment 영역에 추가해야 합니다.
- GMAIL 2단계 인증 페이지 내 <APP 비밀번호>를 생성할 수 있습니다.
