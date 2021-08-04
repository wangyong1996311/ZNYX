package com.mingrisoft.yysb.util;

import java.util.Date;

public class CardViewData {
    private String depotname;
    private String measuringtime;
    private String min;
    private String max;
    private String mean;
    public void setDepotname(String depotname) {
        this.depotname = depotname;
    }
    public String getDepotname() {
        return depotname;
    }

    public void setMeasuringtime(String measuringtime) {
        this.measuringtime = measuringtime;
    }
    public String getMeasuringtime() {
        return measuringtime;
    }

    public void setMin(String min) {
        this.min = min;
    }
    public String getMin() {
        return min;
    }

    public void setMax(String max) {
        this.max = max;
    }
    public String getMax() {
        return max;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
    public String getMean() {
        return mean;
    }

    public CardViewData(String depotname, String measuringtime, String min, String max, String mean) {
        this.depotname = depotname;
        this.measuringtime = measuringtime;
        this.min= min;
        this.max = max;
        this.mean = mean;
    }
}

