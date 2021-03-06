package com.mongoDB.mongoDB.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class StudentController {

    private Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;




    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping(path = "/findAllStudents",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> getAllStudents(HttpServletResponse response) {
       return studentService.getAllStudents();

    }

    @GetMapping(path = "/findAllStudents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student getStudent(@PathVariable ObjectId id, HttpServletResponse response){
        for (Student obj :studentService.getAllStudents() ) {
            if (obj.getId().equals(id))
                return studentService.getById(id,response);
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return null;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path="/add")
    public @ResponseBody
    String addStudent(@RequestBody Student student,HttpServletResponse response) throws JsonProcessingException {
        return studentService.save(student,response);

    }

    @DeleteMapping(path = "/delete/{id}")
    public String deleteStudent(@PathVariable ObjectId id,HttpServletResponse response){
        return studentService.deleteById(id,response);
    }


    @PutMapping(path = "/students/{id}")
    public String updateStudent(@PathVariable("id") ObjectId id,
                                @RequestBody Student student, HttpServletResponse response) throws JsonProcessingException {

        return studentService.updateStudent(id, student, response);
    }



}
