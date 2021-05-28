package com.tourguide.model;

import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUser {

  private User user;
  UUID userId;
  String userName;
  String phoneNumber;
  String emailAddress;

  @BeforeEach
  private void setUp() {
    userId = UUID.randomUUID();
    userName = "jon";
    phoneNumber = "000";
    emailAddress = "afqgg@mail.fr";
    user = new User(userId, userName,
        phoneNumber, emailAddress);
  }

  @Test
  public void testToString() {
    String expected = "User[userId= " + userId + ", userName= " + userName + ", phoneNumber="
        + phoneNumber + ", visitedLocations=" + user.getVisitedLocations() + ", userRewards=" + user.getUserRewards()
        + "]";
    Assert.assertEquals(expected, user.toString());
  }

}
