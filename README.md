# GrowOrders

## 1. 패키지구조

```java

src/main/java
└─org
    └─example
        └─groworders
            ├─common
            │  ├─exception
            │  └─model
            ├─config
            │  ├─database
            │  ├─filter
            │  ├─oauth
            │  └─security
            ├─domain
            │  ├─inventory
            │  │  ├─controller
            │  │  ├─model
            │  │  ├─repository
            │  │  └─service
            │  ├─orders
            │  │  ├─controller
            │  │  ├─model
            │  │  ├─repository
            │  │  └─service
            │  └─users
            │      ├─controller
            │      ├─model
            │      ├─repository
            │      └─service
            └─utils
```

## 2. BaseResponse

- 응답 형식

```java

{
    "success": true,
    "message": "등록 성공",
    "data": {...}
}

```
- Sample Code

```java

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success; // 요청 성공 여부
    private String message;  // 메시지
    private T data;          // 응답 데이터

    // 성공 응답
    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .message("요청이 성공했습니다.")
                .data(data)
                .build();
    }

    // 성공 응답
    public static <T> BaseResponse<T> successMessage(String message) {
        return BaseResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }

    // 실패 응답
    public static <T> BaseResponse<T> fail(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}


```

## 3. DTO

```java

- Inner 클래스 형식

public class UserDto {
    @Getter
    public static class SignIn {
        private String email;
        private String password;
    }
}

```

## 4. 로그인 구현 방식

```java

- 필터 형식

HTTP Request (POST /login)
    │
    ▼
[Servlet Container]  ←─ 1. 서블릿 컨테이너 수준의 필터 (CorsFilter, LoggingFilter 등)
    │
    ▼
[DelegatingFilterProxy]  ←─ 2. "springSecurityFilterChain"으로 요청 전달 (Spring과 Servlet 연결)
    │
    ▼
[Spring Security Filter Chain]  ←─ 3. SecurityContext 초기화 + 커스텀/기본 필터 실행
    │   │
    │   ├─ [JwtAuthFilter]  ←─ 4. JWT 검증 (헤더/쿠키에서 토큰 추출)
    │   │
    │   ├─ [LoginFilter]  ←─ 5. `/login` 요청 시 인증 시도 (UsernamePasswordAuthenticationFilter 대체)
    │   │   │
    │   │   ├─ attemptAuthentication()  ←─ 6. UsernamePasswordAuthenticationToken 생성
    │   │   │
    │   │   ▼
    │   │   [AuthenticationManager (ProviderManager)]  ←─ 7. 인증 관리자 호출
    │   │       │
    │   │       ▼
    │   │       [DaoAuthenticationProvider]  ←─ 8. 실제 인증 수행
    │   │           │
    │   │           ├─ 1. userDetailsService.loadUserByUsername() 호출
    │   │           │    │
    │   │           │    ▼
    │   │           │    [CustomUserDetailsService]  ←─ 9. DB에서 사용자 조회 (컨트롤러 거치지 않음!)
    │   │           │        │─ UserDetails 반환 (이메일, 암호화된 비밀번호, 권한)
    │   │           │
    │   │           ├─ 2. passwordEncoder.matches()  ←─ 10. 비밀번호 검증
    │   │           │
    │   │           ▼
    │   │           [인증 성공] → Authentication 객체 반환
    │   │           [인증 실패] → AuthenticationException 발생
    │   │
    │   ▼
    │   [LoginFilter.successfulAuthentication()]  ←─ 11. JWT 생성 후 쿠키 저장 (SWY_AT)
    │
    ▼
[Spring Security Filter Chain]  ←─ 12. 추가 필터 처리 (SessionManagement, CSRF 등)
    │
    ▼
[DelegatingFilterProxy]  ←─ 13. 응답 후처리
    │
    ▼
[Servlet Container]  ←─ 14. Servlet Filter 응답 처리
    │
    ▼
HTTP Response (Set-Cookie: SWY_AT=xxx)  ←─ 15. 클라이언트에게 JWT 전달

```

## 5. 기능구현

- 로그인
- 주문
- 재고관리
- 결제
- ...

## 6. swagger-UI

- 준비 중... 