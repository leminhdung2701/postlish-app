package com.example.application.object;

import java.util.ArrayList;

/*public class WritingComment extends BaseComment {
    private String writingId;

    private ArrayList<ReplyComment> replies;
} */
public class WritingComment extends BaseComment{

    private String writingId;
    private ArrayList<ReplyComment> replies;


    public WritingComment (String a, String b, String c){
        super(a,b);
        writingId = c;    
    }
    public void addReply(ReplyComment x){
        replies.add (x);
    }
}
