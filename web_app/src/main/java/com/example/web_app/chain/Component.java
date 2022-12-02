package com.example.web_app.chain;

import lombok.Getter;

@Getter
public abstract class Component implements ComponentWithCompany{
    protected Container container;
    private final String domain;

    protected Component(String domain) {this.domain = domain;}
}
