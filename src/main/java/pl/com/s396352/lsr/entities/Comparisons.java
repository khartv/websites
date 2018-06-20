package main.java.pl.com.s396352.lsr.entities;

import main.java.pl.com.s396352.lsr.businessObjects.Website;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="COMPARISONS")
public class Comparisons {
    int id;
    int leftWebsiteId;
    int rightWebsiteId;
    String comparator;
    Double result;

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="LEFT_WEBSITE_ID")
    public int getLeftWebsiteId() {
        return leftWebsiteId;
    }

    public void setLeftWebsiteId(int leftWebsiteId) {
        this.leftWebsiteId = leftWebsiteId;
    }

    @Column(name="RIGHT_WEBSITE_ID")
    public int getRightWebsiteId() {
        return rightWebsiteId;
    }

    public void setRightWebsiteId(int rightWebsiteId) {
        this.rightWebsiteId = rightWebsiteId;
    }

    @Column(name="COMPARATOR")
    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    @Column(name="RESULT")
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
