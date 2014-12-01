package wedt.keywords;

import morfologik.stemming.PolishStemmer;
import morfologik.stemming.WordData;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author kjrz
 */
public class Keywords {

    private static final Logger LOG = Logger.getLogger(Keywords.class);

    public List<String> find(String text) {
        LOG.info("string to token and stem: " + text);

        List<String> keywords = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(text, "!?.,-() ");
        PolishStemmer stemmer = new PolishStemmer();
        while (tokenizer.hasMoreTokens()) {
            List<WordData> stems = stemmer.lookup(tokenizer.nextToken());
            if (stems.size() > 0) {
                keywords.add(stems.get(0).getStem().toString());
            }
        }

        // TODO: real keywords search

        return keywords;
    }
}
