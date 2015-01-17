package wedt.keywords;

import morfologik.stemming.PolishStemmer;
import morfologik.stemming.WordData;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author kjrz
 */
public class Keywords {

    private static final Logger LOG = Logger.getLogger(Keywords.class);
    private static final String DELIM = "!?.,-–()«|0123456789+„”/»=;\"*<> ";
    private static final PolishStemmer STEMMER = new PolishStemmer();
    private static final PolishMostFrequent POLISH_FREQUENT = new PolishMostFrequent(5000);
    public static final String WWW_DOT = "//";

    private final String text;
    private final String pageName;

    public Keywords(String text, String url) {
        LOG.info("text:\n" + text);
        this.text = text;
        this.pageName = getPageName(url);
        LOG.info("page name = " + pageName);
    }

    private String getPageName(String url) {
        String afterWWW = url.substring(url.indexOf(WWW_DOT) + WWW_DOT.length());
        return afterWWW.substring(0, afterWWW.indexOf("/"));
    }

    public List<String> find(int n) {
        List<PotentialKeyword> candidates = stemsWithOccurrences();
        filterPolishFrequentAndPageTitle(candidates);
        Collections.sort(candidates);
        return pickNBest(candidates, n);
    }

    private List<String> pickNBest(List<PotentialKeyword> candidates, int n) {
        LOG.info("all found keywords sorted:\n" + candidates);
        List<String> ans = new LinkedList<>();
        for (PotentialKeyword k : candidates.subList(0, n)) {
            ans.add(k.stem);
        }
        return ans;
    }

    private List<PotentialKeyword> stemsWithOccurrences() {
        Map<String, PotentialKeyword> potentialKeywords = new HashMap<>();

        StringBuilder unstemmableInRow = new StringBuilder();

        StringTokenizer tokenizer = new StringTokenizer(text, DELIM);
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            List<WordData> polishLemma = STEMMER.lookup(word);
            // TODO:
            // don't stem English, use e.g. a spell checker
            // to determine if word is English

            if (polishLemma.isEmpty()) {
                append(word, unstemmableInRow);
            } else {
                stash(unstemmableInRow, potentialKeywords);
                String firstStem = polishLemma.get(0).getStem().toString();
                seen(firstStem, potentialKeywords);
            }
        }
        stash(unstemmableInRow, potentialKeywords);

        List<PotentialKeyword> formattedAnswer = new ArrayList<>();
        formattedAnswer.addAll(potentialKeywords.values());
        return formattedAnswer;
    }

    private void append(String word, StringBuilder unstemmableInRow) {
        if (unstemmableInRow.length() > 0) {
            unstemmableInRow.append(" ");
        }
        unstemmableInRow.append(word);
    }

    private void seen(String stem, Map<String, PotentialKeyword> memory) {
        if (!memory.containsKey(stem)) {
            memory.put(stem, new PotentialKeyword(stem));
        }
        memory.get(stem).seen();
    }

    private void stash(StringBuilder unstemmableInRow, Map<String, PotentialKeyword> memory) {
        if (unstemmableInRow.length() == 0) {
            return;
        }

        String unknownWhole = unstemmableInRow.toString();

        String sameThingTwice = sameThingTwice(unknownWhole);
        if (sameThingTwice.isEmpty()) {
            seen(unknownWhole, memory);
        } else {
            seen(sameThingTwice, memory);
            seen(sameThingTwice, memory);
        }

        unstemmableInRow.setLength(0);
    }

    private String sameThingTwice(String s) {
        if (s.length() % 2 == 0) {
            return "";
        }
        int halfWay = s.length() / 2;
        String firstHalf = s.substring(0, halfWay);
        String secondHalf = s.substring(halfWay + 1);
        if (firstHalf.equals(secondHalf)) {
            return firstHalf;
        } else {
            return "";
        }
    }

    private void filterPolishFrequentAndPageTitle(List<PotentialKeyword> candidates) {
        Iterator<PotentialKeyword> iterator = candidates.iterator();
        while (iterator.hasNext()) {
            PotentialKeyword k = iterator.next();
            if (POLISH_FREQUENT.contains(k.stem) || pageName.contains(k.stem)) {
                iterator.remove();
            }
        }
    }
}
