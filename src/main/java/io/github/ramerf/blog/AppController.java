package io.github.ramerf.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * The type App controller.
 *
 * @author ramer
 */
@EntityScan({"io.github.ramerf.blog.entity.domain", "io.github.ramerf.blog.system.entity.domain"})
@SpringBootApplication
public class AppController {
  public static void main(String[] args) {
    SpringApplication.run(AppController.class, args);
  }
}
