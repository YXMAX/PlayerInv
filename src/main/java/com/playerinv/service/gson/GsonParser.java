package com.playerinv.service.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class GsonParser {

    private final Gson gson = new Gson();

    private Gson prettyGson;

    public String toJson(Object object){
        return gson.toJson(object);
    }

    public JsonArray parseToJsonArray(String s){
        return gson.fromJson(s,JsonArray.class);
    }

    public HashMap<String,String> parseToHashMap(InputStream stream){
        return gson.fromJson(new InputStreamReader(stream),new TypeToken<HashMap<String,String>>(){}.getType());
    }

    public HashMap<String,String> parseToHashMap(String s){
        return gson.fromJson(s,new TypeToken<HashMap<String,String>>(){}.getType());
    }

    public HashMap<Integer,String> parseToVaultAttributes(String s){
        return gson.fromJson(s,new TypeToken<HashMap<Integer,String>>(){}.getType());
    }

    public String toPrettyJson(Object o){
        if(prettyGson == null){
            prettyGson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        }
        return prettyGson.toJson(o);
    }

    public String getHeadValue(String name){
        try {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            JsonObject obj = gson.fromJson(result, JsonObject.class);
            String uid = obj.get("id").toString().replace("\"","");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            obj = gson.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = gson.fromJson(decoded,JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception ignored){ }
        return null;
    }

    private static String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ignored) { }
        finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ignored) { }
        }
        return sb.toString();
    }
}
