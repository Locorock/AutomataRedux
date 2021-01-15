package enums;

public enum EnviroList {
    Desert (50, 110, 0, 20, 30, 20),
    Forest (20, 60, 30, 75, 40, 120),
    Glacial (-10, 15, 0, 20, 40, 10),
    Jungle (40, 110, 50, 110, 30, 140),
    Plains (20, 65, 20, 60, 60, 100),
    Savanna (35, 75, 10, 65, 50, 80),
    Taiga (0, 30, 25, 70, 50, 80),
    Tundra (-10, 30, 0, 30, 40, 40),
    Wetland (0, 50, 60, 110, 40, 160),
    Snowland (-10, 15, 25, 110, 40, 20);


    private final int tempMin, tempMax, humMin, humMax, rarity, riverMod;

    EnviroList(int tempMin, int tempMax, int humMin, int humMax, int rarity, int riverMod) {
        this.tempMin = tempMin;
        this.humMin = humMin;
        this.tempMax = tempMax;
        this.humMax = humMax;
        this.rarity = rarity;
        this.riverMod = riverMod;
    }

    public int getTempMin() {
        return this.tempMin;
    }

    public int getHumMin() {
        return this.humMin;
    }

    public int getTempMax() {
        return this.tempMax;
    }

    public int getHumMax() {
        return this.humMax;
    }

    public int getRarity() {
        return this.rarity;
    }

    public int getRiverMod() { return this.riverMod; }
}