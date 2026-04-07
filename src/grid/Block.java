package grid;

import core.Tile;

public class Block extends Tile {
    public enum Type {
        INACCESSIBLE,
        MARKET,
        COMMON
    }

    private Type type;
    boolean hasHeros;

    public Block(int row, int col) {
        super(row,col);
        this.type = null;
        this.hasHeros = false;
    }

    public Block(int row, int col, Type type) {
        super(row,col);
        this.type = type;
        this.hasHeros = false;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean isAccessible() {
        return type != Type.INACCESSIBLE;
    }

    public boolean isMarket() {
        return type == Type.MARKET;
    }

    public boolean isCommon() {
        return type == Type.COMMON;
    }
    

    public void moveHerosOn() {
        hasHeros = true;
    }

    public void moveHerosOff() {
        hasHeros = false;
    }

    // public boolean hasHeros() {
    //     return hasHeros;
    // }



    // @Override //TODO: do we need this?
    // public String print() {
    //     switch (type) {
    //         case INACCESSIBLE: return "X";
    //         case MARKET: return "M";
    //         case COMMON: return " ";
    //         default: return " ";
    //     }
    // }

    public String getSymbol() {
        if (hasHeros) {
            return "P";
        }
        switch (type) {
            case INACCESSIBLE: return "X";
            case MARKET: return "M";
            case COMMON: return " ";
            default: return " ";
        }
    }

}