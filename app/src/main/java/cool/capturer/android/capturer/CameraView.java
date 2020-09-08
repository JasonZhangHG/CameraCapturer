package cool.capturer.android.capturer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import cool.capturer.android.capturer.constant.Constant;

public class CameraView extends GLSurfaceView {

    private VideoManager mVideoManager;
    private boolean mIsFrontCamera = true;

    public CameraView(Context context) {
        super(context);
        init(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        mVideoManager = VideoManager.createInstance(context);
        mVideoManager.allocate(1920, 1080, 30, mIsFrontCamera ? Constant.CAMERA_FACING_FRONT : Constant.CAMERA_FACING_BACK);
        mVideoManager.setRenderView(this);
    }

    public void onResume() {
        if (mVideoManager != null) {
            mVideoManager.startCapture();
        }
    }

    public void onPause() {
        if (mVideoManager != null) {
            mVideoManager.stopCapture();
        }
    }

    public void switchCamera() {
        if (mVideoManager != null) {
            mVideoManager.switchCamera();
        }
    }
}
