package com.example.front.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

public class DBIPUtils {
    private static final DatabaseReader reader;

    static {
        try {
            String path = Objects.requireNonNull(DBIPUtils.class.getResource("/Country.mmdb")).getPath();
            reader = new DatabaseReader.Builder(
                new File(path)
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