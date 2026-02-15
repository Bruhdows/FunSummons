package com.bruhdows.funsummons.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Material;

import java.io.IOException;

public class MaterialTypeAdapter extends TypeAdapter<Material> {
    
    @Override
    public void write(JsonWriter out, Material material) throws IOException {
        if (material == null) {
            out.nullValue();
        } else {
            out.value(material.name());
        }
    }
    
    @Override
    public Material read(JsonReader in) throws IOException {
        String materialName = in.nextString();
        if (materialName == null || materialName.isEmpty()) {
            return null;
        }
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
