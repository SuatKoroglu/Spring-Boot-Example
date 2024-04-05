package com.example.demo;

import com.example.demo.api.EncryptionService;
import com.example.demo.api.JWTService;
import com.example.demo.api.LoginBody;
import com.example.demo.api.RegistrationBody;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.user.LocalUser;
import com.example.demo.user.LocalUserRepository;
import com.example.demo.user.UserService;

import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

//    @InjectMocks
//    UserService userService;

    @InjectMocks
    private UserService underTest;


    @Mock
    LocalUserRepository localUserRepository;

    @Mock
    JWTService jwtService;

    @Mock
    EncryptionService encryptionService;


    @BeforeEach
    void setUp() {
//        RegistrationBody body = new RegistrationBody();
//        body.setUsername("UserA");
//        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
//        body.setFirstname("FirstName");
//        body.setLastname("LastName");
//        body.setPassword("MySecretPassword123");
    }

    @Test
    void testRegisterUserAlreadyExistsScenarios() throws UserAlreadyExistsException {
        // Arrange (use data provider or separate methods for better organization)
        RegistrationBody existingUsernameBody = new RegistrationBody("existingUsername", "anyEmail@example.com", "firstName", "lastName", "password");
        RegistrationBody existingEmailBody = new RegistrationBody("newUsername", "existingEmail@example.com", "firstName", "lastName", "password");


        // Username already exists scenario
        when(localUserRepository.findByUsernameIgnoreCase(existingUsernameBody.getUsername())).thenReturn(Optional.of(new LocalUser()));
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> underTest.registerUser(existingUsernameBody), "Username should already be in use.");

        // Email already exists scenario (reset mocks)
        Mockito.reset(localUserRepository); // Clear previous interactions
        when(localUserRepository.findByEmailIgnoreCase(existingEmailBody.getEmail())).thenReturn(Optional.of(new LocalUser()));
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> underTest.registerUser(existingEmailBody), "Email should already be in use.");

    }

    @Test
    public void testRegisterUserSuccess() throws Exception {

        // Create test data
        RegistrationBody registrationBody = new RegistrationBody("UserA",
                "UserServiceTest$testRegisterUser@junit.com",
                "MySecretPassword123", "FirstName", "LastName");
        String encryptedPassword = "MySecretPassword123";

        LocalUser localUser = new LocalUser("UserA", "MySecretPassword123",
                "UserServiceTest$testRegisterUser@junit.com", "FirstName", "LastName");

        // Set up mocks
        when(encryptionService.encryptPassword(anyString())).thenReturn(encryptedPassword);
        when(localUserRepository.save(any(LocalUser.class))).thenReturn(localUser);



        // Create the user service with mocks
        UserService userService = new UserService(localUserRepository, encryptionService, jwtService);

        // Execute the method
        LocalUser savedUser = userService.registerUser(registrationBody);

        // Verify outcomes
        Assertions.assertNotNull(savedUser);
        assertEquals(registrationBody.getUsername(), savedUser.getUsername());
        assertEquals(encryptedPassword, savedUser.getPassword());
        assertEquals(registrationBody.getEmail(), savedUser.getEmail());
        assertEquals(registrationBody.getFirstname(), savedUser.getFirstName());
        assertEquals(registrationBody.getLastname(), savedUser.getLastName());

        assertThat(savedUser).isEqualTo(localUser);

        // Ensure methods were called with correct arguments
        verify(encryptionService).encryptPassword(registrationBody.getPassword());
        verify(localUserRepository).save(any());
    }


    @Test
    public void testLoginUserSuccessfulLogin() {
        // Set up mocks
        LoginBody loginBody = new LoginBody("testUsername", "asdasd1");
        String expectedJwt = "generatedJwtToken";
        LocalUser user = new LocalUser("testUsername", "$2a$10$i/Tq1MlvJ4NKNWiq3wXqjuEx6yUoimotj2o0HGMk5CQucZu0OQ9ee",
                "asd@gmail.com","mahmut","mahmudow"); // Assuming password is already encrypted

        when(localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername())).thenReturn(java.util.Optional.of(user));
        when(encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateJWT(user)).thenReturn(expectedJwt);

        // Execute the login method
        String actualJwt = underTest.loginUser(loginBody);

        // Verify results
        assertEquals(expectedJwt, actualJwt);
    }


}
