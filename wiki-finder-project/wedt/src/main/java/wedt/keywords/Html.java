package wedt.keywords;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kjrz
 */
public class Html {

    private final Document parsed;

    public Html(String url) throws IOException {
        this.parsed = Jsoup.connect(url).get();
    }

    public String getTitle() {
        return parsed.title();
    }

    public List<String> getHeaders() {
        return getByTag("h1");
    }

    public List<String> getParagraphs() {
        return getByTag("p");
    }

    public List<String> getByTag(String tag) {
        List<String> result = new LinkedList<>();
        for (Element el : parsed.getElementsByTag(tag)) {
            result.add(el.text());
        }
        return result;
    }

    public String getText() {
        return parsed.body().text();
    }
}
