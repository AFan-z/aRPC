package com.afan.rpc.serialize.json;

import com.afan.rpc.serialize.Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author AFan
 * @date 2021/7/3 17:07
 */
public class GsonSerializer implements Serializer {
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
        String json = new String(bytes, StandardCharsets.UTF_8);
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> byte[] serialize(T object) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
        String json = gson.toJson(object);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                if ("void".equals(str)) {
                    return null;
                }
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override             //   String.class
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            // class -> json
            return new JsonPrimitive(src.getName());
        }
    }
}
