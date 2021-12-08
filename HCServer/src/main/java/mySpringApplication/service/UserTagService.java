package mySpringApplication.service;

import mySpringApplication.model.User;
import mySpringApplication.model.UserTag;
import mySpringApplication.model.UserTagId;
import mySpringApplication.repository.UserRepository;
import mySpringApplication.repository.UserTagRepository;
import mySpringApplication.security.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserTagService {
    @Autowired
    private UserTagRepository userTagRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * adds a userTag
     * @param username username of user
     * @param tag tag being added
     * @return false if tag already exists
     */
    public boolean addUserTag(String username, String tag){
        if(!userTagRepository.existsById(new UserTagId(username,tag))){
            userTagRepository.save(new UserTag(username,tag));
            return true;
        }else{
            return false;
        }

    }

    /**
     * gets a list of all the tags belonging to a user
     * @param username the username of the user
     * @return a list of tags for the user
     */
    public List<UserTag> getUserTags(String username){
        //TODO this could probably be less demanding on the database (it uses findAll)
        List<UserTag> userTags = userTagRepository.findAll();
        List<UserTag> thisUserTags = new ArrayList<UserTag>();
        for (UserTag userTag: userTags) {
            if(userTag.getUsername().equals(username)){
                thisUserTags.add(userTag);
            }
        }
        return thisUserTags;
    }

    /**
     * finds a userTag
     * @param username username of user
     * @param tag tag name of tag
     * @return userTag of user with username and tag of name tag. Returns null if userTag does not exist
     */
    public UserTag getUserTag(String username, String tag){
        if(userTagRepository.findById(new UserTagId(username,tag)).isPresent()){
            return userTagRepository.findById(new UserTagId(username,tag)).get();
        }else{
            return null;
        }
    }

    public void deleteUserTag(UserTag userTag) {
        userTagRepository.delete(userTag);
    }

}