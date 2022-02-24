# sproutfx-oauth-authorization

* Project tree

  ```
  ├─ src
  │   ├─ main
  │   │   ├─ java/kr/sproutfx/oauth/authorization
  │   │   │   ├─ api
  │   │   │   │   ├─ authorize // 인증 관련 api
  │   │   │   │   │   ├─ controller
  │   │   │   │   │   ├─ enumeration
  │   │   │   │   │   ├─ exception
  │   │   │   │   │   └─ service
  │   │   │   │   ├─ client // Client CRUD
  │   │   │   │   │   ├─ controller
  │   │   │   │   │   ├─ entity
  │   │   │   │   │   ├─ enumeration
  │   │   │   │   │   ├─ exception
  │   │   │   │   │   └─ service
  │   │   │   │   ├─ member // Member CRUD
  │   │   │   │   │   ├─ controller
  │   │   │   │   │   ├─ entity
  │   │   │   │   │   ├─ enumeration
  │   │   │   │   │   ├─ exception
  │   │   │   │   │   └─ service
  │   │   │   │   └─ project # Project CRUD
  │   │   │   │       ├─ controller
  │   │   │   │       ├─ entity
  │   │   │   │       ├─ enumeration
  │   │   │   │       ├─ exception
  │   │   │   │       └─ service
  │   │   │   ├─ common
  │   │   │   │   ├─ advisor
  │   │   │   │   │   └─ ControllerAdvisor.java // Exception 처리
  │   │   │   │   ├─ dto
  │   │   │   │   │   └─ Response.java // API Response 객체
  │   │   │   │   ├─ entity
  │   │   │   │   │   └─ BaseEntity.java // DB 기본 columns
  │   │   │   │   └─ exception
  │   │   │   │       └─ BaseException.java // Custom exception
  │   │   │   ├─ configuration
  │   │   │   │   ├─ crypto // 암호화, 복호화
  │   │   │   │   │   └─ (생략)
  │   │   │   │   ├─ jpa // JPA 설정
  │   │   │   │   │   └─ (생략)
  │   │   │   │   ├─ jwt // JWT 발급/검증 등
  │   │   │   │   │   └─ (생략)
  │   │   │   │   └─ security // Spring security 설정
  │   │   │   │       └─ (생략)
  │   │   │   └─ AuthServiceApplication.java
  │   │   └─ resources
  │   │       ├─ keystore
  │   │       │   └─ sproutfx-oauth-authorization.jks
  │   │       └─ application.yml
  │   └─ test/java/kr/sproutfx/oauth/authorization
  │       └─ AuthServiceApplicationTests.java
  ├─ .gitignore
  ├─ .gitlab-ci-mvn-settings.xml
  ├─ .gitlab-ci.yml
  ├─ dockerfile
  ├─ pom.xml
  └─ README.md
  ```