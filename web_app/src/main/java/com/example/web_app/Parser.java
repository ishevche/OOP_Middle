package com.example.web_app;


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
    private final String domain;
    private boolean wikiParsed = false;
    private Document wiki;
    private String wikiLink;
    private BrandFetch brandFetch;
    private JSONObject PDAJson;
    private final Company company = new Company();

    public Parser(String domain){
        this.domain = domain;
        this.company.setDomainName(domain);
        this.brandFetch = new BrandFetch(domain);
    }

    private String getCompanySite(String site) throws IOException {
        Document doc = Jsoup.connect("https://www.google.com/search?q=site:" + site + " " + this.domain).get();
        return doc.select("div." + "yuRUbf").select("a").attr("href");
    }

    private String getCompanyWikipedia() throws IOException {
        return getCompanySite("wikipedia.org");
    }

    void getCompanyFacebook() throws IOException {
        if (brandFetch.getFacebook_url() != null){
            this.company.setFacebook(brandFetch.getFacebook_url());
        }
        else {
            this.company.setFacebook(getCompanySite("facebook.com"));
        }
    }

    void getCompanyTwitter() throws IOException {
        if (brandFetch.getTwitter_url() != null){
            this.company.setTwitter(brandFetch.getTwitter_url());
        }
        else {
            this.company.setTwitter(getCompanySite("twitter.com"));
        }
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
        if (brandFetch.getLogo_url() != null){
            this.company.setLogoLink(brandFetch.getLogo_url());
        }
        else {
            if (!wikiParsed) {
                this.wikiLink = getCompanyWikipedia();
                this.wiki = Jsoup.connect(this.wikiLink).get();
                wikiParsed = true;
            }
            String logo = wiki.select("table.infobox")
                    .select("img").attr("src");
            this.company.setLogoLink(logo.replace("//", ""));
        }
    }

    void getCompanyIcon() throws IOException {
        if (brandFetch.getIcon_url() != null){
            this.company.setIconLink(brandFetch.getIcon_url());
        }
        else {
            if (!wikiParsed) {
                this.wikiLink = getCompanyWikipedia();
                this.wiki = Jsoup.connect(this.wikiLink).get();
                wikiParsed = true;
            }
            String logo = wiki.select("table.infobox")
                    .select("img").attr("src");
            this.company.setIconLink(logo.replace("//", ""));
        }
    }

    void getCompanyEmployees(){
        this.company.setEmplooyees(PDAJson.getJSONArray("data").getJSONObject(0).getString("employee_count"));
    }

    void getCompanyAddress() throws IOException {
        ArrayList<String> JSONNames = new ArrayList<String>(Arrays.asList("continent", "country", "street_address", "address_line_2", "geo"));
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < JSONNames.size(); i++){
            if (!PDAJson.getJSONArray("data").getJSONObject(0).getJSONObject("location").isNull(JSONNames.get(i))){
                output.add(PDAJson.getJSONArray("data").getJSONObject(0).getJSONObject("location").getString(JSONNames.get(i)));
            }
            if (i == 1){
                output = new ArrayList<>(Arrays.asList(String.join(", ", output)));
            }
        }
        this.company.setAddress(String.join("\n", output));
    }

    private void fetchPDA() throws IOException {
        String API_KEY = System.getenv("PDL_API_KEY");
        String query = URLEncoder.encode("SELECT NAME FROM COMPANY WHERE WEBSITE='" + this.domain + "'", StandardCharsets.UTF_8);
        URL url = new URL("https://api.peopledatalabs.com/v5/company/search?sql=" + query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        connection.connect();
        String text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
        this.PDAJson = new JSONObject(text);
    }

    public void fetchInformation() {
        try {
            brandFetch.fetchInfo();
            fetchPDA();
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
