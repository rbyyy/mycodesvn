package com.ssm.ssmbee;


public class BeeQiangCallBridge {
	static BeeQiangCallBridge mBridge;

    private OnMethodCallback mCallback;

    private BeeQiangCallBridge() {

    }

    public static BeeQiangCallBridge getInstance() {
        if (mBridge == null) {
            mBridge = new BeeQiangCallBridge();
        }
        return mBridge;
    }

    public void invokeMethod(int position, String orderId) {
        if (mCallback != null) {
            mCallback.doMethod(position, orderId);
        }
    }

    public void setOnMethodCallback(OnMethodCallback callback) {
        mCallback = callback;
    }

    public static interface OnMethodCallback {
        public void doMethod(int position, String orderId);
    }
}
