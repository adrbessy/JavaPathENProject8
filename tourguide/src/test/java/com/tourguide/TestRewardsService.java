package com.tourguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRewardsService {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  TourGuideService tourGuideService;

  @Autowired
  RewardsService rewardsService;

  @BeforeAll
  public static void setUp() {
    InternalTestHelper.setInternalUserNumber(1);
  }

  @Test
  public void a_userGetRewards() {
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    System.out.println("user : " + user);
    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    System.out.println("attraction : " + attraction);
    user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
    System.out.println("user : " + user);
    rewardsService.calculateRewards(user);
    System.out.println("user : " + user);
    List<UserReward> userRewards = user.getUserRewards();
    tourGuideService.tracker.stopTracking();
    System.out.println("userRewards.size() : " + userRewards.size());
    assertTrue(userRewards.size() == 1);
  }

  @Test
  public void b_isWithinAttractionProximity() {
    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
  }


  @Test
  public void c_nearAllAttractions() {
    rewardsService.setProximityBuffer(Integer.MAX_VALUE);

    rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
    List<UserReward> userRewards = rewardsService.getUserRewards(tourGuideService.getAllUsers().get(0));
    tourGuideService.tracker.stopTracking();
    assertEquals(mGpsUtilProxy.getAttractions().size(), userRewards.size());
  }

}
