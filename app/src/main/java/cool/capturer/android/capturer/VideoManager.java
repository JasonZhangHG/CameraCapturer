package cool.capturer.android.capturer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import cool.capturer.android.capturer.capture.VideoCapture;
import cool.capturer.android.capturer.capture.VideoCaptureFactory;
import cool.capturer.android.capturer.constant.Constant;
import cool.capturer.android.capturer.render.VideoRender;


public class VideoManager {

    private static final String TAG = VideoManager.class.getSimpleName();
    private static volatile VideoManager mInstance;
    private Context mContext;

    private int mFacing = Constant.CAMERA_FACING_INVALID;

    private VideoCapture mVideoCapture;
    private VideoRender mVideoRender;

    private int mWidth;
    private int mHeight;
    private int mFrameRate;
    private boolean mNeedsPreview;

    private VideoManager(Context context) {
        mContext = context;
    }

    public static VideoManager createInstance(Context context) {
        if (mInstance == null) {
            synchronized (VideoManager.class) {
                if (mInstance == null) {
                    mInstance = new VideoManager(context);
                }
            }
        }
        return mInstance;
    }

    public boolean allocate(int width, int height, int frameRate, int facing) {
        if (facing == Constant.CAMERA_FACING_FRONT || facing == Constant.CAMERA_FACING_BACK) {
            mFacing = facing;
            mWidth = width;
            mHeight = height;
            mFrameRate = frameRate;
            if (mVideoCapture == null) {
                mVideoCapture = VideoCaptureFactory.createVideoCapture(mContext);
            }

            return mVideoCapture.allocate(width, height, frameRate, facing);
        } else {
            mFacing = Constant.CAMERA_FACING_INVALID;
            Log.e(TAG, "invalid camera id provided");
            return false;
        }
    }

    public void deallocate() {

        if (mVideoCapture != null) {
            mFacing = Constant.CAMERA_FACING_INVALID;
            mVideoCapture.deallocate();
            mVideoCapture = null;
        }

        if (mVideoRender != null) {
            mVideoRender.destroy();
            mVideoRender = null;
        }
    }

    public void startCapture() {
        if (mVideoCapture != null) {
            mVideoCapture.startCaptureMaybeAsync(mNeedsPreview);
        } else {
            Log.w(TAG, "camera not allocated or already deallocated");
        }
    }

    public void stopCapture() {
        if (mVideoCapture != null) {
            mVideoCapture.stopCaptureAndBlockUntilStopped();
        } else {
            Log.w(TAG, "camera not allocated or already deallocated");
        }

    }

    public void setRenderView(GLSurfaceView view) {
        if (view != null) {
            mNeedsPreview = true;
            if (mVideoRender == null) {
                mVideoRender = new VideoRender(mContext);
            }
            mVideoRender.setRenderView(view);
            mVideoCapture.getSrcConnector().connect(mVideoRender);
            mVideoRender.getTexConnector().connect(mVideoCapture);
        } else {
            mNeedsPreview = false;
            Log.w(TAG, "the render view provided is null");
        }
    }

    public void switchCamera() {
        switch (mFacing) {
            case Constant.CAMERA_FACING_INVALID:
                Log.e(TAG, "camera not allocated or already deallocated");
                break;
            case Constant.CAMERA_FACING_BACK:
                stopCapture();
                mVideoCapture.deallocate(false);
                allocate(mWidth, mHeight, mFrameRate, Constant.CAMERA_FACING_FRONT);
                startCapture();
                break;
            case Constant.CAMERA_FACING_FRONT:
                stopCapture();
                mVideoCapture.deallocate(false);
                allocate(mWidth, mHeight, mFrameRate, Constant.CAMERA_FACING_BACK);
                startCapture();
                break;
            default:
                Log.e(TAG, "no facing matched");
        }
    }
}
