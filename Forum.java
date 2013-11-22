/**
 * Auto Generated Java Class.
 */
import java.util.*;
public class Forum {
  String name;
  ArrayList<TopicPost> tppost ;
  public Forum(String name) { 
    /* YOUR CONSTRUCTOR CODE HERE*/
    this.name = name ;
    tppost = new ArrayList<TopicPost> ();
  }
  /* Adds a topic to the forum */
  public void addTopic(TopicPost topic){tppost.add(topic);}
  /* Deletes a topic from the forum */
  public void deleteTopic(int id){
    //  boolean found = false;
    if(!tppost.isEmpty() & id < tppost.size())//check to see that id is valid and array list not empty
    {
      if(id==tppost.get(id).getID())//check if id matches the id of the topic
      {
        tppost.remove(id);//if matches remove the topic
      }
      else//if it doesnt match
      {
        for(int i =0 ; i < tppost.size();i++) //try to find if the arraylist contains a topic with similar id
        {
          
          if(id==tppost.get(i).getID())//if found a topic that matches
          {
            tppost.remove(i);//if matches remove the topic
            //  found=true;//set the boolean element founded to true
            break;
          }    
        }        
      }
    }
    else //if invalid id etc
    {System.out.println("Invalid ID");}
  }
  /* Opens a topic */
  public void viewTopic(int id){
    
    boolean found=false;// boolean for checking whether item is found or not
    
    for(int i =0 ; i < tppost.size();i++) // traverse through the array to find the element
    { if(id==tppost.get(i).getID())//if id of an element matches to that of id provided return true
      { 
      found=true; //marks found to be true
      System.out.println(tppost.get(i).toString());
     }
       }
    if(!found)
    { System.out.println("Invalid Id");}
    }
  
  
  
  
  
  
  
  /* Prints out the name of all the topics in the database*/
  public void viewAllTopics()
  {
    if (tppost.isEmpty())
      
    {System.out.println("No topics have been added to the forum");}
    else
    {
      for(int i =0 ; i < tppost.size();i++)  {
        System.out.println("ID:"+tppost.get(i).getID()+"  "+"Title:"+tppost.get(i).getTitle());}
    }
  }
}
