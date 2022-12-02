package com.example.web_app.chain;

import com.example.web_app.CompanyService;
import lombok.Getter;

@Getter
public abstract class Container extends Component{
    protected Component child;
    private final CompanyService companyService;

    public Container(String domain, CompanyService companyService) {
        super(domain);
        this.companyService = companyService;
    }

    public void addChild(Component component){
        this.child = component;
        component.container = this;
    }
}
