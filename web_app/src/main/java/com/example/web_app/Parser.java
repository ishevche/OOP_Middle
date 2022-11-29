package com.example.web_app;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Parser {
    private Company company;

    @SneakyThrows
    public void getCompanyPageInfo(String query){
        Document doc = Jsoup.connect(query).get();

    }
}
