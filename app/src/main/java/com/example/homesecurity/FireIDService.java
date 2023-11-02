package com.example.homesecurity;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FireIDService extends FirebaseMessagingService {
        public void onTokenRefresh() {
            Task<String> tkn = FirebaseMessaging.getInstance().getToken();
            Log.d("Not","Token ["+tkn+"]");
        }

}
