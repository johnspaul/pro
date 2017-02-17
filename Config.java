package javaapplication2;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;

public class Config {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   // String version;
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
    public void processList()
    {
        int count = 0;
        FileWriter fw;
        BufferedWriter bw;
        BufferedReader input;
        File file;
       try {
            file=new File("netstat.txt");
            file.createNewFile();
             fw = new FileWriter(file);
              
             bw = new BufferedWriter(fw);
            //PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            String line;
            //Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            // Process p = Runtime.getRuntime().exec("runas /savecred /user:JOHNS-pt1490\administrator cmd");
            //Process p =Runtime.getRuntime().exec("cmd /c \"runas/profile /savecred /johns-pt1490:ZOHOCORP\\administrator netstat -a -b\"");
            //Process p=Runtime.getRuntime().exec("runas /noprofile /user:johns-pt1490\\administrator \"netstat -a -b\"");// > C:\\file.txt");
            ProcessBuilder builder = new ProcessBuilder("netstat","-a","-b");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line); //<-- Parse data here.
                bw.write(line);
                bw.write("\n");
                // System.setOut(out);
                count++;
            }
            System.out.println("count:" + count);
            input.close();
           // fw.close();
            bw.close();
        } catch (Exception err) {
            System.out.println(err);
            err.printStackTrace();
        }
        readFile();
    }
    public void setGeneralLog(int port)throws Exception
    {
         String basedir=new String(db.show("basedir", port));
    }
    
     public void readFile()
    {
        String sql=new String();
     int count=0,port_values;
    File file = new File("netstat.txt");
    String strback=new String();
    String str;String[] tokens;
		try (FileInputStream fis = new FileInputStream(file)) {
                      
                    BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			//while ((content = fis.read()) != -1) {
				// convert to char and display it
                                stmt=db.open(3306);
                          while ((str = br.readLine()) !=null) {  
                          if(str.contains("mysqld.exe")||str.contains("mysqld-nt.exe"))
                          {
                              //str=br.readLine();
                             // str=br.readLine();
                              if(strback.contains("."))
                                  //System.out.println(str);
                              { strback=strback.substring(strback.indexOf(":")+1, strback.indexOf(":")+5);
                               //tokens = str.split("\\s+");
                             //System.out.println(tokens[0]+"\n"+tokens[1]+"\n"+tokens[2]+"\n"+tokens[3]+"\n"+tokens[4]+"\n"+tokens[5]+"\n");
                             //str=
                          System.out.println(strback+"tok");
                         sql="insert ignore into mysql_versions(port_no) values("+strback+")";
                       //  System.out.println(sql); 
                         stmt.executeUpdate(sql);
                              }
                          }
                          strback=str;
                          count++;
                          }
System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public void setSlowQueryTime(int time,int port) throws Exception {

        stmt = db.open(port);

        stmt.executeQuery("SET GLOBAL long_query_time=" + time);
        db.close();
    }

    Config() {
        try {

           // version = db.show("version",3306);
            //System.out.println("MySQL version:" + version);

        } catch (Exception e) {

        }
    }

    public void configSlowQueryTime(int port) {
        int choice;
        try {
            
            {
                System.out.println("enter the query time");
                choice = Integer.parseInt(br.readLine());
                setSlowQueryTime(choice,port);
            }
        } catch (Exception e) {

        }
    }

    public boolean checkVersion(int check[],int port)throws Exception {
        String str;
        String version=db.show("version", port);
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
       if(a>check[0])
        return true;
        else if(a<check[0])
         return false;
        else 
       {          
           if(b>check[1])
          return true;
          else if(b<check[1])
               return false;
           else
           {
           if(c>=check[2])
             return true;
           else if(c<check[2])   
           return false;
           }
        }
          
            
        //var=Integer.parseInt(str);
        //return var;
        return true;
    }

    public void configTable(String logType,int port) {
        String sql;
        int choice;
        try {
            db.set("general_log", "OFF",port);
            stmt = db.open(port);
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
            db.set("log_output", "TABLE,FILE",port);
            db.set("general_log", "ON",port);
            db.close();
            stmt=db.open(3306);
            sql = "SHOW TABLES LIKE \"mysql_versions\"";
            rs = stmt.executeQuery(sql);
            if (!rs.first()) {
               
                sql = "CREATE TABLE mysql_versions (port_no int(30))";
                stmt.executeUpdate(sql);
                
                 }
          
        } catch (Exception e) {
            System.out.println("exception" + e);
        }
    }
}
