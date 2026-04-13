package com.playerinv.Util.Yaml;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class CommentYamlConfiguration extends YamlConfiguration {

    private Map<String, String> comments = Maps.newHashMap();

    @Override
    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        StringBuilder builder = new StringBuilder();
        String line;
        StringBuilder commentBuilder = new StringBuilder();
        try (BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
            while ((line = input.readLine()) != null) {
                String detected_line = line.trim();
                if (detected_line.startsWith("#") || line.isEmpty()) {
                    commentBuilder.append(line);
                    commentBuilder.append("\n");
                } else {
                    comments.put(line, commentBuilder.toString());
                    commentBuilder = new StringBuilder();
                }
                builder.append(line);
                builder.append('\n');
            }
        }
        this.loadFromString(builder.toString());
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);
        String data = this.saveToString();

        String[] sArray = data.split("\n");
        String[] stringArray = new String[sArray.length - 1];
        System.arraycopy(sArray, 1, stringArray, 0, sArray.length - 1);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringArray.length; i++) {
            if(!comments.containsKey(stringArray[i])) {
                stringBuilder.append(stringArray[i]);
                stringBuilder.append("\n");
                continue;
            }
            String string = comments.get(stringArray[i]);
            if(string.isEmpty()){
                stringBuilder.append(stringArray[i]);
                stringBuilder.append("\n");
                continue;
            }
            stringBuilder.append(string);
            stringBuilder.append(stringArray[i]);
            stringBuilder.append("\n");
        }
        data = stringBuilder.toString();

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            writer.write(data);
        }
    }

    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        YamlConfiguration config = new CommentYamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | InvalidConfigurationException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        }

        return config;
    }
}
