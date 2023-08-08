package com.axb.upgrade.entities;

public class UpdateInfo extends BaseUpdateInfo {

    @Override
    protected boolean doCheckKey(String s) {
        return false;
    }

    @Override
    protected void doParse(String s, String s1) {

    }
}
