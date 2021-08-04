package com.mingrisoft.yysb.util;

import com.google.gson.Gson;
import com.mingrisoft.yysb.bean.VoiceBean;

import java.util.ArrayList;

public class ParsonActivity {
    public static String parseData(String resultString) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(resultString, VoiceBean.class);

        StringBuffer sb = new StringBuffer();
        ArrayList<VoiceBean.wsBean> ws = voiceBean.ws;
        for (VoiceBean.wsBean wsBean : ws) {
            ArrayList<VoiceBean.wsBean.cwBean> cw = wsBean.cw;
            String word = cw.get(0).w;
            sb.append(word);
        }

        return sb.toString();
    }
}
