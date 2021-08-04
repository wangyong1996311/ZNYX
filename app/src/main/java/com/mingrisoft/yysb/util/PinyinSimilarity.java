package com.mingrisoft.yysb.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

public class PinyinSimilarity {
    String[] englishPinYin26 = {
            "EI1", "BI4", "SEI4", "DI4", "YI4", "EFU1", "JI4",
            "EIQI1", "AI4", "JEI4", "KEI4", "EOU1", "EMEN1", "EN1",
            "OU1", "PI1", "KIU1", "A4", "ESI1", "TI4",
            "YOU4", "WEI4", "DABULIU3", "EKESI1", "WAI4", "ZEI4"
    };
    String englishString26 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String numberStringArabic = "0123456789";
    String numberString = "零一二三四五六七八九十百点";
    String specialHanziString =  " 号仓 号仓温度 昨日温度 正常 粮站  前日  测量故障 仓库 开路 关机 正常测量的粮仓 号仓近两天平均温度变化 号仓近两天最高温度变化 号仓近两天最高温度变化 温度变化 历史记录 测控故障 " +
            "仓库工作状态  测控详情   号仓一周来平均温度变化 号仓一周来最低温度变化  号仓一周来最高温度变化 仓库工作情况 最新数据 最新温度 号仓前日温度 号仓一周前温度  一周前号仓温度 粮站有多少测控故障的仓库 粮站\n" +
            "公司\n" +
            "多少\n" +
            "几个\n" +
            "粮仓\n" +
            "仓库\n" +
            "正常\n" +
            "关机\n" +
            "故障\n" +
            "没有测量\n" +
            "没有回数据\n" +
            "一共\n" +
            "温度测控详情\n" +
            "正常测量的仓库\n" +
            "正常测量的粮仓\n" +
            "温度正常测量的的仓库\n" +
            "温度正常测量的粮仓\n" +
            "未回数据的仓库\n" +
            "没有测量的仓库有哪些\n" +
            "关机的仓库\n" +
            "测控故障的仓库\n" +
            "采集点开路的仓库\n" +
            "号仓温度\n" +
            "号仓的温度情况\n" +
            "最新数据\n" +
            "最新温度\n" +
            "温度情况\n" +
            "仓库工作状态\n" +
            "仓库工作情况\n" +
            "历史信息\n" +
            "历史记录\n" +
            "号仓库近两天最高温度变化\n" +
            "号粮仓近两天最高温度变化\n" +
            "号仓近两天最高温度变化\n" +
            "号仓库近两天平均温度变化\n" +
            "号粮仓近两天平均温度变化\n" +
            "号仓近两天平均温度变化\n" +
            "号仓库近两天最低温度变化\n" +
            "号粮仓近两天最低温度变化\n" +
            "号仓近两天最低温度变化\n" +
            "号仓库一周来最高温度变化\n" +
            "号粮仓一周来最高温度变化\n" +
            "号仓一周来最高温度变化\n" +
            "号仓库一周来平均温度变化\n" +
            "号粮仓一周来平均温度变化\n" +
            "号仓一周来平均温度变化\n" +
            "号仓库一周来最低温度变化\n" +
            "号粮仓一周来最低温度变化\n" +
            "号仓一周来最低温度变化\n" +
            "号仓昨天温度\n" +
            "号仓昨日温度\n" +
            "号仓前天温度\n" +
            "号仓前日温度\n" +
            "号仓一周前温度\n" +
            "一周前号仓温度\n" +
            "历史温度\n" +
            "温度变化\n"+
            "1号仓 2号仓 3号仓 4号仓 5号仓 6号仓 7号仓 8号仓 9号仓 10号仓"
            ;
    String myCharAll = numberString + specialHanziString;

    List<String> numberPinYin = new ArrayList<String>(20);//数字的拼音(10)
    List<String> specialHanziPinYin = new ArrayList<String>(10);//特定汉字集的拼音（除了中文的数字之外的）
    List<String> myCharAllPinYin = new ArrayList<String>(40);//所有拼音的集合

    boolean fuzzyMatching = true;//是否开启模糊匹配功能

    public PinyinSimilarity(boolean fuzzyMatching) {
        this.fuzzyMatching = fuzzyMatching;
        init();
    }

    //拼音中有音标
    //初始化目标汉字集的拼音列表
    public void init() {
        try {
            String str;
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

            str = numberString;//数字
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                String[] vals = PinyinHelper.toHanyuPinyinStringArray(c, format);
                numberPinYin.add(vals[0]);
            }

            str = specialHanziString;//汉字
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                String[] vals = PinyinHelper.toHanyuPinyinStringArray(c, format);
                specialHanziPinYin.add(vals[0]);
            }

            myCharAllPinYin.addAll(numberPinYin);
            myCharAllPinYin.addAll(specialHanziPinYin);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String changeOurWordsWithPinyin(String input) {
        String output = input;
        try {

            //处理符号：不关注符合，遇到，就去掉（要保留小数点）
            output = changeWordProcessSignal(output);

            //处理英文字母：转大写
            output = changeWordProcessEnglish(output);

            //所有汉字进行相似替换
            //LogUtil.logWithMethod(new Exception(),"input.length()="+input.length());
            int index;
            String str;
            String strChanged;
            StringBuilder strBuilder = new StringBuilder();
            for (index = 0; index < input.length(); index++) {
                str = input.substring(index, index + 1);
                strChanged = changeOneWord(str);
                strBuilder.append(strChanged);
            }

            output = strBuilder.toString();
            //LogUtil.logWithMethod(new Exception(),"after changeAllWord: output="+output);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String changeWordProcessSignal(String strInput) {
        String strOutput = strInput;

        //去掉 ，。空格-
        strOutput = strOutput.replace("，", "");
        strOutput = strOutput.replace("。", "");
        strOutput = strOutput.replace("-", "");
        strOutput = strOutput.replace(" ", "");

        return strOutput;
    }

    public static String changeWordProcessEnglish(String strInput) {
        String strOutput = strInput;

        //转大写
        strOutput = strOutput.toUpperCase();

        return strOutput;
    }

    //尾字如果是汉字，进行拼音相同字的替换（零不能替换，可以先转换为0）
    public  String changeOneWord(String strInput) {
        //若已经在目标集合中了，就不需要转换了
        if (numberString.contains(strInput) || numberStringArabic.contains(strInput)) {
            //LogUtil.logWithMethod(new Exception(),"is number");
            return strInput;
        } else if (specialHanziString.contains(strInput)) {
            //LogUtil.logWithMethod(new Exception(),"is specialHanziString");
            return strInput;
        }

        String strChanged;
        List<String> listEnglishPinYin = new ArrayList<String>();

        strChanged = changeWord(strInput, numberPinYin, numberString);
        if (numberString.contains(strChanged)) {
            //LogUtil.logWithMethod(new Exception(),"is number");
            return strChanged;
        }

        return changeWord(strInput, specialHanziPinYin, specialHanziString);
    }
    private  String changeWord(String strInput, List<String> listPinYin, String strSource) {

        //先判断输入，是什么类型的字符：数字、字母、汉字
        String strOutput="";

        String str=strInput.substring(0,1);
        String strPinyin = "";
        boolean flagGetPinyin=false;

        try{

            if(str.matches("^[A-Z]{1}$")){

                strPinyin = englishPinYin26[englishString26.indexOf(str)];
                //LogUtil.logWithMethod(new Exception(), "str="+str+" Pinyin="+strPinyin );
                flagGetPinyin = true;
            }
            else if(str.matches("^[0-9]{1}$")){

                strPinyin = numberPinYin.get(numberString.indexOf(str));
                //LogUtil.logWithMethod(new Exception(), "str="+str+" Pinyin="+strPinyin );
                flagGetPinyin = true;
            }
            else if(str.matches("^[\u4e00-\u9fa5]{1}$")){

                HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
                format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
                format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

                char c = str.charAt(0);
                String[] vals = PinyinHelper.toHanyuPinyinStringArray(c, format);

                strPinyin=vals[0];//token.target;
                flagGetPinyin = true;
            }
            if(flagGetPinyin){
                //在目标拼音集合中查找匹配项
                int num=listPinYin.indexOf(strPinyin);
                if(num>=0){ //拼音精确匹配成功
                    return strSource.substring(num, num+1);
                } else {
                    if(fuzzyMatching){//若开启了模糊匹配
                        //声母替换
                        String strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串
                        strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
                        boolean flagReplacedHeadString = (strPinyinFuzzy==null)?false:true;
                        if(flagReplacedHeadString){
                            num=listPinYin.indexOf(strPinyinFuzzy);
                            if(num>=0){ //拼音模糊匹配成功
                                //LogUtil.logWithMethod(new Exception(), "fuzzy match: "+strPinyinFuzzy+" num="+num);
                                return strSource.substring(num, num+1);
                            }
                        }
                        //韵母替换
                        strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串，不使用声母替换后的字符串
                        strPinyinFuzzy = replaceTailString(strPinyinFuzzy);
                        boolean flagReplacedTailString = (strPinyinFuzzy==null)?false:true;
                        if(flagReplacedTailString){
                            num=listPinYin.indexOf(strPinyinFuzzy);
                            if(num>=0){ //拼音模糊匹配成功
                                //LogUtil.logWithMethod(new Exception(), "fuzzy match: "+strPinyinFuzzy+" num="+num);
                                return strSource.substring(num, num+1);
                            }
                        }
                        //声母韵母都替换
                        if(flagReplacedHeadString && flagReplacedTailString){
                            strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
                            num=listPinYin.indexOf(strPinyinFuzzy);
                            if(num>=0){ //拼音模糊匹配成功
                                //LogUtil.logWithMethod(new Exception(), "fuzzy match: "+strPinyinFuzzy+" num="+num);
                                return strSource.substring(num, num+1);
                            }
                        }
                        strPinyin=strPinyin.substring(0, strPinyin.length()-1);
                        strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串
                        num=findPinyin(strPinyinFuzzy,listPinYin);
                        if(num>=0){ //拼音模糊匹配成功
                            return strSource.substring(num, num+1);
                        }
                        //声母替换
                        strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
                        flagReplacedHeadString = (strPinyinFuzzy==null)?false:true;
                        if(flagReplacedHeadString){
                            num=findPinyin(strPinyinFuzzy,listPinYin);
                            if(num>=0){ //拼音模糊匹配成功
                                return strSource.substring(num, num+1);
                            }
                        }
                        //韵母替换
                        strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串，不使用声母替换后的字符串
                        strPinyinFuzzy = replaceTailString(strPinyinFuzzy);
                        flagReplacedTailString = (strPinyinFuzzy==null)?false:true;
                        if(flagReplacedTailString){
                            num=findPinyin(strPinyinFuzzy,listPinYin);
                            if(num>=0){ //拼音模糊匹配成功
                                return strSource.substring(num, num+1);
                            }
                        }
                        //声母韵母都替换
                        if(flagReplacedHeadString && flagReplacedTailString){
                            strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
                            num=findPinyin(strPinyinFuzzy,listPinYin);
                            if(num>=0){ //拼音模糊匹配成功
                                //LogUtil.logWithMethod(new Exception(), "fuzzy match: "+strPinyinFuzzy+" num="+num);
                                return strSource.substring(num, num+1);
                            }
                        }
                        return str;
                    } else {
                        return str;
                    }
                }
            } else {//若该字符没有找到相应拼音，使用原字符
                //strOutput = strInput;
                strOutput = strInput;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return strOutput;
    }
    private static String replaceHeadString(String strPinyin){

        //声母替换
        String strReplaced = null;
        if(strPinyin.contains("ZH")){
            strReplaced = strPinyin.replace("ZH", "Z");
        } else if(strPinyin.contains("CH")){
            strReplaced = strPinyin.replace("CH", "C");
        } else if(strPinyin.contains("SH")){
            strReplaced = strPinyin.replace("SH", "S");
        }
        else if(strPinyin.contains("Z")){
            strReplaced = strPinyin.replace("Z", "ZH");
        } else if(strPinyin.contains("C")){
            strReplaced = strPinyin.replace("C", "CH");
        } else if(strPinyin.contains("S")){
            strReplaced = strPinyin.replace("S", "SH");
        }
        else if(strPinyin.contains("L")){
            strReplaced = strPinyin.replace("L", "N");
        } else if(strPinyin.indexOf('N')==0){ //n有在后面的，n只在做声母时易混
            strReplaced = strPinyin.replace("N", "L");
        } else {
            return null;
        }

        //LogUtil.logWithMethod(new Exception(),"strReplaced="+strReplaced);
        return strReplaced;//flagReplaced;

    }
    private static String replaceTailString(String strPinyin) {

        // 韵母替换
        String strReplaced = null;
        if (strPinyin.contains("ANG")) {
            strReplaced = strPinyin.replace("ANG", "AN");
        } else if (strPinyin.contains("ENG")) {
            strReplaced = strPinyin.replace("ENG", "EN");
        } else if (strPinyin.contains("ING")) {
            strReplaced = strPinyin.replace("ING", "IN");
        } else if (strPinyin.contains("AN")) {
            strReplaced = strPinyin.replace("AN", "ANG");
        } else if (strPinyin.contains("EN")) {
            strReplaced = strPinyin.replace("EN", "ENG");
        } else if (strPinyin.contains("IN")) {
            strReplaced = strPinyin.replace("IN", "ING");
        } else {
            return null;
        }
        return strReplaced;
    }
    private static int findPinyin(String strPinyin, List<String> listPinYin){
        int num=0;
        //在目标拼音集合中查找匹配项
        for(String strTmp:listPinYin){
            if(strTmp.contains(strPinyin) && strPinyin.length()==(strTmp.length()-1) ){
                return num;
            }
            num++;
        }
        return -1;
    }

}

