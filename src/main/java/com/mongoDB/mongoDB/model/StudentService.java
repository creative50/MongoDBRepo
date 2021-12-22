package com.mongoDB.mongoDB.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.mongoDB.mongoDB.dao.StudentRepository;


@Service
public class StudentService {

    private StudentRepository studentRepository;

    private Student student;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentService() {

    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public String save(Student student, HttpServletResponse response) throws JsonProcessingException, JSONException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        if (student.getName() == null || ("").equals(student.getAge())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        student = studentRepository.save(student);
        JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(student));
        studentJSON.put("id", student.getId().toHexString());
        responseJson.put("saveStudentResponse", studentJSON);

        return responseJson.toString();

    }

    public Student getById(ObjectId id, HttpServletResponse response) throws JSONException {
        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
        return studentRepository.findById(id);

    }


    public String deleteById(ObjectId id, HttpServletResponse response) throws JSONException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            responseJson.put("deleted student with id ", id);
            studentRepository.deleteById(id);



        }
        return responseJson.toString();
    }


    @Transactional
    public String updateStudent(ObjectId id, Student student, HttpServletResponse response) throws JsonProcessingException, JSONException {
        JSONObject responseJson = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Student employee1 = studentRepository.findById(id);

        boolean exists = studentRepository.existsById(id);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        if(student.getName()!=null) {
            employee1.setName(student.getName());
            employee1.setAge(student.getAge());
            studentRepository.save(employee1);

            JSONObject studentJSON = new JSONObject(mapper.writeValueAsString(employee1));
            studentJSON.put("id", employee1.getId().toHexString());
            responseJson.put("getUpdatedStudentResponse", studentJSON);
        }

        return responseJson.toString();

    }


}
