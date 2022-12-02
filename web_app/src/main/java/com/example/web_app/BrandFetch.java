package com.example.web_app;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Getter
public class BrandFetch {
    private final String domain;
    private String icon_url = null;
    private String logo_url = null;
    private String twitter_url = null;
    private String facebook_url = null;

    public BrandFetch(String domain) {
        this.domain = domain;
    }

    public void fetchInfo() throws IOException {
        String API_KEY = System.getenv("BRAND_FETCH_API_KEY");
        String query = URLEncoder.encode(domain, StandardCharsets.UTF_8);
        URL url = new URL("https://api.brandfetch.io/v2/brands/" + query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.connect();
        String text = new Scanner(connection.getInputStream())
                .useDelimiter("\\Z").next();
        parseJson(text);
    }

    private void parseJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        if (jsonObject.has("links")) {
            parseLinks(jsonObject.getJSONArray("links"));
        }
        if (jsonObject.has("logos")) {
            parseLogos(jsonObject.getJSONArray("logos"));
        }
    }

    private void parseLogos(JSONArray logos) {
        for (Object logo: logos) {
            assert logo instanceof JSONObject;
            JSONObject logo_object = (JSONObject) logo;

            if (!logo_object.has("type")) {continue;}

            if ("icon".equals(logo_object.getString("type"))) {
                icon_url = logo_object.getJSONArray("formats")
                                .getJSONObject(0).getString("src");
            } else if ("logo".equals(logo_object.getString("type"))) {
                logo_url = logo_object.getJSONArray("formats")
                                .getJSONObject(0).getString("src");
            }
        }
    }

    private void parseLinks(JSONArray links) {
        for (Object link: links) {
            assert link instanceof JSONObject;

            JSONObject link_object = (JSONObject) link;

            if ("facebook".equals(link_object.getString("name"))) {
                facebook_url = link_object.getString("url");
            } else if ("twitter".equals(link_object.getString("name"))) {
                twitter_url = link_object.getString("url");
            }
        }
    }
}
