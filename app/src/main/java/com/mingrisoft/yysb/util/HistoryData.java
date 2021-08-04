package com.mingrisoft.yysb.util;

import com.mingrisoft.yysb.bean.Historybean;

import java.util.List;

public class HistoryData {
    private List<String> baddepots;
    private List<Historybean.Hasdepots> hasdepots;
    private List<String> hstdepots;
    private List<String> offdepots;
    private String pushmessage;



    public List<String> getBaddepots() {
        return baddepots;
    }

    public void setBaddepots(List<String> baddepots) {
        this.baddepots = baddepots;
    }

    public List<Historybean.Hasdepots> getHasdepots() {
        return hasdepots;
    }

    public void setHasdepots(List<Historybean.Hasdepots> hasdepots) {
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

    public HistoryData(List<String> baddepots, List<Historybean.Hasdepots> hasdepots, List<String> hstdepots, List<String> offdepots, String pushmessage) {
        this.baddepots = baddepots;
        this.hasdepots = hasdepots;
        this.hstdepots = hstdepots;
        this.offdepots = offdepots;
        this.pushmessage = pushmessage;
    }
}
