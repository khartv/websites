package main.java.pl.com.s396352.lsr.businessObjects;

import main.java.pl.com.s396352.lsr.entities.Sentences;
import main.java.pl.com.s396352.lsr.entities.Words;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Sentence {
    int id;
    int websiteId;
    String sentence;
    String type;
    List<Word> words = new ArrayList();

    public Sentence(Session session, String sentence, int websiteId, String type)
    {
        setWebsiteId(websiteId);
        setSentence(sentence);
        setType(type);
        try {


            Sentences s = new Sentences();
            s.setWebsiteId(websiteId);
            s.setSentence(sentence);
            s.setType(type);
            session.persist(s);
            setId(s.getId());

        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        if(type.equals("sentence")) {
            StringTokenizer tokenizer = new StringTokenizer(sentence, " \t\n\r\f,.:;?![]'");
            while (tokenizer.hasMoreTokens()) {
                words.add(new Word(session, tokenizer.nextToken(), getId()));
            }
        }
    }

    public Sentence(Session session, Sentences sentence) {
        setId(sentence.getId());
        setWebsiteId(sentence.getWebsiteId());
        setSentence(sentence.getSentence());
        setType(sentence.getType());

        String sql = "select w from Words w where w.sentenceId = :sentenceId";

        Query<Words> query = session.createQuery(sql);
        query.setParameter("sentenceId", getId());
        List<Words> ws = query.getResultList();

        List<Word> words = new ArrayList<>();

        for(Words w : ws)
        {
            words.add(new Word(session, w));
        }
        setWords(words);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(int websiteId) {
        this.websiteId = websiteId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "sentence='" + sentence + '\'' +
                ", words=" + words +
                '}';
    }
}
