package com.axb.upgrade.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    public T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //返回当前类的父类的Type，也就是BaseActivity
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            //如果支持泛型
            try {
                //获得泛型中的实际类型，可能会存在多个泛型，[0]也就是获得T的type
                Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                //反射inflate
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                //方法调用，获得ViewBinding实例
                binding = (T) method.invoke(null, getLayoutInflater());
            } catch (Exception e) {
                e.printStackTrace();
            }
            setContentView(binding.getRoot());
        }
        init();
    }

    protected abstract void init();
}
