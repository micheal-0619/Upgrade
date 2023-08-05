package com.axb.upgrade;


import com.axb.upgrade.base.BaseActivity;
import com.axb.upgrade.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void init() {
        binding.tvHello.setText("Hello Android");
    }
}