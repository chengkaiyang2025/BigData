package com.yzf.di.newdzlogsinkhive.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yzf.di.newdzlogsinkhive.bean.TokenInfo;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：代码来自于git仓库：
 * http://172.23.40.145/yangchengkai/token-parser/blob/master/src/main/java/com/yzf/support/token/tokenparser/dto/TokenInfo.java
 * 此代码段 clone自 http://172.23.40.145/architecture-group/token-parser/tree/master
 * 主要解决ng中cookie的access_token字段解析问题。
 * @date ：2021/1/28 下午4:53
 */


public class TokenInfoUtil {

    //    // 开发环境秘钥
//    public static final String DEV_SECRET = "devC71E713DB2777E10980A42314A7DBA0A";
//
//    // 测试环境秘钥
//    public static final String CESHI_SECRET = "ceshiC71E713DB2777E10980A42314A7DBA0A";
//
//    // 线上钉钉扫码（客服系统）秘钥
//    public static final String DESK_SECRET = "deskC71E713DB2777E10980A42314A7DBA0A";
//
//    // 线上秘钥
//    public static final String PRO_SECRET = "C71E713DB2777E10980A42314A7DBA0A";
//
//
//    public static Algorithm devAlgo;
//    public static Algorithm ceshiAlgo;
//    public static Algorithm deskAlgo;
//    public static Algorithm proAlgo;
//
//
    public static final String USERNAME = "userName";

    public static final String GSID = "gsId";

    public static final String USERID = "userId";

    public static final String PHONE = "phone";

    public static final String SET = "set";

//    @PostConstruct
//    public void init() throws UnsupportedEncodingException {
//        devAlgo = Algorithm.HMAC256(DEV_SECRET);
//        ceshiAlgo = Algorithm.HMAC256(CESHI_SECRET);
//        deskAlgo = Algorithm.HMAC256(DESK_SECRET);
//        proAlgo = Algorithm.HMAC256(PRO_SECRET);
//    }



//    public TokenInfo parseJwtToken(String accessToken, String env) {
//
//        JWTVerifier verifier = getVerifier(env);
//
//        DecodedJWT jwt = null;
//        try {
//            jwt = verifier.verify(accessToken);
//        } catch (Exception e) {
//            // log.error("解析token异常 token = {}, {}", accessToken, e.getMessage());
//            throw e;
//            //  2018/11/29
//        }
//
//        return getTokenInfo(jwt);
//    }

//    private JWTVerifier getVerifier(String env) {
//        // log.info("getVerifier() env:{}", env);
//        String secret = "";
//        if (env.equals("dev")) {
//            secret = DEV_SECRET;
//        } else if (env.equals("test")) {
//            secret = CESHI_SECRET;
//        } else if (env.equals("pro")) {
//            secret = PRO_SECRET;
//        } else if (env.equals("desk")) {
//            secret = DESK_SECRET;
//        } else {
//            throw new RuntimeException("not supported env! " + env);
//        }
//
//        JWTVerifier verifier = null;
//        Algorithm algorithm = null;
//
//        try {
//            algorithm = Algorithm.HMAC256(secret);
//        } catch (UnsupportedEncodingException e) {
//            // log.error("Algorithm UnsupportedEncodingException, {}", e.getMessage());
//            throw new RuntimeException("Algorithm UnsupportedEncodingException: " + e.getMessage());
//        }
//        verifier = JWT.require(algorithm).withIssuer("auth0").build();
//
//        return verifier;
//    }


    public static TokenInfo decode(String accessToken)  {
        TokenInfo tokenInfo = new TokenInfo();
        try {
            if(accessToken != null && accessToken.length()>0){
                DecodedJWT jwt = JWT.decode(accessToken);
                tokenInfo = getTokenInfo(jwt);
            }
        }catch (Exception e){
            e.printStackTrace();

        }

        return tokenInfo;

    }


    private static TokenInfo getTokenInfo(DecodedJWT jwt) throws NullPointerException{
//        Logger log = LoggerFactory.getLogger(TokenInfoUtil.class);

        TokenInfo tokenInfo = null;
        if (jwt != null) {
            String userName = jwt.getClaim(USERNAME).asString();
            String gsId = jwt.getClaim(GSID).asString();
            String userId = jwt.getClaim(USERID).asString();
            String phone = jwt.getClaim(PHONE).asString();
            String set = jwt.getClaim(SET).asString();
            String iss = jwt.getIssuer();
            Date expiredDate = jwt.getExpiresAt();

            tokenInfo = new TokenInfo();
            tokenInfo.setGsId(gsId);
            tokenInfo.setPhone(phone);
            tokenInfo.setSet(set);
            tokenInfo.setUserId(userId);
            tokenInfo.setUserName(userName);
            tokenInfo.setIss(iss);

            DateTime dateTime = new DateTime(expiredDate);
            String expiredTime = dateTime.toString("yyyy-MM-dd HH:mm:ss");

            tokenInfo.setExpiredTime(expiredTime);
        }

//        log.debug("tokeninfo:{}", tokenInfo);
        return tokenInfo;
    }


//    private boolean isExpired(DecodedJWT jwt) {
//        Date date = jwt.getExpiresAt();
//        DateTime expireTime = new DateTime(date);
//        long currentTime = System.currentTimeMillis();
//        // log.info("expireTime:{}  currentTime:{}", expireTime, currentTime);
//        if (expireTime.isBefore(currentTime)) {
//            return true;
//        } else {
//            return false;
//        }
//    }


//    public Map<String, Object> introspect(String accessToken) throws UnsupportedEncodingException {
//
//        DecodedJWT jwt = JWT.decode(accessToken);
//        TokenInfo tokenInfo = getTokenInfo(jwt);
//
//        boolean isExpired = isExpired(jwt);
//
//        Env env = verifySignature(splitToken(accessToken));
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", tokenInfo);
//        map.put("isExpired", isExpired);
//        map.put("env", env);
//        // log.info("{}", map);
//        return map;
//    }
//
//
//    private Env verifySignature(String[] parts) throws SignatureVerificationException, UnsupportedEncodingException {
//
//
//        byte[] content = String.format("%s.%s", parts[0], parts[1]).getBytes(StandardCharsets.UTF_8);
//        byte[] signature = Base64.decodeBase64(parts[2]);
//
//        try {
//            devAlgo.verify(content, signature);
//            return new Env("dev", "【企业版联调环境(http://sso.devn.yzf.net)，钉钉扫码测试环境(http://adminsso.yzf.net)】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not dev");
//        }
//
//
//        try {
//            ceshiAlgo.verify(content, signature);
//            return new Env("ceshi", "【代账测试环境(134,187)，企业版测试环境(http://sso.fat.yzf.net)】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not ceshi");
//        }
//
//        try {
//            deskAlgo.verify(content, signature);
//            return new Env("desk", "【钉钉扫码线上环境(https://adminsso.yunzhangfang.com)】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not desk");
//        }
//
//
//        try {
//            proAlgo.verify(content, signature);
//            return new Env("product", "【代账，企业版线上环境】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not product");
//        }
//
//        return new Env("unknown", "未知的签名");
//    }
//


    /**
     * Splits the given token on the "." chars into a String array with 3 parts.
     *
     * @param token the string to split.
     * @return the array representing the 3 parts of the token.
     * @throws JWTDecodeException if the Token doesn't have 3 parts.
     */
//    private String[] splitToken(String token) throws JWTDecodeException {
//        String[] parts = token.split("\\.");
//        if (parts.length == 2 && token.endsWith(".")) {
//            //Tokens with alg='none' have empty String as Signature.
//            parts = new String[]{parts[0], parts[1], ""};
//        }
//        if (parts.length != 3) {
//            throw new JWTDecodeException(String.format("The token was expected to have 3 parts, but got %s.", parts.length));
//        }
//        return parts;
//    }
}
