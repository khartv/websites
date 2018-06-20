package main.java.pl.com.s396352.lsr.businessObjects;

import main.java.pl.com.s396352.lsr.entities.Sentences;
import main.java.pl.com.s396352.lsr.entities.Subsequences;
import main.java.pl.com.s396352.lsr.entities.Words;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {
    int id;
    int sentenceId;
    String word;
    Map<Integer, List<String>> subsequences = new HashMap<>();

    public Word(Session session, String word, int sentenceId)
    {
        setSentenceId(sentenceId);
        setWord(word);

        for(int i = 1; i <= word.length(); i++)
        {
            List<String> tmp = createSubsequences(i);
            this.subsequences.put(i, tmp);
        }

        try {
            Words w = new Words();
            w.setSentenceId(sentenceId);
            w.setWord(word);
            session.persist(w);
            setId(w.getId());

            for(Map.Entry<Integer, List<String>> entry : subsequences.entrySet())
            {
                for(String value : entry.getValue())
                {
                    Subsequences s = new Subsequences();
                    s.setWordId(getId());
                    s.setSubsequence(value);
                    s.setLength(entry.getKey());
                    session.persist(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public Word(Session session, Words word)
    {
        setId(word.getId());
        setSentenceId(word.getSentenceId());
        setWord(word.getWord());

        String sql = "select s from Subsequences s where s.wordId = :wordId";

        Query<Subsequences> query = session.createQuery(sql);
        query.setParameter("wordId", getId());
        List<Subsequences> ss = query.getResultList();

        for(Subsequences s : ss)
        {
            List<String> tmp = subsequences.get(s.getLength());
            if(tmp == null)
            {
                tmp = new ArrayList<>();
            }
            tmp.add(s.getSubsequence());
            subsequences.put(s.getLength(), tmp);
        }
    }

    private List<String> createSubsequences(int length) {
        List<String> result = new ArrayList<>();

        for(int i = 0; i <= word.length() - length; i++)
        {
            String tmp = word.substring(i, i + length);
            result.add(tmp);
        }

        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<Integer, List<String>> getSubsequences() {
        return subsequences;
    }

    public void setSubsequences(Map<Integer, List<String>> subsequences) {
        this.subsequences = subsequences;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                //", subsequences=" + subsequences +
                '}';
    }
}
