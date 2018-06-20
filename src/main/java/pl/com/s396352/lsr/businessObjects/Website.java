package main.java.pl.com.s396352.lsr.businessObjects;

import main.java.pl.com.s396352.lsr.comparators.ComparatorIf;
import main.java.pl.com.s396352.lsr.entities.Sentences;
import main.java.pl.com.s396352.lsr.entities.Websites;
import main.java.pl.com.s396352.lsr.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Collectors;

public class Website
{
    private static int counter = 0;
    private int id;
    private Document doc;
    private List<Element> nodes;
    private List<Comparison> comparisons = new ArrayList<>();
    private List<Website> comparedSites = new ArrayList<>();
    private String URL;
    private Set<String> CSSids = new TreeSet<>();
    private Set<String> CSSclasses = new TreeSet<>();
    private String text;
    private List<Sentence> sentences = new ArrayList<>();

    public Website(Session session, String url)
    {
        this.URL = url;
        this.id = counter;
        this.counter++;
        try
        {
            this.doc = Jsoup.connect(url).get();
            findAttributes(this.getDoc().body());

            CSSids.remove("");
            CSSclasses.remove("");
        }
        catch (IOException e)
        {
            System.out.println(URL);
            e.printStackTrace();
        }

        try {
            String sql = "select w from Websites w where w.url = :url";

            Query<Websites> query = session.createQuery(sql);
            query.setParameter("url", URL);
            List<Websites> ws = query.getResultList();

            if(ws.isEmpty()) {

                Websites w = new Websites();
                w.setUrl(URL);
                w.setText(text);
                session.persist(w);
                setId(w.getId());
                findSentences(session, text);
            }
            else
            {
                System.out.println(ws);
                setId(ws.get(0).getId());
                setSentences(findSentences(session));
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    private void findSentences(Session session, String text) {
        BreakIterator bi = BreakIterator.getSentenceInstance();
        bi.setText(text);
        int index = 0;
        while (bi.next() != BreakIterator.DONE) {
            String sentence = text.substring(index, bi.current());
            sentences.add(new Sentence(session, sentence, getId(), "sentence"));
            index = bi.current();
        }
    }

    private List<Sentence> findSentences(Session session)
    {
        String sql = "select s from Sentences s where s.websiteId = :websiteId and s.type = :type";

        Query<Sentences> query = session.createQuery(sql);
        query.setParameter("websiteId", getId());
        query.setParameter( "type", "sentence");
        List<Sentences> ss = query.getResultList();
        System.out.println(ss);
        List<Sentence> sentences = new ArrayList<>();

        for(Sentences s : ss)
        {
            sentences.add(new Sentence(session, s));
        }

        return sentences;
    }

    private void findAttributes(Element node) {
        CSSids.addAll(Arrays.asList(node.attr("id").split(" ")));
        CSSclasses.addAll(Arrays.asList(node.attr("class").split(" ")));
        if(node.text().length() > 0 && text == null)
        {
            text = node.text();
        }
        if(!node.children().isEmpty())
        {
            node.children().stream().forEach(this::findAttributes);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public List<Element> getNodes() {
        return nodes;
    }

    public void setNodes(List<Element> nodes) {
        this.nodes = nodes;
    }

    public List<Comparison> getComparisons() {
        return comparisons;
    }

    public void setComparisons(List<Comparison> comparisons) {
        this.comparisons = comparisons;
    }

    public void addComparison(Comparison c)
    {
        this.comparisons.add(c);
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Set<String> getCSSids() {
        return CSSids;
    }

    public void setCSSids(Set<String> CSSids) {
        this.CSSids = CSSids;
    }

    public Set<String> getCSSclasses() {
        return CSSclasses;
    }

    public void setCSSclasses(Set<String> CSSclasses) {
        this.CSSclasses = CSSclasses;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public List<Website> getComparedSites() {
        return comparedSites;
    }

    public void setComparedSites(List<Website> comparedSites) {
        this.comparedSites = comparedSites;
    }

    public List<Comparison> getComparisonsForWebsite(Website w)
    {
        return comparisons.stream().filter(c -> c.getRight().equals(w) || c.getLeft().equals(w)).collect(Collectors.toList());
    }

    public Comparison getComparisonWith(Website w, ComparatorIf comp)
    {
        System.out.println(comp.getClass().getName());
        System.out.println(this.URL);
        System.out.println(w.getURL());
        List<Comparison> comparisonsForWebsite = getComparisonsForWebsite(w);
        System.out.println(comparisonsForWebsite);

        return comparisonsForWebsite.stream().filter(c -> c.getComparator().getClass().getName().equals(comp.getClass().getName())).findAny().get();
    }

    @Override
    public String toString() {
        return "Website{" +
                "id=" + id +
                ", URL='" + URL + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Website website = (Website) o;
        return id == website.id &&
                Objects.equals(doc, website.doc) &&
                Objects.equals(nodes, website.nodes) &&
                Objects.equals(URL, website.URL) &&
                Objects.equals(CSSids, website.CSSids) &&
                Objects.equals(CSSclasses, website.CSSclasses) &&
                Objects.equals(text, website.text) &&
                Objects.equals(sentences, website.sentences);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, doc, nodes, URL, CSSids, CSSclasses, text, sentences);
    }
}
