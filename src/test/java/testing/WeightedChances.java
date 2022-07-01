package testing;

import io.github.mortuusars.wares.core.ware.PotentialWare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeightedChances {
    public static ArrayList<PotentialWare> wares = new ArrayList<>();
    public static Map<PotentialWare, Integer> results = new HashMap<>();

    public static void run(){
//        wares.add(new PotentialWare.Builder().weight(1f).build());
//        wares.add(new PotentialWare.Builder().weight(1f).build());
//        wares.add(new PotentialWare.Builder().weight(1f).build());
//        wares.add(new PotentialWare.Builder().weight(1f).build());
//        wares.add(new PotentialWare.Builder().weight(2f).build());
//        wares.add(new PotentialWare.Builder().weight(4f).build());
//        wares.add(new PotentialWare.Builder().weight(7f).build());
//        wares.add(new PotentialWare.Builder().weight(20f).build());
//        wares.add(new PotentialWare.Builder().weight(50f).build());

        wares.forEach((w) -> results.put(w, 0));

        final int RUNS_COUNT = 1000;

        for (int i = 0; i < RUNS_COUNT; i++) {
            testWeights();
        }

        printResults(RUNS_COUNT);
    }

    private static void printResults(int runs) {

        System.out.printf("RESULTS for %s runs:\n%n", runs);
//
//        HashMap<Ware, Integer> ordered = new HashMap<>();
//        ordered.putAll(results.entrySet().stream().sorted((e1, e2) -> e1.getValue() > e2.getValue() ? -1 : 1).map);
        for (var res : results.entrySet()){
            PotentialWare ware = res.getKey();
            int pickedCount = res.getValue();
            float chance = (pickedCount / (float)runs) * 100.0f;
            System.out.printf("Item with weight: [%s] - Picked [%s] \t\t-\t\t %s%%\n", ware.weight, pickedCount, String.format("%.1f", chance));
        }

    }

    private static void testWeights() {
        wares.stream().map(w -> w.weight).toList();

        var totals = new ArrayList<Float>();
        float runningTotal = 0;

        for (var ware : wares){
            runningTotal += ware.weight;
            totals.add(runningTotal);
        }

        float pick = new Random().nextFloat(0f, runningTotal);
        for (int i = 0; i < totals.size(); i++) {
            if (pick < totals.get(i)){
                PotentialWare ware = wares.get(i);
                int count = results.get(ware);
                results.compute(wares.get(i), ((ware1, integer) -> ++integer));
                break;
            }
        }

    }
}
