package Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.shahbaapp.lft.R;

public class NotifyMe {


    public static MyNotify with(Context context, String MY_CHANNAL_ID, int NOTIFICATION_ID) {
        return new MyNotify(context, MY_CHANNAL_ID, NOTIFICATION_ID);
    }

    public static class MyNotify {
        private Context mContext;
        private String MY_CHANNAL_ID;
        private int NOTIFICATION_ID;
        private Uri mSound;
        private int mIcon;
        private long[] mVibrationPattern;
        private String mMessage, mTitle;
        private boolean mAutoCancel;
        private PendingIntent mPendingIntent;
        private int mProgressMax;
        private int mProgressMin;
        private boolean mLockscreenVisibility;
        private NotificationManager mNotificationManager;
        private NotificationCompat.Builder mNotification;

        public MyNotify(Context context, String MY_CHANNAL_ID, int NOTIFICATION_ID) {
            mContext = context;
            this.MY_CHANNAL_ID = MY_CHANNAL_ID;
            this.NOTIFICATION_ID = NOTIFICATION_ID;
            init();
        }


        private void init() {
            mSound = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.bey);
            mIcon = mContext.getApplicationInfo().icon;
            mVibrationPattern = new long[]{0, 500, 200, 900};
            mMessage = "My Message!";
            mTitle = "My Title!";
            mAutoCancel = true;
            mLockscreenVisibility = true;
        }

        public MyNotify sound(Uri sound) {
            if (sound != null)
                mSound = sound;
            return this;
        }

        public MyNotify icon(int icon) {
            if (icon != 0)
                mIcon = icon;
            return this;
        }

        public MyNotify vibrationPattern(long[] vibrationPattern) {
            if (vibrationPattern != null)
                mVibrationPattern = vibrationPattern;
            return this;
        }

        public MyNotify message(String message) {
            if (message != null)
                mMessage = message;
            return this;
        }

        public MyNotify title(String title) {
            if (title != null)
                mTitle = title;
            return this;
        }

        public MyNotify autoCancel(boolean autoCancel) {
            mAutoCancel = autoCancel;
            return this;
        }

        public MyNotify pendingIntent(PendingIntent pendingIntent) {
            if (pendingIntent != null)
                mPendingIntent = pendingIntent;
            return this;
        }


        public MyNotify progress(int min, int max) {
            if (min <= max) {
                mProgressMin = min;
                mProgressMax = max;
            }
            return this;
        }


        public MyNotify lockscreenVisibility(boolean isVisibility) {
            mLockscreenVisibility = isVisibility;
            return this;
        }


        public MyNotify onNext(int value) {
            mMessage = value + "%";
            mProgressMin = value;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager.getNotificationChannel(MY_CHANNAL_ID).setImportance(NotificationManager.IMPORTANCE_LOW);
            }
            return this;
        }


        public MyNotify hideProgressbar(){
            mProgressMin = 0;
            mProgressMax = 0;
            return this;
        }


        public void show() {
            mNotification
                    .setAutoCancel(mAutoCancel)
                    .setSmallIcon(mIcon)
                    .setContentTitle(mTitle)
                    .setSound(mSound)
                    .setContentText(mMessage)
                    .setProgress(mProgressMax, mProgressMin, false);

            mNotificationManager.notify(NOTIFICATION_ID, mNotification.build());
        }


        public MyNotify build() {
            if (mProgressMax > 0) {
                mMessage = "0%";
                mPendingIntent = null;
            }

            mNotification = createNotificationBuilder();
            if (mPendingIntent != null)
                mNotification.setContentIntent(mPendingIntent);

            return this;
        }



        private NotificationCompat.Builder createNotificationBuilder() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                if (mSound == null)
                    audioAttributes = null;

                String channelName = mContext.getString(R.string.app_name);
                NotificationChannel notificationChannel = new NotificationChannel(MY_CHANNAL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setLightColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                if (mLockscreenVisibility) {
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                } else {
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                }

                notificationChannel.setSound(mSound, audioAttributes);
                notificationChannel.setVibrationPattern(mVibrationPattern);
                mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
            }
            return new NotificationCompat.Builder(mContext, MY_CHANNAL_ID);
        }


    }
}
