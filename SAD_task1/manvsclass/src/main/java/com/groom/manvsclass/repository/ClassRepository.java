package com.groom.manvsclass.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.groom.manvsclass.model.ClassUT;

public interface ClassRepository	extends MongoRepository<ClassUT,String>{

}
