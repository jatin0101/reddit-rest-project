package com.jg.redditjavaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@EnableMongoRepositories
public class DataHandler {
    @Autowired
    private DataRepository dataRepository;
    public void saveDoc(MyDataModel dataModel){
        dataRepository.save(dataModel);
    }

    public List<MyDataModel> findDocsByQuery(String query){
        return dataRepository.findByTextContaining(query);
    }

    public void deletepostsbyUser(String user){
        dataRepository.deleteByAuthor(user);
    }

    public List<MyDataModel> findDocsByUser(String user){
        return dataRepository.findByAuthor(user);
    }

    public List<MyDataModel> sortByDate(){
        return dataRepository.findAllByOrderByDatetimeDesc();
    }
}
