package com.example.web_app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {
    private final String domain;
    private boolean wikiParsed = false;
    private Document wiki;
    private String wikiLink;
    private final Company company = new Company();

    public Parser(String domain){
        this.domain = domain;
        this.company.setDomainName(domain);
    }

    private String getCompanySite(String site) throws IOException {
        Document doc = Jsoup.connect("https://www.google.com/search?q=site:" + site + " " + this.domain).get();
        return doc.select("div." + "yuRUbf").select("a").attr("href");
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
        String logo = wiki.select("table.infobox")
                .select("img").attr("src");
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

    void getCompanyAddress(){
        this.company.setAddress("Kozelnytska");
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
