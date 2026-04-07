package item;

import java.util.Arrays;
import java.util.List;

public class Potion extends Item {
    
    int effectAmount;
    String effectType;

    public Potion (String name, int price, int level, int effectAmount, String effectType) {
        super(name,price,level,Type.POTION);
        this.effectAmount = effectAmount;
        this.effectType = effectType;
    }

    public static Potion parse(String line, int currLevel) {
        String[] parts = line.trim().split("\\s+");

        String name = parts[0];
        int cost = Integer.parseInt(parts[1]);
        int level = Integer.parseInt(parts[2]);
        int attributeIncrease = Integer.parseInt(parts[3]);
        String attributeAffected = parts[4];

        
        if (level < currLevel) { //TODO:fix!
            return new Potion(name, cost, level, attributeIncrease, attributeAffected);
        }

        return null; 
    }

    public String getEffectType() {
        return effectType;
    }

    public List<String> getEffectTypes() {
        return Arrays.asList(effectType.split("/"));
    }

    public int getEffectAmount() {
        return effectAmount;
    }

    @Override
    public void print() {
        System.out.println("[Potion] " + name + " (10/10) (Price: " + price + " Level: " + level + ", Effect Type: " + effectType + ", Effect Amount: " + effectAmount + ")");
    }
}
