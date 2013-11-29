import java.io.BufferedReader;
import java.io.InputStreamReader;

import common.ChatIF;

/**
 * This class constructs a UI for a chat server. It implements the chat
 * interface, activating the display() method.
 * 
 * @author Ryan Murray, with duplicate code from ClientConsole.
 */
public class ServerConsole implements ChatIF {

	// CLASS VARIABLES

	/**
	 * The default port to connect with. Used if none is specified.
	 */
	final public static int DEFAULT_PORT = 5555;

	// INSTANCE VARIABLES

	ForumServer server;

	// CONSTRUCTORS

	/**
	 * Constructs the ServerConsole
	 * 
	 * @param port The port to connect through.
	 */
	public ServerConsole(Forum forum, int port)
	{
		server = new ForumServer(forum, port, this);
		
		try 
		{
			server.listen(); //Start listening for connections
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	// INSTANCE METHODS

	/**
	 * This method waits for input from the console.  Once it is 
	 * received, it sends it to the client's message handler.
	 */
	public void accept() 
	{
		try
		{
			BufferedReader fromConsole = 
					new BufferedReader(new InputStreamReader(System.in));
			String message;

			while (true) 
			{
				message = fromConsole.readLine();
				server.handleMessageFromServerUI(message);
			}
		} 
		catch (Exception ex) 
		{
			System.out.println
			("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method overrides the method in the ChatIF interface.  It
	 * displays a message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) 
	{
		System.out.println(message);
	}

	/**
	 * This method initiates the ServerConsole
	 * 
	 * @param args[0] The port to connect through.
	 */
	public static void main(String[] args) {
		int port = 0;

		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(Throwable t)
		{
			port = DEFAULT_PORT;
		}
		
		Forum forum = new Forum("The Awesome Forum");
		ServerConsole serverUI = new ServerConsole(forum, port);
		serverUI.accept();
	}
}
