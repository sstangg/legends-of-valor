package monster;

import java.util.List;
import java.util.Random;

import iohandler.FileLoader;

public class Dragon extends Monster {
    public Dragon(int v) {
        super("Dragon", v);
        List<Dragon> dragons = FileLoader.load("Legends_Monsters_and_Heroes/Dragons.txt", line -> Dragon.parse(line));

        if (dragons.isEmpty())
            throw new RuntimeException("No dragons found in file!");

        Dragon randomDragon = dragons.get(new Random().nextInt(dragons.size()));

        this.name = randomDragon.name;
        this.level = randomDragon.level;
        this.HP = level * 100;
        this.defenseValue = randomDragon.defenseValue;
        this.dodgeAbility = randomDragon.dodgeAbility;
        this.baseDamageValue = randomDragon.baseDamageValue;
        this.id = v;
    }

    public Dragon(String name, int level, int defenseValue, int dodgeAbility, int baseDamageValue) {
        super("Dragon");
        this.name = name;
        this.level = level;
        this.defenseValue = defenseValue;
        this.dodgeAbility = dodgeAbility;
        this.baseDamageValue = baseDamageValue;
    }

    public static Dragon parse(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 5) {
            return null;
        }

        String name = parts[0];
        int level = Integer.parseInt(parts[1]);
        int damage = Integer.parseInt(parts[2]);
        int defense = Integer.parseInt(parts[3]);
        int dodge = Integer.parseInt(parts[4]);

        return new Dragon(name, level, damage, defense, dodge);
    }

     // higher damage
        // baseDamageValue *= 1.4;

}
