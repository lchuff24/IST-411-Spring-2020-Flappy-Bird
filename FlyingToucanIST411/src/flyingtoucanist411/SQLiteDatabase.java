/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flyingtoucanist411;

import java.sql.*;

/**
 *
 * @author Jake_Gaming_PC
 */
public class SQLiteDatabase 
{
    
    
    private String filePath = FlyingToucanIST411.class.getResource("/database/Toucans.db").getPath();
    private Connection  myCon = null;

    
    
    public Connection getMyCon() 
    {
        return myCon;
    }// getMyCon

    
    
    public void setMyCon(Connection newMyCon)
    {
        myCon = newMyCon;
    }// setMyCon
    
    
    
    public String getFilePath() {
        return filePath;
    }// getFilePath

    
    
    public void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }// setfilePath
    
  
    
    
    public void createDatabase()
    {
        
        
        try(Connection createCon = DriverManager.getConnection("jdbc:sqlite:" + filePath.substring(1)))// + filePath.substring(1)))
        {
           
            if (createCon != null) 
            {
                DatabaseMetaData meta = createCon.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A database has been created in " + filePath);
                
            }// Creates database if connection is not Null
            
        }
        
        catch(SQLException e)
        {
             System.out.println(e.getMessage());
        }//catches SQLException
        
        
    }// createDatabase
    
    
    
    public void connectDatabase()
    {
     //code followed  from  https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
    
       
        try
        {
           myCon = DriverManager.getConnection("jdbc:sqlite:" + filePath.substring(1));// connects to the database using the filepath
           
           // jdbc:sqlite:D:\\Users\\Jake_Gaming_PC\\Documents\\NetBeansProjects\\FlyingToucansIST411\\FlyingToucanIST411\\src\\database\\Toucans.db
           System.out.println("Connection has been established.\n to jdbc:sqlite:" + filePath.substring(1)); 
            
           
        }// try
        
        catch(SQLException myException)
        {
             System.out.println(myException.getMessage());
        }//
          
    }// connectDatabase
    
    
    
    // gotten from https://www.sqlitetutorial.net/sqlite-java/create-table/
    
    public void createTable(String tableQuery)
    {
        String newTableQuery = tableQuery;
        
        try //(Connection myCon = DriverManager.getConnection(filePath))
        {
            Statement createTable = myCon.createStatement();
            createTable.execute(newTableQuery);
            System.out.println("A Table has been created");
        }//try to creat Table
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }// catch SQLException
    }// createTable
    
    
    
    public void DropTable(String tableQuery)
    {
        String newTableQuery = tableQuery;
        
        try //( myCon )
        {
            Statement createTable = myCon.createStatement();
            createTable.execute(newTableQuery);
            System.out.println("The table has been Dropped");
        }//try to Drop the Table
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }// catch SQLException
    }// DropTable
    
    
    
    public ResultSet TableQuery (String Query) 
    {
        ResultSet rsReturn = null;
        try
        {
            
        Connection cTemp = getMyCon();
        if(cTemp!= null)
        {
            Statement QueryStmt = cTemp.createStatement();
            rsReturn = QueryStmt.executeQuery(Query);
            
        }// if
        
        }// try
        
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
            
        }// catch
        
        return rsReturn;
    }// TableQuery
            
            
    public void inputScores(  String name, String scores)
    {
        String stmt = "INSERT INTO Scores( name, score) VALUES( ?, ?)";
        
        try
        {
             getMyCon().setAutoCommit(true);
            PreparedStatement preStmt = getMyCon().prepareStatement(stmt);
            
          
            preStmt.setString(1,name);
            preStmt.setString(2,scores);
            preStmt.executeUpdate();
            
        }//Try
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }// catch SQLException
        
    }// inputScore Prepared statement    

    
            
    public void disconnect()
    {
        
        
         try {
                if (myCon != null) 
                {
                    myCon.close();// closes the connection
                    System.out.println("Connection has been closed.");
                }// if (myCon != null)
            } // try
            
            catch (SQLException ex) 
            {
                System.out.println(ex.getMessage());
            }//catch
    }//disconnect
    
}// SQLiteDatabase
