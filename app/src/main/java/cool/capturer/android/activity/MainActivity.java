package cool.capturer.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import cool.capturer.android.R;
import cool.capturer.android.capturer.CameraView;

public class MainActivity extends AppCompatActivity {

    private CameraView mCameraView;
    private Button mSwitchCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCameraView = findViewById(R.id.cv_view);
        mSwitchCameraView = findViewById(R.id.btn_switch_camera);
        mSwitchCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView != null) {
                    mCameraView.switchCamera();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraView != null) {
            mCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraView != null) {
            mCameraView.onPause();
        }
    }
}