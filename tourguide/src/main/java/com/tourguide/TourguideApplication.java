package com.tourguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.tourguide")
public class TourguideApplication {

  public static void main(String[] args) {
    SpringApplication.run(TourguideApplication.class, args);
  }

}
