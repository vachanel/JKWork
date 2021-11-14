package com.homework.week2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class HttpClientDemo {
    private final static int WARN_TIME = 1200;

    private final static int READ_TIMEOUT = 8000;

    private final static int CONNECTION_TIMEOUT = 8000;

    private static RestTemplate restTemplate;

    public static void main(String[] args) {
        HttpClientDemo.requestByPost(null,"http://localhost:8801/");
    }

    public static String requestByPost(MultiValueMap<String, String> params, String url) {
        RestTemplate restTemplate = new RestTemplate();
        long startTime = System.currentTimeMillis();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        org.springframework.http.HttpEntity<MultiValueMap<String, String>> requestEntity = new org.springframework.http.HttpEntity<MultiValueMap<String, String>>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(builder.build(true).toUri(), HttpMethod.POST, requestEntity, String.class);
        String rspData = response.getBody();
        //   return response.getBody();
        String rspParams = rspData.substring("response_params".length() + 1, rspData.indexOf("&sign={"));
        return rspParams;
    }

    // @HystrixCommand(fallbackMethod = "fallback")
    public static JSONObject client(String url, HttpEntity requestEntity) {
        long lastTime = System.currentTimeMillis();
        initRestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        return JSON.parseObject(responseEntity.getBody());
    }

    private static RestTemplate initRestTemplate() {
        if (restTemplate != null) {
            return restTemplate;
        }
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(READ_TIMEOUT);
        httpRequestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        restTemplate = new RestTemplate(httpRequestFactory);
        setMsgCharset(restTemplate, StandardCharsets.UTF_8);//UTF-8编码

        return restTemplate;
    }

    private static void setMsgCharset(RestTemplate restTemplate, Charset charset) {
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(charset);//StandardCharsets.UTF_8
        converterList.add(0, converter);
        restTemplate.setMessageConverters(converterList);
    }
}
