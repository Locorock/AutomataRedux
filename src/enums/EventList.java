package enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EventList {
    Earthquake (20000, 0, 1, 0, null),
    Meteor (1000000, 0, 1, 0, null),
    Precipitation (1800, 1, 5, 1.5, null),
    Storm (5000, 1, 2, 1, null),
    Volcano (50000, 0, 2, 1, Collections.singletonList ("Volcano"));

    private final int humAsc, rarity, meanDuration;
    private final double stdDuration;
    private final List<String> biomes;

    EventList(int rarity, int humAsc, int meanDuration, double stdDuration, List<String> biomes) {
        this.rarity = rarity;
        this.humAsc = humAsc;
        this.meanDuration = meanDuration;
        this.stdDuration = stdDuration;
        this.biomes = biomes;
    }

    public int getRarity() {
        return this.rarity;
    }

    public int getHumAsc() {
        return this.humAsc;
    }

    public List<String> getBiomes() {
        return biomes;
    }

    public int getMeanDuration() {
        return meanDuration;
    }

    public double getStdDuration() {
        return stdDuration;
    }
}