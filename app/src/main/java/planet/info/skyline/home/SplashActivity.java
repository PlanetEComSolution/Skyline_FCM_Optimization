package planet.info.skyline.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import planet.info.skyline.R;
import planet.info.skyline.client.ClientHomeActivity;
import planet.info.skyline.shared_preference.Shared_Preference;
import planet.info.skyline.util.Utility;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("skyline", getApplicationContext().MODE_PRIVATE);



        Utility.setAppLaunchedFirstTime(SplashActivity.this);
        boolean isUserLogin = Shared_Preference.isLogin(SplashActivity.this);


        if (!isUserLogin) {  // not login
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

        } else {

            String LOGIN_TYPE = Shared_Preference.getLoginType(SplashActivity.this);
            if (LOGIN_TYPE.equals(Shared_Preference.LOGIN_TYPE_CLIENT)) {
                startActivity(new Intent(SplashActivity.this, ClientHomeActivity.class));

            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }

        }
        finish();


    }
}
