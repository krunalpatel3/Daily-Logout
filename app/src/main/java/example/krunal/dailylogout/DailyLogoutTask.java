package example.krunal.dailylogout;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.MODE_PRIVATE;

public class DailyLogoutTask extends Worker {

    private SharedPreferences mPreferences;
    private static final String mPreferncesName = "MyPerfernces";

    String MyPREFERENCES = "LoginDetails";
    private SharedPreferences mLoginSharedPreferencesFile;

    Context context;

    public DailyLogoutTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        mPreferences = context.getSharedPreferences(mPreferncesName, MODE_PRIVATE);
        mLoginSharedPreferencesFile = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        Log.e("DailyLogoutTask", "DailyLogoutTask call");
    }

    @NonNull
    @Override
    public Result doWork() {

        String getstatus = mPreferences.getString("Status1", "No Task Perform");
        Log.e("---getstatus---", "----getstatus---" + getstatus);
        Log.e("doWork", "doWork call");

        // If doWork method gets called on login button.
        // Then we are not changing the LoginStatus here.
        // But we are changing the status of Status1 in SharedPreferences show after 1 day the user gets
        // automatic logout.
        if (!getstatus.equalsIgnoreCase("Frist")) {

            String LoginStatus = "LoginStatus";
            String getLoginStatus = mLoginSharedPreferencesFile.getString(LoginStatus,"null");

            // Here we are changing the LoginStatus from LoginDetails file.
            // If user in LoginStatus is ACTIVE then we are changing the LoginStatus to DEACTIVE.
            if (!getLoginStatus.equalsIgnoreCase("DEACTIVE")){

                SharedPreferences.Editor editor = mLoginSharedPreferencesFile.edit();
                editor.putString(LoginStatus, "DEACTIVE");
                editor.apply();

                // Canceling PeriodicWorkRequest Here.
                WorkManager.getInstance().cancelUniqueWork("DailyTaskLogout");

            }


        } else {
            Log.e("chack", "Frist time");
            // If Frist time doWork gets called then change Status1 in SharedPreferences file to Second.
            SaveData("Second");
        }


        return Result.success();
    }



    private void SaveData(String str) {
        Log.e("SaveData", str);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("Status1", str);
        preferencesEditor.apply();

    }
}
