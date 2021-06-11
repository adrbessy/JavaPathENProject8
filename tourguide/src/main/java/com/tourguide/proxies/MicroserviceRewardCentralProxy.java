package com.tourguide.proxies;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tourguide-rewardCentral", url = "http://localhost:9007")
public interface MicroserviceRewardCentralProxy {

  @GetMapping("/attractionRewardPoints")
  public int getAttractionRewardPoints(@RequestParam("attractionId") UUID attractionId,
      @RequestParam("userId") UUID userId);

}
