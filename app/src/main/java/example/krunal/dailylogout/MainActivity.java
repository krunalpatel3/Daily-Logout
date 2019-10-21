package example.krunal.dailylogout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private Button mLogin,CheckLoginStatus;
    private EditText mUserId,mPassword;

    private SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";
    SharedPreferences mLoginSharedPreferencesFile;
    PeriodicWorkRequest periodicWorkRequest;


    String MyPREFERENCES = "LoginDetails";
    String LoginStatus = "LoginStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogin = findViewById(R.id.buttonLogin);

        mUserId = findViewById(R.id.editTextUserId);
        CheckLoginStatus = findViewById(R.id.LoginStatus);

        mPassword = findViewById(R.id.editTextPassword);
        mPreferences = getSharedPreferences(mPreferncesName, MODE_PRIVATE);

        // ------------------------ Save Login Status in LoginDetails file ----------------------//
        mLoginSharedPreferencesFile = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mLoginSharedPreferencesFile.edit();

        String getLoginStatus = mLoginSharedPreferencesFile.getString(LoginStatus,"null");

        if (getLoginStatus.equalsIgnoreCase("ACTIVE")){
            Intent intent = new Intent(this,NextActivity.class);
            startActivity(intent);
        }

        mLogin.setOnClickListener(v -> {

            String getUserId = mUserId.getText().toString().trim();
            String getPassword = mPassword.getText().toString().trim();

            if (!getUserId.matches("") || !getPassword.matches("")){

                // Saving value in MyPerfernces File.
                // Show that when PeriodicWork gets called when we click on login button.
                // the user do not gets logout while clicking on login button.
                SaveData("Frist");

                //Here we are Changing the LoginStatus in SharedPreferences file(LoginStatus).

                editor.putString(LoginStatus, "ACTIVE");
                editor.apply();
                editor.commit();


                // Setting up PeriodicWorker here.
                periodicWorkRequest = new PeriodicWorkRequest.Builder(DailyLogoutTask.class,
                        1, TimeUnit.DAYS)
                        .build();


                // For Setting up Unique PeriodicWork. So there is one PeriodicWork active at a time.
                // Remember there is Only one PeriodicWork at a time.
                WorkManager.getInstance().enqueueUniquePeriodicWork("DailyTaskLogout"
                        , ExistingPeriodicWorkPolicy.KEEP
                        , periodicWorkRequest);

                Intent intent = new Intent(getApplication(),NextActivity.class);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(getApplication(),"Enter User Id and Passeord!",Toast.LENGTH_LONG).show();
                mUserId.setText("");
                mPassword.setText("");
            }




        });


        CheckLoginStatus.setOnClickListener(v -> {

            String DisplayLoginStatus = mLoginSharedPreferencesFile.getString(LoginStatus,"null");

            Toast.makeText(getApplication(),DisplayLoginStatus,Toast.LENGTH_SHORT).show();
        });

    }



    private void SaveData(String str) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("Status1", str);
        preferencesEditor.apply();

    }
}
