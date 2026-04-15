package core;

public abstract class Character {
    protected int row;
    protected int col;

    protected int spawnRow;
    protected int spawnCol;
    protected int lane;

    protected Character(int row, int col) {
        this.row = row;
        this.col = col;
        this.spawnRow = row;
        this.spawnCol = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public int getSpawnRow() {
        return spawnRow;
    }

    public int getSpawnCol() {
        return spawnCol;
    }

    public void resetToSpawnPosition() {
        this.row = spawnRow;
        this.col = spawnCol;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public void setSpawnPosition(int row, int col) {
        this.spawnRow = row;
        this.spawnCol = col;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getLane() {
        return lane;
    }
}
