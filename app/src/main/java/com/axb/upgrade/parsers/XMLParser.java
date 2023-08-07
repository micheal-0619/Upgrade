package com.axb.upgrade.parsers;

import android.util.Xml;

import androidx.annotation.NonNull;

import com.axb.upgrade.entities.BaseUpdateInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class XMLParser extends BaseParser {
    public XMLParser() {
    }

    public void parse(@NonNull InputStream inputStream, @NonNull BaseUpdateInfo baseUpdateInfo) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "utf-8");

        for(int type = parser.getEventType(); type != 1; type = parser.next()) {
            if (type == 2) {
                String _name = parser.getName().trim();
                if (baseUpdateInfo.checkKey(_name)) {
                    baseUpdateInfo.parse(_name, parser.nextText());
                }
            }
        }

    }
}
