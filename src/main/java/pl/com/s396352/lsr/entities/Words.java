package main.java.pl.com.s396352.lsr.entities;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "WORDS")
public class Words {
    private int id;
    private int sentenceId;
    private String word;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="SENTENCE_ID")
    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Column(name="WORD")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
