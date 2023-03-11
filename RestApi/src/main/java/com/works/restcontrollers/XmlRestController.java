package com.works.restcontrollers;

import com.works.props.Currency;
import com.works.services.XmlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class XmlRestController {

    final XmlService xmlService;

    @GetMapping("/xml")
    public List<Currency> xml() {
        List<Currency> ls = xmlService.xml();
        return ls;
    }

}
