package tripPricer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TripPricer {

  int providerNumber = 10;

  public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay,
      int rewardsPoints) {
    List<Provider> providers = new ArrayList<>();
    try {
      TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1, 50));
    } catch (InterruptedException interruptedException) {
    }

    ArrayList<String> allProviders = new ArrayList<String>();
    allProviders.add("Holiday Travels");
    allProviders.add("Enterprize Ventures Limited");
    allProviders.add("Sunny Days");
    allProviders.add("FlyAway Trips");
    allProviders.add("United Partners Vacations");
    allProviders.add("Dream Trips");
    allProviders.add("Live Free");
    allProviders.add("Dancing Waves Cruselines and Partners");
    allProviders.add("AdventureCo");
    allProviders.add("Cure-Your-Blues");

    List<String> nProviders = new ArrayList<String>();
    nProviders = pickNRandom(allProviders, providerNumber);

    for (int i = 0; i < nProviders.size(); i++) {
      int multiple = ThreadLocalRandom.current().nextInt(100, 700);
      double childrenDiscount = (children / 3);
      double price = (multiple * adults) + multiple * childrenDiscount * nightsStay + 0.99D - rewardsPoints;
      if (price < 0.0D)
        price = 0.0D;
      providers.add(new Provider(attractionId, nProviders.get(i), price));
    }
    return providers;
  }

  public static List<String> pickNRandom(List<String> lst, int n) {
    List<String> copy = new ArrayList<String>(lst);
    Collections.shuffle(copy);
    return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
  }

  /*
   * public String getProviderName(String apiKey, int adults) { int multiple =
   * ThreadLocalRandom.current().nextInt(1, 10); switch (multiple) { case 1:
   * return "Holiday Travels"; case 2: return "Enterprize Ventures Limited"; case
   * 3: return "Sunny Days"; case 4: return "FlyAway Trips"; case 5: return
   * "United Partners Vacations"; case 6: return "Dream Trips"; case 7: return
   * "Live Free"; case 8: return "Dancing Waves Cruselines and Partners"; case 9:
   * return "AdventureCo"; } return "Cure-Your-Blues"; }
   */

}
