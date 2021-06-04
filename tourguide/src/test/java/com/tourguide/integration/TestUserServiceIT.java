package com.tourguide.integration;

import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import com.tourguide.service.UserService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class TestUserServiceIT {

  @Autowired
  UserService userService;
  @Autowired
  TourGuideService tourGuideService;

  @Autowired
  private MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  private RewardsService rewardsService;

  @Test
  public void getAllUsers() {
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    List<User> allUsers = userService.getAllUsers();

    tourGuideService.tracker.stopTracking();

    assertTrue(allUsers.contains(user));
    assertTrue(allUsers.contains(user2));
  }

}
