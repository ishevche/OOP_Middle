package com.example.web_app.chain;

import com.example.web_app.Company;
import com.example.web_app.Parser;
import lombok.Getter;

@Getter
public class ParserCompany extends Component{

    public ParserCompany(String domain) {super(domain);}

    public Company getCompany() {
        Parser parser = new Parser(container.getDomain());
        parser.fetchInformation();
        return parser.buildCompany();
    }
}
