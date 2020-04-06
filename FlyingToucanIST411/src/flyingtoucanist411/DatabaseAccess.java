package flyingtoucanist411;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import flyingtoucanist411.ScoreResultSet;
import java.sql.*;
/**
 *
 * @author lchuf
 */
public class DatabaseAccess {
    private PreparedStatement sqlFind;
    
    private String filepath = "JDBC:SQlite:FlyingToucansIST411/Toucans.DB";
    private Connection myCon= null;
    
    public ArrayList getScoreList() {
        try{
//            sqlFind.setString(1, lastName );
            ArrayList<ScoreResultSet> scoreList = new ArrayList();
            ResultSet resultSet = sqlFind.executeQuery();

            while (resultSet.next()) {
                ScoreResultSet resultObject = new ScoreResultSet();

                // set AddressBookEntry properties
                resultObject.setName( resultSet.getString( 1 ) ); //if name if first in table
                resultObject.setScore( resultSet.getString( 2 ) ); //if score is second
                scoreList.add(resultObject);
            }

            return scoreList;
        }
        
        catch ( SQLException sqlException ) {
         return null;
      }
    }
    
    public void addScore(String name, String score) {
        SQLiteDatabase ToucanDB = new SQLiteDatabase();// creates database object
        ToucanDB.setFilePath(filepath);// sets filepath for database
        ToucanDB.connectDatabase(myCon);// connects to database with connection myCon
        ToucanDB.inputScores(name, score);// adds name and score into database
        ToucanDB.disconnect(ToucanDB.getMyCon());// disconnects from database
        //add name and score as new entry in db
    }
}
