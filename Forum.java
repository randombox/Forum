import java.util.*;
public class Forum {

	String name;
	ArrayList<TopicPost> tppost;

	public Forum(String name) { 
		this.name = name ;
		tppost = new ArrayList<TopicPost> ();
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

	/* Search for a topic using binary search */
	private int searchTopic(int id, int start, int end) {
		if((end-start) < 0)
			return -1;

		int mid = (end-start)/2;
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
			return "No topic matches this id.";
		} else {
			return tppost.get(index).toString();
		}
	}

	/* Prints out the name of all the topics in the database*/
	public String viewAllTopics()
	{
		String topics = name+"\n";
		for(int i=0; i<tppost.size(); i++) {
			topics += tppost.get(i).getTitle() + "\n";
		}
		
		return topics;
	}
}
