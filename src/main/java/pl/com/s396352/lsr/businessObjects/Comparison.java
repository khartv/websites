package main.java.pl.com.s396352.lsr.businessObjects;

import main.java.pl.com.s396352.lsr.comparators.BaseComparator;
import main.java.pl.com.s396352.lsr.comparators.ComparatorIf;
import main.java.pl.com.s396352.lsr.entities.Comparisons;
import main.java.pl.com.s396352.lsr.entities.Websites;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class Comparison {
    private static int counter = 0;

    private int id;
    private ComparatorIf comparator;
    private Website left;
    private Website right;
    private String result;
    private double resultNumerical;

    public Comparison(Session session, Website left, Website right, ComparatorIf comparator, Double numResult)
    {
        this.id = counter;
        counter++;
        this.left = left;
        this.right = right;
        this.comparator = comparator;
        this.resultNumerical = numResult;

        Comparisons c = new Comparisons();
        c.setLeftWebsiteId(left.getId());
        c.setRightWebsiteId(right.getId());
        c.setComparator(comparator.getClass().getName());
        c.setResult(numResult);
        session.persist(c);
        setId(c.getId());
    }

    public Comparison(Session session, Comparisons comparisons) {
        setId(comparisons.getId());
        String sql = "select w from Websites w where w.id = :id";

        Query<Websites> query = session.createQuery(sql);
        query.setParameter("id", comparisons.getLeftWebsiteId());
        List<Websites> ws = query.getResultList();
        String url = ws.get(0).getUrl();
        setLeft(new Website(session, url));
        query.setParameter("id", comparisons.getRightWebsiteId());
        ws = query.getResultList();
        url = ws.get(0).getUrl();
        setRight(new Website(session, url));


        try {
            setComparator((ComparatorIf) Class.forName(comparisons.getComparator()).newInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        setResultNumerical(comparisons.getResult());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Website getLeft() {
        return left;
    }

    public void setLeft(Website left) {
        this.left = left;
    }

    public Website getRight() {
        return right;
    }

    public void setRight(Website right) {
        this.right = right;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getResultNumerical() {
        return resultNumerical;
    }

    public void setResultNumerical(double resultNumerical) {
        this.resultNumerical = resultNumerical;
    }

    public ComparatorIf getComparator() {
        return comparator;
    }

    public void setComparator(ComparatorIf comparator) {
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        return "Comparison{\n" +
                "\tid=" + id +
                ", \n\tcomparator=" + comparator.getClass().getName() +
                ", \n\tleft=" + left +
                ", \n\tright=" + right +
                ",\n \tresult='" + result + '\'' +
                ", \tresultNumerical=" + resultNumerical +
                "\n}";
    }
}
