package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;
  @Autowired
  RewardsService rewardsService;

  public VisitedLocation trackUserLocation(User user) {
    VisitedLocation visitedLocation = mGpsUtilProxy.getUserLocation(user.getUserId());
    user.addToVisitedLocations(visitedLocation);
    rewardsService.calculateRewards(user);
    return visitedLocation;
  }

}
