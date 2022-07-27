package iObeya.feu.backend.service;

import iObeya.feu.backend.model.User;
import iObeya.feu.backend.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class UserServiceImp implements UserService{


    @Autowired
    UserRepository userRepository ;


    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }
}
