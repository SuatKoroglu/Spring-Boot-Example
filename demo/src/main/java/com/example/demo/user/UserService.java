package com.example.demo.user;

import com.example.demo.api.EncryptionService;
import com.example.demo.api.JWTService;
import com.example.demo.api.LoginBody;
import com.example.demo.api.RegistrationBody;
import com.example.demo.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;


@Service
public class UserService{

    private final LocalUserRepository localUserRepository;

    private final EncryptionService encryptionService;

    private final JWTService jwtService;

    public UserService(LocalUserRepository localUserRepository, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserRepository = localUserRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }


    public void registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException{

        if (localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
                || localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUsername());

        user.setPassword(encryptionService.encryptPasswpord(registrationBody.getPassword()));

        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstname());
        user.setLastName(registrationBody.getLastname());
        localUserRepository.save(user);
    }

    public String loginUser(LoginBody loginBody){
        LocalUser user = localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername()).orElseThrow(null);
        if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())) {
            return jwtService.generateJWT(user);
        }
        return null;
    }
}
