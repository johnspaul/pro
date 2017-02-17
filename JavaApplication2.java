
//package javaapplication2;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class JavaApplication2 {
    
    public void readFile(String link)
    {
     
    File file = new File(link);
    String str;String[] tokens;
		try (FileInputStream fis = new FileInputStream(file)) {
                
                    BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			//while ((content = fis.read()) != -1) {
				// convert to char and display it
                          while ((str = br.readLine()) !=null) {  
                          if(str.equals("Default options are read from the following files in the given order:"))
                          {
                              str=br.readLine();
                                tokens = str.split("[ C]");
                             System.out.println(tokens[0]+"\n"+tokens[1]+"\n"+tokens[2]+"\n"+tokens[3]+"\n"+tokens[4]+"\n"+tokens[5]+"\n");
                          System.out.println(str);
                          }
                          }
                //fis.close();
               // FileOutputStream out = new FileOutputStream(link);
                //props.setProperty("log-bin", "poda");
                //props.store(out, null);
                //out.close();
               
                
                /*
			System.out.println("Total file size to read (in bytes) : "+ fis.available());

			int content;
			while ((content = fis.read()) != -1) {
				// convert to char and display it
				System.out.print((char) content);
			}
*/
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public static void main(String[] args) throws IOException {
                 JavaApplication2 j=new JavaApplication2();
                 
                 ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "mysqld --help --verbose -u root -proot > C:\\mysqld_options.txt");
               
                 builder.redirectErrorStream(true);
                 Process p = builder.start();
               String link=new String("C:\\mysqld_options.txt");
		 j.readFile(link);
       // OutputStream shellOut = p.getOutputStream();
       //shellOut.write("SET GLOBAL general_log = 'ON';".getBytes());
       // shellOut.close();
       //  BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
      /*  while (true) {
            line = br.readLine();
            if (line == null) { break; }
            System.out.println("hi"+line);
			}
*/
        }
}