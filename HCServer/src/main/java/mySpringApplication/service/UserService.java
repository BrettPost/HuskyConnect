package mySpringApplication.service;

import mySpringApplication.model.User;
import mySpringApplication.repository.UserRepository;
import mySpringApplication.security.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * attempts to authenticate the user and make a token for the login session
     * @param username the username of the user
     * @param password the password of the user
     * @return the token key
     */
    public long login(String username, String password) throws Exception{
        if(userRepository.findById(username).isPresent()){
            if(userRepository.findById(username).get().passwordEquals(password)){
                return Authenticator.makeToken(userRepository.findById(username).get());
            }else{
                throw new Exception("Login failed");
            }
        }else{
            throw new Exception("Login failed");
        }


    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public User getUser(String username) {
        if(userRepository.findById(username).isPresent()){
            return userRepository.findById(username).get();
        }else{
            return null;
        }

    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}