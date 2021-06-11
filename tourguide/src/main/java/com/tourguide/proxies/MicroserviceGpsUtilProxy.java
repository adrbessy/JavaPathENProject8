package com.tourguide.proxies;

import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tourguide-gpsUtil", url = "http://localhost:9006")
public interface MicroserviceGpsUtilProxy {

  @GetMapping("/userLocation")
  VisitedLocation getUserLocation(@RequestParam("userId") UUID userId);

  @GetMapping("/attractions")
  List<Attraction> getAttractions();

}
