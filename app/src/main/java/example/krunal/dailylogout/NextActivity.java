package example.krunal.dailylogout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.work.WorkManager;

public class NextActivity extends AppCompatActivity {

    private Button mLogout,CheckLoginStatus;

    String MyPREFERENCES = "LoginDetails";
    SharedPreferences mLoginSharedPreferencesFile;
    String LoginStatus = "LoginStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);


        setTitle("Next Activity");
        mLogout = findViewById(R.id.buttonLogout);
        CheckLoginStatus = findViewById(R.id.CheckLoginStatus);
        mLoginSharedPreferencesFile = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor = mLoginSharedPreferencesFile.edit();
            editor.putString(LoginStatus, "DEACTIVE");
            editor.apply();

            // Canceling PeriodicWorkRequest Here.
            WorkManager.getInstance().cancelUniqueWork("DailyTaskLogout");

            Intent intent = new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
            finish();
        });

        CheckLoginStatus.setOnClickListener(v -> {
            String getLoginStatus = mLoginSharedPreferencesFile.getString(LoginStatus,"null");

            Toast.makeText(getApplication(),getLoginStatus,Toast.LENGTH_SHORT).show();

        });
    }
}
