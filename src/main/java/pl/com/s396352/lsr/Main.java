package main.java.pl.com.s396352.lsr;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Word;
import main.java.pl.com.s396352.lsr.comparators.*;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.entities.Comparisons;
import main.java.pl.com.s396352.lsr.entities.Words;
import main.java.pl.com.s396352.lsr.utils.FileUtils;
import main.java.pl.com.s396352.lsr.utils.FuzzyUtils;
import main.java.pl.com.s396352.lsr.utils.HibernateUtil;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        FileUtils fu = new FileUtils();
        List<String> URLList = fu.getURLsList();
        List<Website> websites = new ArrayList<>();
        List<BaseComparator> comparators = getComparators();


        SessionFactory factory = HibernateUtil.getSessionFactory();

        Session session = factory.getCurrentSession();
        try
        {
            session.getTransaction().begin();
            for(String s : URLList)
            {
                    Website w = new Website(session, s);
                    websites.add(w);
            }
            session.getTransaction().commit();
            session = factory.getCurrentSession();
            session.getTransaction().begin();
            for (Website w1 : websites) {
                for (Website w2 : websites) {
                    if (!w1.equals(w2) && !(w1.getComparedSites().contains(w2) || w2.getComparedSites().contains(w1))) {
                        for (BaseComparator c : comparators) {
                            c.compare(session, w1, w2);
                        }
                        w1.getComparedSites().add(w2);
                        w2.getComparedSites().add(w1);
                    }
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }

        Scanner keyboard = new Scanner(System.in);

        String input = "";
        Integer id1, id2;
        Website w1 = null, w2 = null;
        while(!"exit".equals(input))
        {
            System.out.println("Which site to compare?");
            for(Website w : websites) {
                System.out.println(w.getId() + ". " + w.getURL());
            }
            input = keyboard.nextLine();
            id1 = Integer.parseInt(input);

            System.out.println("With which?");
            for(Website w : websites) {
                System.out.println(w.getId() + ". " + w.getURL());
            }
            input = keyboard.nextLine();
            id2 = Integer.parseInt(input);
            if(id1 == id2)
            {
                System.out.println("Same site.");
                continue;
            }
            for(Website w : websites)
            {
                if(w.getId() == id1)
                {
                    w1 = w;
                }
                if(w.getId() == id2) {
                    w2 = w;
                }
            }
            Variable urlSimmilarity = FuzzyUtils.getUrlSimmilarity(fu, w1, w2);
            Variable structuralSimmilarity = FuzzyUtils.getStructuralSimmilarity(fu, w1, w2, urlSimmilarity.defuzzify());
            Variable generalSimmilarity = FuzzyUtils.getGeneralSimmilarity(fu, w1, w2, structuralSimmilarity.defuzzify());

            FuzzyUtils.plotChart(urlSimmilarity, "URL simmilarity");
            FuzzyUtils.plotChart(structuralSimmilarity, "URL simmilarity");
            FuzzyUtils.plotChart(generalSimmilarity, "URL simmilarity");
            List<Comparison> comparison = w1.getComparisonsForWebsite(w2);
            System.out.println(comparison);

            System.out.println(FuzzyUtils.toNaturalLanguage(urlSimmilarity));
            System.out.println(FuzzyUtils.toNaturalLanguage(structuralSimmilarity));
            System.out.println(FuzzyUtils.toNaturalLanguage(generalSimmilarity));
        }
    }



    private static List<BaseComparator> getComparators()
    {
        List<BaseComparator> result = new ArrayList<>();
        //result.add(new KeywordComparator());
        result.add(new URLLevenshteinComparator());
        result.add(new URLNgramComparator());
        result.add(new NgramTextComparator());
        result.add(new CSSComparator());

        return result;
    }
}
