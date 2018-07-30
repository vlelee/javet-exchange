package com.citi.exchange.util;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.IOUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

import static java.lang.System.out;

public class StockRegistryUtil {
    private final static String fileName = "/res/stock-list.json";
    private JSONArray registryArray;

    public StockRegistryUtil() {
        String registry = new Scanner(getClass().getClassLoader().getResourceAsStream( fileName)).useDelimiter("\\Z").next();
        JSONObject registryJSON = new JSONObject(registry);
        registryArray = (JSONArray) registryJSON.get("");
    }

    public String findStockName(String stockTicker) {
        Iterator iterator = registryArray.iterator();
        while (iterator.hasNext()) {
            JSONObject iterObject = (JSONObject) iterator.next();
            if(iterObject.get("Symbol").equals(stockTicker)) {
                return iterObject.get("Name").toString();
            }
        }
        return "";
    }
}
