package core;

public abstract class Character {
    protected int row;
    protected int col;

    protected Character(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }


    public void displayStats() {
    }

}
