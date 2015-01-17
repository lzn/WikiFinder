package wedt.finder;

import java.util.List;

/**
 * @author kjrz
 */
public class ActualWikiFinder implements WikiFinder {

    public class ActualFinding implements WikiFinder.Finding {
        // TODO: representation of a finding by fields + getters
    }

    @Override
    public List<Finding> match(List<String> keywords) {
        throw new UnsupportedOperationException();
        // TODO: find matches in Wikipedia index
    }
}
