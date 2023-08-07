package com.axb.upgrade.parsers;

import androidx.annotation.NonNull;

import com.axb.upgrade.entities.BaseUpdateInfo;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public abstract class BaseParser {
    public BaseParser() {
    }

    public abstract void parse(@NonNull InputStream var1, @NonNull BaseUpdateInfo var2) throws XmlPullParserException, IOException;
}
