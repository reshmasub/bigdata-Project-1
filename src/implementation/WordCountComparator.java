package implementation;

import java.math.BigDecimal;
import java.util.Comparator;

public class WordCountComparator implements Comparator<Solution.WordCount> {

    @Override
    public int compare(Solution.WordCount o1, Solution.WordCount o2) {
        return o1.getCount().compareTo(o2.getCount());
    }
}

