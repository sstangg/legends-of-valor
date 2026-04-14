package grid;

import java.util.Random;
import core.Board;
import core.Tile;

public class Grid extends Board {

    private Tile[][] grid;
    
    public Grid(int dim) {
        this.dim = dim;
        grid = new Tile[dim][dim];
        initializeGrid();
    }

    private void initializeGrid() {

        // setup grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if ((j == 2 || j == 5)) {
                    grid[i][j] = new Block(i, j, Block.Type.BORDER);
                    continue;
                }
                if ((i == 0 || i == grid.length - 1)) {
                    grid[i][j] = new Block(i, j, Block.Type.NEXUS);
                    continue;
                }
                int roll = rollDice(5);                    
                switch (roll) {
                    case 1:
                        grid[i][j] = new Block(i, j, Block.Type.INACCESSIBLE);
                        break;
                    case 2:
                        grid[i][j] = new Block(i, j, Block.Type.BUSH);
                        break;
                    case 3:
                        grid[i][j] = new Block(i, j, Block.Type.CAVE);
                        break;
                    case 4:
                        grid[i][j] = new Block(i, j, Block.Type.KOULOU);
                        break;
                    default:
                        grid[i][j] = new Block(i, j, Block.Type.PLAIN);
                }  
            }
        }
    }

    private int rollDice(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }

    @Override
    public Tile getTile(int row, int col) {
        return grid[row][col];
    }

    // get left / right adjacent accessible block to the position inputted
    public Block getAdjacentBlock(int r, int c) {
        Block adjacentBlock = null;
        if (c > 0 && this.getTile(r, c-1).isAccessible()) {
            adjacentBlock = ((Block) this.getTile(r, c-1));
        }else if (c< this.dim - 1 && this.getTile(r, c+1).isAccessible()) {
            adjacentBlock = ((Block) this.getTile(r, c+1));
        }
        return adjacentBlock;
    }

    @Override
    public boolean isValidMove(int row, int col) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid.length) {
            return false;
        }

        return grid[row][col].isAccessible();
    }

    private String repeat(char c, int times) {
        StringBuilder str = new StringBuilder(times);
        for (int i = 0; i < times; i++) {
            str.append(c);
        }
        return str.toString();
    }

    @Override
    public void print() {
 
        // Dynamically define puzzle border
        StringBuilder border = new StringBuilder("+");
        for (int i = 0; i < grid.length; i++) {
            border.append(repeat('=', 7)); 
            border.append("+");
        }
        String borderString = border.toString();

        // Print all rows
        for (int i = 0; i < grid.length; i++) {
            System.out.println(borderString);
            printRow((grid[i]));
        }
        System.out.println(borderString);
    }

    // Prints a single row
    private void printRow(Tile[] row) {
        System.out.print("|");
        for (Tile t : row) {
            Block b = (Block) t;
            
            if ( b == null) {
                System.out.print(repeat(' ', 7)); // space for empty
            } else {
                String symbol = b.getSymbol();
                System.out.print(center(symbol,7));
                
            }
                
            System.out.print("|");
        }
        System.out.println();
    }

    private String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }

        int padding = width - text.length();
        int right = padding / 2;
        int left = padding - right;

        return repeat(' ', left) + text + repeat(' ', right);
    }

}