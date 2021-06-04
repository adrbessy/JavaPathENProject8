package com.tourguide.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserPreferences;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceRewardCentralProxy;
import com.tourguide.proxies.MicroserviceTripPricerProxy;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.LocationService;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import com.tourguide.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
public class TestTourGuideServiceImpl {

  @Autowired
  private TourGuideService tourGuideService;

  @MockBean
  private MicroserviceGpsUtilProxy mGpsUtilProxyMock;

  @MockBean
  MicroserviceRewardCentralProxy mRewardCentralProxyMock;

  @Mock
  MicroserviceTripPricerProxy TripPricerProxyMock;

  @Mock
  private RewardsService rewardsServiceMock;

  @MockBean
  LocationService locationServiceMock;

  @MockBean
  UserService userServiceMock;

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


  @Test
  public void testGetUserLocationIfNoVisitedLocation() {
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());

    when(locationServiceMock.trackUserLocation(user)).thenReturn(
        visitedLocation);

    VisitedLocation result = tourGuideService.getUserLocation(user);
    assertThat(result).isEqualTo(visitedLocation);
  }

  @Test
  public void testGetCurrentLocationForAllUsers() {
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    List<User> userList = new ArrayList<>();
    userList.add(user);
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());

    when(userServiceMock.getAllUsers()).thenReturn(
        userList);
    when(locationServiceMock.trackUserLocation(user)).thenReturn(
        visitedLocation);

    List<Map<String, Location>> result = tourGuideService.getCurrentLocationForAllUsers();
    assertThat(result.get(0).get(user.getUserId().toString())).isEqualTo(visitedLocation.location);
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

  @Test
  public void getNearByAttractions() {

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    int points = 29;
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
        new Date());
    double distance = 554;
    Attraction attraction = new Attraction("attractionNam", "city", "state", 0.3, 2.3);
    List<Attraction> attractionList = new ArrayList<>();
    attractionList.add(attraction);
    NearbyAttractions nearbyAttractionObject = new NearbyAttractions(attraction.getAttractionName(),
        attraction.getLatitude(),
        attraction.getLongitude(), visitedLocation.location.latitude,
        visitedLocation.location.longitude,
        distance,
        points);
    List<NearbyAttractions> nearbyAttractionsList = new ArrayList<>();
    nearbyAttractionsList.add(nearbyAttractionObject);

    when(mGpsUtilProxyMock.getAttractions()).thenReturn(
        attractionList);
    when(rewardsServiceMock.getDistance(attraction, location)).thenReturn(
        distance);
    when(mRewardCentralProxyMock.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId()))
        .thenReturn(points);

    List<NearbyAttractions> result = tourGuideService.getNearByAttractions(visitedLocation, user);

    tourGuideService.tracker.stopTracking();

    assertThat(result.get(0).getAttractionLatitude()).isEqualTo(attraction.getLatitude());
    assertThat(result.get(0).getAttractionLongitude()).isEqualTo(attraction.getLongitude());
    assertThat(result.get(0).getAttractionName()).isEqualTo(attraction.getAttractionName());
  }


  @Test
  public void updateUserPreferences() {
    String userName = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    UserPreferences userPreferences = new UserPreferences();
    userPreferences.setAttractionProximity(50);
    userPreferences.setTripDuration(2);
    userPreferences.setNumberOfChildren(3);

    when(userServiceMock.getUser(userName)).thenReturn(user);

    UserPreferences result = tourGuideService.updateUserPreferences(userName,
        userPreferences);
    assertEquals(result, userPreferences);
  }


}
