package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Sentence;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import org.hibernate.Session;

import java.util.Set;

public class CSSComparator extends NgramTextComparator {
    @Override
    public Comparison compare(Session session, Website left, Website right) {
        Set<String> lCSSids = left.getCSSids();
        Set<String> rCSSids = right.getCSSids();
        Set<String> lCSSclasses = left.getCSSclasses();
        Set<String> rCSSclasses = right.getCSSclasses();


        double distanceIds = uRZ(new Sentence(session, String.join(" ", lCSSids), left.getId(), "CSSids"), new Sentence(session, String.join(" ", rCSSids), right.getId(), "CSSids"));
        double distanceClasses = uRZ(new Sentence(session, String.join(" ", lCSSclasses), left.getId(), "CSSclasses"), new Sentence(session, String.join(" ", rCSSclasses), right.getId(), "CSSclasses"));

        return createComparison(session, left, right, this, (distanceIds + distanceClasses)/2);
    }
}
