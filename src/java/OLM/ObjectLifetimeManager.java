/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package OLM;

import java.util.Stack;

/**
 *
 * @author kezhang
 */
public class ObjectLifetimeManager {
    
    private ObjectLifetimeManager(){}
    
    private static ObjectLifetimeManager olminstance = new ObjectLifetimeManager();

    public static ObjectLifetimeManager getOlm() {
        return olminstance;
    }
    
   //Use stack structure to determine the sequence to destroy object.
    private static Stack objectlist = new Stack(); 
    
    /**
     * Used to register objects derived from the Cleanup base class.
     */
    public static int at_exit (CleanUp object){
        objectlist.push(object);
        return 0;
    }
      
    /**
     * Destroy one objects, and pop it out from objectlist.
     */
    public static void fini (){
       CleanUp cu = (CleanUp)objectlist.pop();
       cu.cleanup();
    }
    
}
