package mySpringApplication.controller;


import mySpringApplication.model.User;
import mySpringApplication.model.UserTag;
import mySpringApplication.security.Authenticator;
import mySpringApplication.service.UserService;
import mySpringApplication.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/tags")
public class UserTagController {
    @Autowired
    UserTagService userTagService;

    @Autowired
    UserService userService;

    /**
     * gets the tags of the user
     * @param username username of user that is being gotten
     * @param tokenId tokenId that is required to authenticated being logged in
     * @return a comma separated list of the tags belonging to this user
     */
    @GetMapping("/{username}")
    public ResponseEntity<String> get(@PathVariable String username, @RequestParam String tokenId) {
        //Authentication
        if(Authenticator.getAccess( Long.parseLong(tokenId) ) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            //makes a comma separated list of tags in this user
            StringBuilder tags = new StringBuilder();
            for (UserTag userTag: userTagService.getUserTags(username)) {
                tags.append(userTag.getTag()).append(",");
            }
            tags.deleteCharAt(tags.length() - 1);//removes the last ,

            return new ResponseEntity<>(tags.toString(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * adds a new tag for a user to the database
     * @param tag new tag being added
     * @param tokenId for the user a tag is being added to
     */
    @PostMapping("")
    public ResponseEntity<?> add(@RequestParam String tag,@RequestParam String tokenId) {

        //Authentication
        if(Authenticator.getAccess( Long.parseLong(tokenId) ) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = Authenticator.getAccess( Long.parseLong(tokenId) );
        assert user != null;
        //returns conflict if this tag already exists
        if(userTagService.addUserTag(user.getUsername(),tag)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    /**
     * Deletes UserTag from database
     * @param tokenId token for user
     * @param tag tag on user to be removed
     * @return http status of not_found if userTag doesn't exist
     */
    @DeleteMapping("")
    public ResponseEntity<?> delete(@RequestParam String tag,@RequestParam String tokenId) {
        //Authentication
        if(Authenticator.getAccess( Long.parseLong(tokenId) ) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = Authenticator.getAccess( Long.parseLong(tokenId) );
        assert user != null;
        UserTag userTag = userTagService.getUserTag(user.getUsername(),tag);
        //returns not_found if userTag doesn't exist
        if(userTag != null){
            userTagService.deleteUserTag(userTag);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}