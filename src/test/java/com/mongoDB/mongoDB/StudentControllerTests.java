package com.mongoDB.mongoDB;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongoDB.mongoDB.dao.StudentRepository;
import com.mongoDB.mongoDB.model.Student;
import com.mongoDB.mongoDB.model.StudentService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    public static final String URL_TEMPLATE = "/api";

    private static Student student;

    private static StudentService studentService;

    private static StudentRepository studentRepository;


    @BeforeEach
    public void setupTestSuit() throws JsonProcessingException {

        studentService = new StudentService();
        student = new Student();
    }


    @Test
    public void testCreateStudent() throws Exception {
        String newString =
                "{" +
                        "\"name\":\"arkam\"," +
                "\"dob\" : \"2000-05-20\" " +"}";

        this.mockMvc.perform(post(URL_TEMPLATE+"/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newString))
                .andExpect(status().isOk());}



    @Test
    public void postBadRequest() throws Exception {
        String requestString = "\"username\":\"cassim\",\n" +
                "\"date\":\"1122234009221-23\",\n";

        this.mockMvc.perform(post(URL_TEMPLATE+"/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isBadRequest());
    }



    @Test
    public void testReadStudent() throws Exception{

        mockMvc.perform(get(URL_TEMPLATE + "/findAllStudents"))
                .andExpect(status().isOk());

    }



    @Test
    public void getStudentByName() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/findAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name" ,is("arkam")));
    }

    @Test
    public void getStudentByNameBadReq() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/findStudents/{name}", "543"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStudentSuccess() throws Exception {
        String requestString = "{" +
                "\"name\":\"maryam\"," +
                "\"dob\" : \"2000-05-20\" " +"}";
        String updatedName = "maryam";
        mockMvc.perform(put(URL_TEMPLATE + "/students/{id}", "61c334369b66d609659fef9d")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        mockMvc.perform(get(URL_TEMPLATE + "/findAllStudents/{id}", "61c334369b66d609659fef9d"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(updatedName)));
    }


    @Test
    public void deleteStudentById() throws Exception {
        mockMvc.perform(delete(URL_TEMPLATE + "/delete/{id}", "61c19a86a879c50d44dd967e"))
                .andExpect(status().isOk());


    }

    @Test
    public void deleteStudentNonExisting() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/delete/{id}", "61c19a86a879c50d44dd967e"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }



}
