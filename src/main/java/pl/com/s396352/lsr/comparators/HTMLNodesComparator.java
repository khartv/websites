package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.utils.StringUtils;
import org.hibernate.Session;

import java.util.Set;

public class HTMLNodesComparator extends BaseComparator {
    @Override
    public Comparison compare(Session session, Website left, Website right) {
        Set<String> lIds = left.getCSSids();
        Set<String> lClasses = left.getCSSclasses();
        Set<String> rIds = right.getCSSids();
        Set<String> rClasses = right.getCSSclasses();

        double distanceSum = 0;
        double avgDistance = 0;
        int counter = 0;
        for(int i = 0; i < Math.max(lIds.size(), rIds.size()); i++)
        {
            distanceSum += StringUtils.levenshteinDistance((String)lIds.toArray()[i], (String)rIds.toArray()[i]);
            counter = i;
        }
        avgDistance = distanceSum/counter;

        return createComparison(session, left, right, this, avgDistance);
    }
}
