package com.creeps.hkthn.core_services.utils.firebase_services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by rohan on 7/10/17.
 */

public class FirebaseMessageService extends FirebaseMessagingService {
    private final static String TAG="firebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0){
            System.out.println(TAG+ "msg body "+remoteMessage.getNotification().getBody().toString());
            System.out.println(TAG+ "msg from"+remoteMessage.getFrom());
            System.out.println(TAG+ "msg "+remoteMessage.getMessageType());
        }
    }
}
