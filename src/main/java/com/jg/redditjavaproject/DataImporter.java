package com.jg.redditjavaproject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DataImporter {

    private ObjectMapper om = new ObjectMapper();

    public void importData(String json_string, DataHandler dataHandler){
        try{
            JsonNode reddit_response = om.readTree(json_string);
            JsonNode data = reddit_response.get("data");
            JsonNode data_list = data.get("children");
            Integer counter = 1;
            List <MyDataModel> post_list = new ArrayList<MyDataModel>();
            data_list.forEach(reddit_post -> {
                JsonNode details = reddit_post.get("data");
                MyDataModel dataModel = new MyDataModel();
                if(details.get("id")!=null) dataModel.setId(details.get("id").toString());
                if(details.get("url")!=null) dataModel.setUrl(details.get("url").toString());
                if(reddit_post.get("kind")!=null) dataModel.setKind(reddit_post.get("kind").toString());
                if(details.get("permalink")!=null) dataModel.setPermalink(details.get("permalink").toString());
                if(details.get("subreddit_name_prefixed")!=null) dataModel.setSubreddit_name_prefix(details.get("subreddit_name_prefixed").toString());
                if(details.get("ups")!=null) dataModel.setUpvotes(details.get("ups").toString());
                if(details.get("selftext")!=null) dataModel.setText(details.get("selftext").toString());
                if(details.get("title")!=null) dataModel.setTitle(details.get("title").toString());
                if(details.get("author")!=null) dataModel.setAuthor(details.get("author").toString());
                if(details.get("created_utc")!=null){
                    String utc = details.get("created_utc").toString();
                    double timestampValue = Double.parseDouble(utc);
                    long timestampMillis = (long) timestampValue * 1000L;
                    Date date = new Date(timestampMillis);
                    dataModel.setDatetime(date);
                }
                post_list.add(dataModel);
            });

            for(MyDataModel dd:post_list){
                System.out.println(dd.getUrl() + "   " + dd.getSubreddit_name_prefix() + "   " + dd.getUpvotes()+ "   " + dd.getPermalink());
                dataHandler.saveDoc(dd);
            }
        }catch(IOException e){
            System.out.println(e.toString());
        }
    }
}
