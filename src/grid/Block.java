package grid;

import core.Tile;
import hero.Hero;
import monster.Monster;

public class Block extends Tile {
    public enum Type {
        INACCESSIBLE,
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
        return type != Type.OBSTACLE && type != Type.INACCESSIBLE && type != Type.BORDER;
    }
    public boolean hasHero() {
        return hero != null;
    }

    public boolean removeObstacle() {
        if (type == Type.OBSTACLE) {
            type = Type.PLAIN;
            return true;
        }
        return false; // not an obstacle
    }
    public boolean hasMonster() {
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
            case INACCESSIBLE: return "I";
            case NEXUS: return "N";
            case OBSTACLE: return "X";
            case BUSH: return "B";
            case CAVE: return "C";
            case KOULOU: return "K";
            case PLAIN: return " ";
            default: return " ";
        }
    }

}