import java.util.*;

public class Account {

	private String user;
	private String password;

	public Account(String user, String password) { 
		this.user = user;
		this.password = password;
	}
	public String getUser()
	{
		return user;
	}

	public String getPassword()
	{
		return password;
	}
}
