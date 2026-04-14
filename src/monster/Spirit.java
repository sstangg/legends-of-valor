package monster;

import java.util.List;
import java.util.Random;

import iohandler.FileLoader;

public class Spirit extends Monster {
    
    public Spirit(int v) {
        super("Spirit", v);
        List<Spirit> spirits = FileLoader.load("Legends_of_Valor/Spirits.txt", line -> Spirit.parse(line));

        if (spirits.isEmpty())
            throw new RuntimeException("No spirits found in file!");

        Spirit randomSpirit = spirits.get(new Random().nextInt(spirits.size()));

        this.name = randomSpirit.name;
        this.level = randomSpirit.level;
        this.HP = level * 100;
        this.defenseValue = randomSpirit.defenseValue;
        this.dodgeAbility = randomSpirit.dodgeAbility;
        this.baseDamageValue = randomSpirit.baseDamageValue;
        this.id = v;
    }

    public Spirit(String name, int level, int defenseValue, int dodgeAbility, int baseDamageValue) {
        super("Spirit");
        this.name = name;
        this.level = level;
        this.defenseValue = defenseValue;
        this.dodgeAbility = dodgeAbility;
        this.baseDamageValue = baseDamageValue;
    }

     public static Spirit parse(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 5) {
            return null;
        }

        String name = parts[0];
        int level = Integer.parseInt(parts[1]);
        int damage = Integer.parseInt(parts[2]);
        int defense = Integer.parseInt(parts[3]);
        int dodge = Integer.parseInt(parts[4]);

        return new Spirit(name, level, damage, defense, dodge);
    }

        // boost dodge
        // dodgeAbility *= 1.5;
    
}
