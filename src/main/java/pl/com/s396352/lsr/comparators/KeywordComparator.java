package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.businessObjects.Word;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

public class KeywordComparator extends BaseComparator {

    @Override
    public Comparison compare(Session session, Website left, Website right) {
        List leftWords = left.getSentences().stream().map(s -> s.getWords()).collect(Collectors.toList());
        return createComparison(session, left, right, this, (double)leftWords.size());
    }

    private int ff(Word w, Website ws)
    {
        List words = ws.getSentences().stream().map(s -> s.getWords().stream().filter(wrd -> wrd.getWord().equals(w.getWord()))).collect(Collectors.toList());
        return words.size();
    }

    private Double fp(Word w, Website ws)
    {
        return null;
    }

    private int nTot(Website w)
    {
        return w.getSentences().stream().map(s -> s.getWords()).collect(Collectors.toList()).size();
    }
}
