/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flyingtoucanist411;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import javax.swing.Timer;

/**
 *
 * @author lchuf
 */
public class FlyingToucanIST411 implements ActionListener, KeyListener {
    public static final int FPS = 60, 
                            WIDTH = 640, 
                            HEIGHT = 480,
                            LR_BUFFER = 10,
                            TB_BUFFER = 10;
    
    private JFrame mainFrame;
    private VisPanel gamePanel;
    private Toucan player;
    private ArrayList<Rectangle> obstacles;  //TODO replace with obstacle images
    private int gameTime, gameScroll;
    private Timer timer;
    private boolean stop;
    
    private int score;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FontFormatException {
        // TODO code application logic here
        new FlyingToucanIST411().start();
    }
    
    public void start() throws IOException, FontFormatException {
        player = new Toucan();
        obstacles = new ArrayList<Rectangle>();
        
        mainFrame = new JFrame("Flying Toucan");
        gamePanel = new VisPanel(this, player, obstacles);
        mainFrame.add(gamePanel);
        
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.addKeyListener(this);
        //addMouseListener();
        
        stop = true;
        
        timer = new Timer(1000/FPS, this);
        timer.start();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        gamePanel.repaint();
        //if not stopped
        if(!stop) {
            player.flightPhysics();
            //number below in if statement determines how oftten obstacles are spawned
            if(gameScroll % 100 == 0) {
                int randHeight = ((int)(Math.random() * 7) //makes numbers between 0 and N(number of different heights)
                                                        +1) //between 1-N no only not including 1 and N
                                                       *30 //makes the rand height a multiple of N
                                                       -200; //final height(y) adjustments

                System.out.println(randHeight);
                //top rectangle
                Rectangle r = new Rectangle(WIDTH, randHeight, VisPanel.ABOVE_W, VisPanel.ABOVE_H);
                //below rectangle
//                Rectangle r2 = new Rectangle(WIDTH, randHeight + VisPanel.GAP, VisPanel.BELOW_W, VisPanel.BELOW_H);
                obstacles.add(r);
//                obstacles.add(r2);
            }
            ArrayList<Rectangle> remove = new ArrayList<Rectangle>();
            boolean running = true;
            //handle active obstacles and collision
            for(Rectangle r : obstacles) {
                r.x-=3; //obstacle speed
                if(r.x + r.width <= 0) {
                    remove.add(r);
                }
                
                //location for image collission
                //top ob check
                if( player.xValue < r.x + r.width - LR_BUFFER   //checks collision on right side, number indicates buffer
                 && player.xValue + player.IMG_WIDTH > r.x + LR_BUFFER //checks collision on left side, number indicates buffer
                 && player.yValue < r.y + r.height - TB_BUFFER  //checks collision on bottom side, number indicates buffer
//                 && player.yValue + player.IMG_HEIGHT > r.y + TB_BUFFER //checks collision on top side, number indicates buffer, not needed for top obstacle
                    ) 
                {
                    running = false;
                    gameOver();
                }
                //bottom ob check 
                else if (player.xValue < r.x + r.width - LR_BUFFER  //checks collision on right side, number indicates buffer
                 && player.xValue + player.IMG_WIDTH > r.x + LR_BUFFER //checks collision on left side, number indicates buffer
//                 && player.yValue < r.y + r.height + VisPanel.GAP - LR_BUFFER  //checks collision on bottom side, number indicates buffer, not needed for top obstacle
                 && player.yValue + player.IMG_HEIGHT > r.y + VisPanel.GAP + LR_BUFFER  //checks collision on top side, number indicates buffer
                    )
                {
                    running = false;
                    gameOver();
                }
                
                if(player.xValue>r.x+r.width && player.xValue<r.x +r.width+4) {
                    score++;
                }
                
            }
            obstacles.removeAll(remove);
            gameTime++;
            gameScroll++;
            
            //if player goes out of map
            if(player.yValue + player.IMG_HEIGHT > HEIGHT || player.yValue < 0) {
                gameOver();
                running = false;
            }

            if(!running) {
                obstacles.clear();
                //player.resetToucan();  //commented out to work with hiding player
                gameTime = 0;
                gameScroll = 0;
                score = 0;
                stop = true;
                gamePanel.startButtonReset();
            }
        }
    }
    
    private void gameOver() {
        JOptionPane.showMessageDialog(mainFrame, "GAME OVER\n"+"Score: " + score);
        //remove above for sep inframe panel?
        
        //send score
        //and maybe display other hi scores?
    }
    
    public int getScore() {
        return score;
    }
    
    public void keyPressed(KeyEvent e) {
//        System.out.println("keylistener");
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            if(this.stop == true) {
//                gamePanel.startButtonPressed();
//                this.stop = false;
            } else {
                player.up();
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        
    }
    public void keyTyped(KeyEvent e) {
        
    }
    
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    public boolean isGameStopped() {
        return stop;
    }
        
}
