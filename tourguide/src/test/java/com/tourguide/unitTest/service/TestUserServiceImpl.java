package com.tourguide.unitTest.service;

import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import com.tourguide.service.UserService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
public class TestUserServiceImpl {

  @Autowired
  UserService userService;

  @MockBean
  private MicroserviceGpsUtilProxy mGpsUtilProxyMock;

  @MockBean
  private TourGuideService tourGuideServiceMock;

  @Mock
  private RewardsService rewardsServiceMock;

  /*
   * @Test public void getAllUsers() {
   * InternalTestHelper.setInternalUserNumber(0); TourGuideService
   * tourGuideService = new TourGuideService(mGpsUtilProxyMock,
   * rewardsServiceMock);
   * 
   * User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
   * User user2 = new User(UUID.randomUUID(), "jon2", "000",
   * "jon2@tourGuide.com");
   * 
   * tourGuideService.addUser(user); tourGuideService.addUser(user2);
   * 
   * List<User> allUsers = userService.getAllUsers();
   * 
   * tourGuideService.tracker.stopTracking();
   * 
   * assertTrue(allUsers.contains(user)); assertTrue(allUsers.contains(user2)); }
   */

  /*
   * @Test public void getUser() { User user = new User(UUID.randomUUID(),
   * "Alain", "000", "jon@tourGuide.com");
   * 
   * when(tourGuideServiceMock.isInInternalUserMap(user.getUserName())).thenReturn
   * (true);
   * 
   * when(tourGuideServiceMock.internalUserMap.get(user.getUserName())).thenReturn
   * (user);
   * 
   * User result = userService.getUser(user.getUserName());
   * 
   * assertThat(result).isEqualTo(user); }
   */


}
