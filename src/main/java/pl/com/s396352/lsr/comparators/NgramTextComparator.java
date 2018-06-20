package main.java.pl.com.s396352.lsr.comparators;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Sentence;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.businessObjects.Word;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.util.List;

public class NgramTextComparator extends BaseComparator {
    @Override
    public Comparison compare(Session session, Website left, Website right) {
        List<Sentence> lSentences = left.getSentences();
        List<Sentence> rSentences = right.getSentences();

        double distance = 0;
        double d;
        double temp = 0;
        for(Sentence s1 : lSentences)
        {
            d = 0;
            for(Sentence s2 : rSentences)
            {
                temp = uRZ(s1, s2);
                if (temp > d) {
                    d = temp;
                    if(temp > 0.25)
                    {
//                        System.out.println(s1);
//                        System.out.println(s2);
//                        System.out.println(temp);
//                        System.out.println(d);
//                        System.out.println(distance);
                    }
                }
            }
            distance += d;
        }

//        System.out.println("=================================================================");
//        System.out.println("=================================================================");
//        System.out.println(distance/lSentences.size());
//        System.out.println("=================================================================");
//        System.out.println("=================================================================");
        return createComparison(session, left, right, this, distance/lSentences.size());
    }

    protected double uRZ(Sentence z1, Sentence z2)
    {
        double n = Math.max(n(z1), n(z2));

        double factor = (double)1/n;
        double sum = 0;

        for(int i = 1; i <= n(z1); i++)
        {
            double max = 0;
            for(int j = 1; j <= n(z2); j++)
            {
                Word s1 = z1.getWords().get(i - 1);
                Word s2 = z2.getWords().get(j - 1);
                if(uRSsym(s1, s2) > max)
                {
                    max = uRSsym(s1, s2);
                }
            }
            sum += max;
        }

        return factor * sum;
    }

    protected double uRSsym(Word s1, Word s2)
    {
        return Math.min(uRS(s1, s2), uRS(s2, s1));
    }

    private double uRS(Word s1, Word s2)
    {
        double n = Math.max(n(s1), n(s2));
        double factor = (double)2/(n*n + n);

        int sum = 0;
        for(int i = 1; i <= n(s1); i++)
        {
            for(int j = 1; j <= n(s1) - i + 1; j++)
            {
                sum += h(i, j, s1, s2);
            }
        }

        return factor * sum;
    }

    private int h(int i, int j, Word s1, Word s2)
    {
        if(i > n(s2) || i > n(s1))
        {
            return 0;
        }
        else
        {
            String tmp = s1.getSubsequences().get(i).get(j - 1);
            if(StringUtils.containsIgnoreCase(s2.getWord(), tmp)){
                return 1;
            }
            return 0;
        }
    }

    private int n(Sentence z)
    {
        return z.getWords().size();
    }

    private int n(Word s)
    {
        return s.getWord().length();
    }
}
