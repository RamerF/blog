package io.github.ramerf.blog.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** @author ramer */
@Configuration
@EnableSwagger2
@ConditionalOnExpression(
    "!'${spring.profiles.active}'.equals('prod')&&'!${spring.profiles.active}'.equals('pre')")
public class SwaggerConfig {
  @Value("${spring.swagger.enable:true}")
  private boolean enableSwagger;

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .enable(enableSwagger)
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.github.ramerf.blog"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("项目接口文档")
        .description("api 根目录：http://192.168.0.98:8081/")
        .contact(new Contact("Tang Xiaofeng", "", ""))
        .version("1.0")
        .build();
  }
}
