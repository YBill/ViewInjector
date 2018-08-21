package com.bill.viewinjecterapi;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Bill on 2018/8/17.
 */

public class ViewInjector {
    public static void bind(Activity activity) {
        bindPrivate(activity);
    }

    /**
     * 获取公有构造函数
     * @param activity
     */
    private static void bindPublic(Activity activity) {
        String clsName = activity.getClass().getName();
        try {
            Class<?> cls = Class.forName(clsName + "$ViewInjector");
            Constructor constructor = cls.getConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取私有构造函数
     * @param activity
     */
    private static void bindPrivate(Activity activity) {
        String clsName = activity.getClass().getName();
        try {
            Class<?> cls = Class.forName(clsName + "$ViewInjector");
            Constructor con = cls.getDeclaredConstructor(activity.getClass());
            con.setAccessible(true);
            con.newInstance(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
