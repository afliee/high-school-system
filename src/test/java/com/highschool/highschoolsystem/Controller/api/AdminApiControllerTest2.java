package com.highschool.highschoolsystem.Controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.controller.api.AdminApiController;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AdminApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminApiController adminApiController;


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateTeacherEndpoint() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("john doe");
        registrationRequest.setPassword("password");
        registrationRequest.setEmail("test123@gmail.com");
        registrationRequest.setFullName("John Doe");

        when(adminService.createTeacher(any(RegistrationRequest.class))).thenReturn(new TeacherResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAllMembersEndpoint() throws Exception {
        when(adminService.getAll(any(String.class), any(Integer.class))).thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/members?filter=&page=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}

