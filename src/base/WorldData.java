package base;

import java.util.HashMap;

public class WorldData {
    final HashMap<String, Double[]> critterGeneticBounds;
    final HashMap<String, Double> critterParams;
    final HashMap<String, Double> worldGenParams;
    final HashMap<String, Double> enviroParams;

    public WorldData(int[] values) { //SOON GENETIC
        critterGeneticBounds = new HashMap<> ();
        critterParams = new HashMap<> ();
        worldGenParams = new HashMap<> ();
        enviroParams = new HashMap<> ();

        critterGeneticBounds.put ("MateRate", new Double[]{shiftToRange (0.1, 1, 128, 256), shiftToRange (1, 3, 128, 256)});
        critterGeneticBounds.put ("BaseSpeed", new Double[]{shiftToRange (0.2, 1, 128, 256), shiftToRange (1, 3, 128, 256)});
        critterGeneticBounds.put ("WebbedFeet", new Double[]{shiftToRange (0, 1, 0, 256), shiftToRange (1, 3, 128, 256)});
        critterGeneticBounds.put ("Aggressivity", new Double[]{shiftToRange (0, 3, 0, 256), shiftToRange (3, 50, 128, 256)}); //ZERO
        critterGeneticBounds.put ("BloodSalination", new Double[]{shiftToRange (0, 0.02, 128, 256), shiftToRange (0.01, 0.1, 254, 256)});
        critterGeneticBounds.put ("Wanderlust", new Double[]{shiftToRange (0, 5, 128, 256), shiftToRange (10, 100, 128, 256)});
        critterGeneticBounds.put ("Stealth", new Double[]{shiftToRange (0, 10, 0, 256), shiftToRange (10, 100, 254, 256)});
        critterGeneticBounds.put ("Perception", new Double[]{shiftToRange (0, 10, 0, 256), shiftToRange (10, 100, 254, 256)});
        critterGeneticBounds.put ("Height", new Double[]{shiftToRange (0, 1, 0, 256), shiftToRange (1, 10, 128, 256)});

        critterParams.put ("longevitySizeMultiplier", shiftToRange (20, 80, 128, 256));
        critterParams.put ("longevityBaseValue", shiftToRange (150, 500, 60, 256));
        critterParams.put ("longevityMateMultiplier", shiftToRange (50, 100, 128, 256));
        critterParams.put ("mateCostHungerDenom", shiftToRange (2, 5, 128, 256));
        critterParams.put ("mateCostThirstDenom", shiftToRange (2, 5, 128, 256));
        critterParams.put ("mutationRate", shiftToRange (200, 30000, 0, 256));
        critterParams.put ("stabilityThreshold", shiftToRange (300, 300, 0, 256)); //DA CALCOLARE CON UNA FUNZIONE

        worldGenParams.put ("seaLevel", shiftToRange (0, 120, 128, 256));
        worldGenParams.put ("humMult", shiftToRange (0.5, 1.5, 128, 256));
        worldGenParams.put ("tempOffset", shiftToRange (-40, 20, 128, 256));
        worldGenParams.put ("landSize", shiftToRange (0.07, 0.13, 32, 256));

        enviroParams.put ("globalGrowthMultiplier", shiftToRange (0.8, 1.2, 128, 256)); //
    }

    public static double shiftToRange(double rangeStart, double rangeEnd, int value, int subdivisions) {
        return (((rangeEnd - rangeStart) * value) / subdivisions) + rangeStart;
    }
}

