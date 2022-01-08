package com.czh.redis.common.util;


import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.czh.redis.common.constants.CommonConstants;
import com.czh.redis.common.view.PageView;
import com.github.pagehelper.Page;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author czh
 * @date 2020/5/5
 **/
@Validated
@Slf4j
public class Utils {
    /**
     * 流转字符串
     */
    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        StringBuffer buffer = new StringBuffer();

        try {
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException ex) {
            buffer = new StringBuffer();
        }

        return buffer.toString();
    }

    /**
     * 对象转json string
     *
     * @param object
     * @return json string
     */
    public static String object2Json(Object object) {
        if (object == null) {
            return null;
        }
        String json = null;

        try {
            json = JSONObject.toJSONString(object);
        } catch (Exception e) {
            throw e;
        }
        return json;
    }

    /**
     * json string 转对象
     *
     * @param json json string
     * @return object
     */
    public static <T> T json2Object(String json, Class<T> obj) {
        if (StringUtil.isNullOrEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, obj);
    }

    /**
     * 利用 fastJson 把 map 转成 Object
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        try {
            String json = JSON.toJSONString(map);
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }


    public static String jsonGet(String json, String fieldName) {
        String jsonString = "";
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);

            jsonString = jsonObject.getString(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return jsonString;
    }

    @SuppressWarnings("unchecked")
    public static Map jsonGetMap(String json, String fieldName) {
        try {
            Map<String, Object> map = JSON.parseObject(
                    JSONObject.parseObject(json).getString(fieldName),
                    Map.class);

            return map;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 将对象装换为map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     * @Author: czh
     * @CreateTime: 2019/5/31
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /*public static Integer booleanToInteger(boolean bool) {
        return bool ? CommonEnum.BooleanType.FALSE.getValue() : CommonEnum.BooleanType.TRUE.getValue();
    }*/


    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    /**
     * 获取非值不为null的所有属性名字
     *
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 对象拷贝
     * 数据对象空值不拷贝到目标对象
     *
     * @param src
     * @param target
     * @throws NoSuchMethodException copy
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 使用fast吧对象转成map, 相对于spring的bean转map, 可以读到符类的字段
     * @param object
     * @return
     */
    public static Map objectToMap(Object object) {
        try {
            return JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String md5(String content) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        return md5.digestHex(content);
    }

    /**
     * 是否在dev环境下
     *
     * @return
     */
    public static Boolean isDev() {
        return SpringUtil.getActiveProfile().equals(CommonConstants.Spring.SPRING_BOOT_ACTIVE_DEV);
    }

    /**
     * 是否在test环境下
     *
     * @return
     */
    public static Boolean isTest() {
        return SpringUtil.getActiveProfile().equals(CommonConstants.Spring.SPRING_BOOT_ACTIVE_TEST);
    }

    public static String randomStr(int length) {
        String str = "1234567890qwertyuiopasdfghjklzxcvbnm";
        int sLen = str.length();
        StringBuilder ret = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(sLen);
            ret.append(str.charAt(r));
        }
        return ret.toString();
    }

    public static String aesEncode(String content, String hexKey) {
        byte[] key = HexUtil.decodeHex(hexKey);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptHex(content);
    }

    public static String aesDecode(String content, String hexKey) {
        byte[] key = HexUtil.decodeHex(hexKey);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.decryptStr(content);
    }

    /**
     *  从阿里云抄的驼峰 转下划线
     * @param camelCase
     * @return
     */
    public static String toLine(String camelCase){
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static<T> Map<String, Object> beanToMap(T bean, boolean isToLine) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                Object value = beanMap.get(key);
                String keyVal = key.toString();
                if (isToLine) {
                    keyVal = Utils.toLine(keyVal);
                }
                map.put(keyVal, value);
            }
        }
        return map;
    }

    public static <T> PageView<T> getPageView(Page<T> page) {
        PageView<T> pageView = new PageView<>();
        pageView.setList(page.getResult());
        pageView.setTotal(page.getTotal());
        return pageView;
    }

    public static void main(String[] args) {
        String randomStr =  Utils.randomStr(4);
        System.out.println(randomStr);
    }
}