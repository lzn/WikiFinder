package wedt.rest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wedt.keywords.Html;
import wedt.keywords.Keywords;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author kjrz
 */
@RestController
public class WikiFindingsController {

    private static final Logger LOG = Logger.getLogger(WikiFindingsController.class);

    @RequestMapping("/wiki-finder")
    public WikiFindings wikiFind(@RequestParam(value = "url", defaultValue = "undefined") String url) {
        LOG.info("url = " + url);

        List<String> keywords = getKeywords(url);

        // TODO: search keywords in Wikipedia

        return writeToJson(url, keywords);

        // TODO: write Wikipedia matches instead of keywords
    }

    private List<String> getKeywords(String url) {
        Html html;
        try {
            html = new Html(url);
        } catch (IOException | IllegalArgumentException e) {
            LOG.warn("Failed to load url = \"" + url + "\"", e);
            return Collections.emptyList();
        }

        LOG.info("page text: " + html.getText());

        return new Keywords(html.getText()).find(5);
    }

    private WikiFindings writeToJson(String url, List<String> keywords) {
        WikiFindings findings = new WikiFindings(url);
        int i = 1;
        for (String keyword : keywords) {
            findings.add(i, keyword);
            i++;
        }
        return findings;
    }
}
