package com.jg.redditjavaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@RestController
public class controller {

    @Autowired
    private RedditController redditController;

    @Autowired
    private DataImporter dataImporter;

    @Autowired
    private DataHandler dataHandler;


    @GetMapping("/hi")
    public String gethello(){
        return "Hello world!";
    }

    @GetMapping("/auth")
    public RedirectView createDatabase(){
        return redditController.auth_reddit();

    }

    @GetMapping("auth_redirect")
    public String auth_redirect(@RequestParam("state") String state, @RequestParam("code") String access_code){
        if(!Objects.equals(state, redditController.getState())){
            System.out.println("Access Denied!");
            return "Access Denied!";
        }
        redditController.setAccess_token(access_code);
        return redditController.retrieveAccessToken();
    }

    @GetMapping("loadData")
    public String loadData(){
        String response = redditController.getUpvotedPosts();
        dataImporter.importData(response, dataHandler);
        return response;
    }

    @GetMapping("/queryDoc/{query}")
    public List<MyDataModel> findDocs(@PathVariable String query){
        return dataHandler.findDocsByQuery(query);
    }

    @DeleteMapping("deleteDoc/{username}")
    public String deleteDocbyUser(@PathVariable String username){
        dataHandler.deletepostsbyUser(username);
        return "Deleted Successfully!";
    }

    @GetMapping("findDocbyUser/{username}")
    public List<MyDataModel> findDocsByUser(@PathVariable String username){
        return dataHandler.findDocsByUser(username);
    }

    @GetMapping("sortbyTime")
    public List<MyDataModel> sortByTime(){
        return dataHandler.sortByDate();
    }

    @PostMapping("createPost")
    public String post(@RequestBody PostClass post) throws UnsupportedEncodingException {
        String response_return =  redditController.post(post);
        String response = redditController.getUpvotedPosts();
        dataImporter.importData(response, dataHandler);
        return response_return;
    }

    @GetMapping("ex")
    public PostClass exam(){
        PostClass p = new PostClass();
        p.setSubreddit("test");
        p.setText("Test Text");
        p.setTitle("Test Title");
        return p;
    }

}
