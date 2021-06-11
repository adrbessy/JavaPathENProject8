package com.tourguide.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.AttractionService;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideServiceImpl;
import com.tourguide.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest()
public class TestRewardsServiceIT {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  RewardsService rewardsService;

  @Autowired
  AttractionService attractionService;

  @Autowired
  UserService userService;

  @BeforeAll
  public static void setUp() {
    InternalTestHelper.setInternalUserNumber(1);
  }

  @Test
  public void userGetRewards() {
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    attractionService.setProximityBuffer(10);
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
    rewardsService.calculateRewards(user);
    List<UserReward> userRewards = user.getUserRewards();
    tourGuideServiceImpl.tracker.stopTracking();
    assertTrue(userRewards.size() == 1);
  }

  @Test
  public void isWithinAttractionProximity() {
    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
  }


  @Test
  public void nearAllAttractions() {
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    attractionService.setProximityBuffer(Integer.MAX_VALUE);
    rewardsService.calculateRewards(userService.getAllUsers().get(0));
    List<UserReward> userRewards = rewardsService.getUserRewards(userService.getAllUsers().get(0));
    tourGuideServiceImpl.tracker.stopTracking();
    assertEquals(mGpsUtilProxy.getAttractions().size(), userRewards.size());
  }

}
