package mySpringApplication.security;

import mySpringApplication.model.User;

import java.util.*;

public class Authenticator {
    static HashMap<Long, Token> tokens = new HashMap<>();

    /**
     * makes a new token for the user and adds it to the list of available tokens
     *
     * also cleans out expired tokens
     *
     * @param user the user assigned to this token
     * @return the token id
     */
    public static long makeToken(User user){

        //clean out expired tokens. This doesn't necessarily need to be done here, but it is a convenient place to do it.
        tokenCleanUp();

        Token t = new Token(user);
        tokens.put(t.getTokenId(),t);
        return t.getTokenId();
    }

    /**
     * attempts to get access to the user that belongs to the token of tokenId
     * @param tokenId the id for the token
     * @return the user belonging to the token of tokenId or null if token doesn't exist or if token has expired
     */
    public static User getAccess(long tokenId){
        if(tokens.get(tokenId) != null){
            if(tokens.get(tokenId).getAccess() != null){
                return tokens.get(tokenId).getAccess();
            }
        }
        return null;
    }

    /**
     * removes all expired tokens
     */
    public static void tokenCleanUp(){
        tokens.entrySet().removeIf(entry -> entry.getValue().hasExpired());
    }
}
