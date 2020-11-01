package entity.market;

import entity.Area;
import entity.Store;

import java.util.Random;
import java.util.function.Function;

public class MarketUtils {

    public static int generateId() {
        return Math.abs(new Random().nextInt() * Integer.MAX_VALUE);
    }

    public static int generateIdForStore(Area area) {
        int max = area.getAllStores().stream().map(Store::getId).mapToInt(v->v).max().orElse(0);
        return max + 1;
    }

    public static int generateIdForArea(Market market) {
        int max = market.getAllAreas().stream().map(Area::getId).mapToInt(v->v).max().orElse(0);
        return max + 1;
    }
}
