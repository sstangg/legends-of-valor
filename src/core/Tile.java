package core;

public abstract class Tile {
    
    protected int row;
    protected int col;

    protected Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { 
        return row; 
    }

    public int getCol() { 
        return col; 
    }

    public abstract boolean isAccessible();
    // public abstract String print();
}
