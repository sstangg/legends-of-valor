package grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        int total = grid.length * grid[0].length;
        
        int market = total * 3 / 10;
        int inaccessible = total * 2 / 10;
        int common = total - inaccessible - market - 2;

        List<Block.Type> blocks = new ArrayList<>();

        for (int i = 0; i < market; i++) {
            blocks.add(Block.Type.MARKET);
        }
        for (int i = 0; i < inaccessible; i++) {
            blocks.add(Block.Type.INACCESSIBLE);
        }
        for (int i = 0; i < common; i++) {
            blocks.add(Block.Type.COMMON);
        }

        Collections.shuffle(blocks);

        grid[0][0] = new Block(0,0, Block.Type.COMMON); // fixed hero starting point
        ((Block) grid[0][0]).moveHerosOn();

        grid[1][0] = new Block(1,0, Block.Type.COMMON); // ensures there is always a way to exit 

        int idx = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if ((i == 0 && j == 0) || (i == 1 && j == 0)) {
                    continue;
                }
                grid[i][j] = new Block(i, j, blocks.get(idx++));
            }
        }
    }

    @Override
    public Tile getTile(int row, int col) {
        return grid[row][col];
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
            border.append(repeat('-', 3)); 
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
                System.out.print(repeat(' ', 1 + 2)); // space for empty
            } else {
                System.out.print(" " + b.getSymbol() + " ");
            }
            System.out.print("|");
        }
        System.out.println();
    }

}