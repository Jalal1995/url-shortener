package com.test.urlshortener;

import com.google.common.hash.Hashing;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class Controller {
    Map<String, String> storage = new HashMap<>();

    @GetMapping("/s/{shortedurl}")
    public RedirectView getUrl(@PathVariable String shortedurl) {
        String url = storage.get(shortedurl);
        log.info("URL Retrieved: " + url);
        if (url == null) throw new RuntimeException("There is no URL for : " + shortedurl);
        return new RedirectView(url);
    }

    @GetMapping("/short")
    public String create(@RequestParam String url) {

        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );

        if (urlValidator.isValid(url)) {
            String shortedUrl = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            log.info("shorted URL generated: " + shortedUrl);
            storage.put(shortedUrl, url);
            return String.format("http://localhost:8081/s/%s", shortedUrl);
        }
        throw new RuntimeException("URL Invalid: " + url);
    }
}
