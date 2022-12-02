package com.example.web_app;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {
    private boolean wikiParsed = false;
    private Document wiki;

    @SneakyThrows
    public void getCompanyPageInfo(String query){
        Document doc = Jsoup.connect(query).get();
    }

    private String getCompanySite(String site, String query) throws IOException {
        Document doc = Jsoup.connect("https://www.google.com/search?q=site:" + site + " " + query).get();
        String siteUrl = doc.select("div." + "yuRUbf").select("a").attr("href");
        return siteUrl;
    }

    public String getCompanyWikipedia(String query) throws IOException {
        return getCompanySite("wikipedia.org", query);
    }

    public String getCompanyFacebook(String query) throws IOException {
        return getCompanySite("facebook.com", query);
    }

    public String getCompanyTwitter(String query) throws IOException {
        return getCompanySite("twitter.com", query);
    }

    public String getCompanyName(String query) throws IOException {
        if (!wikiParsed){
            this.wiki = Jsoup.connect(new Parser().getCompanyWikipedia(query)).get();
            wikiParsed = true;
        }
        return wiki.select("span.mw-page-title-main").text();
    }

    public String getCompanyLogo(String query) throws IOException {
        if (!wikiParsed){
            this.wiki = Jsoup.connect(new Parser().getCompanyWikipedia(query)).get();
            wikiParsed = true;
        }
        String logo = wiki.select("div.mw-parser-output").select("img").attr("src");
        return logo.replace("//", "");
    }
}
