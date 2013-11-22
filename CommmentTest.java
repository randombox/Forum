/**
 * Auto Generated Java Class.
 */
public class CommmentTest {
  
  
  public static void main(String[] args) { 
    
    Forum forum = new Forum("Rafehs Forum");
    
    TopicPost topic1 = new TopicPost(0,"today","LOL","Friday Hater","Who hates Friday");
        TopicPost topic2 = new TopicPost(1,"today","LOL","Friday Hater","MYaanann");
    CommentPost cpost = new CommentPost(0,"today","works","Mike");
     CommentPost cpost1 = new CommentPost(1,"today","works1","Mike");
      CommentPost cpost2 = new CommentPost(2,"today","works2","Mike");
       CommentPost cpost3 = new CommentPost(3,"today","works3","Mike");
       
       topic1.addComment(cpost);
       topic1.addComment(cpost1);
         topic1.addComment(cpost2);
         topic1.addComment(cpost3);
      
        forum.addTopic(topic1);
        forum.addTopic(topic2);
    //forum.viewAllTopics();
   

   

  
       
       // forum.viewAllTopics();
          forum.deleteTopic(0);
            forum.viewAllTopics();
           forum.deleteTopic(1);

           
  }
  
  /* ADD YOUR CODE HERE */
  
}
