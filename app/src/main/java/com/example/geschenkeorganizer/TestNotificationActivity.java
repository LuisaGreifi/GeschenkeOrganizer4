package com.example.geschenkeorganizer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TestNotificationActivity extends AppCompatActivity {

    private Button button;
    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_NAME = "CH0";
    private final static String NOTIFICATION_CHANNEL_ID = "0";

    public static final int INTENT_ITEM_SELECTED_ID = 0;
    public static final String INTENT_ITEM_SELECTED_NAME = "IntentForLoadingSelectedNote";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: hier mit DB verbinden
                createNotification("TestTitel", "TestText");
            }
        });

    }

    //todo: überall statt TestNotificationActivity.this bei Fragment: getActivity()

    private void createNotification(String title, String text) {
        createNotificationChannel();
        Notification.Builder mBuilder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            mBuilder =
                    new Notification.Builder(TestNotificationActivity.this, NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name)).setContentText(title)
                            .setStyle(new Notification.BigTextStyle().bigText(text)).setAutoCancel(true);
        }
        else {
            mBuilder =
                    new Notification.Builder(TestNotificationActivity.this).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name)).setContentText(title)
                            .setStyle(new Notification.BigTextStyle().bigText(text)).setAutoCancel(true);
        }

        // Check if we're running on Android 5.0 or higher, older versions don't support visibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            mBuilder.setVisibility(Notification.VISIBILITY_SECRET);
        }
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(TestNotificationActivity.this, MainActivity.class);
        resultIntent
                .putExtra(INTENT_ITEM_SELECTED_NAME, INTENT_ITEM_SELECTED_ID);
        resultIntent.putExtra(INTENT_ITEM_SELECTED_NAME, INTENT_ITEM_SELECTED_ID);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to
        // the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(TestNotificationActivity.this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) TestNotificationActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_LOW;
            CharSequence channelName = NOTIFICATION_CHANNEL_NAME;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            NotificationManager notificationManager = TestNotificationActivity.this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
