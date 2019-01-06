package com.tianma.customsystemservice.aidl;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedHelpers;

public class CustomService extends ICustomService.Stub {

    private static final String TAG = "CustomService";

    private static CustomService sInstance;

    private Context mContext;

    public CustomService(Context context) {
        mContext = context;
    }

    @Override
    public String toUpperCase(String str) throws RemoteException {
        return str.toUpperCase();
    }

    @Override
    public int add(int num1, int num2) throws RemoteException {
        return num1 + num2;
    }

    public static void register(Context context, ClassLoader classLoader) {
        Class<?> svcManager = XposedHelpers.findClass("android.os.ServiceManager", classLoader);

        sInstance = new CustomService(context);

        XposedHelpers.callStaticMethod(svcManager,
                /* methodName */"addService",
                /* name       */getServiceName(),
                /* service    */ sInstance,
                /* allowIsolated */ true);

        log("register service succeed");
    }

    private static String getServiceName() {
        // 5.0 之后，selinux "user." 前缀
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? "user." : "") + "customservice";
    }

    public static ICustomService getService() {
        try {
            Class<?> svcManager = Class.forName("android.os.ServiceManager");
            Method getServiceMethod = svcManager.getDeclaredMethod("getService", String.class);
            IBinder binder = (IBinder) getServiceMethod.invoke(null, getServiceName());
            return ICustomService.Stub.asInterface(binder);
        } catch (Exception e) {
            e.printStackTrace();
            log(e);
        }
        return null;
    }

    private static void log(String text) {
        Log.d(TAG, text);
    }

    private static void log(Throwable t) {
        Log.e(TAG, Log.getStackTraceString(t));
    }
}
