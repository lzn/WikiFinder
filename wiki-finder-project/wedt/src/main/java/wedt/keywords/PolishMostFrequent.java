package wedt.keywords;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @author kjrz
 */
public class PolishMostFrequent {
    private static final Logger LOG = Logger.getLogger(Keywords.class);

    private static final List<String> FIVE_THOUSAND_ORDERED;

    public static final String WIKTIONARY_POLISH_WORDLIST =
            "http://en.wiktionary.org/wiki/Wiktionary:Frequency_lists/Polish_wordlist";

    static {
        List<String> ans;
        try {
            String pageText = new Html(
                    WIKTIONARY_POLISH_WORDLIST)
                    .getText();
            int listStart = pageText.indexOf("nie");
            int listEnd = pageText.indexOf("Retrieved");
            ans = Arrays.asList(pageText
                    .substring(listStart, listEnd)
                    .replaceAll("\\d", "")
                    .split("\\s+"));
        } catch (IOException e) {
            LOG.warn("Could not load page");
            ans = Collections.emptyList();
        }
        FIVE_THOUSAND_ORDERED = new ArrayList<>();
        FIVE_THOUSAND_ORDERED.addAll(ans);
    }

    private final static String[] TUNING =
            {"komentarz", "skomentować", "komentować", "blog", "www", "pl"};

    private final Set<String> set;

    public PolishMostFrequent(int number) {
        if (number > FIVE_THOUSAND_ORDERED.size()) {
            throw new IllegalArgumentException("max = " + (FIVE_THOUSAND_ORDERED.size()));
        }
        List<String> include = FIVE_THOUSAND_ORDERED.subList(0, number);
        include.addAll(Arrays.asList(TUNING));
        this.set = new HashSet<>(include);
    }

    public boolean contains(String word) {
        return word.length() == 1 || set.contains(word);
    }

    public static void main(String[] args) {
        LOG.info("most frequent Polish:");
        LOG.info(FIVE_THOUSAND_ORDERED);

        LOG.info(new PolishMostFrequent(500).contains("to"));
    }

    // TODO: stem them all
}
