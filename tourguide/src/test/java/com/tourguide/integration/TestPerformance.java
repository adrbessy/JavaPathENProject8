package com.tourguide.integration;

import static org.junit.Assert.assertTrue;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.InternalTestHelper;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class TestPerformance {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  @Autowired
  RewardsService rewardsService;

  @Autowired
  TourGuideService tourGuideService;

  /*
   * A note on performance improvements:
   * 
   * The number of users generated for the high volume tests can be easily
   * adjusted via this method:
   * 
   * InternalTestHelper.setInternalUserNumber(100000);
   * 
   * 
   * These tests can be modified to suit new solutions, just as long as the
   * performance metrics at the end of the tests remains consistent.
   * 
   * These are performance metrics that we are trying to hit:
   * 
   * highVolumeTrackLocation: 100,000 users within 15 minutes:
   * assertTrue(TimeUnit.MINUTES.toSeconds(15) >=
   * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
   *
   * highVolumeGetRewards: 100,000 users within 20 minutes:
   * assertTrue(TimeUnit.MINUTES.toSeconds(20) >=
   * TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
   */

  @Test
  public void highVolumeTrackLocation() {

    InternalTestHelper.setInternalUserNumber(100000);

    List<User> allUsers = new ArrayList<>();
    allUsers = tourGuideService.getAllUsers();

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    for (User user : allUsers) {
      tourGuideService.trackUserLocation(user);
    }
    stopWatch.stop();
    tourGuideService.tracker.stopTracking();

    System.out.println(
        "highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
    assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
  }

  @Test
  @Disabled
  public void highVolumeGetRewards() {

    // Users should be incremented up to 100,000, and test finishes within 20
    // minutes
    InternalTestHelper.setInternalUserNumber(100000);
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Attraction attraction = mGpsUtilProxy.getAttractions().get(0);
    List<User> allUsers = new ArrayList<>();
    allUsers = tourGuideService.getAllUsers();

    for (User u : allUsers) {
      u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
      rewardsService.calculateRewards(u);
    }

    /*
     * allUsers.forEach(u -> u.addToVisitedLocations(new
     * VisitedLocation(u.getUserId(), attraction, new Date())));
     * System.out.println("hello !"); allUsers.forEach(u ->
     * rewardsService.calculateRewards(u));
     */

    for (User user : allUsers) {
      assertTrue(user.getUserRewards().size() > 0);
    }
    stopWatch.stop();
    tourGuideService.tracker.stopTracking();

    System.out.println(
        "highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
    assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
  }

}
