/*
 * DownloadFile.java
 *
 * Created on December 17, 2006, 9:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package FishCS;
import java.io.*;
import java.util.*;
import java.net.*;
/**
 *
 * @author Ali Javadzadeh
 */
public class DownloadFile implements Serializable{
    
    /** Creates a new instance of DownloadFile */
    private String filePath;
    private String fileName;
    private byte[]fileContent;
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public DownloadFile(String filename,String fpath)
    {
        this.filePath=fpath;
        this.fileName=filename;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= 
 public byte[] getFileContent()
 {
     return this.fileContent;
 }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=      
    public boolean readFile()
    { 
       try
        {
            File file=new File(filePath);
            if(file.exists())
            {
                
              FileInputStream inFile=new FileInputStream(filePath);
              fileContent=new byte[(int)file.length()];
              inFile.read(fileContent,0,(int)file.length());
              inFile.close();
              return true;
            }
          else System.out.println("File doesn't exist.");
         }
          catch(IOException e)
         {
            System.out.println("IO Exception.Can not read the file.");
         }
       return false;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
    public void saveFile(String destpath,String filename)
    {
        if(fileContent.length>0)
         try
         {
            FileOutputStream out=new FileOutputStream(new File(destpath+'\\'+filename));
            out.write(this.getFileContent());
            out.close();
           
         }
        
        catch(Exception e)
        {
            System.out.println("Error in saving file");
        }
    }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=  
  
}
