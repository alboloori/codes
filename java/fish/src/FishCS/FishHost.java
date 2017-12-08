/*
 * FishHost.java
 *
 * Created on December 16, 2006, 5:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;

import java.io.Serializable;
import java.util.*;
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
/**
 *
 * @author Ali Javadzadeh
 */
public class FishHost implements Serializable{
    int hostPort;
    String hostIP;
  ArrayList filesList;
    /**
     * Creates a new instance of FishHost
     */
    public FishHost(String hIP,int hPort,ArrayList files) {
        
       this.hostIP=hIP;
       this.filesList=files;
       this.hostPort=hPort;
      //  String hostPath;
    }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public String getHostIP()
    {
        return hostIP;
    }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public int getHostPort()
    {
        return hostPort;
    }
     //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public ArrayList getFileList()
    {
        return filesList;
    }
}
