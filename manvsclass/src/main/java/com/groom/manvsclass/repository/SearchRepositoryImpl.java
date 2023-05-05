package com.groom.manvsclass.repository;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.groom.manvsclass.model.ClassUT;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class SearchRepositoryImpl {

	/*
	public SearchRepositoryImpl()
	{
		
	}
	*/
	@Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;

    
    public List<ClassUT> findByText(String text) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        
        

        
        
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
        	    new Document("index", "default")
	            .append("text", 
	    new Document("query", "Classe01")
	                .append("path", Arrays.asList("name", "description"))))));

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
}
