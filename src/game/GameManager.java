package game;

import core.Game;
import iohandler.Input;

public class GameManager {
    private final Input input = new Input();

    public void start() {
        
        String name = input.nextLine("Enter your name: ");
        int count = input.nextInt("Enter the hero count (1-3): ", 1, 3);
        int[] heroTypes = new int[count];
        for (int i = 0; i < count; i++) {
            System.out.printf("Select your hero %d(1: Warrior, 2: Sorcerer, 3: Paladin):", i+1);
            heroTypes[i] = input.nextInt("",1,3);
        }       
        
        boolean playing = true;
        boolean won = true;

        while (playing) {
            System.out.printf("Welcome to Monsters And Heros %s!%n", name);
            System.out.println("Prepare yourself for new adventures and challenges ahead!");

            Game game = new MonstersAndHeros(input, heroTypes);
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
