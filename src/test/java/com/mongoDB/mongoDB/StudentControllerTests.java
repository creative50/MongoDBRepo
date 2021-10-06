package com.mongoDB.mongoDB;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongoDB.mongoDB.model.Student;
import com.mongoDB.mongoDB.model.StudentService;
import org.json.JSONObject;
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


    @BeforeEach
    public void setupTestSuit() throws JsonProcessingException {

        studentService = new StudentService();
        student = new Student();
    }


    @Test
    public void testCreateStudent() throws Exception {

        String requestString =  "{" + "\"saveStudentResponse\":{" + "\"dob\":[2000,5,20]," +
                "\"name\":\"arkam\"," +
                "\"id\":\"61518085aafda645fc69af74\"," +
                "\"age\":21}}";

        String newString =
                "{" +
                        "\"name\":\"arkam\"," +
                "\"dob\" : \"2000-05-20\" " +"}";
        JSONObject response = new JSONObject(requestString);

        this.mockMvc.perform(post(URL_TEMPLATE+"/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saveStudentResponse" ,hasValue(21)));

    }

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.getAllStudentResponse",hasSize(4)))

        ;

    }

    @Test
    public void getStudentNonExisting() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/findStudents/{id}", "6140cbdc5a27fa77eabe7ba2"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    public void getStudentByName() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/findStudent/{name}", "arkam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name" ,is("arkam")));
    }

    @Test
    public void getStudentByNameBadReq() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/findStudents/{name}", "543"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteStudentById() throws Exception {
        mockMvc.perform(delete(URL_TEMPLATE + "/delete/{id}", "6155c6cdee399717d24e751a"))
                .andExpect(status().isOk());


    }



}
