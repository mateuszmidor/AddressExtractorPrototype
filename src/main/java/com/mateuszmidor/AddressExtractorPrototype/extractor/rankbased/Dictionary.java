package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Dictionary extends LinkedList<String> {

    private static final long serialVersionUID = 1018433824638220292L;

    public static Dictionary fromFile(String filename) {

        Dictionary result = new Dictionary();
        try (Scanner s = new Scanner(Paths.get(filename))) {
            result.readLines(s);
        } catch (IOException e) {
            System.out.println("Could not load dictionary " + filename);
        }

        return result;
    }

    private void readLines(Scanner s) {
        while (s.hasNextLine()) {
            String line = s.nextLine().trim().toLowerCase();
            if (!line.isEmpty()) {
                add(line);
            }
        }
    }

    public void generateMutations() {
        generateSurnames();
        generateDeclinations();
    }

    private void generateSurnames() {
        List<String> mutations = new LinkedList<String>();
        for (String s : this) {
            if (s.contains(" ")) {
                String mutation = s.substring(s.lastIndexOf(" ") + 1);
                if (mutation.length() > 2) {
                    mutations.add(mutation);
                }
            }
        }
        this.addAll(mutations);
    }

    private void generateDeclinations() {
        Map<String, String> MODAL_NORMAL = new HashMap<>();
        MODAL_NORMAL.put("ska", "skiej"); // krakowskiej
        MODAL_NORMAL.put("cka", "ckiej"); // tynieckiej
        MODAL_NORMAL.put("ny", "nach"); // czyżynach
        MODAL_NORMAL.put("czne", "cznym"); // słonecznym
        List<String> mutations = new LinkedList<String>();

        for (String s : this) {
            for (Entry<String, String> modal : MODAL_NORMAL.entrySet()) {
                if (s.endsWith(modal.getKey())) {
                    mutations.add(s.replaceAll(modal.getKey(), modal.getValue()));
                }
            }
        }

        this.addAll(mutations);
    }

    public void print() {
        for (String key : this) {
            System.out.println(key);
        }
    }

}
