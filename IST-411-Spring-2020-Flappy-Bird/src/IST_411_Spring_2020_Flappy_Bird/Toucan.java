/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IST_411_Spring_2020_Flappy_Bird;

import java.awt.*;
import java.io.*;
import javax.imageio.*;

/**
 *
 * @author lchuf
 */
public class Toucan {
    private Image toucanImage;
    public static final int IMG_HEIGHT = 32;
    public static final int IMG_WIDTH = 112;
    public static final int RAD = 32;
    public float xValue, yValue, velocityX, velocityY;
    
    public Toucan() throws IOException {
        xValue = 150;
        yValue = FlyingToucanIST411.HEIGHT/2;
        toucanImage = ImageIO.read(new File("Birb Outline.png"));
    }
    
    public void flightPhysics() {
        if(xValue < 0 || yValue < 0) {
            xValue = 150;
            yValue = FlyingToucanIST411.HEIGHT/2;
        }
        
        xValue += velocityX;
        yValue += velocityY;
        velocityY += .5f;
    }
    
    public void updateAsset(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawImage(toucanImage, Math.round(xValue-RAD),Math.round(yValue-RAD),IMG_WIDTH,IMG_HEIGHT, null);

//boundary tests
//g.drawRect(Math.round(xValue-RAD),Math.round(yValue-RAD),IMG_WIDTH,IMG_HEIGHT);
//g.drawOval(Math.round(xValue-RAD)-5,Math.round(yValue-RAD)-5, 10, 10);
//g.drawOval(Math.round(xValue-RAD)-5 + IMG_WIDTH,Math.round(yValue-RAD)-5 + IMG_HEIGHT, 10, 10);
    }
    
    public void up() {
        velocityY = -8;
    }
    
    public void resetToucan() {
        xValue = 150;
        yValue = FlyingToucanIST411.HEIGHT/2;
        velocityX = velocityY = 0;
    }
    
    public void hideToucan() {
        xValue = -100;
        yValue = -100;
        velocityX = velocityY = 0;
    }
    
}
