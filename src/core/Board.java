package core;

public abstract class Board {

    protected int dim;

    public int getDim() {
        return dim;
    }
    public abstract Tile getTile(int row, int col);
    public abstract boolean isValidMove(int row, int col);
    public abstract void print();
}
