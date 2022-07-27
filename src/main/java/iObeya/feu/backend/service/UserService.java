package iObeya.feu.backend.service;

import iObeya.feu.backend.model.User;
import org.springframework.stereotype.Service;

public interface UserService {

    Iterable<User> getUsers();


}
