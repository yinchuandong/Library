package com.gw.library.daemon;

import android.util.Log;

public class Daemon {

	static {
		System.loadLibrary("Library");
	}
	/**
	 * 设置环境参数
	 */
    public static native void setJNIEnv();
    
    /**
     * 守护进程的开启
     */
    public static native void mainThread();
    
//    //由JNI中的线程回调
//    public static void fromJNI(final int i)
//    {
//    	Log.i("Java------>fromJNI", ""+i);
//    }
	
}
