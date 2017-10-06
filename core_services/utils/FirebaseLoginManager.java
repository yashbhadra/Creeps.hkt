package com.creeps.hkthn.core_services.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by rohan on 6/10/17.
 */

public class FirebaseLoginManager implements FirebaseLoginManagerConstants{
    private static FirebaseLoginManager mFirebaseLoginManager;/* for singleton*/
    private Context context;/* to prevent gc*/

    private SharedPreferenceManager sharedPreferenceManager;/* to store data about user*/
    public FirebaseLoginManager(Context context){
        this.context=context;
        this.sharedPreferenceManager=SharedPreferenceManager.getInstance(context);
    }
    public static FirebaseLoginManager prepareInstance(Context context){
        return FirebaseLoginManager.mFirebaseLoginManager !=null?FirebaseLoginManager.mFirebaseLoginManager:new FirebaseLoginManager(context);
    }
    public static FirebaseLoginManager getInstance(){

        return FirebaseLoginManager.mFirebaseLoginManager;

    }

    private void handleCallback(FirebaseLoginCallback firebaseLoginCallback,Task<AuthResult> task){
        if(firebaseLoginCallback!=null && task!=null)
            firebaseLoginCallback.call(task.isSuccessful());
        FirebaseLoginManager.this.sharedPreferenceManager.addBoolean(IS_USER_LOGGED_IN,task.isComplete());
    }
    /* passes in false only when the entered email already exist
    * NOTE a createUser call must be followed by a callToSignIn
    * @param email -email to be sent to serverr
    * @param password - password of user
    * @param firebaseLoginManager the callback which sends the result
    * @param signIn - to call signIn or createUser...
    * ...
    * */
    public void createUser(String email, String password, final FirebaseLoginCallback firebaseLoginManager,boolean signIn){
        Task firebaseTask = signIn?FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password):FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password);


                   firebaseTask .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseLoginManager.this.handleCallback(firebaseLoginManager,task);
            }
        });
        if(!signIn)
            createUser(email,password,firebaseLoginManager,true);
    }


    /* returns the email of the user whose currently logged into the system*/
    public String getEmail(){
        return (this.sharedPreferenceManager.getBoolean(IS_USER_LOGGED_IN))?(FirebaseAuth.getInstance().getCurrentUser().getEmail()):null;
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        /* todo set sharedprefs to false*/
        this.sharedPreferenceManager.addBoolean(IS_USER_LOGGED_IN,false);
    }
}
interface FirebaseLoginManagerConstants{
    public static final String IS_USER_LOGGED_IN="isFirstLogIn";
}
