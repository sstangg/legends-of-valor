package grid;

import core.Tile;
import hero.Hero;
import monster.Monster;

public class Block extends Tile {
    public enum Type {
        INACCESSIBLE,
        // MARKET, // TODO: delete when refactored
        // COMMON, // TODO: delete when refactored
        // TODO: implement tile spaces across grid, gameplay
        NEXUS, // same as market
        OBSTACLE,
        PLAIN, // same as common
        BUSH,
        CAVE,
        KOULOU,
        BORDER
    }

    private Type type;
    private Hero hero;
    private Monster monster;
    
    public Block(int row, int col) {
        super(row,col);
        this.type = null;
        this.hero = null;
        this.monster = null;
    }

    public Block(int row, int col, Type type) {
        super(row,col);
        this.type = type;
        this.hero = null;
        this.monster = null;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean isAccessible() {
        return type != Type.INACCESSIBLE;
    }

    public boolean hasHeros() {
        return hero != null;
    }

    public boolean hasMonsters() {
        return monster != null;
    }

    public void moveHeroOn(Hero hero) {
        this.hero = hero;
    }

    public void moveHeroOff() {
        this.hero = null;
    }

    public void moveMonsterOn(Monster monster) {
        this.monster = monster;
    }

    public void moveMonsterOff() {
        this.monster = null;
    }

    public Hero getHero() {
        return hero;
    }

    public Monster getMonster() {
        return monster;
    }

    public String getSymbol() {
        if (hero != null && monster != null) {
            return "H" + hero.getId() + " M" + monster.getId(); // both hero and monster present
        } else if (hero != null) {
            return "H" + hero.getId(); 
        } else if (monster != null) {
            return "M" + monster.getId(); 
        }

        switch (type) {
            case BORDER: return "███████";
            case INACCESSIBLE: return "X";
            case NEXUS: return "N";
            case OBSTACLE: return "O";
            case BUSH: return "B";
            case CAVE: return "C";
            case KOULOU: return "K";
            case PLAIN: return " ";
            default: return " ";
        }
    }

}