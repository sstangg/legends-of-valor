package game;

import core.Game;
import iohandler.Input;

public class GameManager {
    private final Input input = new Input();

    public void start() {
        
        String name = input.nextLine("Enter your name: ");
        int[] heroTypes = new int[3]; // 3 heroes
        System.out.println("Select 3 heroes (1: Warrior, 2: Sorcerer, 3: Paladin:");
        for (int i = 0; i < 3; i++) {
            System.out.printf("Hero %d: ", i+1);
            heroTypes[i] = input.nextInt("",1,3);
        }       
        
        boolean playing = true;
        boolean won = true;

        while (playing) {
            System.out.printf("Welcome to Legends of Valor %s!%n", name);
            System.out.println("Prepare yourself for new adventures and challenges ahead!");

            Game game = new LegendsOfValor(input, heroTypes);
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
