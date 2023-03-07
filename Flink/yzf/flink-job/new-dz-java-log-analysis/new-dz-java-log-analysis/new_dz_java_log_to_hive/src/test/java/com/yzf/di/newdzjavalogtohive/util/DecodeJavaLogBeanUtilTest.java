package com.yzf.di.newdzjavalogtohive.util;

import com.yzf.di.newdzjavalogtohive.bean.JavaLogBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecodeJavaLogBeanUtilTest {
    private String infoString;
    private String errorString;
    private String errorIllegalString;
    private String errorIllegalString2;
    private String warnString;
    private String readFromFile(String filePath){
        InputStream is = DecodeJavaLogBeanUtilTest.class.getClassLoader().getResourceAsStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        Stream<String> lines = bufferedReader.lines();
//        System.out.println(lines);
        StringBuilder sb = new StringBuilder();

        lines.collect(Collectors.toList()).stream().forEach(m -> sb.append(m));
        return sb.toString();
    }
    @Before
    public void prepare(){
        infoString = readFromFile("example_info.txt");
        errorString = readFromFile("example_error.txt");
        errorIllegalString = readFromFile("example_error_illegal.txt");
        errorIllegalString2 = readFromFile("example_error_illegal2.txt");
        warnString = readFromFile("example_warn.txt");
    }

    @Test
    public void testInfo(){
        JavaLogBean javaLogBean = new JavaLogBean();
        javaLogBean.setMessage(infoString);
        DecodeJavaLogBeanUtil.decode(javaLogBean);
        System.out.println(javaLogBean);
        Assert.assertEquals("INFO",javaLogBean.getMessage_level());
        Assert.assertEquals("2021-11-16 16:20:21,881",javaLogBean.getMessage_date());
        Assert.assertEquals("LogUtils.java",javaLogBean.getMessage_file());
        Assert.assertEquals("80",javaLogBean.getMessage_line());
        Assert.assertEquals("http-nio-8202-exec-17",javaLogBean.getMessage_thread());

        Assert.assertEquals("c.yzf.fintax.task.utils.LogUtils",javaLogBean.getMessage_logger());

    }
    @Test
    public void testWARN(){
        JavaLogBean javaLogBean = new JavaLogBean();
        javaLogBean.setMessage(warnString);
        DecodeJavaLogBeanUtil.decode(javaLogBean);
        System.out.println(javaLogBean);

        Assert.assertEquals("WARN",javaLogBean.getMessage_level());
        Assert.assertEquals("2021-11-16 16:20:57,425",javaLogBean.getMessage_date());
        Assert.assertEquals("PreInterceptor.java",javaLogBean.getMessage_file());
        Assert.assertEquals("34",javaLogBean.getMessage_line());
        Assert.assertEquals("http-nio-8092-exec-13",javaLogBean.getMessage_thread());

        Assert.assertEquals("c.y.f.t.w.i.PreInterceptor",javaLogBean.getMessage_logger());

    }
    @Test
    public void testERROR(){
        JavaLogBean javaLogBean = new JavaLogBean();
        javaLogBean.setMessage(errorString);
        DecodeJavaLogBeanUtil.decode(javaLogBean);
        System.out.println(javaLogBean);
        Assert.assertEquals("ERROR",javaLogBean.getMessage_level());
        Assert.assertEquals("2021-11-16 16:20:10,190",javaLogBean.getMessage_date());
        Assert.assertEquals("FetchDataService.java",javaLogBean.getMessage_file());
        Assert.assertEquals("490",javaLogBean.getMessage_line());
        Assert.assertEquals("SimpleAsyncTaskExecutor-5",javaLogBean.getMessage_thread());

        Assert.assertEquals("[税务]-[取数业务]-",javaLogBean.getMessage_logger());

    }

    @Test
    public void testIllegalLogERROR(){
        JavaLogBean javaLogBean = new JavaLogBean();
        javaLogBean.setMessage(errorIllegalString);
        DecodeJavaLogBeanUtil.decode(javaLogBean);
        System.out.println(javaLogBean);
        Assert.assertEquals("ERROR",javaLogBean.getMessage_level());
        Assert.assertEquals("2021-11-18 03:17:35,413",javaLogBean.getMessage_date());
        Assert.assertEquals("LoginPhoneLock.java",javaLogBean.getMessage_file());
        Assert.assertEquals("42",javaLogBean.getMessage_line());
        Assert.assertEquals("http-nio-8082-exec-15",javaLogBean.getMessage_thread());

        Assert.assertEquals("c.y.f.task.lock.LoginPhoneLock",javaLogBean.getMessage_logger());
    }


    /**
     * TODO 这个 DUBBO 的日志也是 match_bucket 4个，暂时不清洗
     */
    @Test
    public void testIllegalLogERROR2(){
        JavaLogBean javaLogBean = new JavaLogBean();
        javaLogBean.setMessage(errorIllegalString2);
        DecodeJavaLogBeanUtil.decode(javaLogBean);
        System.out.println(javaLogBean);

        Assert.assertEquals("ERROR",javaLogBean.getMessage_level());
        Assert.assertEquals("2021-11-18 03:17:35,413",javaLogBean.getMessage_date());

    }
    /**
     * https://www.programminghunter.com/article/1518263279/
     *

     {[^{}]+}


     (?<=\[).*(?=\])



     */
    @Test
    @Ignore
    public void testRegex(){
        final String regex = "\\[[^\\[]*\\] ";
        final String string = warnString;
        final String subst = "";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher m = pattern.matcher(string);

        while (m.find()) {
            String sub = string.substring(m.start(), m.end());
            System.out.println(sub);
        }
    }
    @Test
    public void testMessageLoggerForShort(){
        Assert.assertEquals("c.y.f.t.q.R",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger("c.y.f.t.q.ReportValueChangedQueue");
        }}).getMessage_logger_for_short());


        Assert.assertEquals("c.y.f.t.q.R",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger(" c.y.f.t.q.ReportValueChangedQueue ");
        }}).getMessage_logger_for_short());

        Assert.assertEquals("c.y.f.t.s.R",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger("c.y.f.tax.service.ReportService");
        }}).getMessage_logger_for_short());

        Assert.assertEquals("c.y.f.a.s.c.D",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger("com.yzf.fintax.application.support.config.DefaultExceptionHandlerAdvice");
        }}).getMessage_logger_for_short());

        Assert.assertEquals("非法类路径",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger("【申报业务处理】 -");
        }}).getMessage_logger_for_short());

        Assert.assertEquals("非法类路径",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger("【任务下发记录业务处理】-");
        }}).getMessage_logger_for_short());

        Assert.assertEquals("非法类路径",DecodeJavaLogBeanUtil.messageLoggerForShort(new JavaLogBean(){{
            setMessage_logger("[税务中台]-[任务管理状态修复定时任务]-");
        }}).getMessage_logger_for_short());
    }
}