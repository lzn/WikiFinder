package wedt.finder;

import java.util.List;

/**
 * @author kjrz
 */
public interface WikiFinder {

    public interface Finding {}

    public List<Finding> match(List<String> keywords);
}
