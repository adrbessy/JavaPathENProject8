package com.tourguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;

@SpringBootTest()
public class TestRewardsService {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  TourGuideService tourGuideService;

  @Autowired
  RewardsService rewardsService;

  @Test
  public void userGetRewards() {

    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
    rewardsService.calculateRewards(user);
    List<UserReward> userRewards = user.getUserRewards();
    tourGuideService.tracker.stopTracking();
    assertTrue(userRewards.size() == 1);
  }

  @Test
  public void isWithinAttractionProximity() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
  }


  @Test
  public void nearAllAttractions() {
    rewardsService.setProximityBuffer(Integer.MAX_VALUE);

    InternalTestHelper.setInternalUserNumber(1);

    String userName = "testUser";
    String phone = "000";
    String email = userName + "@tourGuide.com";
    User user = new User(UUID.randomUUID(), userName, phone, email);

    List<Attraction> attractions = mGpsUtilProxy.getAttractions();
    for (Attraction attraction : attractions) {
      user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
          new Location(attraction.latitude, attraction.longitude), null));
    }

    rewardsService.calculateRewards(user);
    List<UserReward> userRewards = tourGuideService.getUserRewards(user);
    tourGuideService.tracker.stopTracking();
    assertEquals(mGpsUtilProxy.getAttractions().size(), userRewards.size());
  }

}
