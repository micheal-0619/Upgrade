package com.axb.upgrade.entities;

import androidx.annotation.NonNull;

public abstract class BaseUpdateInfo {
    private int version;
    private String versionName;
    private boolean forceUpdate;
    private String url;
    private String description;

    public BaseUpdateInfo() {
    }

    protected abstract boolean doCheckKey(String var1);

    protected abstract void doParse(String var1, String var2);

    public boolean checkKey(String _name) {
        switch (_name) {
            case "version":
            case "versionName":
            case "forceUpdate":
            case "url":
            case "description":
                return true;
            default:
                return doCheckKey(_name);
        }
    }

    public void parse(String _name, String _value) {
        switch (_name) {
            case "version":
                setVersion(Integer.parseInt(_value));
                break;
            case "versionName":
                setVersionName(_value);
                break;
            case "forceUpdate":
                setForceUpdate(_value.equals("True"));
                break;
            case "url":
                setUrl(_value);
                break;
            case "description":
                setDescription(_value);
                break;
            default:
                doParse(_name, _value);
        }

    }

    @NonNull
    public String toString() {
        return "BaseUpdateInfo{" + version + "|" + versionName + "|" + forceUpdate + "|'" + url + '}';
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isForceUpdate() {
        return this.forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

