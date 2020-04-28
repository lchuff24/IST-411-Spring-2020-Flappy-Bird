/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flyingtoucanist411;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author drewlister
 */
public class Top5Bean {
    /**
     *
     * @return
     */
    public ScoreResultSet GetTopPlayer() {
        ScoreResultSet playerResults = new ScoreResultSet();
        ArrayList topPlayerList = new ArrayList();
        ArrayList playerList = new ArrayList();
             
        try {
            String filePath = FlyingToucanIST411.class.getResource("/database/Toucans.db").getPath();
            Connection myC = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            String PLAYER = "SELECT name FROM Scores ORDER BY CAST(score AS INTEGER) DESC LIMIT 5";
            Statement myStatement = myC.createStatement();
            
            ResultSet rs = myStatement.executeQuery(PLAYER);
            while(rs.next()){
                playerList.add(rs.getString("name"));
                //playerList.add(rs.getString("score"));
            }
            topPlayerList.add(playerList);
            myStatement.close();         
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(VisPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        playerResults.setTopPlayer(topPlayerList);
        return playerResults;
    }
    
    public ScoreResultSet getTopScore() {
        ScoreResultSet scoreResults = new ScoreResultSet();
        ArrayList topScoresList = new ArrayList();
        ArrayList scoreList = new ArrayList();      
        
        try {
            String filePath = FlyingToucanIST411.class.getResource("/database/Toucans.db").getPath();
            Connection myC = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            String SCORE = "SELECT score FROM Scores ORDER BY CAST(score AS INTEGER) DESC LIMIT 5";
            Statement myStatement = myC.createStatement();
            
            ResultSet rs2 = myStatement.executeQuery(SCORE);
            while(rs2.next()){
                scoreList.add(rs2.getString("score"));
            }
            topScoresList.add(scoreList);
            myStatement.close();         
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(VisPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        scoreResults.setTopScore(topScoresList);
        return scoreResults;
    }

}
