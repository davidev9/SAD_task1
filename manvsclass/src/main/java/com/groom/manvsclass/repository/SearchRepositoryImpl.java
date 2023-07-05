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
import com.mongodb.client.MongoIterable;

@Component
public class SearchRepositoryImpl {

	@Autowired
    MongoClient client;
 
    @Autowired
    MongoConverter converter;

    public long getLikes(String name) {
    	MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("interaction");
        
        AggregateIterable<Document> result = collection.aggregate(
        	    Arrays.asList(
        	        new Document("$search", 
        	            new Document("text", 
        	                new Document("query", name)
        	                .append("path", Arrays.asList("name"))
        	            )
        	        ),
        	        new Document("$match",
        	            new Document("type", 1)
        	        ),
        	        new Document("$count", "count")
        	    )
        	);
        long count =0;
        if(result !=null) {
        	Document firstResult = result.first();
        	if (firstResult != null) {
                count = ((Number) firstResult.get("count")).longValue();
            }
        }
        else {
        	count = 0;
        }
        return count;
    }
    
    public List<ClassUT> findByText(String text) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document("$search", 
                    new Document("text", 
                        new Document("query", text)
                        .append("path", Arrays.asList("name", "description"))
                    )
                )
            )
        );

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
   
    
    public List<ClassUT> searchAndFilter(String text, String category) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$search",
                                new Document("index", "default")
                                        .append("text",
                                                new Document("query", text)
                                                        .append("path", Arrays.asList("name", "description")))),
                        new Document("$match",
                                new Document("category", category))
                )
        );

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    public List<ClassUT> filterByCategory(String category) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
        	    new Document("index", "default")
                .append("text", 
        new Document("query", category)
                    .append("path", "category")))));

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    
    public List<ClassUT> orderByDate() {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");
		
		AggregateIterable<Document> result = collection.aggregate(
	    Arrays.asList(
	        new Document("$sort",
	            new Document("date", 1)
	        	)
	    	)
		);

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    public List<ClassUT> orderByName() {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");
		
		AggregateIterable<Document> result = collection.aggregate(
	    Arrays.asList(
	        new Document("$sort",
	            new Document("name", 1)
	        	)
	    	)
		);

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
        public List<ClassUT> filterByDifficulty(String difficulty) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
        	    new Document("index", "default")
                .append("text", 
        new Document("query", difficulty)
                    .append("path", "difficulty")))));

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
        
        public List<ClassUT> searchAndDFilter(String text, String difficulty) {

            final List<ClassUT> posts = new ArrayList<>();

            MongoDatabase database = client.getDatabase("manvsclass");
            MongoCollection<Document> collection = database.getCollection("ClassUT");

            AggregateIterable<Document> result = collection.aggregate(
                    Arrays.asList(
                            new Document("$search",
                                    new Document("index", "default")
                                            .append("text",
                                                    new Document("query", text)
                                                            .append("path", Arrays.asList("name", "description")))),
                            new Document("$match",
                                    new Document("difficulty", difficulty))
                    )
            );

            result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

            return posts;
        }    
    
}
