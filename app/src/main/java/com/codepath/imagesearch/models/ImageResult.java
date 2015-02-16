package com.codepath.imagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResult {
    private String fullUrl;
    private String thumbUrl;
    private String title;
    
    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getTitle() {
        return title;
    }

    public static ImageResult fromJson(JSONObject jsonObject) {
        ImageResult result = new ImageResult();
        try {
            result.fullUrl = jsonObject.has("url") ? jsonObject.getString("url") : "";
            result.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            result.thumbUrl = jsonObject.has("tbUrl") ? jsonObject.getString("tbUrl") : "";
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray jsonArray) {
        if(jsonArray == null || jsonArray.length() < 1) return null;
        ArrayList<ImageResult> results = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject resultJson = null;
            try {
                resultJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            ImageResult result = ImageResult.fromJson(resultJson);
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }
}
