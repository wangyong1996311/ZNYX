package com.mingrisoft.yysb.util;

import com.mingrisoft.yysb.bean.Lastbean;

import java.util.List;

public class NumberData {
    private List<String> baddepots;
    private List<Lastbean.Hasdepots> hasdepots;
    private List<String> hstdepots;
    private List<String> offdepots;
    private String pushmessage;

    public List<String> getBaddepots() {
        return baddepots;
    }

    public void setBaddepots(List<String> baddepots) {
        this.baddepots = baddepots;
    }

    public List<Lastbean.Hasdepots> getHasdepots() {
        return hasdepots;
    }

    public void setHasdepots(List<Lastbean.Hasdepots> hasdepots) {
        this.hasdepots = hasdepots;
    }

    public List<String> getHstdepots() {
        return hstdepots;
    }

    public void setHstdepots(List<String> hstdepots) {
        this.hstdepots = hstdepots;
    }

    public List<String> getOffdepots() {
        return offdepots;
    }

    public void setOffdepots(List<String> offdepots) {
        this.offdepots = offdepots;
    }
    public String getPushmessage() {
        return pushmessage;
    }
    public void setPushmessage(String pushmessage) {
        this.pushmessage = pushmessage;
    }

    public NumberData(List<String> baddepots, List<Lastbean.Hasdepots> hasdepots, List<String> hstdepots, List<String> offdepots, String pushmessage) {
        this.baddepots = baddepots;
        this.hasdepots = hasdepots;
        this.hstdepots = hstdepots;
        this.offdepots = offdepots;
        this.pushmessage = pushmessage;
    }
}
