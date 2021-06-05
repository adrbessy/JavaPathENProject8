package com.tourguide.unitTest.service;

import com.tourguide.service.LocationService;
import com.tourguide.service.Tracker;
import com.tourguide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
public class TestTrackerImpl {

  @Autowired
  Tracker tracker;

  @MockBean
  UserService userServiceMock;
  @MockBean
  LocationService locationServiceMock;
  /*
   * @Test public void testRun() { User user = new User(UUID.randomUUID(), "jon",
   * "000", "jon@tourGuide.com"); Location location = new Location(0.1, 0.2);
   * VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(),
   * location, new Date()); // user.addToVisitedLocations(visitedLocation);
   * List<User> userList = new ArrayList<>(); userList.add(user);
   * 
   * 
   * when(userServiceMock.getAllUsers()).thenReturn( userList);
   * when(locationServiceMock.trackUserLocation(user)).thenReturn(
   * visitedLocation);
   * 
   * tracker.run();
   * 
   * assertThat(user.getLastVisitedLocation()).isEqualTo(visitedLocation); }
   */

}
