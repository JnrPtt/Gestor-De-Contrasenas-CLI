package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Type;

public class PasswordStore {
    private final String filePath;
    private final List<Entry> entries;
    private final Gson gson;

    public PasswordStore(String filePath) {
        this.filePath = filePath;
        this.entries = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadEntriesFromFile();
    }

    public List<Entry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(Entry entry){
        entries.add(entry);
        saveEntriesToFile();
    }

    public Entry findEntryByService(String serviceName){
        for (Entry entry : entries){
            if(entry.getService().equalsIgnoreCase(serviceName)){
                return entry;
            }
        }
        return null;
    }

    public boolean removeEntryByService(String serviceName){
        boolean removed = entries.removeIf(e -> e.getService().equalsIgnoreCase(serviceName));
        if(removed){
            saveEntriesToFile();
        }
        return removed;
    }

    private void saveEntriesToFile(){
        try(Writer writer = new FileWriter(filePath)){
            gson.toJson(entries, writer);
        } catch (IOException e) {
            System.err.println("Error al guardas las entradas: " + e.getMessage());
        }
    }


    private void loadEntriesFromFile(){
        File file = new File(filePath);
        if(!file.exists()){
            return;
        }
        try(Reader reader = new FileReader(filePath)){
            Type listType = new TypeToken<List<Entry>>(){}.getType();
            entries.addAll(gson.fromJson(reader, listType));
        } catch (IOException e) {
            System.err.println("Error al carga las entradas: " + e.getMessage());
        }
    }
}
