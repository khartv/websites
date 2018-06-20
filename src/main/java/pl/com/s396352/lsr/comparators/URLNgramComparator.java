package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Sentence;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.businessObjects.Word;
import org.hibernate.Session;

public class URLNgramComparator extends NgramTextComparator {
    @Override
    public Comparison compare(Session session, Website left, Website right) {
        Sentence lURL = new Sentence(session, left.getURL(), left.getId(), "url");
        Sentence rURL = new Sentence(session, right.getURL(), right.getId(), "url");

        double distance = uRSsym(new Word(session, lURL.getSentence(), lURL.getId()), new Word(session, rURL.getSentence(), rURL.getId()));

        return createComparison(session, left, right, this, distance);
    }
}
