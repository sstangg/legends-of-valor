package monster;

import java.util.ArrayList;

import core.Character;

public class Monster extends Character {
    
    protected String name;
    protected int level; 
    protected int HP;
    protected double baseDamageValue;
    protected double defenseValue;
    protected double dodgeAbility;
    protected String type;
    protected int id;


    public Monster(String name, int level, int id) {
        super(-1,-1); //TODO: decide if setting position at intializing obj or when intializing board
        // super(name, level);

        this.name = name;
        this.HP = level * 100;
        this.id = id;

        // base stats (shared baseline)
        // this.baseDamageValue = level * 10;
        // this.defenseValue = level * 5;
        // this.dodgeAbility = level * 5;
    }

    public Monster(String name) {
        super(-1,-1);

        this.name = name;
    }


    public Monster(String type, int id) {
        super(-1,-1);
        this.type = type;
        this.id = id;
        this.HP = level * 100;

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

    public void setHP(int hp) {
        this.HP = hp;
    }

    public String getName() {
        return name;
    }

    public double getDefense() {
        return defenseValue;
    }

    public double getDodge() {
        return dodgeAbility;
    }

    public double getDamage() {
        return baseDamageValue;
    }

    public void setDamage(int updatedDamageValue) {
        baseDamageValue = updatedDamageValue;
    }

    public void setDefense(int updatedDefenseValue) {
        defenseValue = updatedDefenseValue;
    }

    public void setDodge(int updatedDodgeAbility) {
        dodgeAbility = updatedDodgeAbility;
    }

    public void print() {
        System.out.println("Name: "+ name + "Level: " + level + ", Damage: " + baseDamageValue + ", Defense: " + defenseValue + ", Dodge: " + dodgeAbility +"Type: Dragon");
    }
}
