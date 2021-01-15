package enums;

public enum EnviroListRedux {
    Desert (0, 60, 30, 20),
    Forest (50, 30, 40, 120),
    Glacial (0, -10, 40, 60),
    Jungle (70, 50, 30, 140),
    Plains (30, 30, 60, 100),
    Savanna (20, 40, 50, 80),
    Taiga (20, 10, 50, 100),
    Tundra (10, 0, 40, 40),
    Wetland (70, 30, 40, 160),
    Snowland (20, 0, 40, 60);


    private final int avgTemp, avgHum, rarity, riverMod;

    EnviroListRedux(int avgHum, int avgTemp, int rarity, int riverMod) {
        this.avgHum = avgHum;
        this.avgTemp = avgTemp;
        this.rarity = rarity;
        this.riverMod = riverMod;
    }

    public int getAvgHum(){
        return this.avgHum;
    }

    public int getAvgTemp(){
        return this.avgTemp;
    }

    public int getRarity() {
        return this.rarity;
    }

    public int getRiverMod() { return this.riverMod; }
}