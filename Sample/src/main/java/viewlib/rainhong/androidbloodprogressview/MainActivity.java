package viewlib.rainhong.androidbloodprogressview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import viewlib.rainhong.bloodprogressviewlib.BloodProgressView;


public class MainActivity extends AppCompatActivity {
    private final AnimationHandle Animationhandle = new AnimationHandle(this);
    private BloodProgressView DemoBloodProgressView1;
    private BloodProgressView DemoBloodProgressView2;
    private BloodProgressView DemoBloodProgressView3;
    private BloodProgressView DemoBloodProgressView4;
    private TextView DemoBloodTextView1;
    private TextView DemoBloodTextView2;
    private TextView DemoBloodTextView3;
    private TextView DemoBloodTextView4;
    private TextView SeekBarTextView;
    private SeekBar DemoSeekBar;

    private int DemoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DemoValue = 0;

        DemoBloodProgressView1 = (BloodProgressView) findViewById(R.id.DemoBloodView1);
        DemoBloodProgressView2 = (BloodProgressView) findViewById(R.id.DemoBloodView2);
        DemoBloodProgressView3 = (BloodProgressView) findViewById(R.id.DemoBloodView3);
        DemoBloodProgressView4 = (BloodProgressView) findViewById(R.id.DemoBloodView4);

        // Enable Color Animation.
        DemoBloodProgressView2.setColorToBlood(true);
        DemoBloodProgressView4.setColorToBlood(true);
        DemoBloodProgressView4.setDefaultAnimationTime(1500);

        // Demo Color Setting.
        DemoBloodProgressView1.setProgressIndicatorColor(Color.argb(255, 0, 0, 255));
        DemoBloodProgressView1.setProgressColor(Color.BLUE);
        DemoBloodProgressView1.setProgressColor_alpha(100);
        DemoBloodProgressView3.setProgressIndicatorColor(Color.argb(255, 0, 255, 0));
        DemoBloodProgressView3.setProgressColor(Color.RED);
        DemoBloodProgressView3.setProgressXYAxisLabelLineColor(Color.BLACK);
        DemoBloodProgressView3.setProgressXYAxisLabelLineColor_alpha(100);
        DemoBloodProgressView4.setProgressIndicatorColor(Color.RED);

        //init value.
        DemoBloodProgressView1.setPercentValue(0, false);
        DemoBloodProgressView2.setPercentValue(100, false);
        DemoBloodProgressView3.setPercentValue(0, false);
        DemoBloodProgressView4.setPercentValue(100, false);

        DemoBloodTextView1 = (TextView) findViewById(R.id.DemoBloodTextView1);
        DemoBloodTextView2 = (TextView) findViewById(R.id.DemoBloodTextView2);
        DemoBloodTextView3 = (TextView) findViewById(R.id.DemoBloodTextView3);
        DemoBloodTextView4 = (TextView) findViewById(R.id.DemoBloodTextView4);

        DemoBloodTextView1.setText(getString(R.string.DemoValueLabel, DemoBloodProgressView1.getPercentValue()));
        DemoBloodTextView4.setText(getString(R.string.DemoValueLabel, DemoBloodProgressView4.getPercentValue()));

        SeekBarTextView = (TextView) findViewById(R.id.SeekBarTextView);
        SeekBarTextView.setText(getString(R.string.DemoValueLabel, 0));
        DemoSeekBar = (SeekBar) findViewById(R.id.DemoSeekBar);
        DemoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DemoBloodProgressView1.setPercentValue(progress, false);
                DemoBloodProgressView4.setPercentValue(100 - progress, true);
                DemoBloodTextView1.setText(getString(R.string.DemoValueLabel, DemoBloodProgressView1.getPercentValue()));
                DemoBloodTextView4.setText(getString(R.string.DemoValueLabel, DemoBloodProgressView4.getPercentValue()));
                SeekBarTextView.setText(getString(R.string.DemoValueLabel, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(DemoRun).start();
    }

    private Runnable DemoRun = new Runnable() {
        boolean Run = true;
        @Override
        public void run() {
            while(Run) {
                try {
                    DemoValue++;
                    if(DemoValue > 100)
                        DemoValue = 0;
                    Animationhandle.sendMessage(new Message());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static class AnimationHandle extends Handler {
        private final WeakReference<MainActivity> mActivity;
        public AnimationHandle(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.DemoBloodProgressView2.setPercentValue(activity.DemoValue, false);
                activity.DemoBloodProgressView3.setPercentValue(100 - activity.DemoValue, true);
                activity.DemoBloodTextView2.setText(activity.getString(R.string.DemoValueLabel, activity.DemoBloodProgressView2.getPercentValue()));
                activity.DemoBloodTextView3.setText(activity.getString(R.string.DemoValueLabel, activity.DemoBloodProgressView3.getPercentValue()));
            }
        }
    }
}
