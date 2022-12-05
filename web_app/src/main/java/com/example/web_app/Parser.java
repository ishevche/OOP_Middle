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
import java.util.List;
import java.util.Scanner;

public class Parser {
    private final String domain;
    private boolean wikiParsed;
    private Document wiki;
    private String wikiLink;
    private final BrandFetch brandFetch;
    private JSONObject PDAJson;
    private final Company company;

    public Parser(String domain) {
        this.domain = domain;
        wikiParsed = false;
        company = new Company();
        company.setDomainName(domain);
        brandFetch = new BrandFetch(domain);
        PDAJson = new JSONObject("{\"data\":[{\"employee_count\":null," + "\"location\":{\"continent\":\"null\",\"geo\":null,\"country\":" + "\"null\",\"street_address\":null,\"metro\":null,\"name\":\"null" + "\",\"locality\":null,\"address_line_2\":null,\"region\":null," + "\"postal_code\":null}}]}");
    }

    private String getCompanySite(String site) throws IOException {
        Document doc = Jsoup.connect("https://www.google.com/search?q=site:" + site + " " + domain).get();
        return doc.select("div." + "yuRUbf").select("a").attr("href");
    }

    private String getCompanyWikipedia() throws IOException {
        return getCompanySite("wikipedia.org");
    }

    void getCompanyFacebook() throws IOException {
        if (brandFetch.getFacebook_url() != null) {
            company.setFacebook(brandFetch.getFacebook_url());
        } else {
            company.setFacebook(getCompanySite("facebook.com"));
        }
    }

    void getCompanyTwitter() throws IOException {
        if (brandFetch.getTwitter_url() != null) {
            company.setTwitter(brandFetch.getTwitter_url());
        } else {
            company.setTwitter(getCompanySite("twitter.com"));
        }
    }

    void getCompanyName() throws IOException {
        if (brandFetch.getName() != null) {
            company.setName(brandFetch.getName());
            return;
        } else if (!wikiParsed) {
            wikiLink = getCompanyWikipedia();
            wiki = Jsoup.connect(wikiLink).get();
            wikiParsed = true;
        }
        company.setName(wiki.select("span.mw-page-title-main").text());
    }

    void getCompanyLogo() throws IOException {
        if (brandFetch.getLogo_url() != null) {
            company.setLogoLink(brandFetch.getLogo_url());
        } else {
            if (!wikiParsed) {
                wikiLink = getCompanyWikipedia();
                wiki = Jsoup.connect(wikiLink).get();
                wikiParsed = true;
            }
            String logo = wiki.select("table.infobox").select("img").attr("src");
            company.setLogoLink(logo.replace("//", "https://"));
        }
    }

    void getCompanyIcon() throws IOException {
        if (brandFetch.getIcon_url() != null) {
            company.setIconLink(brandFetch.getIcon_url());
        } else {
            if (!wikiParsed) {
                wikiLink = getCompanyWikipedia();
                wiki = Jsoup.connect(wikiLink).get();
                wikiParsed = true;
            }
            String logo = wiki.select("table.infobox").select("img").attr("src");
            company.setIconLink(logo.replace("//", "https://"));
        }
    }

    void getCompanyEmployees() {
        if (!PDAJson.getJSONArray("data").getJSONObject(0).isNull("employee_count")) {
            company.setEmployees(Integer.toString(PDAJson.getJSONArray("data").getJSONObject(0).getInt("employee_count")));
        } else {
            company.setEmployees("Not Found");
        }
    }

    void getCompanyAddress() throws IOException {
        ArrayList<String> JSONNames = new ArrayList<>(Arrays.asList("continent", "country", "street_address", "address_line_2", "geo"));
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < JSONNames.size(); i++) {
            if (!PDAJson.getJSONArray("data").getJSONObject(0).getJSONObject("location").isNull(JSONNames.get(i))) {
                output.add(PDAJson.getJSONArray("data").getJSONObject(0).getJSONObject("location").getString(JSONNames.get(i)));
            }
            if (i == 1) {
                output = new ArrayList<>(List.of(String.join(", ", output)));
            }
        }
        company.setAddress(String.join("\n", output));
    }

    private void fetchPDA() throws IOException, NoSuchFieldException {
        String API_KEY = System.getenv("PDL_API_KEY");
        if (API_KEY == null) {
            throw new NoSuchFieldException("You should provide PDL_API_KEY environment variable");
        }
        String query = URLEncoder.encode("SELECT NAME FROM COMPANY WHERE WEBSITE='" + domain + "'", StandardCharsets.UTF_8);
        URL url = new URL("https://api.peopledatalabs.com/v5/company/search?sql=" + query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
            PDAJson = new JSONObject(text);
        }
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
        } catch (IOException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Company buildCompany() {
        return company;
    }
}
