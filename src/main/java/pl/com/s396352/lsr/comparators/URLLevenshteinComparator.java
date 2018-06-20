package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.utils.StringUtils;
import org.hibernate.Session;

public class URLLevenshteinComparator extends BaseComparator {
    @Override
    public Comparison compare(Session session, Website left, Website right) {
        String lURL = left.getURL();
        String rURL = right.getURL();

        double distance = StringUtils.levenshteinDistance(lURL, rURL);

        return createComparison(session, left, right, this, 1-(distance/Math.max(lURL.length(), rURL.length())));
    }
}
