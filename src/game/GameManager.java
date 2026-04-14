package game;

import core.Game;
import iohandler.Input;

public class GameManager {
    private final Input input = new Input();

    public void start() {
        
        String name = input.nextLine("Enter your name: ");
        int[] heroTypes = new int[3]; // 3 heroes
        int[] heroLanes = new int[3]; // 3 heroes

        System.out.println("");
        System.out.println("Select difficulty:");
        System.out.println("1. Easy (monsters spawn every 5 rounds)");
        System.out.println("2. Medium (monsters spawn every 4 rounds)");
        System.out.println("3. Hard (monsters spawn every 3 round)");
        int difficulty = input.nextInt("Choice (1-3): ", 1, 3);

        for (int i = 0; i < 3; i++) {
            System.out.printf("Select type for Hero " + (i+1) + " (1: Warrior, 2: Sorcerer, 3: Paladin): ");
            heroTypes[i] = input.nextInt("",1,3);
            System.out.printf("Select lane for Hero " + (i+1) + " (1: Left, 2: Middle, 3: Right): ");
            heroLanes[i] = input.nextInt("",1,3);
            if (i == 2) {
                while (true) {
                    if (heroLanes[0] == heroLanes[1] && heroLanes[1] == heroLanes[2]) {
                        System.out.printf("Cannot have more than 2 heroes in the same lane. Please choose a different lane for Hero 3: ");
                        heroLanes[2] = input.nextInt("",1,3);
                    } else {
                        break;
                    }
                }
            }
        }       
        
        boolean playing = true;
        boolean won = true;

        while (playing) {
            System.out.println();
            System.out.printf("Welcome to Legends of Valor %s!%n", name);
            System.out.println("Prepare yourself for new adventures and challenges ahead!");
            System.out.println();
            System.out.println("The game will start shortly with the following heroes:");
            System.out.println();

            Game game = new LegendsOfValor(input, heroTypes, heroLanes, difficulty);
            
            won = game.play();
            if (won) {
                System.out.println("Congrats you won!");
            }
            playing = askReplay();
        }
    }


    public boolean askReplay() {
        Input input = new Input();
        while (true) {
            String answer = input.nextLine("Do you want to play another game? (Y/N) ");

            if (answer.equalsIgnoreCase("Y")) {
                return true;
            } 
            else if (answer.equalsIgnoreCase("N")) {
                System.out.println("Have a good day :D");
                return false;
            } 
            else {
                System.out.println("Invalid input! Please type Y or N.");
            }
        }
    }
}
