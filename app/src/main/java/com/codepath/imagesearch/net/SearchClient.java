package com.codepath.imagesearch.net;

import android.content.Context;
import android.util.Log;

import com.codepath.googleimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchClient {
    public static final String TAG = SearchClient.class.getSimpleName();
    private static final String API_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";
    private AsyncHttpClient client;
    private Context context;

    public SearchClient(Context context) {
        this.client = new AsyncHttpClient();
        this.context = context;
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void getImageSearchResults(final String query, JsonHttpResponseHandler handler) {
        String url = getApiUrl("?v=1.0&rsz=" + context.getResources().getInteger(R.integer.page_size)  + "&q=");
        Log.d(TAG, url);
        client.get(url + query, handler);
    }
}
