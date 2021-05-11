package com.tourguide;

import static org.junit.Assert.assertTrue;
import com.tourguide.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import rewardCentral.RewardCentral;

@SpringBootTest()
public class TestRewardsService {

  /*
   * @Test public void userGetRewards() { GpsUtil gpsUtil = new GpsUtil();
   * RewardsService rewardsService = new RewardsService(gpsUtil, new
   * RewardCentral());
   * 
   * InternalTestHelper.setInternalUserNumber(0); TourGuideService
   * tourGuideService = new TourGuideService(gpsUtil, rewardsService);
   * 
   * User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
   * Attraction attraction = gpsUtil.getAttractions().get(0);
   * user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction,
   * new Date())); tourGuideService.trackUserLocation(user); List<UserReward>
   * userRewards = user.getUserRewards(); tourGuideService.tracker.stopTracking();
   * assertTrue(userRewards.size() == 1); }
   */

  @Test
  public void isWithinAttractionProximity() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    Attraction attraction = gpsUtil.getAttractions().get(0);
    assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
  }

  /*
   * @Disabled("Needs fixed - can throw ConcurrentModificationException")
   * 
   * @Test public void nearAllAttractions() { GpsUtil gpsUtil = new GpsUtil();
   * RewardsService rewardsService = new RewardsService(gpsUtil, new
   * RewardCentral()); rewardsService.setProximityBuffer(Integer.MAX_VALUE);
   * 
   * InternalTestHelper.setInternalUserNumber(1); TourGuideService
   * tourGuideService = new TourGuideService(gpsUtil, rewardsService);
   * 
   * rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
   * List<UserReward> userRewards =
   * tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
   * tourGuideService.tracker.stopTracking();
   * 
   * assertEquals(gpsUtil.getAttractions().size(), userRewards.size()); }
   */

}
