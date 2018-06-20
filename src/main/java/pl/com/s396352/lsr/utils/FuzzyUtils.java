package main.java.pl.com.s396352.lsr.utils;

import main.java.pl.com.s396352.lsr.businessObjects.Comparison;
import main.java.pl.com.s396352.lsr.businessObjects.Website;
import main.java.pl.com.s396352.lsr.comparators.*;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyUtils {
    public static Variable getUrlSimmilarity(FileUtils fu, Website w, Website w1) {
        FIS fis = fu.getUrlSimFis();
        FunctionBlock functionBlock = fis.getFunctionBlock("url_simmilarity");
        Comparison levenshteinComp = w.getComparisonWith(w1, new URLLevenshteinComparator());
        Comparison nGramComp = w.getComparisonWith(w1, new URLNgramComparator());
        functionBlock.setVariable("levenshtein", levenshteinComp.getResultNumerical());
        functionBlock.setVariable("ngram", nGramComp.getResultNumerical());
        // Evaluate
        fis.evaluate();
        // Show output variable's chart
        Variable simmilarity = functionBlock.getVariable("url_simmilarity");
        //JFuzzyChart.get().chart(simmilarity.getDefuzzifier(), w.getURL() + "vs" + w1.getURL(), true);
        // Print ruleSet
        return simmilarity;
    }

    public static Variable getStructuralSimmilarity(FileUtils fu, Website w, Website w1, Double urlSimmilarity) {
        FIS fis = fu.getStrSimFis();
        FunctionBlock functionBlock = fis.getFunctionBlock("structural_simmilarity");
        Comparison CSSComp = w.getComparisonWith(w1, new CSSComparator());
        functionBlock.setVariable("url", urlSimmilarity);
        functionBlock.setVariable("css", CSSComp.getResultNumerical());

        // Evaluate
        fis.evaluate();
        // Show output variable's chart
        Variable simmilarity = functionBlock.getVariable("structural_simmilarity");
        //JFuzzyChart.get().chart(simmilarity.getDefuzzifier(), w.getURL() + "vs" + w1.getURL(), true);
        // Print ruleSet
        return simmilarity;
    }

    public static Variable getGeneralSimmilarity(FileUtils fu, Website w, Website w1, Double structuralSimmilarity) {
        FIS fis = fu.getGenSimFis();
        FunctionBlock functionBlock = fis.getFunctionBlock("general_simmilarity");
        Comparison NgramComp = w.getComparisonWith(w1, new NgramTextComparator());
        Comparison KeywordComp = w.getComparisonWith(w1, new KeywordComparator());
        functionBlock.setVariable("structure", structuralSimmilarity);
        functionBlock.setVariable("text", NgramComp.getResultNumerical());

        // Evaluate
        fis.evaluate();
        // Show output variable's chart
        Variable simmilarity = functionBlock.getVariable("general_simmilarity");
        //JFuzzyChart.get().chart(simmilarity.getDefuzzifier(), w.getURL() + "vs" + w1.getURL(), true);
        // Print ruleSet
        return simmilarity;
    }

    public static void plotChart(Variable v, String title)
    {
        JFuzzyChart.get().chart(v.getDefuzzifier(), title, true);
    }

    public static String toNaturalLanguage(Variable v) {
        Double sim = v.defuzzify();
        if(sim < 0.33)
        {
            return "Websites are not simmilar in terms of " + v.getName();
        }
        else if(sim < 0.66)
        {
            return "Websites are somewhat simmilar in terms of " + v.getName();
        }
        else
        {
            return "Websites are simmilar in terms of " + v.getName();
        }
    }
}
