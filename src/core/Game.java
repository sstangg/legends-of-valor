package core;

public abstract class Game {

    protected boolean complete = false;

    // public boolean start() {
    //     init();

    //     while(!complete) {
    //         render();
    //         handleInput();
    //         update();
    //     }
    // }

    protected Board board;

    // protected abstract void initializeGame();
    // protected abstract void explainGame();

    public abstract boolean play();
    public abstract void printBoard();
}