package com.tourguide.unitTest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceRewardCentralProxy;
import com.tourguide.service.AttractionService;
import com.tourguide.service.RewardsService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
public class TestRewardsServiceImpl {

  @Autowired
  RewardsService rewardsService;

  @MockBean
  private MicroserviceGpsUtilProxy mGpsUtilProxyMock;
  @MockBean
  private AttractionService attractionServiceMock;
  @MockBean
  MicroserviceRewardCentralProxy mRewardCentralProxyMock;

  @Test
  public void testCalculateRewards() {
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
    user.addToVisitedLocations(visitedLocation);
    Attraction attraction = new Attraction("attractionNam", "city", "state", 0.3, 2.3);
    List<Attraction> attractionList = new ArrayList<>();
    attractionList.add(attraction);
    int points = 29;

    when(mGpsUtilProxyMock.getAttractions()).thenReturn(attractionList);
    when(attractionServiceMock.nearAttraction(visitedLocation, attraction)).thenReturn(true);
    when(mRewardCentralProxyMock.getAttractionRewardPoints(attraction.getAttractionId(), user.getUserId()))
        .thenReturn(points);

    rewardsService.calculateRewards(user);

    assertThat(user.getUserRewards().size()).isEqualTo(1);
  }


}
