package com.msc.ms.users.user.error;

public class AlreadyExistingUsernameException extends Exception{



    public AlreadyExistingUsernameException(String username){
        super("The username: " + username + " already exists in the system");
    }
}
