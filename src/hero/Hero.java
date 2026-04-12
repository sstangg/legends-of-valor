package hero;

import java.util.ArrayList;
import java.util.List;

import core.Character;
import item.Armor;
import item.Item;
import item.Potion;
import item.Spell;
import item.Weapon;
import iohandler.Input;

public abstract class Hero extends Character {
    
    protected String name;
    protected int level;
    protected int baseHP;
    protected int HP; // hit points
    protected int MP; //mana/magic points for casting spells
    protected int strength;
    protected int dexterity;
    protected int agility;
    protected int gold; 
    protected List<Item> inventory; 
    protected Weapon equippedWeapon;
    protected Armor equippedArmor;

    protected int experience;
    protected String type;
    protected int id;

    public Hero() {
        super(0,0);
        this.inventory = new ArrayList<>();
        this.HP = level * 100;
        this.baseHP = HP;
    }

    public Hero(String type, int id) {
        super(0,0);
        this.type = type;
        this.id = id;
        this.inventory = new ArrayList<>();
        this.HP = level * 100;
        this.baseHP = HP;

    }

    public Hero(String name, String type, int id) {
        super(0,0);
        this.name = name;
        this.type = type;
        this.id = id;
        this.inventory = new ArrayList<>();
        this.HP = level * 100;
        this.baseHP = HP;

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getHP() {
        return HP;
    }

    public int getBaseHP() {
        return baseHP;
    }

    public int getMP() {
        return MP;
    }

    public void setHP(int hp) {
        this.HP = hp;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getGold() {
        return gold;
    }

    public Weapon getWeapon() {
        for (Item item : inventory) {
            if (item instanceof Weapon ) {
                return (Weapon) item;
            }
        }
        return null;
    }

    public Armor getArmor() {
        for (Item item : inventory) {
            if (item instanceof Armor ) {
                return (Armor) item;
            }
        }
        return null;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void equip(Weapon w) {
        equippedWeapon = w;
    }

    public void equip(Armor a) {
        equippedArmor = a;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public List<Spell> getSpells() {
        List<Spell> spells =  new ArrayList<>();

        for (Item i : inventory) {
            if (i instanceof Spell) {
                spells.add(((Spell) i));
            }
        }
        return spells;
    }

    public List<Potion> getPotions() {
        List<Potion> potions =  new ArrayList<>();

        for (Item i : inventory) {
            if (i instanceof Potion) {
                potions.add(((Potion) i));
            }
        }
        return potions;
    }

    public void moveTo(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    public void levelUp(int monsters) {
        experience += level * 10;
        gold += monsters * 100;
        level++;

        strength *= 1.05;
        dexterity *= 1.05;
        agility *= 1.05;

        applyBonus();
    }


    public void regain() {
        HP *= 1.1; 
        MP *= 1.1;
    }

    protected abstract void applyBonus();

    public void print() {
        System.out.println("=======================================");
        System.out.println("==== HERO: " + name + " ====");
        System.out.println("Class: " + type);
        System.out.println("Level: " + level);
        System.out.println("Health Points (HP): " + HP);
        System.out.println("Mana Points (MP): " + MP);
        System.out.println("Strength: " + strength);
        System.out.println("Agility: " + agility);
        System.out.println("Dexterity: " + dexterity);
        System.out.println("Experience: " + experience);
        System.out.println("Gold: " + gold);
        System.out.printf("Equipped Weapon: ");
        Weapon w = getEquippedWeapon();
        if (w == null) {
            System.out.println("None");
        } else {
            System.out.println(getWeapon().getName());
        }
        
        System.out.printf("Equipped Armor: ");
        Armor a = getEquippedArmor();
        if (a == null) {
            System.out.println("None");
        } else {
            System.out.println(getArmor().getName());
        }

        System.out.println("Inventory: ");
        if (inventory.size() == 0) {
            System.out.println("  (empty)");
        } else {
            for (Item i : inventory) {
                System.out.println(" - " + i.getName());
            }
        }
        System.out.println("=======================================");
    }

    public void buy(Item item) {
        if (gold < item.getPrice()) {
            return;
        }

        gold -= item.getPrice();
        inventory.add(item);
        
    }

    public void sell(Item item) {

        gold += item.getPrice()/2;
        inventory.remove(item);
        
    }

    public void use(Spell spell) {
        MP -= spell.getManaCost();
        inventory.remove(spell);
    }

    public void take(Potion potion) {
        int amount = potion.getEffectAmount();
        for (String type : potion.getEffectTypes()) {
        switch (type.toLowerCase()) {
            case "health":
                HP += amount;
                System.out.println("Potion " + potion.getName() + " increased health by " + amount);
                break;
            case "mana":
                System.out.println("Potion " + potion.getName() + " increased MP by " + amount);
                MP += amount;
                break;
            case "strength":
                System.out.println("Potion " + potion.getName() + " increased strength by " + amount);
                strength += amount;
                break;
            case "dexterity":
                System.out.println("Potion " + potion.getName() + " increased dexterity by " + amount);
                dexterity += amount;
                break;
            case "agility":
                agility += amount;
                System.out.println("Potion " + potion.getName() + " increased agility by " + amount);
                break;
            }
    
        }
    }
}
