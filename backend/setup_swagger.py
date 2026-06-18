import os
import re

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb"
pom_path = os.path.join(base_dir, "pom.xml")
swagger_config_path = os.path.join(base_dir, r"src\main\java\com\QuanLyDatBanNhaHang\demo\config\SwaggerConfig.java")
security_config_path = os.path.join(base_dir, r"src\main\java\com\QuanLyDatBanNhaHang\demo\security\SecurityConfig.java")

# 1. Update pom.xml
with open(pom_path, "r", encoding="utf-8") as f:
    pom_content = f.read()

if "springdoc-openapi-starter-webmvc-ui" not in pom_content:
    swagger_dep = """
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.5.0</version>
        </dependency>
"""
    pom_content = pom_content.replace("</dependencies>", f"{swagger_dep}    </dependencies>")
    with open(pom_path, "w", encoding="utf-8") as f:
        f.write(pom_content)

# 2. Create SwaggerConfig.java
os.makedirs(os.path.dirname(swagger_config_path), exist_ok=True)
with open(swagger_config_path, "w", encoding="utf-8") as f:
    f.write("""package com.QuanLyDatBanNhaHang.demo.config;

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
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("API Quản lý Nhà hàng")
                        .version("1.0")
                        .description("Tài liệu API cho Hệ thống Quản lý Nhà hàng (Web Booking & POS)"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
""")

# 3. Update SecurityConfig.java
with open(security_config_path, "r", encoding="utf-8") as f:
    sec_content = f.read()

if "/swagger-ui/**" not in sec_content:
    sec_content = sec_content.replace(
        '.requestMatchers("/api/auth/**", "/error").permitAll()',
        '.requestMatchers("/api/auth/**", "/error", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()'
    )
    with open(security_config_path, "w", encoding="utf-8") as f:
        f.write(sec_content)

print("Swagger setup completed.")
