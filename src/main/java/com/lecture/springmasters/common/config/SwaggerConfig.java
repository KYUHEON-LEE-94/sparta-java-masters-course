package com.lecture.springmasters.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.lecture.springmasters.common.config fileName       : SwaggerConfig author :
 * LEE KYUHEON date           : 24. 12. 23. description    :
 * =========================================================== DATE              AUTHOR NOTE
 * -----------------------------------------------------------
 * 24. 12. 23.        LEE KYUHEON       최초 생성
 */
@OpenAPIDefinition(
    info = @Info(
        // index 페이지에 들어갈 제목
        title = "API Docs 예시",
        description = "Description",
        version = "v1"
    )
)

@Configuration
public class SwaggerConfig {

  private static final String BEARER_TOKEN_PREFIX = "Bearer";

  @Bean
  public OpenAPI openAPI() {
    String securityJwtName = "JWT";
    // API 요청에서 사용자가 인증해야 하는 방법(스키마)을 지정
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);
    // OpenAPI 문서에서 인증 방식과 관련된 정보를 저장
    Components components = new Components()
        .addSecuritySchemes(securityJwtName, new SecurityScheme()
            .name(securityJwtName)
            .type(SecurityScheme.Type.HTTP)
            .scheme(BEARER_TOKEN_PREFIX)
            .bearerFormat(securityJwtName)
        );

    return new OpenAPI()
        //API 요청마다 JWT 인증이 필요하다는 것을 명시
        .addSecurityItem(securityRequirement)
        //정의된 인증 스키마를 OpenAPI 문서에 포함.
        .components(components);
  }
}
