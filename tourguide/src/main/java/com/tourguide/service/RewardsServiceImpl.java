package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceRewardCentralProxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardsServiceImpl implements RewardsService {

  private static final Logger logger = LogManager.getLogger(RewardsServiceImpl.class);

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  MicroserviceRewardCentralProxy mRewardCentralProxy;

  @Autowired
  AttractionService attractionService;

  private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;


  private int attractionProximityRange = 200;

  public RewardsServiceImpl() {

  }

  /**
   * Calculate the rewards with the visitedLocations of the user
   * 
   * @param user An user
   */
  @Override
  public void calculateRewards(User user) {
    logger.debug("in the method calculateRewards in the class RewardsServiceImpl");
    CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
    userLocations.addAll(user.getVisitedLocations());
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

  /**
   * Check if a location is within an attraction proximity.
   * 
   * @param attraction An attraction
   * @param location   A location
   * @return true if the attraction is enough near from the location.
   */
  @Override
  public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
    logger.debug("in the method isWithinAttractionProximity in the class RewardsServiceImpl");
    return getDistance(attraction, location) > attractionProximityRange ? false : true;
  }

  /**
   * Calculate the distance between two locations.
   * 
   * @param loc1 A location
   * @param loc2 A location
   * @return the distance.
   */
  @Override
  public double getDistance(Location loc1, Location loc2) {
    logger.debug("in the method getDistance in the class RewardsServiceImpl");
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

  /**
   * Retrieve the list of rewards of an user.
   * 
   * @param user An user
   * @return the list of rewards.
   */
  @Override
  public List<UserReward> getUserRewards(User user) {
    logger.debug("in the method getUserRewards in the class RewardsServiceImpl");
    return user.getUserRewards();
  }

}
