package edu.eci.labinfo.bookinglab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.labinfo.bookinglab.data.UserRepository;
import edu.eci.labinfo.bookinglab.model.UserEntity;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity addUser(UserEntity user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public UserEntity getUserByUserName(String username) {
        Optional<UserEntity> optinalUser = userRepository.findByUserName(username);
        if (optinalUser.isPresent()) {
            return optinalUser.get();
        }
        return null;
    }

    public UserEntity getUserByFullName(String fullName) {
        Optional<UserEntity> optinalUser = userRepository.findByFullName(fullName);
        if (optinalUser.isPresent()) {
            return optinalUser.get();
        }
        return null;
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity updateUser(UserEntity user) {
        if (userRepository.existsById(user.getUserId())) {
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String userName) {
        userRepository.delete(getUserByUserName(userName));
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}