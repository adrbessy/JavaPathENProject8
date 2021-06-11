package com.tourguide.proxies;

import com.tourguide.model.tripPricer.Provider;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tourguide-tripPricer", url = "http://localhost:9008")
public interface MicroserviceTripPricerProxy {

  @GetMapping("/price")
  public List<Provider> getPrice(@RequestParam("tripPricerApiKey") String tripPricerApiKey,
      @RequestParam("userId") UUID userId,
      @RequestParam("numberOfAdults") int numberOfAdults, @RequestParam("numberOfChildren") int numberOfChildren,
      @RequestParam("tripDuration") int tripDuration,
      @RequestParam("cumulatativeRewardPoints") int cumulatativeRewardPoints);

}
