package mx.itesm.alertify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.webkit.ConsoleMessage;

import java.io.Console;

public class SplashScreenActiv extends AppCompatActivity {

    Handler handler;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mPreferences.getBoolean("loginsuccesful", false))
                {
                    Log.i("Debug", "I got here");
                    Intent intent = new Intent(SplashScreenActiv.this, InicioActiv.class);
                    startActivity(intent);
                }

                else
                {
                    Log.i("Debug", "I got here2");
                    mEditor.putBoolean("firstrun", false).commit();
                    Intent intent = new Intent(SplashScreenActiv.this, LoginActiv.class);
                    startActivity(intent);
                }

                finish();

            }
        },2000);

    }
}
