package com.nhnacademy.bookstoreuserapi.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        final String userIdSecuritySchemeName = "X-USER-ID";
        final String ownerTypeSecuritySchemeName = "X-OWNER-TYPE";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(userIdSecuritySchemeName))
                .components(new Components()
                        .addSecuritySchemes(userIdSecuritySchemeName, new SecurityScheme()
                                .name(userIdSecuritySchemeName)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("사용자 ID (예: testuser)"))
                        .addSecuritySchemes(ownerTypeSecuritySchemeName, new SecurityScheme()
                                .name(ownerTypeSecuritySchemeName)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("소유자 타입 (예: USER, GUEST)")))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("User-API Swagger")
                .description("BeanSolid-BookStore의 유저에 관한 REST API")
                .version("1.0.0");
    }
}
