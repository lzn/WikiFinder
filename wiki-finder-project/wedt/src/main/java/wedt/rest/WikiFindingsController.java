package wedt.rest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wedt.finder.ActualWikiFinder;
import wedt.keywords.Html;
import wedt.keywords.Keywords;
import wedt.finder.KeywordWikiFinder;
import wedt.finder.WikiFinder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author kjrz
 */
@RestController
public class WikiFindingsController {
    private static final Logger LOG = Logger.getLogger(WikiFindingsController.class);
    private static final int NUMBER_OF_KEYWORDS = 5; // TODO: fine tune

    @RequestMapping("/wiki-finder")
    public WikiFinderAnswer wikiFind(@RequestParam(value = "url", defaultValue = "undefined") String url) {
        LOG.info("url = " + url);
        List<String> keywords = getKeywords(url);
        List<WikiFinder.Finding> findings = getMatches(keywords);
        return writeToJson(url, findings);
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

        return new Keywords(html.getText(), url).find(NUMBER_OF_KEYWORDS);
    }

    private List<WikiFinder.Finding> getMatches(List<String> keywords) {
        WikiFinder matcher = new KeywordWikiFinder();
        // WikiFinder matcher = new ActualWikiFinder(); // TODO: use instead
        return matcher.match(keywords);
    }

    private WikiFinderAnswer writeToJson(String url, List<WikiFinder.Finding> findings) {
        return new WikiFinderAnswer<>(url, findings);
    }
}
