package com.example.web_app;


import lombok.SneakyThrows;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {
    private String domain;
    private boolean wikiParsed = false;
    private Document wiki;
    private String wikiLink;
    private Company company = new Company();

    public Parser(String domain){
        this.domain = domain;
        this.company.setDomainName(domain);
    }

    private String getCompanySite(String site) throws IOException {
        Document doc = Jsoup.connect("https://www.google.com/search?q=site:" + site + " " + this.domain).get();
        String siteUrl = doc.select("div." + "yuRUbf").select("a").attr("href");
        return siteUrl;
    }

    private String getCompanyWikipedia() throws IOException {
        return getCompanySite("wikipedia.org");
    }

    void getCompanyFacebook() throws IOException {
        this.company.setFacebook(getCompanySite("facebook.com"));
    }

    void getCompanyTwitter() throws IOException {
        this.company.setTwitter(getCompanySite("twitter.com"));
    }

    void getCompanyName() throws IOException {
        if (!wikiParsed){
            this.wikiLink = getCompanyWikipedia();
            this.wiki = Jsoup.connect(this.wikiLink).get();
            wikiParsed = true;
        }
        this.company.setName(wiki.select("span.mw-page-title-main").text());
    }

    void getCompanyLogo() throws IOException {
        if (!wikiParsed){
            this.wikiLink = getCompanyWikipedia();
            this.wiki = Jsoup.connect(this.wikiLink).get();
            wikiParsed = true;
        }
        String logo = wiki.select("table.infobox").select("img").attr("src");
        this.company.setLogoLink(logo.replace("//", ""));
    }

    void getCompanyIcon() throws IOException {
        if (!wikiParsed){
            this.wikiLink = getCompanyWikipedia();
            this.wiki = Jsoup.connect(this.wikiLink).get();
            wikiParsed = true;
        }
        String logo = wiki.select("table.infobox").select("img").attr("src");
        this.company.setIconLink(logo.replace("//", ""));
    }

    void getCompanyEmployees(){
        this.company.setEmplooyees("100-500");
    }

    void getCompanyAddress() throws IOException {
        String API_KEY = System.getenv("PDL_API_KEY");
        String query = URLEncoder.encode("SELECT NAME FROM COMPANY WHERE WEBSITE='" + this.domain + "'", StandardCharsets.UTF_8);
        URL url = new URL("https://api.peopledatalabs.com/v5/company/search?sql=" + query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        connection.connect();
        String text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
        JSONObject jsonObject = new JSONObject(text);
        ArrayList<String> JSONNames = new ArrayList<String>(Arrays.asList("continent", "country", "street_address", "address_line_2", "geo"));
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < JSONNames.size(); i++){
            if (!jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("location").isNull(JSONNames.get(i))){
                output.add(jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("location").getString(JSONNames.get(i)));
            }
            if (i == 1){
                output = new ArrayList<>(Arrays.asList(String.join(", ", output)));
            }
        }
        this.company.setAddress(String.join("\n", output));
    }

    public void fetchInformation() {
        try {
            getCompanyName();
            getCompanyFacebook();
            getCompanyTwitter();
            getCompanyLogo();
            getCompanyIcon();
            getCompanyAddress();
            getCompanyEmployees();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Company buildCompany(){
        return this.company;
    }
}
