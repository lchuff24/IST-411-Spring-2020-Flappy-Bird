package flyingtoucanist411;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author lchuf
 */
public class VisPanel extends JPanel {
    private Toucan player;
    private ArrayList<Rectangle> obstacles;
    private FlyingToucanIST411 game;
    private Font gameFont, menuFont, titleFont;
    public static final Color bg = new Color(0, 160, 160); //TODO swap for image
    public static final int OB_WIDTH = 50, OB_HIEGHT = 30;
    private Image obTop, obBody;  //swap for single obstacles
    
    private Image obAbove, obBelow;
    public static final int ABOVE_H = 260, ABOVE_W = 104,
                             BELOW_H = 260, BELOW_W = 112;
    public static final int GAP = 400;
    private Image background;
    private Image start, startPush, scores, scoresPush;
    private JToggleButton startButton;
    private JToggleButton scoreButton;
    
    public VisPanel(FlyingToucanIST411 gameIn, Toucan playerIn, ArrayList<Rectangle> obs) throws FontFormatException, IOException {
        this.game = gameIn;
        this.player = playerIn;
        this.obstacles = obs;
        
//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("pixel_font.ttf")));
        
        gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("pixel_font.ttf")).deriveFont(30f);
        titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("pixel_font.ttf")).deriveFont(100f);
        
//        gameFont = new Font("Arial", Font.BOLD, 18); //change from comic sans gross lol
//        menuFont = new Font("Arial", Font.BOLD, 48);
        
        try {
            setImages();

            //initialize buttons
            this.setLayout(null);
            loadStartButton();
            loadScoreButton();
        
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
        
        if(!game.isGameStopped()) {
            player.updateAsset(g);
            g.setFont(gameFont);
            g.setColor(Color.white);
            g.drawString("Score: "+game.getScore(), 10, 30);
        }
        
        if(game.isGameStopped()) {
            player.hideToucan();
            //startButton.setSelected(false);
            this.add(startButton);
            scoreButton.setSelected(false);
            this.add(scoreButton);
            
            //title and key guide
            g.setColor(Color.white);
            g.setFont(titleFont);
            g.drawString("Flying Toucan", 70, 100);
            g.setColor(Color.gray);
            g.setFont(gameFont);
            g.drawString("Press [ space ] to fly up!", 185, 330);
        }
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
                startButtonActionPerformed(evt);
            }
        });
    }
        
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        gameStarted();
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
                scoreButtonActionPerformed(evt);
            }
        });
    }
    
    private void scoreButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //TODO remove home page and show scores from sql
    }
    
    private void setImages() throws IOException {
        obTop = ImageIO.read(new File("78px-Pipe.png"));
        obBody = ImageIO.read(new File("pipe_part.png"));

        obAbove = ImageIO.read(new File("Vines Outline.png"));
        obBelow = ImageIO.read(new File("Bush Outline.png"));
        background = ImageIO.read(new File("Jungle Background.png"));
        start = ImageIO.read(new File("Start Big.png"));
        startPush = ImageIO.read(new File("Start Big Pushed.png"));
        scores = ImageIO.read(new File("hi-scores.png"));
        scoresPush = ImageIO.read(new File("hi-scores pushed.png"));
    }
    
    /*
     * VARIOUS ACTIONS
     */
    
    public void startButtonPressed() {
        startButton.setSelected(true);
        //TimeUnit.SECONDS.sleep(1);
        gameStarted();
    }
    
    public void startButtonReset() {
        startButton.setSelected(false);
    }
    
    private void gameStarted() {
        game.setStop(false);
        this.remove(startButton);
        this.remove(scoreButton);
    }
}
