package com.afan.rpc.config;


import com.afan.rpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author AFan
 */
public abstract class ResourceConfig {
    public static int PORT;
    public static Serializer.Algorithm SERIALIZER;
    public static String REGISTRY_TYPE;
    public static String LOAD_BALANCE_TYPE;

    private static Properties properties;
    private final static String CONFIG_PATH = "/rpcConfig.properties";
    static {
        try (InputStream in = ResourceConfig.class.getResourceAsStream(CONFIG_PATH)) {
            properties = new Properties();
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        initProperties();
    }

    private static void initProperties() {
        String port = properties.getProperty("server.port");
        String serializer = properties.getProperty("serializer.algorithm");
        String registry = properties.getProperty("server.registry");
        String loadBalance = properties.getProperty("server.loadBalance");


        if(port == null || "".equals(port)) {
            PORT = 10880;
        } else {
            PORT = Integer.parseInt(port.trim());
        }

        if(serializer == null || "".equals(serializer)) {
            SERIALIZER =  Serializer.Algorithm.Java;
        } else {
            SERIALIZER = Serializer.Algorithm.valueOf(serializer.trim());
        }

        if (registry == null || "".equals(registry)) {
            REGISTRY_TYPE = "nacos";
        }else {
            REGISTRY_TYPE = registry.trim();
        }

        if (loadBalance == null || "".equals(loadBalance)) {
            LOAD_BALANCE_TYPE = "consistentHash";
        }else {
            LOAD_BALANCE_TYPE = loadBalance.trim();
        }
    }

//    public static int getServerPort() {
//        String value = properties.getProperty("server.port").trim();
//        if(value == null) {
//            return 10880;
//        } else {
//            return Integer.parseInt(value);
//        }
//    }
//    public static Serializer.Algorithm getSerializerAlgorithm() {
//        String value = properties.getProperty("serializer.algorithm").trim();
//        if(value == null) {
//            return Serializer.Algorithm.Java;
//        } else {
//            return Serializer.Algorithm.valueOf(value);
//        }
//    }
}