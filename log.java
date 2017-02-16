package javaapplication2;
import java.io.*;
/*import java.sql.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.lang.Character.*;
import java.util.concurrent.TimeUnit;
*/

public class log {
    DatabaseHandler db=  new DatabaseHandler();
    FileHandler fileh=new FileHandler();
    Config conf=new Config();
    TimerCollect tc=new TimerCollect(2);
   int version;
    public void menu()
          
    {
        String type;
     // version=conf.checkVersion();
      //System.out.println(version);
      if(conf.checkVersion())
         tc.Tasker();
      else
          tc.taskerFile();
        try
        {
         
            conf.configTable("general_log");
            conf.configTable("slow_log");
         
        int choice=3,gchoice=0;
        String status=new String("ON");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    
        while(choice<5)
        {
        System.out.println("1.General Log 2.Slow Log 3.Error Log 4.Report 5.Exit");
        choice=Integer.parseInt(br.readLine());
        switch(choice)
        {
            case 1:
                gchoice=7;
                while(gchoice!=6)
                {

                    //while(status.equals("ON"))
                    status=db.show("general_log");
                      System.out.println("GENERAL QUERY LOGGING IS:"+status);
             if(status.equals("OFF"))
                {
                System.out.print("1.Turn ON 2.SELECT Queries 3.INSERT Queries 4.UPDATE queries 5.DELETE Queries 6.Exit");
                 gchoice=Integer.parseInt(br.readLine());
               if(gchoice==1)
                db.set("general_log","ON");
                }
               else
                {
                System.out.println("1.Turn OFF 2.SELECT Queries 3.INSERT Queries 4.UPDATE queries 5.DELETE Queries 6.Exit");
                 gchoice=Integer.parseInt(br.readLine());
                       if(gchoice==1)
                       db.set("general_log","OFF");
               }
                switch(gchoice)
                {
                    case 2:
                        db.queryLog("select",1);
                        break;
                    case 3:
                        db.queryLog("insert",1);
                        break;  
                    case 4:
                        db.queryLog("update", 1);
                        break;
                    case 5:
                        db.queryLog("delete", 1);
                        break;
                }
                }
                break;
            case 2:
                gchoice=6;
                  while(gchoice!=5)
                {
                    status=db.show("slow_query_log");
                System.out.println("SLOW QUERY LOGGING IS:"+status);
                 System.out.println("The query time to log to slow query log is:"+db.getSlowQueryTime());
                if(status.equals("OFF"))
                {
                   
                System.out.print("1.Turn ON 2.SELECT Queries 3.INSERT Queries 4.slow query time 5.Exit");
                 gchoice=Integer.parseInt(br.readLine());
                    if(gchoice==1)
                    db.set("slow_query_log","ON");
                }else
                {
                    System.out.println("1.Turn OFF 2.SELECT Queries 3.INSERT Queries 4.slow query time 5.Exit");
                     gchoice=Integer.parseInt(br.readLine());
                       if(gchoice==1)
                       db.set("slow_query_log","OFF");
                }
                switch(gchoice)
                {
                    case 2:
                        db.querySlowLog("select",1);
                        break;
                    case 3:
                        db.querySlowLog("insert",1);
                        break;
                    case 4:
                        conf.configSlowQueryTime();
                        break;
                }
                }
                break;
            case 3:
                 gchoice=5;
                 
                  while(gchoice!=4)
                {
                    System.out.print("1.View ERROR 2.View WARNINGS 3.View NOTES 4.Exit\n");
                 gchoice=Integer.parseInt(br.readLine());
                String  gqueryfile=new String();
                gqueryfile=db.show("log_error");
               gqueryfile=gqueryfile.substring(2);
                System.out.println("error file"+ gqueryfile);
                 
                String link=new String("C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Data");
                switch(gchoice)
                {case 1:
                    //fileh.readGeneralLog();
              fileh.readFile(link,gqueryfile,"[ERROR]");
                break;
                case 2:
                  fileh.readFile(link,gqueryfile,"[Warning]");
                  break;
                case 3:
                    fileh.readFile(link,gqueryfile,"[Note]");
                    break;
                case 4:
                    //link=link+"\\"+gqueryfile;
                    //File file=new File(link);
                    }
                }
                break;
            case 4:
                db.loggedin();
                db.selection();
                break;
            case 5:
                db.close();
                tc.stopTask();
                break;
        }
        // System.out.println("1.General Log 2.Slow Log 3.Error Log 4.Exit");
        //choice=Integer.parseInt(br.readLine());
        }
        }
        catch(Exception e){
        }
    }
    
    
    
  public static void main(String [] args)
        {
     log dbo=new log();
    dbo.menu(); 
        }
}
