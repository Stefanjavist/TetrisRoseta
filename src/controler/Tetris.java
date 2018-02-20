package controler;

import model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.Arrays;
import java.util.Random;

/**
 * Config is a class default by area of visibility.
 *
 */


public class Tetris extends JPanel implements Runnable
 {

     enum Dir{
        right(1,0), down(0,1), left(-1,0);

        Dir(int x, int y) {
            this.x = x;
            this.y = y;
        }

        final int x,y;
     }

     public static final int EMPTY = -1;
     public static final int BORDER = -2;

     int fallingRow =0;
     int fallingCol = 0;

     int[][] grid = new int[nRows][nCols];

     Shape fallingShape;
     Shape nextShape;

     final static Random rand = new Random();
     Thread fallingThread;
     final Scoreboard scoreboard = new Scoreboard();


     Tetris() {
         setPreferredSize(dim);
         setBackground(Color.black);

         initGrid();
         selectShape();

         addMouseListener(new MouseAdapter() {

             public void mousePressed() {
                 if(scoreboard.isGameOver()) {
                    startNewGame();
                    repaint();
                 }
             }

         });

         addKeyListener(new KeyAdapter() {
                 boolean fastDown;
             @Override
             public void keyPressed(KeyEvent e) {

                 switch (e.getKeyCode()) {
                     case KeyEvent.VK_UP:
                         if(canRotate(fallingShape)) {
                             rotate(fallingShape);
                         }
                         break;
                     case KeyEvent.VK_RIGHT:
                         if(canMove(fallingShape, Dir.right)) {
                             move(Dir.right);
                         }
                         break;
                     case KeyEvent.VK_LEFT:
                         if(canMove(fallingShape, Dir.left)) {
                             move(Dir.left);
                         }
                         break;
                     case KeyEvent.VK_DOWN:
                         if(!fastDown) {
                             fastDown = true;

                             while(canMove(fallingShape, Dir.down)) {
                                 move(Dir.down);
                             }
                             shapeHasLanded();
                         }
                 }
                 repaint();
             }

             @Override
             public void keyReleased(KeyEvent e) {
                fastDown  = false;
             }
         });
     }

     void startNewGame() {
         stop();
         selectShape();
         initGrid();
         scoreboard.reset();
         (fallingThread = new Thread(this)).start();
     }

     void shapeHasLanded() {
         addShape(fallingShape);
         if(fallingRow < 2) {
               scoreboard.setTopscore();
               scoreboard.setGameOver();
               stop();

         }else {
             scoreboard.addLines(removeLines());
         }
         selectShape();
     }

     int removeLines() {

         int count = 0;
         for (int r = 0; r < nRows - 1; r++) {
             for (int c = 1; c < nCols - 1; c++) {
                 if (grid[r][c] == EMPTY) {
                     break;
                 }

                 if (c == nCols - 2) {
                    count++;
                    removeLine(r);
                 }
             }
         }

         return count;
     }

     void removeLine(int line) {
         for (int c = 0; c < nCols; c++) {
             grid[line][c] = EMPTY;
         }


         for (int c = 0; c < nCols ; c++) {
             for (int r = line; r > 0 ; r--) {
                 grid[r][c] = grid[line -1][c];
             }
         }

     }

     void addShape(Shape s) {
         for (int[] p: s.pos) {
             grid[fallingRow + p[1]][fallingCol + p[0]] = s.ordinal();
         }
     }

     void stop() {
         if(fallingThread != null) {
             Thread tmp = fallingThread;
             fallingThread = null;
             tmp.interrupt();
         }
     }

     void selectShape() {
        fallingRow =1;
        fallingCol = 5;
        fallingShape = nextShape;
        Shape[] shapes = Shape.values();
        nextShape = shapes[rand.nextInt(shapes.length)];
        if(fallingShape != null)
            fallingShape.reset();
     }

     boolean canRotate(Shape s) {
         if(s == Shape.Square)
             return false;

         int [][] pos = new int[4][2];
         for(int i=0; i < pos.length; i ++) {
            pos[i] = s.pos[i].clone();
         }

         for(int[] row : pos) {
             int tmp = row[0];
             row[0] = row[1];
             row[1] = -tmp;
         }

        for(int[] p : pos) {
             int newRow = fallingRow + p[1];
             int newCol = fallingCol + p[0];
             if(grid[newRow][newCol] != EMPTY) {
                 return false;
             }

        }

         return true;
     }

     boolean canMove(Shape shape, Dir dir) {
         for (int[] p: shape.pos.clone()) {
             int newRow = fallingRow + dir.y + p[1];
             int newCol = fallingCol + dir.x + p[0];
             if(grid[newRow][newCol] != EMPTY)
                 return false;
         }
        return true;
     }

     void move(Dir dir) {
         fallingRow += dir.x;
         fallingCol += dir.y;
     }

     void rotate(Shape s) {
         if(s == Shape.Square)
             return;

         for(int[] row : s.pos.clone()) {
             int tmp = row[0];
             row[0] = row[1];
             row[1] = -tmp;
         }
     }

     void initGrid() {
         for (int r = 0; r < nRows; r++) {
             Arrays.fill(grid[r], EMPTY);
             for (int c = 0; c < nCols; c++) {
                 if(c == 0 || c == nCols-1 || r == nRows -1)
                     grid[r][c] = BORDER;
             }
         }
     }


     @Override
     public void run() {
            while(Thread.currentThread() == fallingThread) {
                try {
                    Thread.sleep(scoreboard.getSpeed());
                } catch(InterruptedException e ) {
                    return;
                }

                if(!scoreboard.isGameOver()) {
                    move(Dir.down);
                } else{
                    shapeHasLanded();
                }

            }
     }

}
