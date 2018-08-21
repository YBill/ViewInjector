package com.bill.viewinjecterapi;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Bill on 2018/8/17.
 */

public class ViewInjector {

    static final Map<Class<?>, Constructor> BINDINGS = new LinkedHashMap<>();

    public static void bind(Activity activity) {
        Constructor constructor = findBindingConstructorForClass(activity.getClass());
        try {
            constructor.newInstance(activity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Constructor findBindingConstructorForClass(Class<?> cls) {
        Constructor constructor = BINDINGS.get(cls);
        if (constructor == null) {
            try {
                Class<?> bindingClass = Class.forName(cls.getName() + "$ViewInjector");
                constructor = bindingClass.getDeclaredConstructor(cls);
                constructor.setAccessible(true);
                BINDINGS.put(cls, constructor);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    /**
     * 获取公有构造函数
     *
     * @param activity
     */
    @Deprecated
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
     *
     * @param activity
     */
    @Deprecated
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
