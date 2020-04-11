package flyingtoucanist411;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author lchuf
 */
public class VisPanel extends JPanel {
    private Toucan player;
    private ArrayList<Rectangle> obstacles;
    private FlyingToucanIST411 game;
    private Font gameFont, menuFont, titleFont;
    public static final int OB_WIDTH = 50, OB_HIEGHT = 30, GAP = 400;
    public static final int ABOVE_H = 260, ABOVE_W = 104,
                             BELOW_H = 260, BELOW_W = 112;
    
    private Image obAbove, obBelow;
    private Image background, start, startPush, scores, scoresPush, save, savePush, back, backPush;
    private JToggleButton startButton, scoreButton, saveButton, backButton;
    private JTextField nameField;
    private boolean nameFieldAdded = false, saveButtonVisible = true, nameFieldEnabled = true;
    DatabaseAccess database;
            
    public VisPanel(FlyingToucanIST411 gameIn, Toucan playerIn, ArrayList<Rectangle> obs) throws FontFormatException, IOException {
        this.game = gameIn;
        this.player = playerIn;
        this.obstacles = obs;
        
        gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("pixel_font.ttf")).deriveFont(30f);
        titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("pixel_font.ttf")).deriveFont(100f);
        database = new DatabaseAccess();
        
        try {
            setImages();

            //initialize buttons
            this.setLayout(null);
            loadStartButton();
            loadScoreButton();
            loadSaveButton();
            loadBackButton();
            loadNameField();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 0, -150, this);
        
        for(Rectangle ob: obstacles) {
            Graphics2D flatG = (Graphics2D) g;            
//test
//flatG.setColor(Color.RED);
//flatG.drawRect(ob.x-30, ob.y-30, ABOVE_W, ABOVE_H);
//flatG.drawRect(ob.x-30, ob.y-30 + GAP, ABOVE_W, ABOVE_H);
//test end
            
            //NOTE, the obstacles/boxes dont really draw in on point, adding the 30s below seemed to fix. 
            flatG.drawImage(obAbove, ob.x-30, ob.y-30, ABOVE_W, ABOVE_H, null);
            flatG.drawImage(obBelow, ob.x-30, ob.y-30 + GAP, ABOVE_W, ABOVE_H, null);          
        }
        
        //player will be on top of obstacles
        player.updateAsset(g);
        
        //update score
        if(!game.isGameStopped()) {
            g.setFont(gameFont);
            g.setColor(Color.white);
            g.drawString("Score: "+game.getScore(), 10, 30);
        }
        
        //allows the player to hover after start clicked, to prevent falling to death
        if(!game.isFlying() && !game.isGameStopped()) { 
            player.resetToucan();
        }
        
        //when in menu
        if(game.isGameStopped()) {
            if(game.isShowScores()) {
                drawHiScores(g);
            }
            else if(game.isInitMenu()) {
                startButton.setSelected(false);
                this.add(startButton);
                scoreButton.setSelected(false);
                this.add(scoreButton);
                player.hideToucan();
                
                //title and key guide
                g.setColor(Color.white);
                g.setFont(titleFont);
                g.drawString("Flying Toucan", 70, 100);
                g.setColor(Color.gray);
                g.setFont(gameFont);
                g.drawString("Press [ space ] to fly up!", 185, 330);
            } else {
                //menu after first attempt
                
                startButton.setSelected(false);
                this.add(startButton);
                scoreButton.setSelected(false);
                this.add(scoreButton);
                //display "game over" and player score
                g.setColor(Color.white);
                g.setFont(titleFont);
                g.drawString("GAME OVER", 120, 100);
                g.setFont(gameFont);
                g.drawString("Your score: " + game.getScore(), 250, 140);
                
                //Enter name, will all need to be moved
                g.drawString("Name: ", 180, 180);
                if(!nameFieldAdded) {
                    this.add(nameField);
                    this.nameFieldAdded = true;
                }
                if(!nameFieldEnabled) {
                    nameField.setEnabled(false);
                } else {
                    nameField.setEnabled(true);
                }
                nameField.grabFocus();
//                if(saveButtonVisible) {
//                    this.add(saveButton); 
//                }
                if(!nameField.getText().isEmpty() && nameField.getText() != null && saveButtonVisible) {
                    this.add(saveButton);
                } else {
                    this.remove(saveButton);
                }
                saveButton.setSelected(false);
                
                //change button bounds
                startButton.setBounds(FlyingToucanIST411.WIDTH/2-100, FlyingToucanIST411.HEIGHT/2-40, 200, 88);
                scoreButton.setBounds(FlyingToucanIST411.WIDTH/2-100, FlyingToucanIST411.HEIGHT/2+60, 200, 40);
            }
            
            
        }
        
        //TODO do a version of menu that shows score and doesnt show title
            //also asks user to enter name if desired, and a submit button
    }

    
    /*
     *ASSETS & LOADING
     */
    
    private void loadStartButton() {
        startButton = new JToggleButton(new ImageIcon(start));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setBounds(FlyingToucanIST411.WIDTH/2-100, FlyingToucanIST411.HEIGHT/2-80, 200, 88);
        startButton.setSelectedIcon(new ImageIcon(startPush));
        startButton.setFocusable(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameStarted();
            }
        });
    }
    
    private void loadScoreButton() {
        scoreButton = new JToggleButton(new ImageIcon(scores));
        scoreButton.setOpaque(false);
        scoreButton.setContentAreaFilled(false);
        scoreButton.setBorderPainted(false);
        scoreButton.setFocusPainted(false);
        scoreButton.setBounds(FlyingToucanIST411.WIDTH/2-100, FlyingToucanIST411.HEIGHT/2+20, 200, 40);
        scoreButton.setSelectedIcon(new ImageIcon(scoresPush));
        scoreButton.setFocusable(false);
        scoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showScores();
            }
        });
    }
    
    private void loadSaveButton() {
        saveButton = new JToggleButton(new ImageIcon(save));
        saveButton.setOpaque(false);
        saveButton.setContentAreaFilled(false);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setBounds(400, 150, 92, 40);
        saveButton.setSelectedIcon(new ImageIcon(savePush));
        saveButton.setFocusable(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonPressed();
                //disable save button after entry that isnt ""
            }
        });
    }
    
    private void saveButtonPressed() {
        String name = "";
        String score = Integer.toString(game.getScore());
        if(!nameField.getText().isEmpty() && nameField.getText() != null) {
            name = nameField.getText();
            
            database.addScore(name, score);
            
            saveButtonVisible = false;
            this.remove(saveButton);
            nameFieldEnabled = false;
        }
    }
    
    private void loadBackButton() {
        backButton = new JToggleButton(new ImageIcon(back));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setBounds(FlyingToucanIST411.WIDTH/2-100, 385, 200, 40);
        backButton.setSelectedIcon(new ImageIcon(backPush));
        backButton.setFocusable(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonPressed();
            }
        });
    }
    
    private void backButtonPressed() {
        this.remove(backButton);
        game.setShowScores(false);
    }
    
    private void drawHiScores(Graphics g) {
        g.setColor(Color.white);
        g.setFont(titleFont);
        g.drawString("Flying Toucan", 70, 100);
        
        backButton.setSelected(false);
        g.setColor(Color.white);
        g.setFont(gameFont);
        g.drawString("HI-SCORES", 270, 160);
        
        ArrayList<ScoreResultSet> topFiveList = new ArrayList();
        //get topfive score from database, other class for sql
//topFiveList = database.getScoreList();
        
        g.setColor(Color.white);
        g.setFont(gameFont);
        
        int yScore = 200;
        //temp
        for(int x = 1; x<6; x++) {
            g.drawString(x + ". " + "temp", 240, yScore);
            yScore += 40;
        }
        
        
        //when done with temp
//        for(int x = 1; x<6; x++) {
//            ScoreResultSet result = topFiveList.get(x);
//            g.drawString(x + ". " + result.getName() + ": " + result.getScore(), 240, yScore);
//            yScore += 40;
//        }
    }
    
    //TODO add to new panel ontop of gamepanel
    private void loadNameField() {
        nameField = new JTextField();
        nameField.setBounds(250, 155, 140, 30);
        Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white);
        nameField.setBorder(border);
        nameField.setOpaque(false);
        nameField.setForeground(Color.white);
        nameField.setCaretColor(Color.white);
        nameField.setFont(gameFont);
        //actions listener for enter? which presses button
    }
    
    private void setImages() throws IOException {
        obAbove = ImageIO.read(new File("Vines Outline.png"));
        obBelow = ImageIO.read(new File("Bush Outline.png"));
        background = ImageIO.read(new File("Jungle Background.png"));
        start = ImageIO.read(new File("Start Big.png"));
        startPush = ImageIO.read(new File("Start Big Pushed.png"));
        scores = ImageIO.read(new File("hi-scores.png"));
        scoresPush = ImageIO.read(new File("hi-scores pushed.png"));
        save = ImageIO.read(new File("save.png"));
        savePush = ImageIO.read(new File("save push.png"));
        back = ImageIO.read(new File("back.png"));
        backPush = ImageIO.read(new File("back push.png"));
    }
    
    
    /*
     * VARIOUS ACTIONS
     */
    
    public void startButtonPressed() {
        startButton.setSelected(true);
        gameStarted();
    }
    
    public void startButtonReset() {
        startButton.setSelected(false);
    }
    
    private void gameStarted() {
        this.remove(startButton);
        this.remove(scoreButton);
        this.remove(saveButton);
        this.remove(nameField);
        this.nameField.setText("");
        this.nameFieldAdded = false;
        this.nameFieldEnabled = true;
        
        game.resetAssets();
        
        game.setStop(false);
        game.setInitMenu(false);
        game.setScore(0);
        saveButtonVisible = true;
    }
    
    private void showScores() {
        this.remove(startButton);
        this.remove(scoreButton);
        this.remove(saveButton);
        this.remove(nameField);
        this.nameField.setText("");
        this.nameFieldAdded = false;
        
        game.setShowScores(true);
        this.add(backButton);
    }
    
    //TODO remove menu method
}
