package wedt.rest;

import java.util.List;

/**
 * @author kjrz
 */
@SuppressWarnings("UnusedDeclaration")
public class WikiFinderAnswer<T> {

    private final String page;
    private final List<T> findings;

    public WikiFinderAnswer(String page, List<T> findings) {
        this.page = page;
        this.findings = findings;
    }

    public String getPage() {
        return page;
    }

    public List<T> getFindings() {
        return findings;
    }
}
