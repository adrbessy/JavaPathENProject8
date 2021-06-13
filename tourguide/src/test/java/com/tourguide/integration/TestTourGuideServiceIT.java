package com.tourguide.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.LocationService;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import com.tourguide.service.TourGuideServiceImpl;
import com.tourguide.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest()
public class TestTourGuideServiceIT {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  TourGuideService tourGuideService;

  @Autowired
  RewardsService rewardsService;

  @Autowired
  LocationService locationService;

  @Autowired
  UserService userService;

  @Test
  public void getTrackUserLocation() {
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    VisitedLocation visitedLocation = locationService.trackUserLocation(user);
    tourGuideServiceImpl.tracker.stopTracking();
    assertTrue(visitedLocation.userId.equals(user.getUserId()));
  }

  @Test
  public void getNearbyAttractions() {
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    VisitedLocation visitedLocation = locationService.trackUserLocation(user);
    List<NearbyAttractions> attractions = tourGuideService.getNearByAttractions(visitedLocation, user);
    tourGuideServiceImpl.tracker.stopTracking();
    assertEquals(5, attractions.size());
  }

  @Test
  public void getTripDeals() {
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    List<Provider> providers = tourGuideService.getTripDeals(user);
    tourGuideServiceImpl.tracker.stopTracking();
    assertEquals(5, providers.size());
  }


  @Test
  public void getLastSavedLocationAllUsers() {
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    User user = new User(UUID.randomUUID(), "Isabelle", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "Isabelle2", "000",
        "jon2@tourGuide.com");

    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
    user.addToVisitedLocations(visitedLocation);

    Location location2 = new Location(0.4, 0.9);
    VisitedLocation visitedLocation2 = new VisitedLocation(UUID.randomUUID(), location2, new Date());
    user2.addToVisitedLocations(visitedLocation2);

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    Map<String, Location> allCurrentLocations = tourGuideService.getLastSavedLocationForAllUsers();

    tourGuideServiceImpl.tracker.stopTracking();

    assertEquals(allCurrentLocations.get(user.getUserId().toString()), location);
    assertEquals(allCurrentLocations.get(user2.getUserId().toString()),
        location2);
  }

  @Test
  public void testAddUserAndGetUser() {
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideServiceImpl tourGuideServiceImpl = new TourGuideServiceImpl(mGpsUtilProxy, rewardsService);
    User user = new User(UUID.randomUUID(), "Adrien", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "Adrien2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    User retrivedUser = userService.getUser(user.getUserName());
    User retrivedUser2 = userService.getUser(user2.getUserName());

    tourGuideServiceImpl.tracker.stopTracking();

    assertEquals(user, retrivedUser);
    assertEquals(user2, retrivedUser2);
  }


}
