package wedt.keywords;

import java.util.Arrays;

/**
 * @author kjrz
 */
public class PotentialKeyword implements Comparable<PotentialKeyword>{

    public final String stem;
    private Integer occurrences;

    public PotentialKeyword(String stem) {
        this.stem = stem;
        occurrences = 0;
    }

    public void seen() {
        occurrences++;
    }

    @Override
    public int compareTo(PotentialKeyword o) {
        return o.occurrences.compareTo(occurrences);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PotentialKeyword && obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{stem});
    }

    @Override
    public String toString() {
        return stem + " x" + occurrences;
    }
}
