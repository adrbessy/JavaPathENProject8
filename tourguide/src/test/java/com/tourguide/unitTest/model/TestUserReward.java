package com.tourguide.unitTest.model;

import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import java.util.Date;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUserReward {

  private UserReward userReward;
  private VisitedLocation visitedLocation;
  private Attraction attraction;

  @BeforeEach
  private void setUp() {
    attraction = new Attraction("attractionNam", "city", "state", 0.3, 2.3);
    Location location = new Location(0.1, 0.2);
    visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
        new Date());
    userReward = new UserReward(visitedLocation, attraction);
  }

  @Test
  public void testToString() {
    String expected = "UserReward[attraction=" + attraction + "]";
    Assert.assertEquals(expected, userReward.toString());
  }

}
