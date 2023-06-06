package com.jg.redditjavaproject;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends MongoRepository<MyDataModel, String> {
    List<MyDataModel> findByTextContaining(String query);
    void deleteByAuthor(String name);

    List <MyDataModel> findByAuthor(String user);
    List<MyDataModel> findAllByOrderByDatetimeDesc();
}