이 문서는 메가 커피 교육 직원들의 프로그램 관리에 필요한 Rest API Spec을 기술한 문서입니다.

아래 정보들을 필수로 확인 및 참고해주시길 바랍니다.

# Http Method

Rest API에서 사용하는 Http Method는 표준 Http와 Rest 규약을 따릅니다.

|          |                                |
|----------|--------------------------------|
| Method   | 설명                             |
| `GET`    | 리소스를 가져올 때 사용                  |
| `POST`   | 새 리소스를 만들 때 사용                 |
| `PUT`    | 기존 리소스 Spec 전체를 수정할 때 사용       |
| `PATCH`  | 기존 리소스의 일부를 수정할 때 사용           |
| `DELETE` | 기존 리소스를 삭제할 때 사용               |

# Http 상태 코드

Rest API에서 사용하는 Http StatusCode는 표준 Http와 Rest 규약을
따릅니다.

|                             |                                |
|-----------------------------|--------------------------------|
| 상태 코드                       | 설명                             |
| `200 OK`                    | 요청을 성공적으로 처리                   |
| `201 Created`               | 새 리소스를 성공적으로 생성                |
| `204 No Content`            | 성공적으로 요청한 리소스 처리               |
| `400 Bad Request`           | 잘못된 요청                         |
| `404 Not Found`             | 요청한 리소스가 존재하지 않음               |
| `409 Conflicts`             | 요청한 리소스가 비즈니스 처리 과정에서 충돌됨      |
| `500 Internal Server Error` | 예기치 못한 서버 에러 발생                |

# 오류

Input Validation 에러와 비즈니스 에러로 구분됩니다.

## Input Validation

Http 상태코드는 400 Bad Request로 동일하게 응답됩니다.

[Example]

```json
{
    "code": "input_validation",
    "message": "An error occurred in the process of validating the input value.",
    "errors": [
        {
            "field": "email",
            "message": "이메일 형식이 아닙니다."
        }
    ]
}
```

## Business Validation

[Example]

```json
{
    "code": "already_exist",
    "message": "이미 존재하는 유저입니다.",
    "errors": []
}
```

## Error Code

비즈니스 에러 코드에 따라 Http 상태코드가 다릅니다.

|       |                  |                      |
|-------|------------------|----------------------|
| 상태 코드 | 코드               | 설명                   |
| 400   | input_validation | 요청한 입력값이 잘못된 경우      |
| 400   | illegal_argument | 리소스 핸들링중 요청값이 잘못된 경우 |
| 400   | already_exist    | 이미 존재하는 리소스인 경우      |
| 404   | not_found        | 리소스가 존재하지 않을 경우      |
| 409   | already_full     | 리소스가 이미 가득차 있는 경우    |
| 409   | conflict_field   | 리소스가 비즈니스적 충돌이 있는 경우 |
| 409   | expired_field    | 만료된 경우               |
| 409   | not_register     | 리소스를 등록할 수 없는 경우     |

# API 공통

요청, 응답간 공통으로 적용된 부분에 대해 설명합니다.

## Response

응답 Spec중 Header에 Trace Id가 포합됩니다.

|                     |                |
|---------------------|----------------|
| header              | 설명             |
| x-meloning-trace-id | API 요청을 추적할 ID |

<SecurityDefinitions />
