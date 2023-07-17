package com.chuqiyun.proxmoxveams.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author mryunqi
 * @date 2023/5/20
 */
public class ModUtil {
    /**
    * @Author: mryunqi
    * @Description: 获取域名的一级域名
    * @DateTime: 2023/5/20 20:37
    * @Params: String domain
    * @Return String domain
    */
    public static String getTopLevelDomain(String domain) {
        String[] domainParts = domain.split("\\.");
        if (domainParts.length >= 2) {
            return domainParts[domainParts.length - 2] + "." + domainParts[domainParts.length - 1];
        } else {
            return "";
        }
    }

    /**
    * @Author: mryunqi
    * @Description: 判断对象中属性值是否全为空
    * @DateTime: 2023/5/20 20:43
    */
    public static boolean isNull(Object object) {
        if (null == object) {
            return true;
        }

        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);

                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    /**
    * @Author: mryunqi
    * @Description: 将JSONObejct分页
    * @DateTime: 2023/7/14 21:39
    * @Params: JSONObject jsonObject 原始jsonObject
     *         Integer page 页码
     *         Integer size 每页大小
    * @Return JSONObject newJsonObject 分页后的jsonObject
    */
    public static JSONObject jsonObjectPage(JSONObject jsonObject,Integer page,Integer size){
        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("page",page);
        newJsonObject.put("size",size);
        newJsonObject.put("total",jsonObject.size());
        // 可分几页
        int totalPage = jsonObject.size()/size;
        // 不能整除，多一页
        if (jsonObject.size()%size!=0){
            totalPage++;
        }
        newJsonObject.put("totalPage",totalPage);
        int start = (page-1)*size;
        int end = page*size;
        int i = 0;
        // 创建一个新的jsonObject
        JSONArray data = new JSONArray();
        for (String key : jsonObject.keySet()) {
            if (i>=start && i<end){
                JSONObject jsonData = new JSONObject();
                jsonData.put(key,jsonObject.get(key));
                data.add(jsonData);
            }
            i++;
        }
        newJsonObject.put("data",data);
        return newJsonObject;
    }
}