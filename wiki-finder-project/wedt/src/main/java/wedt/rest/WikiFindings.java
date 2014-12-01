package wedt.rest;

import java.util.LinkedList;
import java.util.List;

/**
 * @author kjrz
 */
@SuppressWarnings("UnusedDeclaration")
public class WikiFindings {

    private final String page;
    private final List<Finding> findings;

    public WikiFindings(String page) {
        this.page = page;
        this.findings = new LinkedList<Finding>();
    }

    public String getPage() {
        return page;
    }

    public List<Finding> getFindings() {
        return findings;
    }

    public void add(int index, String finding) {
        findings.add(new Finding(index, finding));
    }

    private class Finding {

        private final int index;
        private final String content;

        private Finding(int index, String content) {
            this.index = index;
            this.content = content;
        }

        public int getIndex() {
            return index;
        }

        public String getContent() {
            return content;
        }
    }
}
