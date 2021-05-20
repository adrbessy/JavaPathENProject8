package rewardCentral;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RewardCentral {
  public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
    /*
     * try { TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1,
     * 1000)); } catch (InterruptedException interruptedException) { }
     */
    int randomInt = ThreadLocalRandom.current().nextInt(1, 1000);
    return randomInt;
  }
}
