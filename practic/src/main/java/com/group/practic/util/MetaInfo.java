package com.group.practic.util;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public interface MetaInfo {

    public static final String META_TITLE_QUERY = "meta[property=og:title]";

    public static final String CONTENT_TAG = "content";

    public static final String TITLE_TAG = "title";


    public static String getTitle(String url) {
        Request request = new Request.Builder().header("Accept-Encoding", "identity;q=1.0,*;q=0")
                .url(url).build();
        Document document = null;
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            document = Jsoup.parse(response.body().string());
            Elements elements = document.head().select(TITLE_TAG);
            if (elements.isEmpty()) {
                elements = document.head().select(META_TITLE_QUERY);
                if (elements.isEmpty()) {
                    int slash = url.lastIndexOf('/');
                    return url.lastIndexOf('.') > slash ? url.substring(slash + 1) : url;
                }
                return elements.get(0).attr(CONTENT_TAG);
            }
            return elements.isEmpty() ? url : elements.get(0).text();
        } catch (IOException e) {
            return url;
        }
    }

}
