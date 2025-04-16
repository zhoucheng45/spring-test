package com.example.front.utils;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

public class DBIPUtils {
    private static  DatabaseReader reader;
    private static Reader reader1;
    static {
        try {
//            ClassPathResource resource = new ClassPathResource("Country.mmdb");
//            InputStream inputStream = resource.getInputStream();
//            reader = new DatabaseReader.Builder(
//                    inputStream
//            ).withCache(new CHMCache()).build();

            ClassPathResource resource1 = new ClassPathResource("ipinfo_lite.mmdb");
            reader1 = new Reader(resource1.getInputStream(), new CHMCache());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB-IP database", e);
        }
    }

    public static String getCountryCode(String ip) {
        try {
            CountryResponse response = reader.country(InetAddress.getByName(ip));
            return response.getCountry().getIsoCode();
        } catch (Exception e) {
            return null;
        }
    }
    public static Country getCountry(String ip) {
        try {
            CountryResponse response = reader.country(InetAddress.getByName(ip));
            return response.getCountry();
        } catch (Exception e) {
            return null;
        }
    }
    public static String getCountryName(String ip) {
        try {
            CountryResponse response = reader.country(InetAddress.getByName(ip));
            return response.getCountry().getName();
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, Object> queryIpInfo(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            // 获取原始数据映射
            return reader1.get(address,Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根据需要添加解析特定字段的方法
    public static String queryCountryCode(String ip) {
        Map<String, Object> info = queryIpInfo(ip);
        if (info != null && info.containsKey("country_code")) {
            return (String) info.get("country_code");
        }
        return null;
    }
    // 根据需要添加解析特定字段的方法
    public static String queryCountryName(String ip) {
        Map<String, Object> info = queryIpInfo(ip) ;
        if (info != null && info.containsKey("country")) {
            return (String) info.get("country");
        }
        return null;
    }
    public static void main(String[] args) {
        Map<String, Object> ipInfo = queryIpInfo("182.239.117.103");
        System.out.println(queryCountryCode("182.239.117.103"));
        System.out.println(queryCountryName("182.239.117.103"));
    }
}