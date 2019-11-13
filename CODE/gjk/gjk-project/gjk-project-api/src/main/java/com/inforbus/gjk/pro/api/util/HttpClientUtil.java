package com.inforbus.gjk.pro.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static HttpClient httpClient;

    public static HttpResponse toPost(String url, Map<String, String> params){
        createHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            formparams.add(new BasicNameValuePair(key, params.get(key)));
        }
        try {
            HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");
            HttpPost httpPost1 = new HttpPost("http://127.0.0.1:9000/api/projects/create");
            httpPost1.setEntity(reqEntity);
            return httpClient.execute(httpPost1);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static void createHttpClient(){
        if(httpClient == null){
            httpClient = HttpClients.createDefault();
        }
    }

}
