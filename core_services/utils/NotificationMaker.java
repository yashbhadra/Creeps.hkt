package com.creeps.hkthn.core_services.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.creeps.hkthn.MainActivity;
import com.creeps.hkthn.R;

/**
 * Created by rohan on 7/10/17.
 */

public class NotificationMaker {
    private NotificationManager mNotificationMgr;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteViews;
    private int id;
    private Context mContext;
    public NotificationMaker(Context ct){
        this.mContext=ct;
        this.initNotiBuilder();
    }
    public NotificationMaker(Context cOntext,int notificationId){
        this(cOntext);
        this.id= notificationId;
    }


    private void initNotiBuilder(){
        this.id=(int) System.currentTimeMillis();
        this.mNotificationMgr=(NotificationManager)(mContext.getSystemService(Context.NOTIFICATION_SERVICE));
        this.mRemoteViews=new RemoteViews(this.mContext.getPackageName(), R.layout.not_layout);



        this.mBuilder=new NotificationCompat.Builder(this.mContext);
        //setting pendingintent to MainActivity... change it later / pass some data to the bundle
        PendingIntent pd=PendingIntent.getActivity(this.mContext, 123,new Intent(this.mContext, MainActivity.class),0);

        this.mBuilder.setCustomBigContentView(this.mRemoteViews);
        this.mBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        this.mBuilder.setContentIntent(pd);
        this.mBuilder.setOngoing(false);
    }


    public void makeNotification() {
        this.mNotificationMgr.notify(this.id,this.mBuilder.build());
    }

    public void dismissNotification(){
        this.mNotificationMgr.cancel(this.id);
    }




    public void setTitleText(String x){
        this.mRemoteViews.setTextViewText(R.id.noti_title,x);
    }
    public void setContentText(String a){
        this.mRemoteViews.setTextViewText(R.id.noti_content,a);
    }
}
