package mySpringApplication.security;

import mySpringApplication.model.User;

import java.util.Objects;
import java.util.Random;

public class Token {
    private final long tokenId;
    private final User user;
    private long timeLastUsed;

    private final long EXPIRE_TIME = 900000; //15 minutes

    public Token(User user) {
        this.user = user;
        tokenId = new Random().nextLong();
        timeLastUsed = System.currentTimeMillis();
    }

    /**
     * attempts gets access to the user of this token and updates the time last used
     *
     * @return user of token if access is granted, null if it has expired
     */
    public User getAccess(){
        if(timeLastUsed + EXPIRE_TIME > System.currentTimeMillis()){
            timeLastUsed = System.currentTimeMillis();
            return user;
        }else {
            return null;
        }

    }

    public long getTokenId(){
        return tokenId;
    }

    /**
     * checks if the token has expired without updating time last used
     * @return true if the token is expired
     */
    public boolean hasExpired(){
        return timeLastUsed + EXPIRE_TIME < System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return tokenId == token.tokenId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId);
    }
}
