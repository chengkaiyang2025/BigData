package com.yzf.common;

import org.apache.flink.api.java.tuple.Tuple;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：将ng中的request请求与页面名称进行关联。
 * @date ：2021/1/31 上午11:02
 */


public class WebUrl {

    public static final Map<String,String> urlAndWebPage = new HashMap<String,String>(){{
        // 假设一家代账公司m个企业，则完整抓取一家企业需要的请求数量
        //登录后-进入(一直没找到页面)
        put("/portal/api/index/ajax/mainOther","主页面");
        //登录后-企业列表旧接口
        put("/portal/api/index/ajax/homeQyListByCondition","企业列表(旧)接口");
        //登录后-企业列表新接口(一直没找到页面)
        put("/portal/index/book/list","企业列表(新)接口");

        //客户管理-客户信息-客户列表|无参数|翻页场景|访问频次：m/页面上企业数量
        put("/portal/api/customerManager/queryCompanyList","客户管理-客户信息-客户列表");


        //企业账套信息|有参数|拼接参数场景|访问频次：m
        put("/portal/api/customerManager/quertQyZtxx","企业账套信息");
        //企业账套信息-基础设置-企业信息-基本信息|有参数|拼接参数场景|访问频次：m
        put("/portal/api/customerManager/queryZtxxKjzdAndBwbAndKjhy","企业账套信息-基础设置-企业信息-基本信息");
        //企业账套信息-基础设置-会计科目|无参数|拼接参数场景|访问频次：m
        put("/system/api/system/ztkm/getAllZtkm","企业账套信息-基础设置-会计科目");

        //企业账套信息-财务处理-凭证|有参数|拼接参数场景|访问频次：m*kjqj
        put("/pingzheng/api/pz/listPz","/企业账套信息-财务处理-凭证");
        //企业账套信息-财务处理-凭证-查询凭证末级科目数据|有参数|拼接参数场景|访问频次：m
        put("/system/api/system/ztkm/getAllPzMjkm","企业账套信息-财务处理-凭证-查询凭证末级科目数据");

        //企业账套信息-财务处理-帐薄-科目余额-科目余额|有参数|拼接参数场景|访问频次：>m,m*kjqj
        put("/pingzheng/api/zhangmu/listKemuYeM","企业账套信息-财务处理-帐薄-科目余额-科目余额");
        //企业账套信息-财务处理-帐薄-科目余额|有参数|拼接参数场景|访问频次：m
        put("/pingzheng/api/zhangmu/listKemuYe","企业账套信息-财务处理-帐薄-科目余额");
        //企业账套信息-财务处理-帐薄-科目余额-数量金额科目余额表|有参数|拼接参数场景|访问频次：m
        put("/pingzheng/api/zhangmu/slhs/getKmyeSlhs","企业账套信息-财务处理-帐薄-科目余额-数量金额科目余额表");
    }};
}
