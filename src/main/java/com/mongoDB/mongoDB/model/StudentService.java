package com.mongoDB.mongoDB.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongoDB.mongoDB.dao.StudentRepository;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService {

    private StudentRepository studentRepository;

    private Student student;

    private Logger logger;


    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentService() {

    }

    public String getAll(HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        try {
            List<Student> studentList = studentRepository.findAll();
            JSONArray studentArray = new JSONArray();
            if (studentList != null && studentList.size() > 0) {
                for (Student s : studentList) {
                    JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(s));
                    studentJSON.put("id", s.getId().toHexString());
                    studentArray.put(studentJSON);
                }
            }
            responseJson.put("getAllStudentResponse", studentArray);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return responseJson.toString();
    }


    public List<Student> getAllByCount(int count) {
        List<Student> students;
        List<Student> tempStudents = new ArrayList<>();
        if (studentRepository.count() <= count) {
            return studentRepository.findAll();
        } else {
            students = studentRepository.findAll();

            for (int i = 0; i < count; i++) {
                tempStudents.add(students.get(i));


            }
            return tempStudents;
        }

    }

    public String save(Student student,HttpServletResponse response) throws JsonProcessingException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        if(student.getName()== null || ("").equals(student.getAge())){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


        student = studentRepository.save(student);
        JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(student));
        studentJSON.put("id", student.getId().toHexString());
        responseJson.put("saveStudentResponse", studentJSON);

        return responseJson.toString();


    }

    public String deleteById(ObjectId id,HttpServletResponse response){
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            responseJson.put("deleted employee with id ", id);
            studentRepository.deleteById(id);



        }
        return responseJson.toString();
    }

    public String getById(ObjectId id,HttpServletResponse response) throws JsonProcessingException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);


        } else {

        student= studentRepository.findById(id);

            JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(student));
            studentJSON.put("id", student.getId().toHexString());
            responseJson.put("getStudentResponse", studentJSON);


        }
        return responseJson.toString();

    }

    public List<Student> getByName(String name){
        return studentRepository.findByName(name);
    }





    public List<Student> getByNameSorted(){
        Pageable sortByName = PageRequest.of(0,100,
                Sort.by("name").ascending());

        return studentRepository.findAll(sortByName).getContent();
    }



    public List<Student>findAllBySorting(){
        Pageable sortByAge = PageRequest.of(0,100,
                Sort.by("dob").ascending());

        return studentRepository.findAll(sortByAge).getContent();
    }





    @Transactional
    public String updateStudent(ObjectId id, Student student, HttpServletResponse response) throws JsonProcessingException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Student student1 = studentRepository.findById(id);


        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

            if(student.getName()!=null) {
            student1.setName(student.getName());
            student1.setDob(student.getDob());
            studentRepository.save(student1);



                JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(student1));
                studentJSON.put("id", student1.getId().toHexString());
                responseJson.put("getUpdatedStudentResponse", studentJSON);
        }




        return responseJson.toString();

    }


}
