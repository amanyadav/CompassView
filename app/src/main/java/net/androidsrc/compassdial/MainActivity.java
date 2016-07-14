package net.androidsrc.compassdial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.androidsrc.darkcompass.CompassView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    CompassView compassView;
    Random RAND = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compassView=(CompassView)findViewById(R.id.compassView);
        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        int angle = 0;

        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        angle=RAND.nextInt(360);
                        compassView.setAngle(angle);
                        Log.d(TAG, "new angle is " + angle);
                    }
                });
            }

        }
    }


}
