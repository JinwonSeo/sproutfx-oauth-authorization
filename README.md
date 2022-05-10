# sproutfx-oauth-authorization

* Project tree

  ```
  ├─ src
  │   ├─ main
  │   │   ├─ java/kr/sproutfx/oauth/authorization
  │   │   │   ├─ api
  │   │   │   │   ├─ authorize // 인증, 토큰 발급 관련 API
  │   │   │   │   │   └─ (생략)
  │   │   │   │   ├─ client // Client CRUD
  │   │   │   │   │   └─ (생략)
  │   │   │   │   ├─ member // Member CRUD
  │   │   │   │   │   └─ (생략)
  │   │   │   │   └─ project // Project CRUD
  │   │   │   │       └─ (생략)
  │   │   │   ├─ common
  │   │   │   │   ├─ advisor
  │   │   │   │   │   └─ RestControllerAdvisor.java // Exception 처리
  │   │   │   │   ├─ aspect
  │   │   │   │   │   └─ RestControllerAspect.java // Rest controller logging 
  │   │   │   │   ├─ base
  │   │   │   │   │   ├─ BaseController.java
  │   │   │   │   │   ├─ BaseEntity.java
  │   │   │   │   │   ├─ BaseException.java
  │   │   │   │   │   └─ BaseResponse.java
  │   │   │   │   └─ exception
  │   │   │   │       └─ BaseException.java // Custom exception
  │   │   │   ├─ configuration
  │   │   │   │   ├─ crypto // 암호화, 복호화
  │   │   │   │   │   └─ (생략)
  │   │   │   │   └─ jpa // JPA 설정
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

* Dependency

  ```xml
  <projects>
    <dependencies>
        <!-- Custom logging(using ELK) -->
        <dependency>
            <groupId>kr.sproutfx.common</groupId>
            <artifactId>sproutfx-common-logging-spring-boot-stater</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <!-- Security -->
        <dependency>
            <groupId>kr.sproutfx.common</groupId>
            <artifactId>sproutfx-common-security-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
  </projects>
  ```

* gitlab package registry 설정

  ```xml
  <!-- {maven_home}/conf/settings.xml -->
  <settings>
    <servers>
        <server>
            <id>gitlab-maven</id>
            <configuration>
                <httpHeaders>
                    <property>
                        <name>Private-Token</name>
                        <value>{PRIVATE_TOKEN}</value>
                    </property>
                </httpHeaders>
            </configuration>
        </server>
    </servers>
  
    <mirrors>
        <mirror>
            <id>gitlab-maven</id>
            <mirrorOf>snapshot, release</mirrorOf>
            <url>https://gitlab.com/api/v4/groups/{GROUP_ID}/-/packages/maven/</url>
        </mirror>
    </mirrors>
  </settings>
  ```

* Disable maven ssl validation options

  ```sh
  -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true
  ```