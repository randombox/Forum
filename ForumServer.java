// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import common.*;
import java.util.*;
import java.util.Calendar;
import java.text.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class ForumServer extends AbstractServer 
{
	//Class variables *************************************************

	public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	//Instace variables ***********************************************

	//Changed for E50 by Ryan Murray *****

	/**
	 * The instance of the server that created the EchoServer
	 */
	ChatIF serverUI;

	/*
	 * The instance of the forum.
	 */
	Forum forum;

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * @param serverUI The server user interface.
	 */

	public ForumServer(Forum forum, int port, ChatIF serverUI) 
	{
		super(port);
		this.serverUI = serverUI;
		this.forum = forum;
	}

	public ForumServer(int port)
	{
		super(port);
	}

	//Instance methods ************************************************

	//Changed for E51 by Ryan Murray *****

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient
	(Object msg, ConnectionToClient client)
	{
		String str = msg.toString();
		StringTokenizer st = new StringTokenizer(str);
		st.nextToken();

		if(str.startsWith("#register"))
		{
			clientRegister(str, client);
		}
		else if(str.startsWith("#login"))
		{
			clientLogin(str, client);
		}
		else if(str.startsWith("#logoff"))
		{
			clientLogoff(client);
		}
		else if(str.startsWith("#viewAll"))
		{
			try
			{
				client.sendToClient(forum.viewAllTopics());
			}
			catch(IOException e)
			{
				System.out.println("Could not send to client.");
			}
		}
		else if(str.startsWith("#viewTopic"))
		{	
			int id;
			try {
				String id_s = st.nextToken();
				id = Integer.parseInt(id_s);
			} catch(Throwable t) {
				sendFormat(str, client);
				return;
			}

			String topic = forum.viewTopic(id);

			if(topic == "")
				topic = "Message: No topic matches this id.";

			if(forum.numComments(id) != 0) {
				topic += "\n---------------";
				topic += forum.getComments(id);
			}

			try
			{
				client.sendToClient(topic);
			}
			catch(IOException e)
			{
				System.out.println("Could not send to client.");
			}
		}
		else if(str.startsWith("#postTopic"))
		{
			if(isLoggedIn(client)) {

				String title;
				String text;

				try {
					st = new StringTokenizer(str, "\"");
					st.nextToken();
					title = st.nextToken();
					st.nextToken();
					text = st.nextToken();
				} catch(Throwable t) {
					sendFormat(str, client);
					return;
				}

				if(title.equals("") || text.equals("")) {
					sendFormat(str, client);
					return;
				}

				Calendar calen = Calendar.getInstance();
				SimpleDateFormat sim = new SimpleDateFormat(dateFormat);
				String date = sim.format(calen.getTime());

				Account acc = (Account) client.getInfo("Account");
				String user = acc.getUser();

				TopicPost tp = new TopicPost(date, text, user, title);
				forum.addTopic(tp);
				try
				{
					client.sendToClient("Message: Topic added.");
				}
				catch(IOException e)
				{
					System.out.println("Could not send to client.");
				}
			} else {
				try
				{
					client.sendToClient("Message: You must be logged in to post.");
				}
				catch(IOException e)
				{
					System.out.println("Could not send to client.");
				}
			}
		}
		else if(str.startsWith("#postComment"))
		{
			if(isLoggedIn(client)) {
				String text;
				int id;

				try {
					String id_s = st.nextToken();
					st = new StringTokenizer(str, "\"");
					st.nextToken();
					text = st.nextToken();

					id = Integer.parseInt(id_s);
				} catch(Throwable t) {
					sendFormat(str, client);
					return;
				}

				if(text.equals("")) {
					sendFormat(str, client);
					return;
				}

				Calendar calen = Calendar.getInstance();
				SimpleDateFormat sim = new SimpleDateFormat(dateFormat);
				String date = sim.format(calen.getTime());

				Account acc = (Account) client.getInfo("Account");
				String user = acc.getUser();

				CommentPost cp = new CommentPost(date, text, user);
				boolean added = forum.addComment(id, cp);
				String reply = "";

				if(added) {
					reply = "Message: The comment was added to "+id;
				} else {
					reply = "Message: The topic id could not be found.";
				}

				try
				{
					client.sendToClient(reply);
				}
				catch(IOException e)
				{
					System.out.println("Could not send to client.");
				}
			} else {
				try
				{
					client.sendToClient("Message: You must be logged in to post.");
				}
				catch(IOException e)
				{
					System.out.println("Could not send to client.");
				}
			}
		}
		else if(str.startsWith("#deleteTopic"))
		{
			if(isLoggedIn(client)) {
				String id_s;
				int id;
				String reply = "";

				try {
					id_s = st.nextToken();
					id = Integer.parseInt(id_s);
				} catch(Throwable t) {
					sendFormat(str, client);
					return;
				}

				TopicPost tp = forum.getTopic(id);

				if(tp == null) {
					reply = "Message: The topic id doesn't exists.";
				} else {

					Account acc = (Account) client.getInfo("Account");

					if(forum.isAuthor(forum.getTopic(id), acc)) {
						if(forum.deleteTopic(id)) {
							reply = "Message: Topic deleted.";
						} else {
							reply = "Message: This topic id id doesn't exist.";
						}
					} else {
						reply = "Message: You are not the author of this topic.";
					}
				}

				try
				{
					client.sendToClient(reply);
				}
				catch(IOException e)
				{
					System.out.println("Could not send to client.");
				}
			} else {
				try
				{
					client.sendToClient("Message: You must be logged in to delete.");
				}
				catch(IOException e)
				{
					System.out.println("Could not send to client.");
				}
			}
		}
		else if(str.startsWith("#deleteComment"))
		{
			if(isLoggedIn(client)) {
				String topicID;
				String commentID;

				int tID;
				int cID;

				try {
					topicID = st.nextToken();
					commentID = st.nextToken();

					tID = Integer.parseInt(topicID);
					cID = Integer.parseInt(commentID);
				} catch(Throwable t) {
					sendFormat(str, client);
					return;
				}

				String reply = "";

				TopicPost tp = forum.getTopic(tID);
				CommentPost cp = tp.getComment(cID);

				Account acc = (Account) client.getInfo("Account");

				if(forum.isAuthor(cp, acc)) {
					if(tp.removeComment(cID)) {
						reply = "Message: Topic deleted.";
					} else {
						reply = "Message: This comment id id doesn't exist.";
					}
				} else {
					reply = "Message: You are not the author of this comment.";
				}

			try
			{
				client.sendToClient(reply);
			}
			catch(IOException e)
			{
				System.out.println("Could not send to client.");
			}
		} else {
			try
			{
				client.sendToClient("Message: You must be logged in to delete.");
			}
			catch(IOException e)
			{
				System.out.println("Could not send to client.");
			}
		}
	} else {
		try
		{
			client.sendToClient("Message: Invalid command.");
		}
		catch(IOException e)
		{
			System.out.println("Could not send to client.");
		}
	}
}

private void sendFormat(String com, ConnectionToClient client) {
	String format = "";

	if(com.startsWith("#register")) {
		format = "Message: #register [username] [password]";

	} else if(com.startsWith("#login")) {
		format = "Message: #login [username] [password]";

	} else if(com.startsWith("#viewTopic")) {
		format = "Message: #viewTopic [topic id]";

	} else if(com.startsWith("#postTopic")) {
		format = "Message: #postTopic \"title\" \"text\"";

	} else if(com.startsWith("#postComment")) {
		format = "Message: #postComment [topic id] \"text\"";

	} else if(com.startsWith("#deleteTopic")) {
		format = "Message: #deleteTopic [topic id]";

	} else if(com.startsWith("#deleteComment")) {
		format = "Message: #deleteComment [topic id] [comment id]";
	}

	try
	{
		client.sendToClient(format);
	}
	catch(IOException e)
	{
		System.out.println("Could not send to client.");
	}
}

private boolean isLoggedIn(ConnectionToClient client) {
	if(client.getInfo("Account") == null) {
		return false;
	} else {
		return true;
	}
}

/**
 * This method handles any messages received from the server UI.
 * 
 * @param msg The message from the server UI.
 */
public void handleMessageFromServerUI(String msg)
{
	if(msg.charAt(0) == '#')
	{
		doCommand(msg);
	} else {
		System.out.println("Invalid command.");
	}
}

//Changed for E50 by Ryan Murray *****

/**
 * This method performs the client login
 * 
 * @param id The login id of the client.
 */
private void clientLogin(String str, ConnectionToClient client)
{
	String usr;
	String pwd;

	StringTokenizer st = new StringTokenizer(str);

	try {
		st.nextToken();
		usr = st.nextToken();
		pwd = st.nextToken();
	} catch(Throwable t) {
		sendFormat(str, client);
		return;
	}

	if(usr.equals("") || pwd.equals("")) {
		sendFormat(str, client);
		return;
	}

	if(isLoggedIn(client))
	{
		try
		{
			client.sendToClient("Message: You are already logged in.");
		}
		catch(IOException e)
		{
			System.out.println("Could not send to client.");
		}
	}
	else
	{
		Account acc = forum.getAccount(usr, pwd);

		if(acc == null) {
			try
			{
				client.sendToClient("Message: Invalid username and password.");
			}
			catch(IOException e)
			{
				System.out.println("Could not send to client.");
			}
		} else {
			client.setInfo("Account", acc);

			try
			{
				client.sendToClient("Message: Login successful.");
			}
			catch(IOException e)
			{
				System.out.println("Could not send to client.");
			}
		}
	}
}

private void clientRegister(String str, ConnectionToClient client) {
	String usr;
	String pwd;

	StringTokenizer st = new StringTokenizer(str);

	try {
		st.nextToken();
		usr = st.nextToken();
		pwd = st.nextToken();
	} catch(Throwable t) {
		sendFormat(str, client);
		return;
	}

	if(usr.equals("") || pwd.equals("")) {
		sendFormat(str, client);
		return;
	}

	if(isLoggedIn(client))
	{
		try
		{
			client.sendToClient("Message: You are already logged in.");
		}
		catch(IOException e)
		{
			System.out.println("Could not send to client.");
		}
	}
	else
	{
		boolean available = forum.addAccount(usr, pwd);
		String msg = "";

		if(available) {
			msg = "Account created. Try #login.";
		} else {
			msg = "Username already taken. Try again.";
		}

		try
		{
			client.sendToClient(msg);
		}
		catch(IOException e)
		{
			System.out.println("Could not send to client.");
		}	
	}
}

private void clientLogoff(ConnectionToClient client) {
	if(isLoggedIn(client))
	{
		client.setInfo("Account", null);
		try
		{
			client.sendToClient("Message: Logoff successful.");
		}
		catch(IOException e)
		{
			System.out.println("Could not send to client.");
		}
	}
	else
	{
		try
		{
			client.sendToClient("You are not logged in.");
		}
		catch(IOException e)
		{
			System.out.println("Could not send to client.");
		}	
	}
}

/**
 * This method handles commands from the server UI.
 * 
 * @param cmd The command to be performed.
 */
public void doCommand(String cmd) 
{
	StringTokenizer tk = new StringTokenizer(cmd);
	String token = tk.nextToken();

	if(token.equals("#quit"))
	{
		try
		{
			stopListening();
			close();
			System.exit(0);
		}
		catch(IOException e)
		{
			System.exit(0);
		}
	} 
	else if(token.equals("#stop"))
	{
		stopListening();
		System.out.println("stop successful.");
	} 
	else if(token.equals("#close"))
	{
		try
		{
			stopListening();
			close();
			System.out.println("close successful.");
		}
		catch(IOException e)
		{
			System.exit(0);
		}
	} 
	else if(token.equals("#setport"))
	{
		if(!isListening() && getNumberOfClients() == 0)
		{
			setPort(Integer.parseInt(tk.nextToken()));
			System.out.println("setport successful.");
		}
		else
		{
			System.out.println("You must close the server before setting the port.");
		}
	} 
	else if(token.equals("#start"))
	{
		if(!isListening())
		{
			try
			{
				listen();
				System.out.println("start successful.");
			}
			catch(IOException e)
			{
				System.out.println("Could not start listening for connections.");
			}
		}
	} 
	else 	  if(token.equals("#getport"))
	{
		System.out.println("port is: "+getPort());
	} 
	else 
	{
		System.out.println("Invalid command.");
	}
}

/**
 * This method overrides the one in the superclass.  Called
 * when the server starts listening for connections.
 */
protected void serverStarted()
{
	System.out.println
	("Server listening for connections on port " + getPort());
}

/**
 * This method overrides the one in the superclass.  Called
 * when the server stops listening for connections.
 */
protected void serverStopped()
{
	System.out.println
	("Server has stopped listening for connections.");
}

//Changed for E49 by Ryan Murray *****

/**
 * A method that handles the connection of a client. Notifies
 * server user.
 * 
 * @param client The client that has connected.
 */
public void clientConnected(ConnectionToClient client)
{
	System.out.println("A client has connected.");
}

//Changed for E49 by Ryan Murray *****

/**
 * A method that handles the disconnection of a client. Notifies
 * server user.
 * 
 * @param client The client that has disconnected.
 */
public void clientDisconnected(ConnectionToClient client)
{
	System.out.println("A client has disconnected.");
}

//Changed for E49 by Ryan Murray *****

/**
 * A method that handles client disconnections.
 * 
 * @param client The client that has disconnected.
 * @param t The error or exception.
 */
public void clientException(ConnectionToClient client, Throwable t)
{
	clientDisconnected(client);
}
}
//End of EchoServer class
