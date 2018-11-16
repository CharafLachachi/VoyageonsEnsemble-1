package com.example.thamazgha.voyageonsensemble.tools;

public class CommentItem implements Comparable<CommentItem> {

    private int pub_id;
    private String content;
    private String userNameOwner;
    private String date;


    public CommentItem(int pub_id, String userNameOwner, String date, String content) {

        this.pub_id = pub_id;
        this.userNameOwner = userNameOwner;
        this.date = date;
        this.content = content;
    }





    public int getPub_id() {
        return pub_id;
    }

    public void setPub_id(int pub_id) {
        this.pub_id = pub_id;
    }

    public String getUserNameOwner() {
        return userNameOwner;
    }

    public void setUserNameOwner(String userNameOwner) {
        this.userNameOwner = userNameOwner;
    }

    public String getDate(){  return this.date ; }

    public void setDate(String date){ this.date = date; }

    public String getContent(){  return this.content ; }

    public void setContent(String date){ this.content = content; }


    @Override
    public boolean equals(Object obj) {

        if( obj instanceof CommentItem){

            CommentItem o = (CommentItem) obj;
            if(pub_id == o.getPub_id() && content.equals(o.getContent()) && userNameOwner.equals(o.getUserNameOwner()) && date.equals(o.getDate())) return true;
            return false ;
        }
        return false ;

    }

    @Override
    public int compareTo(CommentItem o) {

        if(pub_id == o.getPub_id() && content.equals(o.getContent()) && userNameOwner.equals(o.getUserNameOwner()) && date.equals(o.getDate())) return 0 ;
        return 1;
    }
}
