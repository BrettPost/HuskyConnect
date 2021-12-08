package mySpringApplication.service;

import mySpringApplication.model.Connection;
import mySpringApplication.model.User;
import mySpringApplication.model.UserTag;
import mySpringApplication.model.UserTagId;
import mySpringApplication.repository.ConnectionRepository;
import mySpringApplication.repository.UserRepository;
import mySpringApplication.repository.UserTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * adds a connection request from user1 to user2.
     * If a request already exists from user2, then the request will be accepted
     * @param user1 requester
     * @param user2 request receiver
     * @return bool if the request was successful
     */
    /*
    public boolean addConnection(String user1, String user2){
        //TODO
        if(!userTagRepository.existsById(new UserTagId(username,tag))){
            userTagRepository.save(new UserTag(username,tag));
            return true;
        }else{
            return false;
        }

    }*/

    /**
     * lists all the connections for this user (not pending connections)
     * @param username username of user
     * @return a list of users that are connections to this user
     */
    public List<User> getConnections(String username){

        List<User> thisUserConnectionsUsers = new ArrayList<>();
        for (Connection connection: connectionRepository.findAll()) {
            if(connection.getUser1().equals(username) && connection.getAccepted()){
                //is present not required because connection.user is a foreign key for user.username
                thisUserConnectionsUsers.add(userRepository.findById(connection.getUser2()).get());
            }else if(connection.getUser2().equals(username) && connection.getAccepted()){
                //is present not required because connection.user is a foreign key for user.username
                thisUserConnectionsUsers.add(userRepository.findById(connection.getUser1()).get());
            }
        }
        return thisUserConnectionsUsers;
    }

    /**
     * lists all the connections for this user (not pending connections)
     * @param username username of user
     * @return a list of users that are connections to this user
     */
    public List<User> getConnectionRequests(String username){

        List<User> thisUserConnectionsUsers = new ArrayList<>();
        for (Connection connection: connectionRepository.findAll()) {
            if(connection.getUser2().equals(username) && !connection.getAccepted()){
                //is present not required because connection.user is a foreign key for user.username
                thisUserConnectionsUsers.add(userRepository.findById(connection.getUser1()).get());
            }
        }
        return thisUserConnectionsUsers;
    }

    /**
     * adds a connection request from user1 to user2.
     * If user2 has already requested user1, then the request from user2 will be accepted
     * @param user1 user requesting
     * @param user2 user being requested
     * @return false if request already exists, true otherwise (and true if user2 has already requested user1)
     */
    public boolean addConnectionRequest(String user1, String user2){
        for (Connection connection: connectionRepository.findAll()) {
            if(connection.getUser1().equals(user1) && connection.getUser2().equals(user2)){
                //request already exists
                return false;
            }else if(connection.getUser2().equals(user1) && connection.getUser1().equals(user2) && !connection.getAccepted()){
                //request from user2 already exists
                connection.setAccepted(true);
                return true;
            }
        }
        //add the connection request
        connectionRepository.save(new Connection(user1,user2,false));
        return true;
    }

}