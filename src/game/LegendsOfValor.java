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
    private Hero[] heros;
    private List<Monster> monsters;
    private List<Item> marketStock; 
    private int round;
    // private int difficulty; //TODO: add difficulty
    
    public LegendsOfValor(Input input, int[] heroType) {
        this.input = input;
        this.round = 1;
        heros = new Hero[3];
        initializeHeros(heroType);
        initializeMonsters();

        loadMarketStock(2);
    }

    private void initializeHeros(int[] heroType) {
        String idx = "I";
        int v = 1;
        for (int i = 0; i < heroType.length; i++) {
            switch (heroType[i]) {
            case 1:
                heros[i] = new Warrior(idx,v);
                break;
            case 2:
                heros[i] = new Sorcerer(idx,v);
                break;
            default:
                heros[i] = new Paladin(idx,v);
            }
            idx += "I";
            v++;
        }
    }

    private Hero getHero(int i) {
        return heros[i];
    }

    private Monster getMonster(int i) {
        return monsters.get(i);
    }

    private boolean handleCommand(char command, int idx) {
        System.out.println("idx: " + idx);
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
            manageInventory();
            return false;
        case 'C':
            manageInventory();
            return false;
        case 'M':
            if (getCharactersBlock(heros[idx]).getType() == Block.Type.NEXUS) {
                printMarketWelcome();
                enterMarket();
            } else {
                System.out.println("Not on market space, move and try again.");
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
            wait(2);

            if (completedMove) {
                break; // only exiting loop after a valid move action is complete
            }
        }

        if (getCharactersBlock(heros[idx]).getType() == Block.Type.PLAIN) {
            if (rollDice(2) == 1) {
                System.out.println("Oh no! Monsters on the loose! Entering battle now!!");
                wait(2);
                startBattle();
                // then enter battle
            } else {
                System.out.println("No monsters! You are safe this time.");
                System.out.println();
                wait(2);
            }
        }
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
                if (attemptMoveMonster(idx, -1, 0)) {
                    wait (2);
                    return true;
                } else if (attemptMoveMonster(idx, 0, -1)) {
                    wait (2);
                    return true;
                } else if (attemptMoveMonster(idx, 0, 1)) {
                    wait (2);
                    return true;
                } else if (attemptMoveMonster(idx, 1, 0)) {
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
    
            if (hero.getHP() > 0) {
                int distance = Math.abs(hero.getRow() - monster.getRow()) + Math.abs(hero.getCol() - monster.getCol());
                if (distance <= 1) {
                    System.out.println("Found hero: " + hero.getName() + "(" + hero.getRow() + "," + hero.getCol() + ") in range of " + monster.getName() + "!");
                    return hero;
                }
            }
        }
        return null;
    }

    private void attackFromMonster(Monster monster, Hero hero) {
        System.out.println(monster.getName() + " is attacking " + hero.getName() + "!");
        wait(2);

        double damage = monster.getDamage(); 

        double dodgeChance = hero.getAgility() * 0.002; // scaling dodge
        dodgeChance = Math.min(dodgeChance, 0.5); 

        if (Math.random() < dodgeChance) {
            System.out.println(hero.getName() + " dodged the attack!");
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

        if (board.isValidMove(newRow, newCol)) {
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

    private void initializeMonsters() {
        int size = heros.length;
        monsters = new ArrayList<>();
        int num = 1;
        for (int i = 0; i < size; i++) {
            Monster m = createRandomMonster(num);
            monsters.add(m); 
            num++;
        }
    }

    private Monster createRandomMonster(int idx) {
        int result = rollDice(3);
        switch (result) {
            case 1:
                return new Exoskeleton(idx);
            case 2:
                return new Spirit(idx);
            default:
                return new Dragon(idx);
        }
    }




    private void printHeroOptions(int idx) {
        System.out.println("Controls:");
        System.out.println("W/A/S/D - move");
        System.out.println("I/C - manage inventory (view info, equip/use items)");
        System.out.println("M - enter market (if on market tile)");
        System.out.println("Q - quit game");
        System.out.println("H - Help/Information");
        System.out.printf(heros[idx].getName() + "'s' move: ");
    }


    private void startBattle() {
        initializeMonsters(); 

        while (!(allHeroesDead() || allMonstersDead())) {
            System.out.println();
            System.out.println("----- ROUND " + round + " -----");
            System.out.println("=============================================");
            printHeros();
            System.out.println();
            printMonsters();
            System.out.println("=============================================");

            herosBattleTurn();
            monstersBattleTurn();

            endOfRoundRecovery();
        }

        endBattle(); 

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

    private void herosBattleTurn() {
        System.out.println("--- Heros' Turn ---");
        for (int i = 0; i < heros.length; i++) {
            if (heros[i].getHP() > 0) {
                heroAction(i);
            }
        }

    }

    private void heroAction(int idx) {
        Hero hero = heros[idx];

        System.out.println("Actions for "+ hero.getName() + " (A=Attack, S=Spell, P=Potion, E=Equip, I=Info, Q=Quit):");
        List<String> validOptions = Arrays.asList("A", "S", "P", "E", "I", "Q");
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
            case 'A':
                attackFrom(hero);
                break;
            case 'S':
                castSpell(hero);
                break;
            case 'P':
                usePotion(hero);
                break;
            case 'E':
                manageInventory();
                break;
            case 'Q':
                quitGame();
                break;
            default:
                manageInventory();
        }
            
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
        for (Hero hero : heros) {
            if (hero.getHP() > 0) {
                hero.regain();
            } 
        }
    }

    private void endBattle() {
        if (!allHeroesDead()) {
            int num = monsters.size();
            monsters = null;

            int maxLevel = 0;
            for (Hero hero : heros) {
                // heros that didn't faint level up
                if (hero.getHP() > 0) {
                    hero.levelUp(num);
                    if (maxLevel < hero.getLevel()) {
                        maxLevel = hero.getLevel();
                    }
                } else { // heros that fainted get their HP reset
                    hero.setHP(hero.getBaseHP()); 
                }
            }
    
            loadMarketStock(maxLevel);
        }
    }

    private void castSpell(Hero hero) {
        if (hero.getSpells() == null || hero.getSpells().isEmpty()) {
            System.out.println(hero.getName() + " has no spells to use!");
            return;
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
            return;
        }

        System.out.println("Select target monster:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (m.getHP() > 0) {
                System.out.printf("(%d) %s HP: %d%n", i+1, m.getName(), m.getHP());
            }
        }

        int targetIdx = input.nextInt(1, monsters.size());
        Monster target = monsters.get(targetIdx - 1);

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
            return;
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

        
    }
    
    private void usePotion(Hero hero) {
         if (hero.getPotions() == null || hero.getPotions().isEmpty()) {
            System.out.println(hero.getName() + " has no potions to use!");
            return;
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
        this.board = new Grid(8);
        initializePlayers(board);

        while (!isDone()) {
            System.out.println("============HEROS' MOVE===============");
            for (int i = 0; i < heros.length; i++) {
                if (heros[i].getHP() > 0) {
                    handleHerosMove(i);
                }
            }
            System.out.println("============MONSTERS' MOVE===============");
            for (int i = 0; i < monsters.size(); i++) {
                if (monsters.get(i).getHP() > 0) {
                    handleMonstersMove(i);
                }
            }
        }
     
        return false;
    }

    private void initializePlayers(Board board) {

        // move heros and monsters onto their respective starting positions
        move(getHero(0), 0, 0+rollDice(2));
        move(getHero(1), 0, 3+rollDice(2));
        move(getHero(2), 0, 6+rollDice(2));
        move(getMonster(0), board.getDim() - 1, 0+rollDice(2));
        move(getMonster(1), board.getDim() - 1, 3+rollDice(2));
        move(getMonster(2), board.getDim() - 1, 6+rollDice(2));
    }

    private void move(Character ch, int row, int col) {
        ch.setPosition(row, col);
        if (ch instanceof Hero) {
             ((Block) board.getTile(row, col)).moveHeroOn((Hero) ch);
        } else if (ch instanceof Monster) {
            ((Block) board.getTile(row, col)).moveMonsterOn((Monster) ch);
        }
    }

    public boolean isDone() {
        return false; // game runs indefinitely
    }

    private Monster attackFrom(Hero hero) {
        if (hero.getInventory() == null) {
            return null;
        }
        System.out.println("Select target monster:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            if (m.getHP() > 0) {
                System.out.printf("(%d) %s HP: %d%n", i+1, m.getName(), m.getHP());
                // monsters[i].print();
            }
        }
       
        int option = input.nextInt(1,monsters.size());
        Monster target = monsters.get(option-1);

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
            return target;
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

        return monsters.get(option-1);
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

    private void reviveHeros() {
        // TODO: at the end of each round, heros that have not fainted regain some of their HP and mana
        // A fainted hero is revived with half of their HP and mana, but they will not gain any gold or experience.
    }

    @Override
    public void printBoard() {
        board.print();
    }


    private Block getCharactersBlock(Character ch) {
        int row = ch.getRow();
        int col = ch.getCol();
        // System.out.printf("Heros currently at row: %d, col %d/n", row, col);
        return (Block) board.getTile(row,col);
    }


    private boolean attemptMove(int idx, int rowChange, int colChange) {
        System.out.println("Attempting hero;s move!");
        int newRow = heros[idx].getRow() + rowChange;
        int newCol = heros[idx].getCol() + colChange;

        if (board.isValidMove(newRow, newCol)) {
            System.out.println("Before move: " + heros[idx].getRow() + "," + heros[idx].getCol());
            Block oldB = getCharactersBlock(heros[idx]);

            oldB.moveHeroOff();

            Block newB = (Block) board.getTile(newRow,newCol);
            newB.moveHeroOn(heros[idx]);

            heros[idx].setPosition(newRow, newCol);
            System.out.println("After move: " + heros[idx].getRow() + "," + heros[idx].getCol());

            return true;
        } else {
            System.out.println("You can't move there!");
            return false;
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
                    if (Integer.parseInt(option) > heros.length) {
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
                        if (Integer.parseInt(option) > heros.length) {
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
                    if (!equipWeapon(heros[i-1])) {
                        manageInventory();
                    }
                    break;
                case 2: 
                    if (!equipArmor(heros[i-1])) {
                        manageInventory();
                    }
                    break;
                case 3:
                    usePotion(heros[i-1]);
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
        System.out.println("==== " + heros[i].getName() + " ====");
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
                manageInventory();
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
        int i = input.nextInt(1,heros.length);
        Hero hero = heros[i-1];
        System.out.println("Hero Gold Budget: " + hero.getGold());
        System.out.printf("Enter item to buy: ");
        List<Item> inventory = hero.getInventory();
        int val = input.nextInt(1, marketStock.size());
        Item item = marketStock.get(val-1);

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
        int i = input.nextInt(1,heros.length);
        Hero hero = heros[i-1];
        
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
        String folder = "Legends_Monsters_and_Heroes/";
        marketStock.addAll(FileLoader.load(folder + "Armory.txt", line -> Armor.parse(line, i)));
        marketStock.addAll(FileLoader.load(folder + "Weaponry.txt", line -> Weapon.parse(line, i)));
        marketStock.addAll(FileLoader.load(folder + "Potions.txt", line -> Potion.parse(line, i)));
        marketStock.addAll(FileLoader.load(folder + "FireSpells.txt", line -> Spell.parse(line, i, "Fire")));
        marketStock.addAll(FileLoader.load(folder + "IceSpells.txt", line -> Spell.parse(line, i, "Ice")));
        marketStock.addAll(FileLoader.load(folder + "LightningSpells.txt", line -> Spell.parse(line, i, "Lightning")));
    }

    private void showHelp() {

        System.out.println("=============== Helpful Info about how to play Monsters and Heros: ===============");
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
        System.out.println("   Market spaces, where items can be bought or sold");
        System.out.println("   Common spaces, where battles can occur");
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