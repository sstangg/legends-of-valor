package hero;

import java.util.List;
import java.util.Random;

import iohandler.FileLoader;

public class Warrior extends Hero {

    public Warrior(String num, int v) {
        super("Warrior", v);
        // load all warriors 
        List<Warrior> warriors = FileLoader.load("Legends_of_Valor/warriors.txt", line -> Warrior.parse(line));

        if (warriors.isEmpty()) {
            throw new RuntimeException("No warriors found in file!");
        }

        // pick random warrior
        Warrior randomWarrior = warriors.get(new Random().nextInt(warriors.size()));

        this.name = randomWarrior.name + " " + num;
        this.MP = randomWarrior.MP;
        this.strength = randomWarrior.strength;
        this.agility = randomWarrior.agility;
        this.dexterity = randomWarrior.dexterity;
        this.HP = randomWarrior.HP;
        this.gold = randomWarrior.gold;
        this.level = randomWarrior.level;
        this.experience = randomWarrior.experience;
    }


    public Warrior(String name, int mana, int strength, int agility, int dexterity, int gold, int experience, int v) {
        super(name, "Warrior", v);
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
        // warriors favor strength + agility
        strength *= 1.05;
        agility *= 1.05;
    }

    public static Warrior parse(String line) {
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

        return new Warrior(name, mana, strength, agility, dexterity, money, experience, 0);
    }
}
