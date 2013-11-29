public class CommentPost extends Post {

	private static int commentIdCount = 0;
	
	public CommentPost(String date, String text, String user) { 
		super(commentIdCount++, date,text,user);
	}

	public String toString()
	{
		String sum = "\n\n["+getID()+"] "+getUser()+" @ "+getDate()+"\n"+getText();
		return sum;
	}
}
