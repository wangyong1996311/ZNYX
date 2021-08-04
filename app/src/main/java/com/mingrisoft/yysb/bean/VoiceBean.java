package com.mingrisoft.yysb.bean;

import java.util.ArrayList;

public class VoiceBean {
    public ArrayList<wsBean> ws;

    public class wsBean {

        public ArrayList<cwBean> cw;

        public class cwBean {

            public String w;
        }
    }
}
