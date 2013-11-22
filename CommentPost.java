public class CommentPost extends Post {

	public CommentPost(int id,String date, String text, String usname) { 
		super(id,date,text,usname);
	}

	public String toString()
	{
		String a = "ID:"+getID()+" Date:"+getDate()+" UserName:"+getUser()+ "\n";
		a += "Comment:"+getText()+"\n";
		return a;
	}
}
