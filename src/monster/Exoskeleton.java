package monster;

import java.util.List;
import java.util.Random;

import iohandler.FileLoader;

public class Exoskeleton extends Monster {
    public Exoskeleton(int v) {
        super("Exoskeleton", v);
        List<Exoskeleton> exoskeletons = FileLoader.load("Legends_Monsters_and_Heroes/Exoskeletons.txt", line -> Exoskeleton.parse(line));

        if (exoskeletons.isEmpty())
            throw new RuntimeException("No exoskeletons found in file!");

        Exoskeleton randomExoskeleton = exoskeletons.get(new Random().nextInt(exoskeletons.size()));

        this.name = randomExoskeleton.name;
        this.level = randomExoskeleton.level;
        this.HP = level * 100;
        this.defenseValue = randomExoskeleton.defenseValue;
        this.dodgeAbility = randomExoskeleton.dodgeAbility;
        this.baseDamageValue = randomExoskeleton.baseDamageValue;
        this.id = v;
    }

    public Exoskeleton(String name, int level, int defenseValue, int dodgeAbility, int baseDamageValue) {
        super("Exoskeleton");
        this.name = name;
        this.level = level;
        this.defenseValue = defenseValue;
        this.dodgeAbility = dodgeAbility;
        this.baseDamageValue = baseDamageValue;
    }

     public static Exoskeleton parse(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 5) {
            return null;
        }

        String name = parts[0];
        int level = Integer.parseInt(parts[1]);
        int damage = Integer.parseInt(parts[2]);
        int defense = Integer.parseInt(parts[3]);
        int dodge = Integer.parseInt(parts[4]);

        return new Exoskeleton(name, level, damage, defense, dodge);
    }

        // higher defense
        // defenseValue *= 1.4;


}
