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


    @CrossOrigin(origins = "*")
    @GetMapping(path = "/findAllStudents",produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllStudents(HttpServletResponse response) {
       return studentService.getAll(response);

    }


    @GetMapping(path = "/findCount/{count}")
    public List<Student> getStudentsByCount(@PathVariable int count) {
        return studentService.getAllByCount(count);
    }

    @GetMapping(path = "/findStudentByAge")
    public List<Student> getStudentsByAge() {
        return studentService.findAllBySorting();
    }

    @GetMapping(path = "/findStudentByNameSorted")
    public List<Student> getStudentsByName() {
        return studentService.getByNameSorted();
    }


    @GetMapping(path = "/findStudents/{id}")
    public String getStudentById(@PathVariable("id") ObjectId id,HttpServletResponse response) throws JsonProcessingException {

        return studentService.getById(id,response);
    }



    @GetMapping(path = "/findStudent/{name}")
    public List<Student> getStudentByName(@PathVariable String name) {
        return studentService.getByName(name);
    }



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
