public abstract class Post{
	private int id;
	private String date;
	private String text;
	private String user;

	public Post(int id,String date, String text, String user)

	{
		this.id = id;
		this.date = date;
		this.text = text;
		this.user = user;
	}

	public int getID(){return id;}
	public String getDate(){return date;}
	public String getText(){return text;}
	public String getUser(){return user;}



















}