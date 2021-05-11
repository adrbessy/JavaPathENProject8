package com.tourguide.model;

import java.util.UUID;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.javamoney.moneta.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
public class UserPreferences {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private UUID userId;

  private int attractionProximity = Integer.MAX_VALUE;
  private CurrencyUnit currency = Monetary.getCurrency("USD");
  private Money lowerPricePoint = Money.of(0, currency);
  private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
  private int tripDuration = 1;
  private int ticketQuantity = 1;
  private int numberOfAdults = 1;
  private int numberOfChildren = 0;

  public void setAttractionProximity(int attractionProximity) {
    this.attractionProximity = attractionProximity;
  }

  public int getAttractionProximity() {
    return attractionProximity;
  }

  public Money getLowerPricePoint() {
    return lowerPricePoint;
  }

  public void setLowerPricePoint(Money lowerPricePoint) {
    this.lowerPricePoint = lowerPricePoint;
  }

  public Money getHighPricePoint() {
    return highPricePoint;
  }

  public void setHighPricePoint(Money highPricePoint) {
    this.highPricePoint = highPricePoint;
  }

  public int getTripDuration() {
    return tripDuration;
  }

  public void setTripDuration(int tripDuration) {
    this.tripDuration = tripDuration;
  }

  public int getTicketQuantity() {
    return ticketQuantity;
  }

  public void setTicketQuantity(int ticketQuantity) {
    this.ticketQuantity = ticketQuantity;
  }

  public int getNumberOfAdults() {
    return numberOfAdults;
  }

  public void setNumberOfAdults(int numberOfAdults) {
    this.numberOfAdults = numberOfAdults;
  }

  public int getNumberOfChildren() {
    return numberOfChildren;
  }

  public void setNumberOfChildren(int numberOfChildren) {
    this.numberOfChildren = numberOfChildren;
  }

}
