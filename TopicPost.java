import java.util.*;

public class TopicPost extends Post {

	private static int topicIdCount = 0;
	private String title;
	private ArrayList<CommentPost> cmpost;

	public TopicPost(String date, String text, String user, String title) { 
		super(topicIdCount++, date,text,user);
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
	
	public String toString() {
		String str = getSummary()+"\n"+getText();
		return str;
	}
	
	public CommentPost getComment(int id) {
		int index = searchComment(id, 0, cmpost.size()-1);
		
		if(index == -1)
			return null;
		else
			return cmpost.get(index);
	}

	public boolean removeComment(int id)
	{
		int start = 0;
		int end = cmpost.size() - 1;
		int index = searchComment(id, start, end);
		
		if(index == -1) {
			return false;
		} else {
			cmpost.remove(index);
			return true;
		}
	}
	
	private int searchComment(int id, int start, int end)
	{
		if((end-start) < 0)
			return -1;
		
		int middle = (end + start)/2;
		CommentPost cp = cmpost.get(middle);
		
		if(cp.getID() == id) {
			return middle;
		} else if(cp.getID() < id) {
			return searchComment(id, middle+1, end);
		} else {
			return searchComment(id, start, middle-1);
		}
	}
	
	public String getSummary() {
		String sum = "["+getID()+"] \""+getTitle()+"\" "+getUser()+" @ "+getDate();
		return sum;
	}
	
	public int numComments() {
		return cmpost.size();
	}
	
	public String getComments() {
		String comments = "";
		
		for(int i=0 ; i < cmpost.size();i++)
		{
			comments += cmpost.get(i).toString();
		}
		
		return comments;
	}

}
