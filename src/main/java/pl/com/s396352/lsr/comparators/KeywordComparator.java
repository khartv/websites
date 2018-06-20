package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Sentence;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.entities.Words;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class KeywordComparator extends NgramTextComparator {

    @Override
    public Comparison compare(Session session, Website left, Website right) {
        String sql = "select w from Words w where w.sentenceId in (select s.id from Sentences s where s.websiteId = :websiteId and s.type = 'sentence')";

        Query<Words> query = session.createQuery(sql);
        query.setParameter("websiteId", left.getId());
        List<Words> ws = query.getResultList();

        for (Words w : ws) {
            w.setImportance(r(ff(session, w, left), fp(session, w, left), nTot(session, left)));
            session.persist(w);
        }

        query.setParameter("websiteId", right.getId());
        ws = query.getResultList();

        for (Words w : ws) {
            w.setImportance(r(ff(session, w, right), fp(session, w, right), nTot(session, right)));
            session.persist(w);
        }

        sql = "select w from Words w where w.sentenceId in (select s.id from Sentences s where s.websiteId = :websiteId and s.type = 'sentence') order by w.importance desc";
        query = session.createQuery(sql);
        query.setParameter("websiteId", left.getId());
        query.setMaxResults(10);
        List<Words> leftTop10 = query.getResultList();
        query.setParameter("websiteId", right.getId());
        query.setMaxResults(10);
        List<Words> rightTop10 = query.getResultList();

        double distanceTopWords = uRZ(new Sentence(session, String.join(" ", leftTop10.stream().map(w -> w.getWord()).collect(Collectors.toList())), left.getId(), "topWords"),
                new Sentence(session, String.join(" ", rightTop10.stream().map(w -> w.getWord()).collect(Collectors.toList())), right.getId(), "topWords"));

        return createComparison(session, left, right, this, distanceTopWords);
    }

    private double ff(Session session, Words w, Website ws)
    {
        String sql = "select w from Words w where w.sentenceId in (select s.id from Sentences s where s.websiteId = :websiteId and s.type = 'sentence') and w.word = :word";

        Query<Words> query = session.createQuery(sql);
        query.setParameter("word", w.getWord());
        query.setParameter("websiteId", ws.getId());

        List<Words> wrds = query.getResultList();

        return wrds.size();
    }

    private Double fp(Session session, Words w, Website ws)
    {
        String sql = "select w from Words w where w.sentenceId in (select s.id from Sentences s where s.websiteId = :websiteId and s.type = 'sentence') and w.word = :word order by w.id";

        Query<Words> query = session.createQuery(sql);
        query.setParameter("websiteId", ws.getId());
        query.setParameter("word", w.getWord());
        List<Words> wrds = query.getResultList();

        Integer firstId = wrds.get(0).getId();
        Integer lastId = wrds.get(wrds.size() - 1).getId();
        double score = 0, counter = 0;
        for (Words wrd : wrds) {
            counter++;
            if (wrd.getId() <= firstId + (lastId - firstId) / 4 || wrd.getId() >= firstId + 3 * (lastId - firstId) / 4) {
                score += 3;
            } else {
                score += 1;
            }
        }
        return score / counter;
    }

    private double nTot(Session session, Website w)
    {
        String sql = "select w from Words w where w.sentenceId in (select s.id from Sentences s where s.websiteId = :websiteId and s.type = 'sentence')";

        Query<Words> query = session.createQuery(sql);
        query.setParameter("websiteId", w.getId());
        List<Words> ws = query.getResultList();

        return ws.size();
    }

    private double r(double ff, double fp, double nTot) {
        return (ff + fp) / (2 * nTot);
    }
}
