package com.tourguide.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import java.util.Date;
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

}
