package com.tourguide.model;

import java.util.UUID;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import org.javamoney.moneta.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class UserPreferences {

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

  public UserPreferences() {
  }

}
