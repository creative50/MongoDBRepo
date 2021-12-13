package com.mongoDB.mongoDB.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongoDB.mongoDB.dao.StudentRepository;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class StudentService {

    private  StudentRepository studentRepository;

    private Student student;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentService() {

    }

    public List<Student> getAllEmployees(){
        return studentRepository.findAll();
    }


    public String save(Student employee,HttpServletResponse response) throws JsonProcessingException, JSONException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        if (employee.getName() == null || ("").equals(employee.getAge())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        employee = studentRepository.save(employee);
        JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(employee));
        studentJSON.put("id", employee.getId().toHexString());
        responseJson.put("saveEmployeeResponse", studentJSON);

        return responseJson.toString();

    }

    public String deleteById(ObjectId id, HttpServletResponse response) throws JSONException {
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


    @Transactional
    public String updateEmployee(ObjectId id, Student employee, HttpServletResponse response) throws JsonProcessingException, JSONException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Student employee1 = studentRepository.findById(id);


        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        if(employee.getName()!=null) {
            employee1.setName(employee.getName());
            employee1.setAge(employee.getAge());
            studentRepository.save(employee1);



            JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(employee1));
            studentJSON.put("id", employee1.getId().toHexString());
            responseJson.put("getUpdatedEmployeeResponse", studentJSON);
        }




        return responseJson.toString();

    }


}
