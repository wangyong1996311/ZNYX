package com.mingrisoft.yysb.bean;

import java.util.List;

public class Historybean {
    private int code;
    private List<Data> data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public void setData(List<Historybean.Data> data) {
        this.data = data;
    }
    public List<Historybean.Data> getData() {
        return data;
    }

    public class Hasdepots {

        private String depotname;
        private String max;
        private String mean;
        private String measuringtime;
        private String min;
        public void setDepotname(String depotname) {
            this.depotname = depotname;
        }
        public String getDepotname() {
            return depotname;
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

    }
    public class Data {

        private List<String> allopenids;
        private List<String> baddepots;
        private List<Historybean.Hasdepots> hasdepots;
        private List<String> hstdepots;
        private List<String> offdepots;
        private String pushmessage;
        private String username;
        public void setAllopenids(List<String> allopenids) {
            this.allopenids = allopenids;
        }
        public List<String> getAllopenids() {
            return allopenids;
        }

        public void setBaddepots(List<String> baddepots) {
            this.baddepots = baddepots;
        }
        public List<String> getBaddepots() {
            return baddepots;
        }

        public void setHasdepots(List<Historybean.Hasdepots> hasdepots) {
            this.hasdepots = hasdepots;
        }
        public List<Historybean.Hasdepots> getHasdepots() {
            return hasdepots;
        }

        public void setHstdepots(List<String> hstdepots) {
            this.hstdepots = hstdepots;
        }
        public List<String> getHstdepots() {
            return hstdepots;
        }

        public void setOffdepots(List<String> offdepots) {
            this.offdepots = offdepots;
        }
        public List<String> getOffdepots() {
            return offdepots;
        }

        public void setPushmessage(String pushmessage) {
            this.pushmessage = pushmessage;
        }
        public String getPushmessage() {
            return pushmessage;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public String getUsername() {
            return username;
        }
    }
}
