package io.github.ramerf.blog.system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Common bean.
 *
 * @author ramer
 */
@Configuration
@EnableSwagger2
public class CommonBean {

  /**
   * 支持Spring bean的自定义校验器,可直接注入Validator.
   *
   * <p>https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation-beanvalidation-spring.
   *
   * <pre>
   *   {@code @Resource private Validator commonValidator;}
   *    final Set<ConstraintViolation<TagRequest>> validate = commonValidator.validate(instance) }
   * </pre>
   *
   * @return the local validator factory bean
   */
  @Bean
  public LocalValidatorFactoryBean commonValidator() {
    return new LocalValidatorFactoryBean();
  }

  /**
   * Ok http client ok http client.
   *
   * @return the ok http client
   */
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool())
        .build();
  }

  /**
   * Message converter mapping jackson 2 http message converter.
   *
   * @return the mapping jackson 2 http message converter
   */
  @Bean
  public MappingJackson2HttpMessageConverter messageConverter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(new ObjectMapper());
    return converter;
  }

  /**
   * Password encoder password encoder.
   *
   * @return the password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    String idForEncode = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap<>(4);
    encoders.put(idForEncode, new BCryptPasswordEncoder());
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());
    return new DelegatingPasswordEncoder(idForEncode, encoders);
  }

  /**
   * Web server factory customizer web server factory customizer.
   *
   * @return the web server factory customizer
   */
  @Bean
  @SuppressWarnings("rawtypes")
  public WebServerFactoryCustomizer webServerFactoryCustomizer() {
    return (WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>)
        factory -> {
          ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/400");
          ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
          ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
          ErrorPage error500Page =
              new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/templateEngineException");
          factory.addErrorPages(error400Page, error401Page, error404Page, error500Page);
        };
  }
}
