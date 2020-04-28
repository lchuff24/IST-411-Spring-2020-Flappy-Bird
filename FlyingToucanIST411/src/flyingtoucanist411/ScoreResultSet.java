package flyingtoucanist411;

import java.util.ArrayList;

/**
 *
 * @author lchuf
 */
public class ScoreResultSet {
    private String score = null;
    private String name = null;
    
    /**
     * @return the score
     */
    public  String getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(String score) {
        this.score = score;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    private ArrayList player = new ArrayList();
    public ArrayList getTopPlayer() {
        return player;
    }

    /**
     * @param player the dropdownList to set
     */
    public void setTopPlayer(ArrayList player) {
        this.player = player;
    }   
    
    private ArrayList highScore = new ArrayList();
    public ArrayList getTopScore() {
        return highScore;
    }

    /**
     * @param highScore the dropdownList to set
     */
    public void setTopScore(ArrayList highScore) {
        this.highScore = highScore;
    } 
}
