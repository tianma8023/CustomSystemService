package com.tianma.customsystemservice.hook;

import android.content.Context;
import android.os.Build;

import com.tianma.customsystemservice.aidl.CustomService;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class CustomServiceHook implements IXposedHookZygoteInit {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // android 5.0+
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            XposedBridge.hookAllMethods(activityThread, "systemMain", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    Class<?> ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", classLoader);

                    XposedHelpers.findAndHookConstructor(ams,
                            Context.class,
                            new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    CustomService.register((Context) param.args[0], classLoader);
                                }
                            });
                }
            });
        } else {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> ams = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", classLoader);
            XposedBridge.hookAllMethods(ams,
                    "main",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            CustomService.register((Context) param.getResult(), classLoader);
                        }
                    });
        }
    }
}
