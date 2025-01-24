package com.project.citymanagement.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.citymanagement.entity.User;
import com.project.citymanagement.repository.UserRepository;
import com.project.citymanagement.security.JwtTokenUtil;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AuthController#createAuthenticationToken(com.project.citymanagement.entity.User)}
     */
    @Test
    void testCreateAuthenticationToken() throws Exception {
        when(this.userDetailsService.loadUserByUsername((String) any()))
                .thenReturn(new org.springframework.security.core.userdetails.User("janedoe", "iloveyou", new ArrayList<>()));
        when(this.jwtTokenUtil.generateToken((String) any())).thenReturn("ABC123");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        com.project.citymanagement.entity.User user = new com.project.citymanagement.entity.User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link AuthController#createAuthenticationToken(com.project.citymanagement.entity.User)}
     */
    @Test
    void testCreateAuthenticationToken2() throws Exception {
        when(this.userDetailsService.loadUserByUsername((String) any()))
                .thenReturn(new org.springframework.security.core.userdetails.User("janedoe", "iloveyou", new ArrayList<>()));
        when(this.jwtTokenUtil.generateToken((String) any())).thenReturn("ABC123");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenThrow(new DataIntegrityViolationException("?"));

        com.project.citymanagement.entity.User user = new com.project.citymanagement.entity.User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Unable to authenticate"));
    }

    /**
     * Method under test: {@link AuthController#createAuthenticationToken(com.project.citymanagement.entity.User)}
     */
    @Test
    void testCreateAuthenticationToken3() throws Exception {
        when(this.userDetailsService.loadUserByUsername((String) any()))
                .thenReturn(new org.springframework.security.core.userdetails.User("janedoe", "iloveyou", new ArrayList<>()));
        when(this.jwtTokenUtil.generateToken((String) any())).thenReturn("ABC123");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenThrow(new BadCredentialsException("?"));

        com.project.citymanagement.entity.User user = new com.project.citymanagement.entity.User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Invalid username or password"));
    }

    /**
     * Method under test: {@link AuthController#createAuthenticationToken(User)}
     */
    @Test
    void testCreateAuthenticationToken4() throws Exception {
        when(this.userDetailsService.loadUserByUsername((String) any())).thenReturn(null);
        when(this.jwtTokenUtil.generateToken((String) any())).thenReturn("ABC123");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Unable to authenticate"));
    }

    /**
     * Method under test: {@link AuthController#registerUser(User)}
     */
    @Test
    void testRegisterUser() throws Exception {
        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

        User user1 = new User();
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully!"));
    }

    /**
     * Method under test: {@link AuthController#registerUser(User)}
     */
    @Test
    void testRegisterUser2() throws Exception {
        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.passwordEncoder.encode((CharSequence) any())).thenThrow(new DataIntegrityViolationException("?"));

        User user1 = new User();
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Username already exists"));
    }

    /**
     * Method under test: {@link AuthController#registerUser(User)}
     */
    @Test
    void testRegisterUser3() throws Exception {
        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.passwordEncoder.encode((CharSequence) any())).thenThrow(new BadCredentialsException("?"));

        User user1 = new User();
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Unable to register user"));
    }

    /**
     * Method under test: {@link AuthController#resetPassword(java.util.Map)}
     */
    @Test
    void testResetPassword() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reset-password");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AuthController#verifyUsername(String)}
     */
    @Test
    void testVerifyUsername() throws Exception {
        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(this.userRepository.findByUsername((String) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/verify-username/{username}", "janedoe");
        MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Username exists"));
    }

    /**
     * Method under test: {@link AuthController#verifyUsername(String)}
     */
    @Test
    void testVerifyUsername2() throws Exception {
        when(this.userRepository.findByUsername((String) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/verify-username/{username}", "janedoe");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Username not found"));
    }

    /**
     * Method under test: {@link AuthController#verifyUsername(String)}
     */
    @Test
    void testVerifyUsername3() throws Exception {
        User user = new User();
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(this.userRepository.findByUsername((String) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/verify-username/{username}", "janedoe");
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.authController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Username exists"));
    }
}

