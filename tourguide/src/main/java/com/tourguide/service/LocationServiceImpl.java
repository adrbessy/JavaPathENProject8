package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

  private static final Logger logger = LogManager.getLogger(LocationServiceImpl.class);

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;
  @Autowired
  RewardsService rewardsService;

  /**
   * Localize the current location of an user, add it to the visitedLocationList,
   * then calculate rewards.
   * 
   * @param user An user
   * @return the new location
   */
  @Override
  public VisitedLocation trackUserLocation(User user) {
    logger.debug("in the method trackUserLocation in the class LocationServiceImpl");
    VisitedLocation visitedLocation = mGpsUtilProxy.getUserLocation(user.getUserId());
    user.addToVisitedLocations(visitedLocation);
    rewardsService.calculateRewards(user);
    return visitedLocation;
  }

}
