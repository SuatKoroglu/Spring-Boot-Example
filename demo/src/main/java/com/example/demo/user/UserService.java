package com.example.demo.user;

import com.example.demo.api.EncryptionService;
import com.example.demo.api.JWTService;
import com.example.demo.api.LoginBody;
import com.example.demo.api.RegistrationBody;
import com.example.demo.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


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


    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException{

        if (localUserRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()
                || localUserRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUsername());

        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstname());
        user.setLastName(registrationBody.getLastname());
        return localUserRepository.save(user);
    }

    public String loginUser(LoginBody loginBody){
        LocalUser user = localUserRepository.findByUsernameIgnoreCase(loginBody.getUsername()).orElseThrow(null);
        if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())) {
            return jwtService.generateJWT(user);
        }
        return null;
    }

    @Transactional
    public LocalUser updateUser(Long id, LocalUser localUser) {

        LocalUser user = localUserRepository.findById(id).orElseThrow(() -> new NoSuchElementException("user with id " + id + " does not exist"));

        Optional.ofNullable(localUser.getUsername())
                .filter(username -> !username.isEmpty())
                .filter(username -> !Objects.equals(user.getUsername(), username))
                .ifPresent(user::setUsername);

        Optional.ofNullable(localUser.getPassword())
                .filter(password -> !password.isEmpty())
                .filter(password -> !Objects.equals(user.getPassword(), password))
                .ifPresent(user::setPassword);

        Optional.ofNullable(localUser.getEmail())
                .filter(email -> !email.isEmpty())
                .filter(email -> !Objects.equals(user.getEmail(), email))
                .ifPresent(user::setEmail);

        Optional.ofNullable(localUser.getFirstName())
                .filter(firstName -> !firstName.isEmpty())
                .filter(firstName -> !Objects.equals(user.getFirstName(), firstName))
                .ifPresent(user::setFirstName);

        Optional.ofNullable(localUser.getLastName())
                .filter(lastName -> !lastName.isEmpty())
                .filter(lastName -> !Objects.equals(user.getLastName(), lastName))
                .ifPresent(user::setLastName);

        return user;
    }
}
