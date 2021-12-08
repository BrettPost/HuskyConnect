package mySpringApplication.controller;


import mySpringApplication.model.Connection;
import mySpringApplication.model.User;
import mySpringApplication.model.UserTag;
import mySpringApplication.security.Authenticator;
import mySpringApplication.service.ConnectionService;
import mySpringApplication.service.UserService;
import mySpringApplication.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/connections")
public class ConnectionController {
    @Autowired
    ConnectionService connectionService;

    @Autowired
    UserService userService;

    /**
     * gets a list of users that are friends of the user
     * @param username username of user that is being gotten
     * @param tokenId tokenId that is required to authenticated being logged in
     * @return a list of users that are friends
     */
    @GetMapping("/{username}")
    public ResponseEntity<List<User>> get(@PathVariable String username, @RequestParam String tokenId) {
        //Authentication
        if(Authenticator.getAccess( Long.parseLong(tokenId) ) == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {

            return new ResponseEntity<>(connectionService.getConnections(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * gets a list of users that are requesting to be friends of the user
     * @param tokenId token of the user
     * @return a list of users that are requesting to be friends
     */
    @GetMapping("/requests")
    public ResponseEntity<List<User>> get(@RequestParam String tokenId) {
        //Authentication
        User user = Authenticator.getAccess( Long.parseLong(tokenId) );
        if(user == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {

            return new ResponseEntity<>(connectionService.getConnectionRequests(user.getUsername()), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * adds a request to be friends with a user
     * @param username of user that is being friended
     * @param tokenId for the user that is friending
     */

    @PostMapping("")
    public ResponseEntity<?> add(@RequestParam String username, @RequestParam String tokenId) {

        //Authentication
        User user = Authenticator.getAccess( Long.parseLong(tokenId) );
        if(user == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //returns conflict if this request already exists
        if(connectionService.addConnectionRequest(user.getUsername(),username)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


}