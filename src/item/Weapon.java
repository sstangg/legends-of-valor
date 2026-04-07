package item;

public class Weapon extends Item {
    
    int damage;
    int hands;

    public Weapon(String name, int price, int level, int damage, int hands) {
        super(name,price,level,Type.WEAPON);
        this.damage = damage;
        this.hands = hands;
    }

    public int getDamage() {
        return damage;
    }

    public static Weapon parse(String line, int currLevel) {
        String[] parts = line.trim().split("\\s+");

        String name = parts[0];
        int cost = Integer.parseInt(parts[1]);
        int level = Integer.parseInt(parts[2]);
        int damages = Integer.parseInt(parts[3]);
        int reqHands = Integer.parseInt(parts[4]);
        if (level < currLevel) { //TODO:fix!
            return new Weapon(name, cost, level, damages, reqHands);
        }
        return null;
    }

    @Override
    public void print() {
        System.out.println("[Weapon] " + name + " (10/10) (Price: " + price + ", Level: " + level + ", Damage: " + damage + ", Hands:" + hands + ")");
    }

    @Override
    public String getName() {
        return name;
    }

    public int getHands() {
        return hands;
    }
}