package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.entities.Comparisons;
import main.java.pl.com.s396352.lsr.entities.Websites;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public abstract class BaseComparator implements ComparatorIf {
    @Override
    public abstract Comparison compare(Session session, Website left, Website right);

    public Comparison makeCompare(Session session, Website left, Website right)
    {
        String sql = "select c from Comparisons c where c.comparator = :comparator AND (c.leftWebsiteId = :leftId OR c.rightWebsiteId = :rightId)";

        Query<Comparisons> query = session.createQuery(sql);
        query.setParameter("comparator", this.getClass().getName());
        query.setParameter("leftId", left.getId());
        query.setParameter("rightId", right.getId());
        List<Comparisons> cs = query.getResultList();

        if(cs.isEmpty())
        {
            return compare(session, left, right);
        }
        else
        {
            return createComparison(session, cs.get(0));
        }
    }

    protected Comparison createComparison(Session session, Comparisons comparisons) {
        Comparison c = new Comparison(session, comparisons);

        String sql = "select w from Websites w where w.id = :id";

        Query<Websites> query = session.createQuery(sql);
        query.setParameter("id", comparisons.getLeftWebsiteId());
        List<Websites> ws = query.getResultList();
        String url = ws.get(0).getUrl();
        new Website(session, url).addComparison(c);
        query.setParameter("id", comparisons.getRightWebsiteId());
        ws = query.getResultList();
        url = ws.get(0).getUrl();
        new Website(session, url).addComparison(c);

        return c;
    }

    protected Comparison createComparison(Session session, Website left, Website right, ComparatorIf comparator, Double numResult)
    {


        Comparison c = new Comparison(session, left, right, comparator, numResult);

        left.addComparison(c);
        right.addComparison(c);

        return c;
    }

    public boolean equals(Object o)
    {
        return o.getClass().getName().equals(this.getClass().getName());
    }


}
