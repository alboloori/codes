/*
 * GUIClient.java
 *
 * Created on December 18, 2006, 8:35 AM
 */

package FishCS;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author  Arya
 */
public class GUIClient extends javax.swing.JFrame {
    
    public static final int DEFAULT_PORT=80;
    public static final String DEFAULT_SERVER="localhost";
    public static final String DEFAULT_PATH="C:\\";
    private ArrayList fileList;//ArrayList is serializable
    private Socket clientSocket = null;
    private ObjectInputStream in=null;
    private ObjectOutputStream out=null;
    ArrayList searchResults;
    //private  ClientP2PListener listener;
    P2PHandler handler;
    private String sharedPath="";
    private int conPort=3333;
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= 
    
    /** Creates new form GUIClient */
    public GUIClient() {
        fileList=new ArrayList();
        initComponents();
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        log_ScrollPane = new javax.swing.JScrollPane();
        log_TextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        ip_Label = new javax.swing.JLabel();
        ip_TextField = new javax.swing.JTextField();
        port_label = new javax.swing.JLabel();
        port_TextField = new javax.swing.JTextField();
        connect_Button = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        shareFolderPath_Label = new javax.swing.JLabel();
        shareFolderPath_TextField = new javax.swing.JTextField();
        selectFolder_Button = new javax.swing.JButton();
        sharePortNumber_Label = new javax.swing.JLabel();
        sharePortNumber_TextField = new javax.swing.JTextField();
        register_Button = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        enterKeyword_Label = new javax.swing.JLabel();
        enterKeyword_TextField = new javax.swing.JTextField();
        search_Button = new javax.swing.JButton();
        searchResult_Label = new javax.swing.JLabel();
        destinationFolder_Label = new javax.swing.JLabel();
        destinationFolder_TextField = new javax.swing.JTextField();
        selectDestinationFolder_Button = new javax.swing.JButton();
        fileName_Label = new javax.swing.JLabel();
        fileName_TextField = new javax.swing.JTextField();
        download_Button = new javax.swing.JButton();
        searchResult_ScrollPane1 = new javax.swing.JScrollPane();
        searchResult_List = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        exit_Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Log"));
        log_TextArea.setColumns(20);
        log_TextArea.setRows(5);
        log_ScrollPane.setViewportView(log_TextArea);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(log_ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(log_ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Connect to Server"));
        ip_Label.setText("IP:");

        port_label.setText("Port:");

        connect_Button.setText("Connect");
        connect_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connect_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ip_Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ip_TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(port_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(port_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(connect_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ip_Label)
                    .addComponent(connect_Button)
                    .addComponent(port_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(port_label)
                    .addComponent(ip_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Define Folder to Share"));
        shareFolderPath_Label.setText("Share Folder Path:");

        selectFolder_Button.setText("Select Folder");

        sharePortNumber_Label.setText("Prot Number:");

        register_Button.setText("Register");
        register_Button.setEnabled(false);
        register_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                register_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shareFolderPath_Label)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sharePortNumber_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sharePortNumber_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(shareFolderPath_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(register_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectFolder_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shareFolderPath_Label)
                    .addComponent(shareFolderPath_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectFolder_Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sharePortNumber_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sharePortNumber_Label)
                    .addComponent(register_Button))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));
        enterKeyword_Label.setText("Enter Keyword:");

        search_Button.setText("Search");
        search_Button.setEnabled(false);
        search_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_ButtonActionPerformed(evt);
            }
        });

        searchResult_Label.setText("Search Result:");

        destinationFolder_Label.setText("Destination Folder Path:");

        selectDestinationFolder_Button.setText("Select Folder");

        fileName_Label.setText("File Name:");

        download_Button.setText("Download");
        download_Button.setEnabled(false);
        download_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                download_ButtonActionPerformed(evt);
            }
        });

        searchResult_List.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchResult_ListMouseClicked(evt);
            }
        });

        searchResult_ScrollPane1.setViewportView(searchResult_List);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(download_Button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fileName_Label)
                            .addComponent(destinationFolder_Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(destinationFolder_TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectDestinationFolder_Button))
                            .addComponent(fileName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(enterKeyword_Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addComponent(searchResult_Label))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchResult_ScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(enterKeyword_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(search_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enterKeyword_Label)
                    .addComponent(enterKeyword_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search_Button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchResult_Label)
                    .addComponent(searchResult_ScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationFolder_Label)
                    .addComponent(selectDestinationFolder_Button)
                    .addComponent(destinationFolder_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileName_Label)
                    .addComponent(fileName_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(download_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        exit_Button.setText("Exit");
        exit_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exit_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exit_Button)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exit_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exit_ButtonActionPerformed
        
        exit();
        System.exit(0);
        
    }//GEN-LAST:event_exit_ButtonActionPerformed

    private void searchResult_ListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchResult_ListMouseClicked
        int selected_index = searchResult_List.getSelectedIndex();
            log_TextArea.append("[index: " + (selected_index+1) + " is selected]\n");
    }//GEN-LAST:event_searchResult_ListMouseClicked

    private void download_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_download_ButtonActionPerformed
        //download
        String destination_folder_path = destinationFolder_TextField.getText();
        String file_name = fileName_TextField.getText();
        
        try {
            log_TextArea.append("[downloading...]\n");
            int selected_index = searchResult_List.getSelectedIndex();
            log_TextArea.append("[index: " + (selected_index+1) + " is selected]\n");
            
            downloadFile((SharedFile)searchResults.get(selected_index),file_name,destination_folder_path);
            
            log_TextArea.append("[Downloaded successfully]\n");
            
        }catch(Exception e){
            System.err.println(e);
            log_TextArea.append("[ Error on downloading ]\n");
        }
        
    }//GEN-LAST:event_download_ButtonActionPerformed

    private void search_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_ButtonActionPerformed
        //search
        String the_keyword = enterKeyword_TextField.getText();
        try {
            
            //cleaning search result area
            searchResult_List.setModel(
                    new javax.swing.AbstractListModel() {
                       
                        String[] strings2 = { "" };
                        public int getSize() { return strings2.length; }
                        public Object getElementAt(int j) { return strings2[j]; }
                        }
                    );
            
            searchResults=serachQuery(the_keyword);
            log_TextArea.append("[searching for " + the_keyword + "]\n");
            final String[] searchItems=new String[searchResults.size()];
            Iterator itr=searchResults.iterator();
            int i=0;
            while(itr.hasNext())
            {
                SharedFile curFile=(SharedFile)itr.next();
                searchItems[i++]= curFile.getFilename() + " @  " + curFile.getHostIP() +":"+curFile.getHostPort();
                
            }
            
            searchResult_List.setModel(
                    new javax.swing.AbstractListModel() {
                       
                        String[] strings = searchItems;
                        public int getSize() { return strings.length; }
                        public Object getElementAt(int i) { return strings[i]; }
                        }
                    );
                    
                    
            download_Button.setEnabled(true);
            int selected_index = searchResult_List.getSelectedIndex();
            log_TextArea.append("[index: " + (selected_index+1) + " is selected]\n");
            
            
        }catch(Exception e){
            System.err.println(e);
            log_TextArea.append("[ Error on Searching - File Not Found ]\n");
        }
    }//GEN-LAST:event_search_ButtonActionPerformed

    private void register_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_register_ButtonActionPerformed
        String shareFolderPath = shareFolderPath_TextField.getText();
        String sharePortNumber_String = sharePortNumber_TextField.getText();
        
        try {
            int sharePortNumber;
            try{
                sharePortNumber = Integer.parseInt(sharePortNumber_String);
                conPort = sharePortNumber;
            } catch(Exception e1){
                System.err.println(e1);
                log_TextArea.append("[Unable to Recognize Port Number]\n[Setting Default Value...]\n");
                sharePortNumber = conPort;
            }
            
            startP2PHandler(sharePortNumber);
            log_TextArea.append("[P2P Handler Started]\n");
            log_TextArea.append("[Registered Successfully]\n");
            share(new File(shareFolderPath));
            log_TextArea.append("[Files added successfuly to list]\n");
            
            search_Button.setEnabled(true);
            register_Button.setEnabled(false);
        }
        catch(Exception e){
            System.err.println(e);
            log_TextArea.append("[ Error on Define Folder to Share ]\n");
        }
    }//GEN-LAST:event_register_ButtonActionPerformed

    private void connect_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connect_ButtonActionPerformed
        String connection_ip = ip_TextField.getText();
        String connection_port_string = port_TextField.getText();
        
        try {
            int connection_port = Integer.parseInt(connection_port_string);
            if(connectToServer(connection_ip,connection_port)){
                //connected to the server
                log_TextArea.append("[connected to the server]\n[...ip: " + connection_ip + " port: " + connection_port + "]\n");
                register_Button.setEnabled(true);
                connect_Button.setEnabled(false);
                ip_TextField.setEnabled(false);
                port_TextField.setEnabled(false);
            }
        } catch(Exception e){
            System.err.println(e);
            log_TextArea.append("[ Error on Connect to Server ]\n");
            //e.printStackTrace();
        }
        System.out.println(getClientIP());
        log_TextArea.append("[ " + getClientIP() + " ]\n");
    }//GEN-LAST:event_connect_ButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIClient().setVisible(true);
            }
        });
    }
    
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public boolean connectToServer(String host, int port)
    {
        try {
	    clientSocket = new Socket(host,port);
            System.out.println("Connected to server");
             out=new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("-------------OutputStream Created-------------------------");
            in=new ObjectInputStream(clientSocket.getInputStream());
             System.out.println("-------------InputStream Created-------------------------");
            return true;
	} 
        catch (UnknownHostException e) {
            System.out.println("Unknown host.");
        }
         catch (ConnectException e) {
            System.out.println("Connection refused.");
        }
        catch (IOException e) {
             e.printStackTrace();
             System.out.println("Unable to open IO streams.Can not send or receive messages.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        return false;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void share(File directory)
    {
        CreateSharedFilesList(directory);
        this.sharedPath=directory.getPath();
        if(fileList!=null)
        {
           
           sendData(new String("$Register$"));
           sendData(new FishHost(this.getClientIP(),this.conPort,  fileList));
        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void unshare()
    {
        sendData(new String("$Unregister$"));
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void exit()
    {
        System.out.println("---------------exit--------------");
        try{
        unshare();
        in.close();
        out.close();
        handler.stopRunning();
        clientSocket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public ArrayList serachQuery(String keyword)
    {
        sendData(new String(Protocol.REQ_SEARCH));
        sendData(new String(keyword));
        Object objMsg=receiveData();
        if(objMsg!=null)
            
        if(((String)objMsg).equals(Protocol.RES_FOUND))
        {
            ArrayList fileList=(ArrayList)receiveData();
            return fileList;
        }
        if(((String)objMsg).equals(Protocol.RES_NOTFOUND))
            System.out.println("file not found");
         return null;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public void CreateSharedFilesList (File shared_file_path)
    {
        System.out.println("---------------CreateSharedFilesList--------------");
        try{
          
            File[] files=shared_file_path.listFiles();
            if(files!=null)
            {
               for(int i=0;i<files.length;i++)
               {
                   if(files[i].isDirectory())
                     CreateSharedFilesList(files[i]);
                   else 
                      if(files[i].canRead()&& !files[i].isHidden())
                         fileList.add(new SharedFile(this.getClientIP(),conPort, files[i].getName(),files[i].getPath()));
               }
              
            }
            else System.out.println("The given path is not a valid directory");
        }
        catch(Exception e)
        {
            e.printStackTrace();
             
        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    private void sendData(Object objData)
    {
        System.out.println("---------------sendData--------------");
        try
        {
            out.writeObject(objData);
       
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to send data.");
        }
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    private Object receiveData()
    {
        System.out.println("---------------receiveData--------------");
        try
        {
       
            return in.readObject();
        }
        catch(IOException e)
        {
            System.out.println("IO Exception occured");
             
        }
          catch( Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to read data.");
            
        }
      return null;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public String getClientIP()
    {
        if(this.clientSocket!=null)
        {
            InetAddress addr= clientSocket.getInetAddress();
            return addr.getHostAddress();
            
       }
        return null;
    }
    //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      public int getClientPort()
    {
        if(this.clientSocket!=null)
        {
            return clientSocket.getLocalPort();
            
       }
        return -1;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public String getClientName()
    {
         if(this.clientSocket!=null)
        {
            InetAddress addr= clientSocket.getLocalAddress();
            return addr.getHostName();
            
       }
        return null;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

 public void addSharedFile(SharedFile file)
 {
     System.out.println("---------------addSharedFile--------------");
     this.fileList.add(file);
     sendData(Protocol.REQ_ADDFILE);
     sendData(file);
 }
 //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    public boolean downloadFile(SharedFile file,String destfilename,String destDirectory)
    {
        System.out.println("-------in client download file---------");
            ObjectOutputStream outHost;
	    ObjectInputStream inHost;
	    try
	    {
		Socket hostSocket = new Socket(file.getHostIP(),file.getHostPort()) ;
                outHost  = new ObjectOutputStream(hostSocket.getOutputStream());
                System.out.println("OutputObjectStream created for download");
                inHost  = new ObjectInputStream(hostSocket.getInputStream());
                System.out.println("InputObjectStream created for download");
		
	    }
	    catch (Exception e)
	    {
                e.printStackTrace();
		System.out.println("Could not connect to host  "+ file.getHostIP() + ":" + file.getHostPort() + ".");
		return false;
	    }
            
            try{
                
                outHost.writeObject(new String(Protocol.REQ_DOWNLOAD));
		outHost.writeObject(file);
                System.out.println("request for download sent to: "+ file.getHostIP());
                String response = (String)inHost.readObject();
                if(response.equals(Protocol.RES_DOWNLOAD_FAILED))
                {
                  System.out.println("Download Failed") ; 
                  inHost.close();
                  outHost.close();
                }
                else if(response.equals(Protocol.RES_START_DOWNLOAD))
                {
                 DownloadFile dlfile=(DownloadFile)inHost.readObject();
                 dlfile.saveFile(destDirectory,destfilename);
                 if(destDirectory.equals(sharedPath))
                 {
                     SharedFile newFile=new SharedFile(getClientIP(),conPort,destfilename,destDirectory);
                     addSharedFile(newFile);
                 }
                 return true;
                }
            }
            catch(Exception e)
            {
                
            }
	    try{
                 inHost.close();
                 outHost.close();
            }
                catch(IOException e)
                {
                   
                }
            

        return false;
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= 
  /*  public void startP2PListener(int port)
    {
        listener=new ClientP2PListener(port);
        listener.start();
    }*/
    public void startP2PHandler(int port)
    {
        handler=new P2PHandler(port);
        handler.start();
    }
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connect_Button;
    private javax.swing.JLabel destinationFolder_Label;
    private javax.swing.JTextField destinationFolder_TextField;
    private javax.swing.JButton download_Button;
    private javax.swing.JLabel enterKeyword_Label;
    private javax.swing.JTextField enterKeyword_TextField;
    private javax.swing.JButton exit_Button;
    private javax.swing.JLabel fileName_Label;
    private javax.swing.JTextField fileName_TextField;
    private javax.swing.JLabel ip_Label;
    private javax.swing.JTextField ip_TextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane log_ScrollPane;
    private javax.swing.JTextArea log_TextArea;
    private javax.swing.JTextField port_TextField;
    private javax.swing.JLabel port_label;
    private javax.swing.JButton register_Button;
    private javax.swing.JLabel searchResult_Label;
    private javax.swing.JList searchResult_List;
    private javax.swing.JScrollPane searchResult_ScrollPane1;
    private javax.swing.JButton search_Button;
    private javax.swing.JButton selectDestinationFolder_Button;
    private javax.swing.JButton selectFolder_Button;
    private javax.swing.JLabel shareFolderPath_Label;
    private javax.swing.JTextField shareFolderPath_TextField;
    private javax.swing.JLabel sharePortNumber_Label;
    private javax.swing.JTextField sharePortNumber_TextField;
    // End of variables declaration//GEN-END:variables
    
}