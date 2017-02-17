package javaapplication2;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class FileHandler {

    DatabaseHandler db = new DatabaseHandler();

    public void readFile(String link, String fname, String type) {
        link = link + "\\" + fname;
        File file = new File(link);

        try (FileInputStream fis = new FileInputStream(file)) {

            System.out.println("Total file size to read (in bytes) : " + fis.available());
            String str;
            //String[] tokens,qtoken;
            // char ch;
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while ((str = br.readLine()) != null) {

                //tokens = str.split("\\s+",4); 
                if (str.contains(type)) {
                    System.out.println(str + "\n");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int readGeneralLog(String type,int port) throws Exception {
       // System.out.println("general log file:"+port);
        String[] tokens, qtoken;
        tokens = new String[10];
        int count = 0;
        String basedir;
        String fileName = db.show("general_log_file",port);
        String link = new String(db.show("datadir", port)+ fileName);
       
        File file = new File(link);
        String str = new String();
        //try (FileInputStream fis = new FileInputStream(file))

        try (FileReader fis = new FileReader(file)) {

            BufferedReader br = new BufferedReader(fis);
            //BufferedReader br = new BufferedReader(new InputStreamReader(fis));
           
            while ((str = br.readLine()) != null) {
                //if(!str.equals("general_log"))
                //System.out.println(str);
                tokens = str.split("\\s+", 4);
                if (str.contains(type)) {
                    qtoken = tokens[3].split("\\s+", 2);
                    if (qtoken[0].equals(type)) {
                        
                        count++;
                        //System.out.println(str + tokens[3]);
                    }
                }
            }
           
           // System.out.println(count);
            //qtoken=tokens[3].split("\\s+",1);
            /* {
                tokens[3]="a";
                tokens = str.split("\\s+",4); 
                if(!str.equals("general_log"))
                { qtoken=tokens[3].split("\\s+",2);
                //if (str.contains(type)) 
                if(qtoken[0].equalsIgnoreCase("select")){
                   // System.out.println(str + "\n");
                    System.out.println(tokens[0]+"  "+tokens[1]+" "+tokens[2]+" "+qtoken[0]+" "+str);
                }
            }}*/
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("exception:" + e + str);
        }
    return count;
    }

     public int readSlowLog(String type,int port) throws Exception {
        
        String[] tokens, qtoken;
        tokens = new String[10];
        int count = 0;
        String fileName = db.show("slow_query_log_file",port);
        String link = new String("C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Data\\" + fileName);
        File file = new File(link);
        String str = new String();
        //try (FileInputStream fis = new FileInputStream(file))

        try (FileReader fis = new FileReader(file)) {

            BufferedReader br = new BufferedReader(fis);
            while ((str = br.readLine()) != null) {
               
                //tokens = str.split(" ", 3);
                //System.out.println(str);
                if (str.contains(type)) {
                    qtoken = str.split("\\s+", 2);
                    if (qtoken[0].equals(type)) {
                        count++;
                        //System.out.println(str + tokens[3]);
                    }
                }
            }
           
      
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("exception:" + e + str);
        }
    return count;
    }
     
   /*  public void findFile(String name,File file)
    {
        File[] list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
                System.out.println(fil.getParentFile());
            }
        }
    }
    public static void main(String[] args) 
    {
        FindFile ff = new FindFile();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the file to be searched.. " );
        String name = scan.next();
        System.out.println("Enter the directory where to search ");
        String directory = scan.next();
        ff.findFile(name,new File(directory));
    }
    
    */
}
