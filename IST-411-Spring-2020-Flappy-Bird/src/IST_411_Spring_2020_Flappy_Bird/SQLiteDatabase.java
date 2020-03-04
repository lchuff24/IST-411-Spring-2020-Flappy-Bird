/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IST_411_Spring_2020_Flappy_Bird;

import java.sql.*;

/**
 *
 * @author Jacob Mullan
 */


public class SQLiteDatabase
{
    
    private String filePath = "";

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }
    
    public void createDatabase()
    {
        
        
        try(Connection myCon = DriverManager.getConnection(filePath))
        {
            
            if (myCon != null) 
            {
                DatabaseMetaData meta = myCon.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A database has been created in " + filePath);
                
            }// Creates database if connection is not Null
        }
        
        catch(SQLException e)
        {
             System.out.println(e.getMessage());
        }//catches SQLException
        
        
    }// createDatabase
    
    
    public void connectDatabase( )
    {
     //All code followed  from  https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
    
       Connection  myCon = null;
        try
        {
            
            myCon = DriverManager.getConnection(filePath);// connects to the database using the filepath
          
            System.out.println("Connection has been established.");
        }
        
        catch(SQLException e)
        {
             System.out.println(e.getMessage());
        }
        
        try {
                if (myCon != null) 
                {
                    myCon.close();// closes the connection
                    System.out.println("Connection has been closed.");
                }// if (myCon != null)
            } // try
            
        catch (SQLException e) 
            {
                System.out.println(e.getMessage());
            }    
        
    
    }// connectDatabase
    
    // gotten from https://www.sqlitetutorial.net/sqlite-java/create-table/
    
    public void createTable(String tableSQL)
    {
        String newTableSQL = tableSQL;
        
        try(Connection myCon = DriverManager.getConnection(filePath))
        {
            Statement createTable = myCon.createStatement();
            createTable.execute(newTableSQL);
            System.out.println("A Table has been created");
        }//try to creat Table
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }// catch SQLException
    }// createTable
    
    public void DropTable(String tableSQL)
    {
        String newTableSQL = tableSQL;
        
        try(Connection myCon = DriverManager.getConnection(filePath))
        {
            Statement createTable = myCon.createStatement();
            createTable.execute(newTableSQL);
            System.out.println("The table has been Dropped");
        }//try to Drop the Table
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }// catch SQLException
    }// DropTable
    
    public void disconnect()
    {
        Connection myCon = null;
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
            }
    }
}
