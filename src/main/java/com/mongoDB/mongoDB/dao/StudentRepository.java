package com.mongoDB.mongoDB.dao;

import com.mongoDB.mongoDB.model.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;


@Repository
public interface StudentRepository extends MongoRepository<Student,UUID> {

        List<Student> findByName(String name);

        Student deleteById(ObjectId id);

        boolean existsById(ObjectId id);

        Student findById(ObjectId id);

}
