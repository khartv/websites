package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import org.hibernate.Session;

public interface ComparatorIf {
    Comparison compare(Session session, Website left, Website right);
}
