import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Client extends JFrame{
  
   
  private JTextField userText;
  private JTextArea chatWindow;
  private ObjectOutputStream output;
  private ObjectInputStream input;
  private String message = "";
  private String serverIP;
  private Socket connection;
  private String name="Time";
  
    
  public Client(String host) { 
      super("Client");
      serverIP=host;
      userText = new JTextField();
      userText.setEditable(false);
      userText.addActionListener(
                                 new ActionListener(){
        public void actionPerformed(ActionEvent event)
        {
        sendMessage(event.getActionCommand());
        userText.setText("");
          
        }
      }
      
      );
      add(userText, BorderLayout.NORTH);
      chatWindow = new JTextArea();
      add(new JScrollPane(chatWindow),BorderLayout.CENTER);
      setSize(300,150);
      setVisible(true);
      
      
  }
//connect to server
  public void startRunning()
  {
    try{
      connectToServer();
      setupStreams();
      whileChatting();
    }catch(EOFException eofException){
      showMessage("\n Client terminated the connection");}
    catch(IOException ioexception){
      ioexception.printStackTrace();}
    finally{
      closeCrap();}
   }
  
//conneect to server

private void connectToServer()throws IOException{
  showMessage("\n Attemption Connection");
  connection = new Socket(InetAddress.getByName(serverIP),6789);
  showMessage("Connected to:" + connection.getInetAddress().getHostName());
}

//to send and revieve mssages
private void setupStreams()throws IOException
{  
   output = new ObjectOutputStream(connection.getOutputStream());
   output.flush();
   input = new ObjectInputStream(connection.getInputStream());
   showMessage("\n streams are now setup");
}

private void whileChatting() throws IOException{
  abletoType(true);
  do{
    try{
      

    message = (String ) input.readObject();
    showMessage("\n"+message);
             if(message.equals("SERVER - ENTERID"))
     { 
           String id = JOptionPane.showInputDialog("Please enter a topic ID to open");
           output.writeObject(id);
              
         }
    }catch(ClassNotFoundException classNotfoundException)
    {
    showMessage("\n I dont know that object type");
    }
  
  }while(!message.equals("SERVER - END"));
  
  
}
// close sockets and stream

private void closeCrap()
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
private void sendMessage(String message)
{
  try{
     output.writeObject("CLIENT - "+ message);
     output.flush();
     showMessage("\n Client - "+ message);
     
  }catch(IOException ioException)
  {chatWindow.append("/n something messed up");}
}

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
}

