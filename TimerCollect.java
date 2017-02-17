/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.util.Timer;
import java.util.TimerTask;

public class TimerCollect extends TimerTask {

    DatabaseHandler db = new DatabaseHandler();
    FileHandler fh=new FileHandler();

    int count_of_select, count_of_insert, count_of_update, count_of_slow_select, count_of_slow_update, count_of_slow_insert;
    //public static void main(String[] args)
    TimerTask tasknew,taskfile;
    Timer timer;
    int tasktype=0;
    int port;
public TimerCollect(int type,int p)
{
    tasktype=type;
   port=p;
}
    @Override
    public void run() {
        //System.out.println("Timer task started at:");
     /*   if(tasktype==1)
        {
            //System.out.println(tasktype);
        count_of_select = db.queryLogCount("select",port);
        count_of_insert = db.queryLogCount("insert",port);
        count_of_update = db.queryLogCount("update",port);
        count_of_slow_select = db.querySlowLogCount("select");
        count_of_slow_insert = db.querySlowLogCount("insert");
        count_of_slow_update = db.querySlowLogCount("update");
         //System.out.println("count of select queries: "+count_of_select+"\ncount of insert queries:  "+count_of_insert+"\ncount of update queries: "+count_of_update+"\ncount of s"
           //     + "low select queries"+count_of_slow_select+"\ncount of slow insert queries"+count_of_slow_insert+"\ncount of slow update queries"+count_of_slow_update);
         
        db.insertion(count_of_select, count_of_insert, count_of_update, count_of_slow_select, count_of_slow_insert);
        }
        else*/
        {
            //System.out.println("hi");
            //System.out.println("timer:"+port);
            try{
            count_of_select=fh.readGeneralLog("select",port);
            count_of_insert=fh.readGeneralLog("insert",port);
            count_of_update =fh.readGeneralLog("update",port);
            count_of_slow_select = fh.readSlowLog("select",port);
            count_of_slow_insert = fh.readSlowLog("insert",port);
            //System.out.println("count of select queries: "+count_of_select+"\ncount of insert queries:  "+count_of_insert+"\ncount of update queries: "+count_of_update+"\ncount of s"
              // + "low select queries"+count_of_slow_select+"\ncount of slow insert queries"+count_of_slow_insert);
            db.insertion(count_of_select, count_of_insert, count_of_update, count_of_slow_select, count_of_slow_insert);
        }catch(Exception e)
        {
            
        }
        }
    }

    public void Tasker(int p) {
        try {
            //System.out.println("tasker:"+p);
            // creating timer task, timer
           
            //TimerTask tasknew = new TimerTask();
            tasknew = new TimerCollect(1,p);
            timer = new Timer();
            timer.scheduleAtFixedRate(tasknew, new java.util.Date(), 60000);

            // Thread.sleep(6000);
        } catch (Exception e) {
            System.out.println(e);
        }
        // scheduling the task at fixed rate
        //timer.scheduleAtFixedRate(tasknew,new java.util.Date(),1000);      
    }
    public void taskerFile(int p)
    {
        port=p;
        taskfile=new TimerCollect(0,p);
        timer=new Timer();
        timer.scheduleAtFixedRate(taskfile, new java.util.Date(), 60000);
    }

    public void stopTask() {
        timer.cancel();
        timer.purge();
        tasknew.cancel();
        tasknew = null;

    }

}
