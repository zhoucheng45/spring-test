package com.example.front.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

public class DBIPUtils {
    private static final DatabaseReader reader;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("Country.mmdb");
            InputStream inputStream = resource.getInputStream();
            reader = new DatabaseReader.Builder(
                    inputStream
            ).build();
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

    public static void main(String[] args) {
        System.out.println(getCountryName("182.239.117.103"));
    }
}