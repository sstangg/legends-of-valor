package item;

public class Spell extends Item {
    
    int damage;
    int manaCost;
    String type;

    public Spell (String name, int price, int level, int damage, int manaCost, String spellType) {
        super(name,price,level,Type.SPELL);
        this.damage = damage;
        this.manaCost = manaCost;
        this.type = spellType;
    }

    public static Spell parse(String line, int currLevel, String type) {
        String[] parts = line.trim().split("\\s+");

        String name = parts[0];
        int cost = Integer.parseInt(parts[1]);
        int level = Integer.parseInt(parts[2]);
        int damages = Integer.parseInt(parts[3]);
        int manaCosts = Integer.parseInt(parts[4]);

        if (level < currLevel) { //TODO:fix!
            return new Spell(name, cost, level, damages, manaCosts, type);
        }
        return null;
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getDamage() {
        return damage;
    }

    public String getType() {
        return type;
    }

    @Override
    public void print() {
        System.out.println("[Spell] " + name + " (10/10) (Price: " + price + ", Level: " + level + ", Spell Type: " + type + ", Damage: " + damage + ", Mana Cost: " + manaCost + ")");
    }
}
