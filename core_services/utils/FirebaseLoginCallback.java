package com.creeps.hkthn.core_services.utils;

/**
 * Created by rohan on 6/10/17.
 * To be implemented by the class thats going to log in a particular user and wants results
 */

public interface FirebaseLoginCallback {
    public void call(boolean isLoggedIn);
}
