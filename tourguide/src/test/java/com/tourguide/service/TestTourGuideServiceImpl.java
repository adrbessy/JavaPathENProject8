package com.tourguide.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceTripPricerProxy;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class TestTourGuideServiceImpl {

  @Autowired
  private TourGuideService tourGuideService;

  @Mock
  private MicroserviceGpsUtilProxy mGpsUtilProxyMock;

  @Mock
  MicroserviceTripPricerProxy TripPricerProxyMock;

  @Mock
  private RewardsService rewardsServiceMock;

  @Test
  public void testGetUserLocation() {
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
        new Date());
    user.addToVisitedLocations(visitedLocation);

    VisitedLocation result = tourGuideService.getUserLocation(user);
    assertThat(result).isEqualTo(visitedLocation);
  }

  /*
   * @Test public void testGetUserLocationIfNoVisitedLocation() { User user = new
   * User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com"); Location location
   * = new Location(0.1, 0.2); VisitedLocation visitedLocation = new
   * VisitedLocation(UUID.randomUUID(), location, new Date());
   * 
   * when(mGpsUtilProxyMock.getUserLocation(user.getUserId())).thenReturn(
   * visitedLocation);
   * doNothing().when(rewardsServiceMock).calculateRewards(user);
   * 
   * VisitedLocation result = tourGuideService.getUserLocation(user);
   * assertThat(result).isEqualTo(visitedLocation); }
   */

  @Test
  public void testAddUserAndGetUser() {
    InternalTestHelper.setInternalUserNumber(0);

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
  public void getAllUsers() {
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(mGpsUtilProxyMock, rewardsServiceMock);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    List<User> allUsers = tourGuideService.getAllUsers();

    tourGuideService.tracker.stopTracking();

    assertTrue(allUsers.contains(user));
    assertTrue(allUsers.contains(user2));
  }

  /*
   * @Test public void getTripDeals() {
   * InternalTestHelper.setInternalUserNumber(0);
   * 
   * User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
   * Provider provider = new Provider(UUID.randomUUID(), "merry trip", 56666);
   * 
   * List<Provider> providerList = new ArrayList<>(); providerList.add(provider);
   * 
   * when(TripPricerProxyMock.getPrice(org.mockito.ArgumentMatchers .anyString(),
   * org.mockito.ArgumentMatchers .any(), org.mockito.ArgumentMatchers .anyInt(),
   * org.mockito.ArgumentMatchers .anyInt(), org.mockito.ArgumentMatchers
   * .anyInt(), org.mockito.ArgumentMatchers .anyInt())).thenReturn(providerList);
   * 
   * List<Provider> providers = tourGuideService.getTripDeals(user);
   * 
   * tourGuideService.tracker.stopTracking();
   * 
   * assertEquals(1, providers.size()); }
   */

}
