package item;

import iohandler.LineParser;

public class Armor extends Item {
    
    int damageReduction;


    public Armor(String name, int price, int level, int damageReduction) {
        super(name, price,level, Type.ARMOR);
        this.damageReduction = damageReduction;
        
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    public static Armor parse(String line, int currLevel) {
        String[] parts = line.trim().split("\\s+");

        String name = parts[0];
        int cost = Integer.parseInt(parts[1]);
        int level = Integer.parseInt(parts[2]);
        int damageReduction = Integer.parseInt(parts[3]);

        if (level <= currLevel) { //TODO:fix!
            return new Armor(name, cost, level, damageReduction);
        }

        return null; 
    }

    @Override
    public void print() {
        System.out.println("[Armor] " + name + " (10/10) (Price: " + price + ", Level: " + level + ", DamageReduction: " + damageReduction + ")");
    }
}
