/*
 * SharedFile.java
 *
 * Created on December 12, 2006, 1:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;

import java.io.Serializable;

/**
 *
 * @author Ali Javadzadeh
 */
public class SharedFile implements Serializable {
    
    /** Creates a new instance of SharedFile */
    private String hostIP;
    private String path;
    //private String hostname;
    private String filename;
    private int hostPort;
    public SharedFile(String hIP,int port, String fname,String fpath) {
   this.hostIP=hIP;
   this.hostPort=port;
   // this.hostname=hName;
    this.path=fpath;
    this.filename=fname;
    }
   //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public String getPath()
    {
        return path;
    }
  public String getHostIP()
    {
        return hostIP;
    }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public String getFilename()
    {
        return filename;
    }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public String toString()
    {
        return filename +":"+":"+hostIP+":"+String.valueOf(hostPort)+":"+path;
    }
  //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public int getHostPort()
    {
        return this.hostPort;
    }
}
