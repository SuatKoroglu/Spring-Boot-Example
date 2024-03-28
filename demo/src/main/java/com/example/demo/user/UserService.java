package com.example.demo.user;

import com.example.demo.api.RegistrationBody;
import com.example.demo.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    public LocalUserRepository localUserRepository;

    public UserService(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException{

        if (localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
                || localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUsername());

        //TODO: Encrypt password!!
        user.setPassword(registrationBody.getPassword());

        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstname());
        user.setLastName(registrationBody.getLastname());
        return localUserRepository.save(user);
    }
}
