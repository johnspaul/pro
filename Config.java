package javaapplication2;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;


public class Config {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String version;
    Statement stmt;
    DatabaseHandler db = new DatabaseHandler();
    ResultSet rs;

    /*   public int getSlowQueryTime()throws Exception
      {
          int time=0;
          stmt=db.open();
          rs=stmt.executeQuery("show global variables like \"long_query_time\"");
          if(rs.next())
          {
              time=rs.getInt(2);
          }
          db.close();
          return time;
      }*/
    public void setSlowQueryTime(int time) throws Exception {

        stmt = db.open();

        stmt.executeQuery("SET GLOBAL long_query_time=" + time);
        db.close();
    }

    Config() {
        try {

            version = db.show("version");
            System.out.println("MySQL version:" + version);

        } catch (Exception e) {

        }
    }

    public void configSlowQueryTime() {
        int choice;
        try {
            
            {
                System.out.println("enter the query time");
                choice = Integer.parseInt(br.readLine());
                setSlowQueryTime(choice);
            }
        } catch (Exception e) {

        }
    }

    public boolean checkVersion() {
        String str;
        String[] val=new String[3];
        int var,a,b,c;
       // str = version.split("");
       str = version.substring(version.indexOf(":") + 1, version.indexOf("-"));
        val = str.split("\\.");
       System.out.println(str+val[0]+val[1]+":"+val[2]);
        a=Integer.parseInt(val[0]);
        b=Integer.parseInt(val[1]);
        c=Integer.parseInt(val[2]);
       // a=6;
        //b=6;
        //c=6;
       if(a>5)
        return true;
        else if(a<5)
         return false;
        else 
       {          
           if(b>6)
          return true;
          else if(b<6)
               return false;
           else
           {
           if(c>=1)
             return true;
           else if(c<1)   
           return false;
           }
        }
          
            
        //var=Integer.parseInt(str);
        //return var;
        return true;
    }

    public void configTable(String logType) {
        String sql;
        int choice;
        try {
            db.set("general_log", "OFF");
            stmt = db.open();
            sql = "use mysql";
            stmt.executeQuery(sql);
            sql = "SHOW TABLES LIKE \"log_report\"";
            rs = stmt.executeQuery(sql);
            if (!rs.first()) {
               
                sql = "alter table " + logType + " engine=MyISAM";
                stmt.executeUpdate(sql);
                sql = "CREATE TABLE log_report (id bigint(20) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,select_queries int(30) DEFAULT NULL,insert_queries int(30) DEFAULT NULL, update_queries int(30) DEFAULT NULL,slow_queries int(30) DEFAULT NULL,slow_update int(30) DEFAULT NULL,entry_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6))";
                stmt.executeUpdate(sql);
                // sql="alter table "+logType+" add seq bigint(20) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY";
                //stmt.executeUpdate(sql);
            }
            db.set("log_output", "TABLE,FILE");
            db.set("general_log", "ON");
            db.close();
          
        } catch (Exception e) {
            System.out.println("exception" + e);
        }
    }
}
