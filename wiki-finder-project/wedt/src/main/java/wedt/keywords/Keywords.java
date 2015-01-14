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

    private final String text;

    public Keywords(String text) {
        LOG.info("text:\n" + text);
        this.text = text;
    }

    public List<String> find(int n) {
        List<PotentialKeyword> candidates = stemsWithOccurrences();
        filterPolishFrequent(candidates);
        Collections.sort(candidates);
        return pickTheBest(candidates, n);
    }

    private List<String> pickTheBest(List<PotentialKeyword> candidates, int n) {
        LOG.info("sorted:\n" + candidates);
        List<String> ans = new LinkedList<>();
        for (PotentialKeyword k : candidates.subList(0, n + 1)) {
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
            List<WordData> polishStems = STEMMER.lookup(word);
            // TODO: check English stems and treat as unstemmable

            if (polishStems.isEmpty()) {
                append(word, unstemmableInRow);
            } else {
                stash(unstemmableInRow, potentialKeywords);
                String firstStem = polishStems.get(0).getStem().toString();
                seen(firstStem, potentialKeywords);
            }
        }
        stash(unstemmableInRow, potentialKeywords);

        List<PotentialKeyword> formattedAnswer = new ArrayList<>();
        formattedAnswer.addAll(potentialKeywords.values());
        return formattedAnswer;
        // TODO: return potential keywords to contain occurrences
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

    private void filterPolishFrequent(List<PotentialKeyword> sortItOut) {
        Iterator<PotentialKeyword> iterator = sortItOut.iterator();
        while (iterator.hasNext()) {
            PotentialKeyword k = iterator.next();
            if (POLISH_FREQUENT.contains(k.stem)) iterator.remove();
        }
    }
}
