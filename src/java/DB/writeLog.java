/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;

import OLM.CleanUp;
import OLM.ObjectLifetimeManager;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kezhang
 */
public class writeLog extends CleanUp{
    
    
    
    private writeLog(){}
    /**
     * Performed by Singleton.
     */
    private static writeLog instance;

    public static writeLog getInstance() { 
        if (instance==null){
            instance = new writeLog();
        }
        ObjectLifetimeManager.at_exit(instance);//register instance to ObjectLifetimeManager.
        return instance; 
    }
    
   //http://blog.sina.com.cn/s/blog_61889f5c010105tr.html
    
    
    private static File file = new File("log.txt");
    private static String iopath = file.getAbsolutePath();
    private FileOutputStream write;
    private BufferedOutputStream out;
    
    public boolean writeDown(String info) throws FileNotFoundException{
        try {    
            if(file.exists()){
               file.delete();
               file.createNewFile();
            }else{
               file.createNewFile();
            }
            write = new FileOutputStream(iopath);          
            out = new BufferedOutputStream(write);          
            out.write(info.getBytes());           
            return true;        
        } catch (IOException ex) {
            Logger.getLogger(writeLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Destructor that deletes 
     * the singleton, avoid singleton
     * leak.
     */

    @Override
    public void cleanup() {
       try {
            //do things here!
            out.close();
            write.close();
        } catch (IOException ex) {
            Logger.getLogger(writeLog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
