package io.github.ramerf.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The type App controller.
 *
 * @author ramer
 */
@EntityScan({"io.github.ramerf.blog.entity.domain", "io.github.ramerf.blog.system.entity.domain"})
@SpringBootApplication
public class BlogApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(BlogApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(BlogApplication.class);
  }
}
