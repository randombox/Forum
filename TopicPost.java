import java.util.*;

public class TopicPost extends Post {

	String title;
	ArrayList<CommentPost> cmpost;

	public TopicPost(int id,String date, String text, String user, String title) { 
		super(id,date,text,user);
		this.title = title;
		cmpost = new ArrayList<CommentPost>();
	}

	public String getTitle()
	{
		return title;
	}

	public void addComment(CommentPost comment)
	{
		cmpost.add(comment);
	}

	public boolean removeComment(int id)
	{
		int start = 0;
		int end = cmpost.size() - 1;
		
		return removeComment(id, start, end);
	}
	
	private boolean removeComment(int id, int start, int end)
	{
		// Currently doing this right now...
		return false;
	}

	public String toString()
	{
		String a = "Topic:"+title+"\n";
		for(int i=0 ; i < cmpost.size();i++)
		{
			a+= cmpost.get(i).toString();
		}
		
		return a;
	}



}
