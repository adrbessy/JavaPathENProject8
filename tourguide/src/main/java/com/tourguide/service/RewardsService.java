package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceRewardCentralProxy;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardsService {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  MicroserviceRewardCentralProxy mRewardCentralProxy;

  @Autowired
  AttractionService attractionService;

  private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;


  private int attractionProximityRange = 200;

  public RewardsService() {

  }

  public void calculateRewards(User user) {
    List<VisitedLocation> userLocations = user.getVisitedLocations();
    List<Attraction> attractions = mGpsUtilProxy.getAttractions();

    for (VisitedLocation visitedLocation : userLocations) {
      for (Attraction attraction : attractions) {
        if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName))
            .count() == 0) {
          if (attractionService.nearAttraction(visitedLocation, attraction)) {
            user.addUserReward(new UserReward(visitedLocation, attraction,
                mRewardCentralProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId())));
          }
        }
      }
    }

  }

  public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
    return getDistance(attraction, location) > attractionProximityRange ? false : true;
  }

  public double getDistance(Location loc1, Location loc2) {
    double lat1 = Math.toRadians(loc1.latitude);
    double lon1 = Math.toRadians(loc1.longitude);
    double lat2 = Math.toRadians(loc2.latitude);
    double lon2 = Math.toRadians(loc2.longitude);

    double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
        + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

    double nauticalMiles = 60 * Math.toDegrees(angle);
    double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    return statuteMiles;
  }

  public List<UserReward> getUserRewards(User user) {
    return user.getUserRewards();
  }

}
