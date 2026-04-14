package hero;

import java.util.List;
import java.util.Random;

import iohandler.FileLoader;

public class Paladin extends Hero {

    public Paladin(String num, int v) {
        super("Paladin", v);
        List<Paladin> paladins = FileLoader.load("Legends_of_Valor/paladins.txt", line -> Paladin.parse(line));

        if (paladins.isEmpty())
            throw new RuntimeException("No paladins found in file!");

        Paladin randomPaladin = paladins.get(new Random().nextInt(paladins.size()));

        this.name = randomPaladin.name + " " + num;
        this.strength = randomPaladin.strength;
        this.dexterity = randomPaladin.dexterity;
        this.agility = randomPaladin.agility;
        this.MP = randomPaladin.MP;
        this.gold = randomPaladin.gold;
        this.experience = randomPaladin.experience;
        this.HP = this.level * 100;
    }

    
    public Paladin(String name, int strength, int dexterity, int agility, int mp, int gold, int experience, int v) {
        super(name, "Paladin", v);
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.MP = mp;
        this.gold = gold;
        this.experience = experience;
        this.HP = this.level * 100;
    }

    @Override
    protected void applyBonus() {
        // balanced, favors strength + dexterity
        strength *= 1.05;
        dexterity *= 1.05;
    }

    public static Paladin parse(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 7) {
            return null;
        }

        String name = parts[0];
        int mana = Integer.parseInt(parts[1]);
        int strength = Integer.parseInt(parts[2]);
        int agility = Integer.parseInt(parts[3]);
        int dexterity = Integer.parseInt(parts[4]);
        int money = Integer.parseInt(parts[5]);
        int experience = Integer.parseInt(parts[6]);

        return new Paladin(name, strength, dexterity, agility, mana, money, experience, 0);
    }
}
