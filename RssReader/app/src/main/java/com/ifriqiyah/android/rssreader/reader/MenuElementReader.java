package com.ifriqiyah.android.rssreader.reader;


import android.util.JsonReader;

import com.ifriqiyah.android.rssreader.MyApplication;
import com.ifriqiyah.android.rssreader.domain.MenuElement;
import com.ifriqiyah.android.rssreader.util.Constants;
import com.ifriqiyah.android.rssreader.util.HttpHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;


public class MenuElementReader {


    public static void readAndFillList() throws IOException {
        HttpHelper.downloadToFile(Constants.MENU_JSON_FILE_URL, "/data/data/" + MyApplication.getMyPackageName() + "/files/menu.json");
        JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream("/data/data/" + MyApplication.getMyPackageName()+ "/files/menu.json"), "UTF-8"));
        reader.beginArray();
        while (reader.hasNext()) {
            try {
                MyApplication.getMenuElementDao().createOrUpdate(readMenuItem(reader));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("qazxsw"+MyApplication.getMenuElementEntities().size());
        reader.endArray();
    }
    private static MenuElement readMenuItem(JsonReader reader) throws IOException {
        int id = -1;
        String text = null;
        String englishText = null;
        String smallIconURL = null;
        String bigIconURL = null;
        String smallIconHash = null;
        String bigIconHash = null;
        String newsRssURL = null;
        String articleRssURL = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("text")) {
                text = reader.nextString();
            } else if (name.equals("englishText")) {
                englishText = reader.nextString();
            } else if (name.equals("smallIconURL")) {
                smallIconURL = reader.nextString();
            } else if (name.equals("bigIconURL")) {
                bigIconURL = reader.nextString();
            } else if (name.equals("smallIconHash")) {
                smallIconHash = reader.nextString();
            } else if (name.equals("bigIconHash")) {
                bigIconHash = reader.nextString();
            } else if (name.equals("articleRssURL")) {
                articleRssURL = reader.nextString();
            } else if (name.equals("newsRssURL")) {
                newsRssURL = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        byte[] bytesBig = HttpHelper.downloadAsByteArray(bigIconURL);
        byte[] bytesSmall = HttpHelper.downloadAsByteArray(smallIconURL);
        MenuElement menuElement = new MenuElement(id, text, englishText, articleRssURL, newsRssURL);
        menuElement.setBigIcon(bytesBig);
        menuElement.setSmallIcon(bytesSmall);
        return menuElement;
    }
}
