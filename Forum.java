import java.util.*;
public class Forum {

	private String name;
	private ArrayList<TopicPost> tppost;
	private ArrayList<Account> accounts;

	public Forum(String name) { 
		this.name = name ;
		tppost = new ArrayList<TopicPost> ();
		accounts = new ArrayList<Account> ();
	}
	
	public boolean addAccount(String username, String password) {
		for(int i=0; i<accounts.size(); i++) {
			if(accounts.get(i).getUser() == username)
				return false; // Username already exists
		}
		
		Account a = new Account(username, password);
		accounts.add(a);
		return true;
	}
	
	public Account getAccount(String username, String password) {
		Account acc;
		String usr;
		String pwd;
		
		for(int i=0; i<accounts.size(); i++) {
			acc = accounts.get(i);
			usr = acc.getUser();
			pwd = acc.getPassword();
			
			if(usr.equals(username) && pwd.equals(password)) {
				return acc;
			}
		}
		
		return null;
	}
	
	public boolean isAuthor(Post post, Account acc) {
		String author = post.getUser();
		String comp = acc.getUser();
		return(author.equals(comp));
	}
	
	public TopicPost getTopic(int id) {
		int index = searchTopic(id, 0, tppost.size()-1);
		
		if(index == -1)
			return null;
		else
			return tppost.get(index);
	}

	/* Adds a topic to the forum */
	public void addTopic(TopicPost topic) {
		tppost.add(topic);
	}

	/* Deletes a topic from the forum */
	public boolean deleteTopic(int id){
		int start = 0;
		int end = tppost.size() - 1;

		int index = searchTopic(id, start, end);
		if(index == -1) {
			return false;
		} else {
			tppost.remove(index);
			return true;
		}
	}
	
	/* Searches for, and deletes a comment */
	public boolean deleteComment(int topic_id, int comment_id) {
		int index = searchTopic(topic_id, 0, tppost.size()-1);
		
		if(index == -1) {
			return false;
		} else {
			TopicPost tp = tppost.get(index);
			return tp.removeComment(comment_id);
		}
	}
	
	public boolean addComment(int topic_id, CommentPost cp) {
		int index = searchTopic(topic_id, 0, tppost.size()-1);
				
		if(index == -1) {
			return false;
		} else {
			TopicPost tp = tppost.get(index);
			tp.addComment(cp);
			return true;
		}
	}
	
	public String getComments(int id) {
		int index = searchTopic(id, 0, tppost.size()-1);
		if(index == -1) {
			return "No comments.";
		} else {
			TopicPost tp = tppost.get(index);
			String comments = tp.getComments();
			return comments;
		}
	}
	
	/* Search for a topic using binary search */
	private int searchTopic(int id, int start, int end) {
		
		if((end-start) < 0)
			return -1;

		int mid = Math.round((end+start)/2);
		TopicPost tp = tppost.get(mid);
		
		if(id == tp.getID()) {
			return mid;
		} else if (id > tp.getID()) {
			return searchTopic(id, mid+1, end);
		} else {
			return searchTopic(id, start, mid-1);
		}
	}

	/* Returns the topic specified by the id */
	public String viewTopic(int id){
		int start = 0;
		int end = tppost.size() - 1;
		
		int index = searchTopic(id, start, end);
		
		if(index == -1) {
			return "";
		} else {
			return tppost.get(index).toString();
		}
	}
	
	public int numComments(int id) {
		int index = searchTopic(id, 0, tppost.size()-1);
		
		if(index == -1) {
			return 0;
		} else {
			TopicPost tp = tppost.get(index);
			int num = tp.numComments();
			return num;
		}
	}

	/* Prints out the name of all the topics in the database*/
	public String viewAllTopics()
	{
		String topics = "--- "+name+" ---";
		for(int i=0; i<tppost.size(); i++) {
			topics += "\n"+tppost.get(i).getSummary();
		}
		
		return topics;
	}
}
