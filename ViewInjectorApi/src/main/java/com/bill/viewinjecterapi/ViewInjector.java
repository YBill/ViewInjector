package com.bill.viewinjecterapi;

import android.app.Activity;
import android.view.View;

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
        bind(activity, activity.getWindow().getDecorView());
    }

    public static void bind(Object target, View view) {
        Constructor constructor = findBindingConstructorForClass(target.getClass());
        try {
            constructor.newInstance(target, view);
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
                constructor = bindingClass.getDeclaredConstructor(cls, View.class);
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
