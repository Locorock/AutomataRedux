package base;

import java.util.HashMap;



public class GeneLibrary {
    private static final HashMap<String, int[]> index = new HashMap<> ();
    private static int size;

    public GeneLibrary() {
        int offset = 0;
        for (GeneIds id : GeneIds.values ()) {
            String name = id.name ();
            int size = id.size;
            int type = id.type;
            index.put (name, new int[]{offset, size, type});
            offset += size;
        }
        size = offset;
    }

    public static int[] searchIndex(String name) {
        return index.get (name);
    }

    public static int getSize() {
        return size;
    }

    public static HashMap<String, int[]> getIndex() {
        return index;
    }

    public enum GeneIds {
        AppearanceRecognition (16, 0, 0),
        AppearanceCluster (16, 0, 0),
        MateRate (32, 1, 1),
        WaterEff (32, 1, 1),
        FoodEff (32, 1, 1),
        BaseSpeed (32, 1, 1),
        Height (32, 1, 1),
        Stealth (32, 1, 0.6),
        Perception (32, 1, 1),
        Wanderlust (32, 1, 0),
        Aggressivity (32, 1, 0),
        WebbedFeet (32, 1, 1),
        Size (32, 1, 0),
        BloodSalination (32, 1, 0),

        ActiveStart(1,0,0),

        Claws(32, 1, 0.5),
        Jaws(32, 1, 0.5),
        PoisonedJaws (32, 1, 0.5),
        Tail(32, 1, 0.5),
        PoisonSpit(32, 1, 0.5),
        AcidSpit(32, 1, 0.5),

        ActiveEnd(1,0,0),
        PassiveStart(1,0,0),

        Carapace(32, 1, 0.5),
        Spikes(32, 1, 0.5),
        PoisonSpikes(32, 1, 0.5),
        PoisonRes(32, 1, 0.5),
        ThickSkin(32, 1, 0.5),

        PassiveEnd(1,0,0),

        LeavesEfficiency (32, 1, 1),
        FruitEfficiency (32, 1, 1),
        MeatEfficiency (32, 1, 1);


        //PropensionCluster (CellList.values ().length * 8, 3),
        //CrossingCluster (CellList.values ().length * 8, 3);


        private final int size, type; //TYPE: 0 = bit, 1 = cardinal, 2 = decimal, 3 = decimal cluster
        public final double instability;

        GeneIds(int size, int type, double instability) {
            this.size = size;
            this.type = type;
            this.instability = instability;
        }

        public String getName(){
            return this.name ();
        }

        public int getSize() {
            return size;
        }

        public int getType() {
            return type;
        }

        public double getInstability() {
            return instability;
        }
    }


}
