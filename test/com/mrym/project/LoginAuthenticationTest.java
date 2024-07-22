package com.mrym.project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoginAuthenticationTest {
    private LoginDetails loginDetails;
    private LoginAuthentication loginAuthentication;
    @BeforeEach
    void setUp() {
        loginDetails = new LoginDetails(10003, "melvingordon","uYWE732g4ga1");
    }


    @Test
    void SuccessfulLogin() throws Exception {
    }

}
