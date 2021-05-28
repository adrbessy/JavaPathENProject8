package com.tourguide.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import nl.jqno.equalsverifier.EqualsVerifier;

@SpringBootTest
public class TestNearbyAttractions {

  @Test
  public void simpleEqualsNearbyAttractions() {
    EqualsVerifier.simple().forClass(NearbyAttractions.class).verify();
  }

}
