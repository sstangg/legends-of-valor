package hero;

import java.util.List;
import java.util.Random;

import iohandler.FileLoader;

public class Sorcerer extends Hero {

    public Sorcerer(String num) {
        super("Sorcerer");
        List<Sorcerer> sorcerers = FileLoader.load("Legends_Monsters_and_Heroes/sorcerers.txt",line -> Sorcerer.parse(line));

        if (sorcerers.isEmpty())
            throw new RuntimeException("No sorcerers found in file!");

        Sorcerer randomSorcerer = sorcerers.get(new Random().nextInt(sorcerers.size()));

        this.name = randomSorcerer.name + " " + num;
        this.MP = randomSorcerer.MP;
        this.strength = randomSorcerer.strength;
        this.agility = randomSorcerer.agility;
        this.dexterity = randomSorcerer.dexterity;
        this.HP = randomSorcerer.HP;
        this.gold = randomSorcerer.gold;
        this.experience = randomSorcerer.experience;
    }


    
    public Sorcerer(String name, int mana, int strength, int agility, int dexterity, int gold, int experience) {
        super(name, "Paladin");
        this.MP = mana;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.HP = 100 + (strength / 2);
        this.gold = gold;
        this.experience = experience;
    }

    @Override
    protected void applyBonus() {
        // favors dexterity + agility
        dexterity *= 1.05;
        agility *= 1.05;
    }

    public static Sorcerer parse(String line) {
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

        return new Sorcerer(name, mana, strength, agility, dexterity, money, experience);
    }
}
