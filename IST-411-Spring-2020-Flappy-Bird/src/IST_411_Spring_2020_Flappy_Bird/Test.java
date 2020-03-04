/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IST_411_Spring_2020_Flappy_Bird;

/**
 *
 * @author Jacob Mullan
 */
public class Test 
{
    public static void main(String[] args) 
    {
       SQLiteDatabase myTest = new SQLiteDatabase();
        
        myTest.setFilePath("jdbc:sqlite:D:/sqlite/DB/Test.db");
        
        myTest.createDatabase();
        
        myTest.connectDatabase();
        
        String SQLTable = "CREATE TABLE IF NOT EXISTS Scores (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    name text NOT NULL,\n"
                + "    Score interger NOT NULL \n"
                + ");";
        
        myTest.createTable(SQLTable);
        
        String SQLDropTable = "DROP TABLE Scores;";
        
        myTest.DropTable(SQLDropTable);
       
    }
   
}
