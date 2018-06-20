package main.java.pl.com.s396352.lsr.entities;


import javax.persistence.*;

import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "WEBSITES")
public class Websites {
    private int id;
    private String url;
    private String text;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "TEXT")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Websites{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
