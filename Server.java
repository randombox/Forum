import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
  
  private JTextField userText;
  private JTextArea chatWindow;
  private ObjectOutputStream output;
  private ObjectInputStream input;
  private ServerSocket server;
  private Socket connection;
  private Forum forum;
  
  public Server(
  )
  {
  super("Rafehs Instant Messanger");

  userText = new JTextField();
  userText.setEditable(false);
  userText.addActionListener(
                             new ActionListener(){
    public void actionPerformed(ActionEvent event){
      sendMessage(event.getActionCommand());
      userText.setText("");
    }
  }
  
  );

  add(userText,BorderLayout.NORTH);
  chatWindow = new JTextArea();
  add(new JScrollPane(chatWindow));
  setSize(300,150);
  setVisible(true) ;
  
  
  }
   // set up and run the server
  public void startRunning()
  {
    try {
      server = new ServerSocket(6789,100);
      while(true){
        try{
          
          waitForConnection();//wait for some one to oconnect
          setupStreams();//set up the connection
          startForum();//starts Forum
          whileChatting();//start chatitng
        }catch(EOFException eofException)
        {
          showMessage("\n Server ended the connection");
        }finally
        {
          closeCrap();
        }
      }
    }catch(IOException ioException){
      ioException.printStackTrace();
    }
  
  }
  
  //wait for connection and then display information
  public void waitForConnection() throws IOException
  {
  showMessage("Waiting for someoen to connect");
  connection = server.accept();
    showMessage("Now connected to "+ connection.getInetAddress().getHostName());
  
  }
  
  public void setupStreams() throws IOException
  {
  output = new ObjectOutputStream(connection.getOutputStream());
  output.flush();
  input = new ObjectInputStream(connection.getInputStream());
  showMessage("\n streams are now setup");
  }
  
  //durirng the conversation
  public void whileChatting() throws IOException{
    String message = "You are now connected";
      sendMessage(message);
     abletoType(true);
     do{
       try{
         if(message.equals("CLIENT - VIEWTOPICS"))
     {
  
   forum.viewAllTopics();
              
         }
         
          if(message.equals("CLIENT - ADDTOPIC"))
     {
           
           TopicPost topic1 = new TopicPost(0,"today","LOL","Friday Hater","Who hates Friday");
           forum.addTopic(topic1);
          

              
         }
          if(message.equals("CLIENT - VIEWTOPIC"))
     {

                 sendMessage("ENTERID");
   
       
            int value = Integer.parseInt((String) input.readObject());
             
          
            forum.viewTopic(value);
          

              
         }
         
         
         message = (String) input.readObject();
         showMessage("\n"+ message);
        
       }catch(ClassNotFoundException classNotFoundException)
       {
       showMessage("\n dont know what user sent");
       }
     }while(!message.equals("CLIENT - END"));
     
  }
  
  public void closeCrap()
  {
    showMessage("/n Closing Connections...");
    abletoType(false);
    try{ 
      output.close();//close stream to them
      input.close();//close stream from them
      connection.close();//close the socket
     
    }catch(IOException ioException)
    {
    ioException.printStackTrace();
    }
  }
  //send messages to the 
  
  
  public void sendMessage(String message)
  {
    try{
      output.writeObject("SERVER - " + message);
      output.flush();
      showMessage("\n Server - "+ message);
    }catch(IOException ioException) {
       chatWindow.append("\n Error");
    }
  
  }
  //updates chatwindow
  public void showMessage(final String text)
  {
    SwingUtilities.invokeLater(
                               new Runnable(){
      public void run(){
        chatWindow.append(text);
      }
    }
  );
  }
  //let the user type stuff into their box
  private void abletoType(final boolean tof)
  {
        SwingUtilities.invokeLater(
                               new Runnable(){
      public void run(){
        userText.setEditable(tof);
        
      }
    }
  );
  
  }
  
  private void startForum()
  {
  Forum forum1 = new Forum("Forum");
  
  this.forum = forum1;
  
  }
  }