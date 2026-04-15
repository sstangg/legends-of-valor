package game;

import core.Board;
import core.Game;
import core.Character;
import grid.Grid;
import grid.Block;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import iohandler.FileLoader;
import iohandler.Input;
import item.Item;
import item.Armor;
import item.Potion;
import item.Spell;
import item.Weapon;
import hero.Hero;
import hero.Warrior;
import hero.Sorcerer;
import hero.Paladin;
import monster.Dragon;
import monster.Exoskeleton;
import monster.Spirit;
import monster.Monster;


public class LegendsOfValor extends Game {
    private Input input;
    private List<Hero> heros;
    private List<Monster> monsters;
    private List<Item> marketStock; 
    private int round;

    private final int[][] laneColumns = {{0,1}, {3,4}, {6,7}};
    private int difficulty; 
    
    public LegendsOfValor(Input input, int[] heroType, int[] lane, int difficulty) {
        this.input = input;
        this.round = 1;
        this.board = new Grid(8);
        this.difficulty = difficulty;

        initializeHeros(heroType, lane);
        initializeMonsters(lane);

        loadMarketStock(1);
    }

    private void initializeHeros(int[] heroType, int[] lane) {
        int size = heroType.length;
        heros = new ArrayList<>();
        wait(1);
        String idx = "I";
        int v = 1;
        for (int i = 0; i < size; i++) {
            switch (heroType[i]) {
            case 1:
                heros.add(new Warrior(idx,v));
                break;
            case 2:
                heros.add(new Sorcerer(idx,v));
                break;
            default:
                heros.add(new Paladin(idx,v));
            }
            System.out.println("==== Hero " + (i+1) + ": " + heros.get(i).getName()+  " ====");
            wait(1);
            idx += "I";
            v++;
        }
        wait(2);
        System.out.println();

        for (int i = 0; i < size; i++) {
            int first = rollDice(2);
            int second = 1 - first;

            int row = board.getDim() - 1;
            int col = laneColumns[lane[i]-1][0] + first;
            Block block = (Block) board.getTile(row, col);

            if (block.hasHero()) {
                col = laneColumns[lane[i]-1][second]; // try other column
                block = (Block) board.getTile(row, col);
            }
            heros.get(i).setPosition(row, col); // TODO: reset to row for testing
            heros.get(i).setSpawnPosition(row, col);
            heros.get(i).setLane(lane[i]-1);
            block.moveHeroOn(heros.get(i));
        }
    }

    private Hero getHero(int i) {
        return heros.get(i);
    }

    private Monster getMonster(int i) {
        return monsters.get(i);
    }

    private boolean handleCommand(char command, int idx) {
        Hero hero = heros.get(idx);
        switch (command) {
        case 'W':
            return attemptMove(idx, -1, 0);
        case 'A':
            return attemptMove(idx, 0, -1);
        case 'D':
            return attemptMove(idx, 0, 1);
        case 'S':
            return attemptMove(idx, 1, 0);
        case 'I':
            displayInfo();
            return false;
        case 'E':
            return manageInventory();
        case 'P':
            return true;
        case 'F':
            return attackFrom(hero);
        case 'C':
            return castSpell(hero);
        case 'U':
            return usePotion(hero);
        case 'T':
            return teleport(hero);
        case 'R':
            return recall(hero);
        case 'M':
            // TODO: only fire at Nexus
            if (getCharactersBlock(heros.get(idx)).getType() == Block.Type.NEXUS) {
                printMarketWelcome();
                enterMarket();
            } else {
                System.out.println("Not on market space, move to nexus and try again.");
            }
            return false;
        case 'H':
            showHelp();
            return false;
        case 'Q':
            quitGame();
            return true;
        default:
            System.out.println("Invalid input.");
            return false;
        }
    }

    private void handleHerosMove(int idx) {

        while (true) {

            printBoard();
            printHeroOptions(idx);

            String inputStr = input.nextLine().trim().toUpperCase();

            // handling empty input
            if (inputStr.isEmpty()) {
                System.out.println("Please enter an option.");
                continue;
            }

            char command = inputStr.charAt(0);

            boolean completedMove = handleCommand(command, idx);
            wait(1);

            if (completedMove) {
                break; // only exiting loop after a valid move action is complete
            }
        }
        wait(2);
    }

    private boolean handleMonstersMove(int idx) {
        Monster monster = monsters.get(idx);

        while (true) {
            Hero target = hasHeroInRange(monster);
            if (target != null) {
                attackFromMonster(monster, target);
                wait(2);
                return true;
            } else {
                if (attemptMoveMonster(idx, 1, 0)) {
                    wait (2);
                    return true;
                } else if (attemptMoveMonster(idx, 0, -1)) {
                    wait (2);
                    return true;
                } else if (attemptMoveMonster(idx, 0, 1)) {
                    wait (2);
                    return true;
                } else if (attemptMoveMonster(idx, -1, 0)) {
                    wait (2);
                    return true;
                } else {
                    System.out.println(monster.getName() + " couldn't move!");
                    wait(2);
                    return false;
                }
            }
        }
    }

    private Hero hasHeroInRange(Monster monster) {   
        for (Hero hero : heros) {
            if (hero.getHP() > 0 && inRange(hero.getLane(), hero.getRow(), monster.getLane(), monster.getRow())) {
                // System.out.println("Found hero: " + hero.getName() + "(" + hero.getRow() + "," + hero.getCol() + ") in range of " + monster.getName() + "!");
                return hero;
            }
        }
        return null;
    }

    private void attackFromMonster(Monster monster, Hero hero) {
        System.out.println(monster.getName() + " attacks " + hero.getName() + "!");
        System.out.println();

        wait(2);

        double damage = monster.getDamage(); 

        double dodgeChance = hero.getAgility() * 0.002; // scaling dodge
        dodgeChance = Math.min(dodgeChance, 0.5);

        if (Math.random() < dodgeChance) {
            System.out.println("---" + hero.getName() + " dodged the attack! ---");
            System.out.println();
            return; 
        }

        hero.setHP((int) Math.max(0, hero.getHP() - damage));
        
        System.out.println(monster.getName() + " hit " + hero.getName() + " with " + (int)damage + " damage");

        if (hero.getHP() == 0) {
            System.out.println(hero.getName() + " was eliminated!");
        }
    }

    private boolean attemptMoveMonster(int idx, int rowChange, int colChange) {
        // System.out.println("Attempting move!");
        Monster monster = monsters.get(idx);

        int newRow = monster.getRow() + rowChange;
        int newCol = monster.getCol() + colChange;

        // TODO: check this
        Block adjacentBlock = ((Grid)board).getAdjacentBlock(monster.getRow(), monster.getCol());
        if (rowChange <= -1 && adjacentBlock != null && adjacentBlock.hasHero()) {
            // You cannot move forward past a hero without battling it.
            return false;
        }
        if (board.isValidMove(newRow, newCol)) {
            System.out.println();
            System.out.println(monster.getName() + " (M" + monster.getId() + ") moved from (" + monster.getRow() + "," + monster.getCol() + ") to (" + newRow + "," + newCol + ")");

            Block oldB = getCharactersBlock(monster);
            oldB.moveMonsterOff();

            Block newB = (Block) board.getTile(newRow,newCol);
            newB.moveMonsterOn(monster);

            monster.setPosition(newRow, newCol);
            wait(2);
            return true;
        } else {
            // System.out.println("You can't move there!");
            return false;
        }
    }


    private int rollDice(int i) {
        Random random = new Random();
        return random.nextInt(i);
    
    }

    private void initializeMonsters(int[] lane) {
        int size = heros.size();
        monsters = new ArrayList<>();
        wait(1);
        System.out.println("");
        System.out.println("MONSTERS SPAWNING...");
        System.out.println("");
        wait(1);
        for (int i = 0; i < size; i++) {
            Monster m = spawnMonster(lane[i]);
            monsters.add(m);
            System.out.println("----- " + getMonster(i).getName() + " (M" + getMonster(i).getId() + ") spawned at (" + getMonster(i).getRow() + "," + getMonster(i).getCol() + ")! -----");
            wait(2);
  
        }

        System.out.println("");
        System.out.println("Let's begin!");
        System.out.println("");
        wait(2);
    }

    private Monster spawnMonster(int lane) {
        int idx = monsters.size() + 1;
        int result = rollDice(3);
        Monster m;
        switch (result) {
            case 1:
                m = new Exoskeleton(idx);
                break;
            case 2:
                m = new Spirit(idx);
                break;
            default:
                m = new Dragon(idx);
                break;
        }

        int first = rollDice(2);
        int second = 1 - first;

        int col = laneColumns[lane-1][first];
        int row = 0;

        Block block = (Block) board.getTile(row, col);
        if (block.hasMonster()) {
            col = laneColumns[lane-1][second]; // try other column
            block = (Block) board.getTile(row, col);
        }

        m.setPosition(row,col);
        m.setLane(lane-1);
        block.moveMonsterOn(m);
        return m;
    }


    private void printHeroOptions(int idx) {
        System.out.println();
        System.out.println("===== " + heros.get(idx).getName() + "'s Turn =====");
        System.out.println("Position: Row " + heros.get(idx).getRow() + ", Col " + heros.get(idx).getCol() + " | Lane: " + (heros.get(idx).getLane()+1));
        System.out.println("HP: " + heros.get(idx).getHP() + " | MP: " + heros.get(idx).getMP() + " | Gold: " + heros.get(idx).getGold() + " | Level: " + heros.get(idx).getLevel());
        System.out.println();

        System.out.println("Controls:");
        System.out.println("W/A/S/D - Move (up/left/down/right)");
        System.out.println("I - Hero Info/Inventory");
        System.out.println("E - Equip/Unequip");
        System.out.println("P - Pass turn");
        System.out.println("F - Attack");
        System.out.println("C - Cast Spell");
        System.out.println("U - Use Potion");
        System.out.println("T - Teleport");
        System.out.println("R - Recall");
        System.out.println("M - Market (only at Nexus)");
        System.out.println("H - Help/Information");
        System.out.println("Q - Quit game");
        System.out.printf("Your move: ");
    }

    private void printHeros() {
        System.out.println("HEROS:");
        for (Hero h : heros) {
            System.out.println(h.getName() + " (HP: " + h.getHP() + ", MP: " + h.getMP() + ")");

        }
    }

    private void printMonsters() {
        System.out.println("MONSTERS:");
        for (Monster m : monsters) {
            System.out.println(m.getName() + " (HP: " + m.getHP() + ")");
        }
    }


    private boolean allHeroesDead() {
        for (Hero hero : heros) {
            if (hero.getHP() > 0) {
                return false;
            }
        }
        System.out.println("All heros are dead!!");
        return true;
    }

    private boolean allMonstersDead() {
        for (Monster monster : monsters) {
            if (monster.getHP() > 0) {
                return false;
            }
        }
        System.out.println("All monsters are dead!!");
        return true;
    }

    private Hero getRandomAliveHero() {
        List<Hero> alive = new ArrayList<>();

        for (Hero h : heros) {
            if (h.getHP() > 0) {
                alive.add(h);
            }
        }

        if (alive.isEmpty()) return null;

        return alive.get(new Random().nextInt(alive.size()));
    }

    private void monstersBattleTurn() {
        System.out.println("--- Monsters' Turn ---");

        for (Monster monster : monsters) {
            if (monster.getHP() > 0) {
                
                Hero target = getRandomAliveHero();
                if (target == null) {
                    continue;
                }
                
                wait(2);
                monsterAttack(monster, target);
                
            }

            
        }
    }

    private void monsterAttack(Monster monster, Hero target) {

        wait(2);

        System.out.println();
        System.out.println(monster.getName() + " attacking " + target.getName() + "...");
        System.out.println();
        wait(2);

        double damage = monster.getDamage(); 

        double dodgeChance = target.getAgility() * 0.002; // scaling dodge
        dodgeChance = Math.min(dodgeChance, 0.5);

        if (Math.random() < dodgeChance) {
            System.out.println();
            System.out.println(target.getName() + " dodged the attack!");
            System.out.println();
            wait(2);
            return; 
        }

        target.setHP((int) Math.max(0, target.getHP() - damage));
        
        System.out.println();
        System.out.println(monster.getName() + " hit " + target.getName() + " with " + (int)damage + " damage");
        System.out.println();
        wait(2);

        if (target.getHP() == 0) {
            System.out.println(target.getName() + " was eliminated!");
            System.out.println();
            wait(2);
        }

        System.out.println();
    }


    private void endOfRoundRecovery() {
        System.out.println("[END OF ROUND " + round + "]");
        for (Hero hero : heros) {
            if (hero.getHP() > 0) {
                hero.regain();
            } else {
                recall(hero);
            }
        }

        if (round + 1 % difficulty == 0) {
            spawnMonster(1);
            spawnMonster(2);
            spawnMonster(3);
        }
    }

    // private void endBattle() {
    //     if (!allHeroesDead()) {
    //         int num = monsters.size();
    //         monsters = null;

    //         int maxLevel = 0;
    //         for (Hero hero : heros) {
    //             // heros that didn't faint level up
    //             if (hero.getHP() > 0) {
    //                 hero.levelUp(num);
    //                 if (maxLevel < hero.getLevel()) {
    //                     maxLevel = hero.getLevel();
    //                 }
    //             } else { // heros that fainted get their HP reset
    //                 hero.setHP(hero.getBaseHP()); 
    //             }
    //         }
    
    //         loadMarketStock(maxLevel);
    //     }
    // }
    // calculate whether character is in range
    private static boolean inRange(int lane1, int row1, int lane2, int row2) {
        return lane1 == lane2 && Math.abs(row1 - row2)<= 1;
    }
    // locates all in range monsters for a hero to attack / cast a spell
    // prompts users to select an in range monster for the hero to target.
    private Monster targetMonster(Hero hero) {
        List<Monster> inRangeMonsters = new ArrayList<>();
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (m.getHP() > 0 && inRange(hero.getLane(), hero.getRow(), m.getLane(), m.getRow())) {
                inRangeMonsters.add(m);
            }
        }
        if (inRangeMonsters.size() == 0) {
            return null;
        }
        System.out.println("Select target monster:");
        for (int i = 0; i < inRangeMonsters.size(); i++) {
            Monster m = inRangeMonsters.get(i);
            System.out.printf("(%d) %s HP: %d%n", i+1, m.getName(), m.getHP());
        }
        int targetIdx = input.nextInt(1, inRangeMonsters.size());
        return inRangeMonsters.get(targetIdx - 1);
    }

    private boolean castSpell(Hero hero) {
        if (hero.getSpells() == null || hero.getSpells().isEmpty()) {
            System.out.println(hero.getName() + " has no spells to use!");
            return false;
        }
        Monster target = targetMonster(hero);
        if (target == null) {
            System.out.println(hero.getName() + " has no monster nearby to target!");
            return false;
        }

        System.out.println("Select a spell to cast:");
        List<Spell> spells = hero.getSpells();
        int idx = 1;
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.println("(" + idx++ +") " + s.getName() +" - [Level: " + s.getLevel() + ", Damage: "+ s.getDamage() + ", Mana Cost:" + s.getManaCost() + ", Type: " + s.getType() + "]");
        }

        int option = input.nextInt(1, spells.size());
        Spell spell = spells.get(option - 1);

        if (hero.getMP() < spell.getManaCost()) {
            System.out.println(hero.getName() + " does not have enough MP to cast " + spell.getName());
            return false;
        }

        hero.use(spell);

        System.out.println();
        System.out.println(hero.getName() + " cast " + spell.getName() + " on " + target.getName());
        System.out.println();
        wait(2);


        double rawDamage = spell.getDamage() + (hero.getDexterity() / 10000.0) * spell.getDamage();
        double damage = rawDamage * (50.0 / (120 + target.getDefense()));


        double dodgeChance = target.getDodge() * 0.002;
        dodgeChance = Math.min(dodgeChance, 0.5);

        if (Math.random() < dodgeChance) {
            System.out.println();
            System.out.println(target.getName() + " dodged the spell!");
            System.out.println();
            wait(2);
            return true;
        }


        System.out.println();
        switch (spell.getType().toLowerCase()) {
            case "ice":
                target.setDamage((int) Math.max(0, target.getDamage() - target.getDamage() * 0.1));
                System.out.println(target.getName() + "'s damage was reduced by 10%");
                break;
            case "fire":
                target.setDefense((int) Math.max(0, target.getDefense() - target.getDefense() * 0.1));
                System.out.println(target.getName() + "'s defense was reduced by 10%");
                break;
            case "lightning":
                target.setDodge((int) Math.max(0, target.getDodge() - target.getDodge() * 0.1));
                System.out.println(target.getName() + "'s dodge chance was reduced by 10%");
                break;
        }
        System.out.println();
        wait(2);

        if (target.getHP() == 0) {
            System.out.println();
            System.out.println(target.getName() + " was eliminated!");
            System.out.println();
            wait(2);
        }
        return true;
        
    }

    
    private boolean usePotion(Hero hero) {
         if (hero.getPotions() == null || hero.getPotions().isEmpty()) {
            System.out.println(hero.getName() + " has no potions to use!");
            return false;
        }

        System.out.println("Select a potion to use:");
        List<Potion> potions = hero.getPotions();
        int idx = 1;
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            p.print();
            System.out.println("(" + idx++ +") " + p.getName() +" - [Level: " + p.getLevel() + ", Effect Type: " + p.getEffectType() + ", Effect Amount: " + p.getEffectAmount() + "]");
        }
        

        int option = input.nextInt(1, potions.size());
        Potion potion = potions.get(option - 1);

        hero.take(potion);
        wait(2);

        System.out.println();
        System.out.println(hero.getName() + " took " + potion.getName() + " and recovered " + potion.getEffectAmount() + " " + potion.getEffectType() + "!");
        System.out.println();
        wait(2);
        return true;
    }

    private boolean equipWeapon(Hero hero) {
        Weapon w = hero.getWeapon();
        if (w == null) {
            System.out.println(hero.getName() + "has no weapons in their inventory. Buy weapons and try again.");
            return false;
        }
        System.out.println("--- Available Weapons ---");
        System.out.println("(1) " + w.getName() + " - Damage: " + w.getDamage() + ", Hands: " + w.getHands());
        System.out.println("(0) Cancel");

        System.out.printf("Select weapon to equip: ");
        
        int val = input.nextInt(0,1);
        if (val == 1) {
            hero.equip(w);
            System.out.println(hero.getName() + " equipped " + w.getName() + "!");

        }
        return true;
    }

    private boolean equipArmor(Hero hero) {
        Armor a = hero.getArmor();
        if (a == null) {
            System.out.println(hero.getName() + "has no armor in their inventory. Buy armory and try again.");
            return false;
        }

        System.out.println("--- Available Armors ---");
        System.out.println("(1) " + a.getName() + " - Damage: " + a.getDamageReduction());
        System.out.println("(0) Cancel");

        System.out.printf("Select armor to equip: ");
        
        int val = input.nextInt(0,1);
        if (val == 1) {
            hero.equip(a);
            System.out.println(hero.getName() + " equipped " + a.getName() + "!");
        }
        return true;
    }

    @Override
    public boolean play() {
        
        initializePlayers(board);
        
        while (!isDone()) {
            wait(2);
            System.out.println();
            System.out.println("[ROUND " + round + "]");
            wait(2);
            System.out.println("============HEROS' MOVE===============");
            for (int i = 0; i < heros.size(); i++) {
                if (heros.get(i).getHP() > 0) {
                    handleHerosMove(i);
                    if (heros.get(i).getRow() == 0) {
                        printBoard();
                        complete = true;
                        return true; // TODO: heroes won
                    }
                }
            }
            System.out.println();
            System.out.println("============MONSTERS' MOVE===============");
            for (int i = 0; i < monsters.size(); i++) {
                if (monsters.get(i).getHP() > 0) {
                    handleMonstersMove(i);
                    /*
                    if (monsters.get(i).getRow() == board.getDim()-1) {
                        printBoard();
                        complete = true;
                        return false; // heroes lost
                    }*/
                }
            }
            endOfRoundRecovery();
            round++;
        }
     
        return false;
    }

    private void initializePlayers(Board board) {

        // move heros and monsters onto their respective starting positions
        // move(getHero(0), board.getDim() - 1, 0+rollDice(2));
        // move(getHero(1), board.getDim() - 1, 3+rollDice(2));
        // move(getHero(2), board.getDim() - 1, 6+rollDice(2));
        // move(getMonster(0), 0, 0+rollDice(2));
        // move(getMonster(1), 0, 3+rollDice(2));
        // move(getMonster(2), 0, 6+rollDice(2));

        // System.out.println("");
        // System.out.println("MONSTERS SPAWNING...");
        // System.out.println("");

        // wait(1);
        // System.out.println("-----" + getMonster(0).getName() + " (M1) spawned at (" + getMonster(0).getRow() + "," + getMonster(0).getCol() + ")! -----");
        
        // wait(2);
        // System.out.println("-----" +getMonster(1).getName() + " (M2) spawned at (" + getMonster(1).getRow() + "," + getMonster(1).getCol() + ")! -----");

        // wait(2);
        // System.out.println("-----" +getMonster(2).getName() + " (M3) spawned at (" + getMonster(2).getRow() + "," + getMonster(2).getCol() + ")! -----");

        // wait(2);
        // System.out.println("Let's begin!");
        // wait(2);

    }
    public boolean isDone() {
        return complete;
    }
    private boolean attackFrom(Hero hero) {
        if (hero.getInventory() == null) {
            return false;
        }
        // TODO: assume hero & monster cannot co-occupy a space
        // TODO: add loop to validate whether chosen monster is in range
        Monster target = targetMonster(hero);
        if (target == null) {
            System.out.println(hero.getName() + " has no monster nearby to target!");
            return false;
        }

        wait(2);
        
        int weaponDamage = 0;
        if (hero.getWeapon() != null) {
            weaponDamage = hero.getWeapon().getDamage();
        }

        double rawDamage = (double) weaponDamage + hero.getStrength();

        double damage = rawDamage; // scaling damage 
        damage = damage * (50.0 / (50 + target.getDefense())); 

        double dodgeChance = target.getDodge() * 0.002; // scaling dodging
        dodgeChance = Math.min(dodgeChance, 0.5);

        if (Math.random() < dodgeChance) {
            System.out.println("");
            System.out.println(target.getName() + " dodged the attack!");
            System.out.println("");
            wait(2);
            return true;
        }


        target.setHP((int) Math.max(0,  target.getHP() - damage));

        System.out.println("");
        System.out.println(hero.getName() + " attacked " + target.getName() + " with " + (int) damage + " damage");
        System.out.println("");
        wait(2);

        if (target.getHP() == 0) {
            System.out.println(target.getName() + " was eliminated!");
            System.out.println();
            wait(2);
        }

        return true;
    }

    private void wait(int sec) {
        try {
            // Sleep for x seconds (x * 1000 milliseconds)
            Thread.sleep(sec * 1000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
    }
    @Override
    public void printBoard() {
        board.print();
    }


    private Block getCharactersBlock(Character ch) {
        int row = ch.getRow();
        int col = ch.getCol();
        return (Block) board.getTile(row,col);
    }
    private boolean attemptMove(int idx, int rowChange, int colChange) {
        if (rowChange + colChange > 1) {
            throw new IllegalArgumentException("Cannot move diagonally or more than 1 block at once!");
        }

        int oldRow = heros.get(idx).getRow();
        int oldCol = heros.get(idx).getCol();
        int newRow = oldRow + rowChange;
        int newCol = oldCol + colChange;

        // TODO: cannot move beyond monster without killing it
        // if monster is on an adjacent or current tile, hero cannot move forward without killing it
        if (!board.isValidMove(newRow, newCol) || ((Block) board.getTile(newRow, newCol)).hasHero()) {
            System.out.println("You can't move there!");
            return false;
        } else if (rowChange >= 1 && (((Grid)board).getAdjacentBlock(oldRow, oldCol).hasMonster() ||
                ((Block)board.getTile(oldRow, oldCol)).hasMonster())) {
            System.out.println("You cannot move forward past a monster without killing it.");
            return false;
        }
        else { // move successful
            Block oldB = getCharactersBlock(heros.get(idx));

            oldB.moveHeroOff();

            Block newB = (Block) board.getTile(newRow,newCol);
            newB.moveHeroOn(heros.get(idx));

            heros.get(idx).setPosition(newRow, newCol);
            return true;
        }
    }

    private boolean manageInventory() {
        displayInfo();

        displayInventoryMenu();

        List<String> validOptions = Arrays.asList("1", "2", "3", "E", "e", "Q", "q");
        String option;
        while (true) {
            option = input.nextLine();
            if (validOptions.contains(option)) {
                if (isInteger(option)) {
                    if (Integer.parseInt(option) > heros.size()) {
                        continue;
                    }
                }
                break;
            } else {
                System.out.println("Invalid input. Try again.");
            }  
        }

        if (isInteger(option)) {
            int i = Integer.parseInt(option);
            displayHeroInventoryOptions(i-1);

            List<String> validOptions2 = Arrays.asList("1", "2", "3", "E", "e", "Q", "q");
            String option2;
            while (true) {
                option2 = input.nextLine();
                if (validOptions2.contains(option2)) {
                    if (isInteger(option)) {
                        if (Integer.parseInt(option) > heros.size()) {
                            continue;
                        }
                    }
                    break;
                } else {
                    System.out.println("Invalid input. Try again.");
                }  
            }
            
            if (isInteger(option2)) {
                int choice = Integer.parseInt(option2);
                switch (choice) {
                case 1:
                    if (!equipWeapon(heros.get(i-1))) {
                        manageInventory();
                    }
                    break;
                case 2: 
                    if (!equipArmor(heros.get(i-1))) {
                        manageInventory();
                    }
                    break;
                case 3:
                    usePotion(heros.get(i-1));
                    break;
                default:
                    displayInfo();
                    break;
                }
            }
        }
            
        if (option.toUpperCase().contains("E")) {   
            return false;
        }
        if (option.toUpperCase().contains("Q")) {    
            quitGame();
        }
        return true;
    }

    private void displayHeroInventoryOptions(int i) {
        System.out.println("==== " + heros.get(i).getName() + " ====");
        System.out.println("(1) Equip Weapon");
        System.out.println("(2) Equip Armor");
        System.out.println("(3) Use Potion");
        System.out.println("(4) View Full Info");
        System.out.println("(E) Back to hero selection");
        System.out.println("(Q) Quit game");
        System.out.printf("Your choice: ");
    }
        

    private void displayInfo() {
        for (Hero h : heros) {
            h.print();
        }
        System.out.println("Press Enter to continue..");

        while (true) {
            String line = input.nextLine();
            if (line.isEmpty()) {
                break; 
            }
        }
    }

    private void displayInventoryMenu() {
        System.out.println("===== INVENTORY MENU =====");
        System.out.println("Select a hero to manage:");

        int idx = 1;
        for (Hero h : heros) {
            System.out.println("(" + idx++ + ") " + h.getName() + " [Level: " + h.getLevel() + ", HP: " + h.getHP() + ", MP: " + h.getMP() + "]");
        }
        System.out.println("(E) Exit inventory");
        System.out.println("(Q) Quit game");
        System.out.printf("Your choice: ");
    }

    private static boolean isInteger(String str) {
        if (str == null) {
            return false; 
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //TODO
    private void enterMarket() {
        printMarketOptions();

        List<String> validOptions = Arrays.asList("I", "B", "S", "R", "E", "Q");
        String option;
        while (true) {
            option = input.nextLine();

            if (validOptions.contains(option.toUpperCase())) {
                break;
            } else {
                System.out.println("Invalid input. Try again.");
            }  
        }

        char command = option.charAt(0);
        switch (command) {
            case 'I':
                displayInfo();
                enterMarket();
                break;
            case 'B':
                buyItem();
                break;
            case 'S':
                sellItem();
                break;
            case 'R':
                repairItem();
            case 'Q':
                quitGame();
            case 'E':
                return;
            default:
                return;
        }

    }


    private void buyItem() {
        System.out.println("Select a hero to buy for:");

        int idx = 1;
        for (Hero h : heros) {
            System.out.println("(" + idx++ + ") " + h.getName() + " [Level: " + h.getLevel() + ", Gold: " + h.getGold() + "]");
        }
        
        System.out.printf("Hero number: ");
        int i = input.nextInt(1,heros.size());
        Hero hero = heros.get(i-1);
        System.out.println("Hero Gold Budget: " + hero.getGold());
        System.out.printf("Enter item to buy: ");
        List<Item> inventory = hero.getInventory();
        int val = input.nextInt(1, marketStock.size());
        Item item = marketStock.get(val);

        if (hero.getGold() < item.getPrice()) {
            System.out.println(hero.getName() + " did not have sufficient funds.");
            System.out.println("Purchase unsuccessful.");
        } else if (hero.getLevel() <  item.getLevel()) {
            System.out.println(hero.getName() + " has not reached level " + item.getLevel() + " to unlock this item");
            System.out.println("Purchase unsuccessful.");
        }
        else {
            hero.buy(item);
            System.out.println("Purchased: " + item.getName());
            System.out.println("Purchase successful.");
        }


    }

    private void sellItem() {
        System.out.println("Select a hero to sell for:");

        int idx = 1;
        for (Hero h : heros) {
            System.out.println("(" + idx++ + ") " + h.getName() + " [Level: " + h.getLevel() + ", Gold: " + h.getGold() + "]");
        }
        
        System.out.printf("Hero number: ");
        int i = input.nextInt(1,heros.size());
        Hero hero = heros.get(i-1);
        
        System.out.println("Hero's inventory: ");


        System.out.printf("Enter item to sell: ");
        List<Item> inventory = hero.getInventory();
        if (inventory == null) {
            System.out.println(hero.getName() + " has not items in inventory to sell");
            System.out.println("Sale unsuccessful.");
            return;
        }

        int index = 1;
        System.out.println("-- " + hero.getName() + " Inventory --");
        for (Item item : inventory) {
            System.out.printf("[%d] ", index++);
            item.print();
        }
        int val = input.nextInt(1, inventory.size());
        Item item = inventory.get(val-1);

        hero.sell(item);
        System.out.println("Sold: " + item.getName());
        System.out.println("Sale successful.");


    }

    private void repairItem() {

    }

    //TODO: teleport
    private boolean teleport(Hero hero) {
        /*
        check if other heroes alive..
        ask user to pick a lane
        get hero in diff lane
        get that heros location
        find the bottom & left/right adjacent blocks
        ask the user which they would like to teleport to
        place hero in the new block & update info
        -- case: what if 3 heroes in 1 lane?

         */
        List<Hero> alive = new ArrayList<>();
        List<Integer> lanes = new ArrayList<>();
        for (Hero h : heros) {
            if (h.getHP() > 0 && h.getLane() != hero.getLane()) { // must be a different lane
                alive.add(h);
                if (!lanes.contains(h.getLane())) {
                    lanes.add(h.getLane());
                }
            }
        }

        if (alive.isEmpty()) {
            System.out.println("No other hero to teleport to");
            return false; // redo..
        }
        int lane;

        System.out.printf("Which lane should %s teleport to? \n", hero.getName());
        for (int i : lanes) {
            System.out.println("Lane " + (i+1)); // 1 indexed for user view
        }
        while (true) {
            lane = input.nextInt(1,3)-1;
            if (lanes.contains(lane)) {
                break;
            }
            System.out.println("Invalid lane. Enter a valid lane: ");
        }
        Hero teleportHero = null;
        for (Hero h : alive) {
            if (h.getLane() == lane) {
                teleportHero = h;
            }
        }
        int r = teleportHero.getRow();
        int c = teleportHero.getCol();
        Block adjBlock = ((Grid)board).getAdjacentBlock(r, c);
        System.out.println("Which position would you like to teleport to?");
        //L/R adjacent
        if (adjBlock != null && board.isValidMove(adjBlock.getRow(), adjBlock.getCol()) && !adjBlock.hasHero()) {
            System.out.printf("1 -  Row: %d, Col: %d \n", adjBlock.getRow(), adjBlock.getCol());
        }if (r < board.getDim()-1 && board.isValidMove(r+1, c) && !adjBlock.hasHero()) { // bottom only
            System.out.printf("2 - Row: %d, Col: %d \n", r+1, c);
        }
        System.out.println("4 - Quit teleporting");
        int blockNum = input.nextInt(1,4);
        if (blockNum == 4) {
            System.out.println("Quit teleporting");
            return false;
        }
        hero.setLane(lane);
        ((Block)board.getTile(hero.getRow(), hero.getCol())).moveHeroOff();
        if (blockNum == 1) {
            hero.setPosition(adjBlock.getRow(), adjBlock.getCol());
            ((Block)board.getTile(adjBlock.getRow(), adjBlock.getCol())).moveHeroOn(hero);
        } else {
            hero.setPosition(r+1, c);
            ((Block)board.getTile(r+1, c)).moveHeroOn(hero);
        }
        System.out.printf("Teleporting to lane %d next to hero %s...\n", lane, hero.getName());

        return true;
    }

    //TODO: recall
    private boolean recall(Hero hero) {
        Block oldBlock = ((Block)board.getTile(hero.getRow(), hero.getCol()));
        Block newBlock = ((Block)board.getTile(hero.getSpawnRow(), hero.getSpawnCol()));
        if (oldBlock.equals(newBlock)) {
            System.out.println("Already at spawn location");
            return false;
        }
        oldBlock.moveHeroOff();
        hero.respawn();
        newBlock.moveHeroOn(hero);
        return true;
    }

    private void printMarketWelcome() {
        System.out.println("=============================================================================");
        System.out.println("Welcome to the Market! Here you can buy and sell items to enhance your heros.");
        System.out.println("=============================================================================");
        System.out.println();

        int idx = 1;
        System.out.println("-- Primary Market Stock --");
        for (Item i : marketStock) {
            System.out.printf("[%d] ", idx++);
            i.print();
        }

    }
    private void printMarketOptions() {
        System.out.println();
        System.out.println("Market controls:");
        System.out.println("I - Show Hero Info");
        System.out.println("B - Buy item");
        System.out.println("S - Sell item");
        System.out.println("R - Repair broken equipment");
        System.out.println("E - Exit market");
        System.out.println("Q - Quit game");
        System.out.println("Enter your choice: ");
    }


    private void loadMarketStock(int i) {
        marketStock = new ArrayList<>();
        String folder = "Legends_of_Valor/";
        marketStock.addAll(FileLoader.load(folder + "Armory.txt", line -> Armor.parse(line, i)));
        marketStock.addAll(FileLoader.load(folder + "Weaponry.txt", line -> Weapon.parse(line, i)));
        marketStock.addAll(FileLoader.load(folder + "Potions.txt", line -> Potion.parse(line, i)));
        marketStock.addAll(FileLoader.load(folder + "FireSpells.txt", line -> Spell.parse(line, i, "Fire")));
        marketStock.addAll(FileLoader.load(folder + "IceSpells.txt", line -> Spell.parse(line, i, "Ice")));
        marketStock.addAll(FileLoader.load(folder + "LightningSpells.txt", line -> Spell.parse(line, i, "Lightning")));
    }

    private void showHelp() {

        System.out.println("=============== Helpful Info about how to play Monsters and Heroes: ===============");
        System.out.println();
        System.out.println("The goal of the game is for the heroes to defeat monsters and level up indefinitely.");
        System.out.println("Each time you enter a common tile there is a chance monsters are created and");
        System.out.println("a battle begins! A battle is complete when either all heros or all monsters have");
        System.out.println("been eliminated. When heros win a battle they regain HP and MP and level up");
        System.out.println("all their powers.");
        System.out.println();
        System.out.println();
        System.out.println("The world of the game is represented by a fixed, square grid of spaces.");
        System.out.println("The grid contains three types of spaces:");
        System.out.println("   Inaccessible spaces, which the heroes cannot enter");
        System.out.println("   Nexus spaces, where characters are spawned and items can be bought or sold at the market");
        System.out.println("   Plain spaces, where battles can occur");
        System.out.println();
        System.out.println();
        System.out.println("The heroes move first in each round. During the heroes’ turn, the player chooses");
        System.out.println("for each hero whether they will do one of the following:");
        System.out.println("   Attack, using the hero’s equipped weapon");
        System.out.println("   Cast a spell from the hero’s inventory");
        System.out.println("   Use a potion from the hero’s inventory");
        System.out.println("   Equip a weapon or piece of armor");
        System.out.println();
        System.out.println("==================================================================================");

        System.out.println("Press Enter to continue..");

        while (true) {
            String line = input.nextLine();
            if (line.isEmpty()) {
                break; 
            }
        }

    }

    private void quitGame() {
        System.out.println("Thank you for playing! Exiting the game...");
        System.exit(0);
    }
}