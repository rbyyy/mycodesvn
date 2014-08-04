package com.ssm.songshangmen.activity;


public class ActivityCallBridge {
	static ActivityCallBridge mBridge;

    private OnMethodCallback mCallback;

    private ActivityCallBridge() {

    }

    public static ActivityCallBridge getInstance() {
        if (mBridge == null) {
            mBridge = new ActivityCallBridge();
        }
        return mBridge;
    }

    public void invokeMethod(int menuPosition, int dishNumber, int dishPosition) {
        if (mCallback != null) {
            mCallback.doMethod(menuPosition, dishNumber, dishPosition);
        }
    }

    public void setOnMethodCallback(OnMethodCallback callback) {
        mCallback = callback;
    }

    public static interface OnMethodCallback {
        public void doMethod(int menuPosition, int dishNumber, int dishPosition);
    }
    
}
