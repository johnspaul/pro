
package javaapplication2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseHandler {
      Connection con;
       Statement stmt;
       ResultSet rs;
       Config cf;
   
 public int getSlowQueryTime()throws Exception
      {
        int time = 0;
        stmt = open();
        rs = stmt.executeQuery("show global variables like \"long_query_time\"");
        if (rs.next()) {
            time = rs.getInt(2);
        }
        close();
        return time;
    }

 public void selection() {
        try {
            int time = 0;
            stmt = open();
            rs = stmt.executeQuery(" select * from log_report where id=(select max(id) from log_report)");
            if (rs.next()) {
                
                System.out.println("count of select queries: " + rs.getInt(2) + "\ncount of insert queries:  " + rs.getInt(3) + "\ncount of update queries: " + rs.getInt(4) + "\ncount of s"
                        + "low select queries" + rs.getInt(5) + "\ncount of slow insert queries" + rs.getInt(6));
                /*"\ncount of slow insert queries"+count_of_slow_insert+"\ncount of slow update queries"+count_of_slow_update);*/
                //db.insertion(count_of_select,count_of_insert,count_of_update,count_of_slow_select);
            }
            close();
        } catch (Exception e) {

        }

    }

 public void insertion(int sel, int ins, int updt, int slow, int supdt) {
        try {
            String sql;
            stmt = open();
            sql = "insert into log_report(select_queries,insert_queries,update_queries,slow_queries,slow_update) values(" + sel + "," + ins + "," + updt + "," + slow + "," + supdt + ")";
           
            stmt.executeUpdate(sql);
            close();
        } catch (Exception e) {
            System.out.println(e);
        }
        }
        
 public Statement open() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?useSSL=false", "root", "root");
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("not connecting");
        }
        return stmt;
    }

     
 public void close()throws Exception
     {
         if (con != null) {
             con.close();
             con = null;
         }
     }
 
     
 public void set(String var,String stat) throws Exception
   {   
       stmt = open();
       stmt.executeQuery("SET GLOBAL " + var + "='" + stat + "'");
       close();
       
   }
   
 public String show(String var) throws Exception
      {
          String status = new String();
          stmt = open();
          rs = stmt.executeQuery("show variables like \"" + var + "\"");

          while (rs.next()) {
              status = rs.getString("Value");
          }
         
          close();
          return status;

   }
   
 public int queryLogCount(String type)
    {
        int count = 0;
        Statement stmt, stmt3;
        ResultSet rssel;

        String sql = new String();
        try {
            stmt3 = open();
            if (type.equals("select")) {
                sql = "select count(*) from mysql.general_log where argument like \"select%from%\"";
            } else if (type.equals("insert")) {
                sql = "select count(*) from mysql.general_log where argument like \"insert%into%\"";
            } else if (type.equals("update")) {
                sql = "select count(*) from mysql.general_log where argument like \"update%set%\"";
            } else if (type.equals("delete")) {
                sql = "select count(*) from mysql.general_log where argument like \"delete%\"";
            }
            rssel = stmt3.executeQuery(sql);

            while (rssel.next()) {
                count = rssel.getInt(1);
                            }
            close();
        } catch (Exception e) {

            System.err.println("Exception: " + e.getMessage());
        }
        return count;

    }
   
 public void queryLog(String type,int log)
    {
       
        Statement stmt, stmt3;
        ResultSet rssel;

        String sql = new String();
        try {
            stmt3 = open();
            
            if (type.equals("select")) {
                sql = "select * from mysql.general_log where argument like \"select%from%\"";
            } else if (type.equals("insert")) {
                sql = "select * from mysql.general_log where argument like \"insert%into%\"";
            } else if (type.equals("update")) {
                sql = "select * from mysql.general_log where argument like \"update%set%\"";
            } else if (type.equals("delete")) {
                sql = "select * from mysql.general_log where argument like \"delete%\"";
            }
            rssel = stmt3.executeQuery(sql);

            String arg, time, cmd;
            String[] tokens = null, qtoken;
            while (rssel.next()) {

                time = rssel.getString("event_time");
                cmd = rssel.getString("command_type");
                arg = rssel.getString("argument");
                qtoken = arg.split("\\s+", 2);
                qtoken[0] = qtoken[0].toLowerCase();

                if (qtoken[0].equals(type)) {
                    System.out.println(time + "\t" + qtoken[0] + "\t" + cmd + "\t" + arg);
                }

            }
            close();
        } catch (Exception e) {

            System.err.println("Exception: " + e.getMessage());
        }

        
        }
     
     
 public int querySlowLogCount(String type)
    {
        Statement stmt3;
        ResultSet rssel;
        String sql;
        int count = 0;
        int log = 0;
        try {
            stmt = open();
            rs = stmt.executeQuery("show global variables like \"long_query_time\"");
            if (rs.next()) {
                log = rs.getInt(2);
            }
            close();
            stmt3 = open();
            sql = "select count(*) from mysql.slow_log where query_time >=" + log + " and  sql_text like \"" + type + "%" + "\"";
            rssel = stmt3.executeQuery(sql);
            String arg, cmd, qtime;
            String[] tokens = null, qtoken;
            while (rssel.next()) {
                count = rssel.getInt(1);

            }
            close();

        } catch (Exception e) {

            System.err.println("Exception: " + e.getMessage());
        }
        return count;
    }
     public void loggedin()
     {
         try{
         String sql,user;
         stmt=open();
         sql="select * from general_log where event_time =(select MAX(event_time) from general_log where command_type like \"connect\") and command_type like \"connect\"";
         rs=stmt.executeQuery(sql);
         while(rs.next())
         {
             user=rs.getString(6);
              user = user.substring(0, user.indexOf("@"));
              System.out.println("the last logged in user: "+user);
             
         }
         }
         catch(Exception e)
         {
             
         }
     }
 public void querySlowLog(String type,int log)
    {
       
        Statement stmt3;
        ResultSet rssel;
        String sql;
        try {
 stmt = open();
            rs = stmt.executeQuery("show global variables like \"long_query_time\"");
            if (rs.next()) {
                log = rs.getInt(2);
            }
            close();
            stmt3 = open();
            sql = "select * from mysql.slow_log where query_time >=" + log + " and  sql_text like \"" + type + "%" + "\"";
            rssel = stmt3.executeQuery(sql);

            String arg, time, cmd, qtime;
            String[] tokens = null, qtoken;
            while (rssel.next()) {

                time = rssel.getString("start_time");
                cmd = rssel.getString("db");
                arg = rssel.getString("sql_text");
                qtime = rssel.getString("query_time");

                qtoken = arg.split("\\s+", 2);
                qtoken[0] = qtoken[0].toLowerCase();

                if (qtoken[0].equals(type)) {
                    System.out.println(time + "\t" + qtoken[0] + "\t" + cmd + "\t" + qtime + "\t" + arg);

                }

            }
            close();
        } catch (Exception e) {

            System.err.println("Exception: " + e.getMessage());
        }

    }
 }
