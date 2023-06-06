package com.jg.redditjavaproject;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="upvoted_posts")
public class MyDataModel {

    @Id
    private String id;

    private String kind;


    private String subreddit_name_prefix;


    private String upvotes;


    private String url;
    public String getKind() {
        return kind;
    }


    private String permalink;
    public String text;
    public String title;
    public String author;
    public Date datetime;


    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date date_time) {
        this.datetime = date_time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public String getSubreddit_name_prefix() {
        return subreddit_name_prefix;
    }

    public void setSubreddit_name_prefix(String subreddit_name_prefix) {
        this.subreddit_name_prefix = subreddit_name_prefix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
