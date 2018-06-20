package main.java.pl.com.s396352.lsr.entities;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="SUBSEQUENCES")
public class Subsequences {
    int id;
    int wordId;
    String subsequence;
    int length;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="WORD_ID")
    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    @Column(name="SUBSEQUENCE")
    public String getSubsequence() {
        return subsequence;
    }

    public void setSubsequence(String subsequence) {
        this.subsequence = subsequence;
    }

    @Column(name="LENGTH")
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
