package com.axb.upgrade.threads;

import com.axb.upgrade.entities.BaseUpdateInfo;
import com.axb.upgrade.parsers.BaseParser;
import com.axb.upgrade.utils.AutoUpgradeTool;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUpdateThread extends Thread {
    private static final int WAIT_TIMEOUT = 10000;
    private String updateUrl;
    private BaseUpdateInfo updateInfo;
    private BaseParser parser;
    private AutoUpgradeTool.OnCheckUpdateListener onCheckUpdateListener;
    private Object userParam;

    public CheckUpdateThread(String updateUrl, BaseUpdateInfo updateInfo, BaseParser parser, AutoUpgradeTool.OnCheckUpdateListener onCheckUpdateListener, Object userParam) {
        this.updateUrl = updateUrl;
        this.updateInfo = updateInfo;
        this.parser = parser;
        this.onCheckUpdateListener = onCheckUpdateListener;
        this.userParam = userParam;
    }

    public void run() {
        try {
            URL url = new URL(updateUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            InputStream is = conn.getInputStream();
            parser.parse(is, updateInfo);
            onCheckUpdateListener.onCheckSuccess(updateInfo, userParam);
        } catch (XmlPullParserException | IOException var4) {
            onCheckUpdateListener.onCheckFailed(var4.getMessage(), userParam);
        }

    }
}
