/*
 * Protocol.java
 *
 * Created on December 18, 2006, 1:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;

/**
 *
 * @author Ali Javadzadeh
 */
  public  class Protocol {
    
    /** Creates a new instance of Protocol */
    public final static String REQ_SEARCH="$Search$";
    public final static String RES_FOUND="$Found$";
    public final static String RES_NOTFOUND="$Not Found$";
    public final static String REQ_DOWNLOAD="$DownloadFile$"; 
    public final static String RES_DOWNLOAD_FAILED="$DownloadFailed$"; 
    public final static String RES_START_DOWNLOAD="$StartDownload$";
    public final static String REQ_REGISTER="$Register$"; 
    public final static String REQ_UNREGISTER="$Unregister$";
    public final static String REQ_PING="$Ping$";
    public final static String RES_PONG="$Pong$";
    public final static String REQ_ADDFILE="$AddFile$";
    public Protocol() {
    }
    
}
