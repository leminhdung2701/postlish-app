package com.example.application.object;

public class ReplyComment extends BaseComment {
    private String commentId;

    public ReplyComment(String userId, String commentId, String commentId1) {
        super(userId, commentId);
        this.commentId = commentId1;
    }

    @Override
    public String getCommentId() {
        return commentId;
    }

    @Override
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
