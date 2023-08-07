package com.axb.upgrade;


import android.annotation.SuppressLint;

import com.axb.upgrade.base.BaseActivity;
import com.axb.upgrade.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @SuppressLint("SetTextI18n")
    @Override
    protected void init() {
        binding.tvHello.setText("Hello Android");
    }
}