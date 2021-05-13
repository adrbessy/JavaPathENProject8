package com.tourguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tripPricer.Provider;

@SpringBootTest()
public class TestTourGuideService {

  @Test
  @Disabled
  public void getUserLocation() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
    tourGuideService.tracker.stopTracking();
    assertTrue(visitedLocation.userId.equals(user.getUserId()));
  }

  @Test
  @Disabled
  public void addUser() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    User retrivedUser = tourGuideService.getUser(user.getUserName());
    User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

    tourGuideService.tracker.stopTracking();

    assertEquals(user, retrivedUser);
    assertEquals(user2, retrivedUser2);
  }

  @Test
  @Disabled
  public void getAllUsers() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    List<User> allUsers = tourGuideService.getAllUsers();

    tourGuideService.tracker.stopTracking();

    assertTrue(allUsers.contains(user));
    assertTrue(allUsers.contains(user2));
  }

  @Test
  @Disabled
  public void trackUser() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

    tourGuideService.tracker.stopTracking();

    assertEquals(user.getUserId(), visitedLocation.userId);
  }

  @Test
  public void getNearbyAttractions() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
    System.out.println("visitedLocation : " + visitedLocation);
    List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
    System.out.println("attractions : " + attractions);
    tourGuideService.tracker.stopTracking();

    assertEquals(5, attractions.size());
  }

  @Test
  @Disabled
  public void getTripDeals() {
    GpsUtil gpsUtil = new GpsUtil();
    RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

    List<Provider> providers = tourGuideService.getTripDeals(user);

    tourGuideService.tracker.stopTracking();

    assertEquals(10, providers.size());
  }


}
