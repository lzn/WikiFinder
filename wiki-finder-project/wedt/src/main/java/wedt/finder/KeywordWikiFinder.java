package wedt.finder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kjrz
 */
public class KeywordWikiFinder implements WikiFinder {

    public class KeywordFinding implements WikiFinder.Finding {
        private final int position;
        private final String keyword;

        private KeywordFinding(int position, String keyword) {
            this.position = position;
            this.keyword = keyword;
        }

        public int getPosition() {
            return position;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    @Override
    public List<Finding> match(List<String> keywords) {
        List<Finding> findings = new ArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            findings.add(new KeywordFinding(i + 1, keywords.get(i)));
        }
        return findings;
    }
}
