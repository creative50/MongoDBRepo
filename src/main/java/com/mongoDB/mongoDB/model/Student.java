package com.mongoDB.mongoDB.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.Period;



@Data
@AllArgsConstructor
@Document(collection = "student")
public class Student {


    public Student() {
    }


    private ObjectId id;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

        private String name;


    private LocalDate dob;



    @Transient
    private int age;




    public int getAge() {
        return Period.between(this.dob,LocalDate.now()).getYears();
    }

    public void setAge(int age) {
        this.age = age;
    }


}
