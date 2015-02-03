package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * This class represents list of known addresses.
 * It allows to load from file.
 * It also allows to generate mutations of address, that can be found in source texts
 * It enforces that entries added with "add" method are always lowercase.
 * @author m.midor
 *
 */
public class Dictionary extends LinkedList<String> {

    private static final long serialVersionUID = 1018433824638220292L;

    public static Dictionary fromFile(String filename) {

        Dictionary dictionary = new Dictionary();
        try (Scanner s = new Scanner(Paths.get(filename), "UTF8")) {
            dictionary.readLines(s);
        } catch (IOException e) {
            System.out.println("Could not load dictionary " + filename);
        }

        return dictionary;
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
        generateWithNoPolishLetters();
    }

    private void generateWithNoPolishLetters() {
        List<String> mutations = new LinkedList<String>();

        for (String s : this) {
            String noPolishChars = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
            if (!noPolishChars.equals(s)) {
                mutations.add(noPolishChars);
            }
        }
        this.addAll(mutations);
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

    public static String removeDeclination(String s) {
        Map<String, String> MODAL_NORMAL = new HashMap<>();
        MODAL_NORMAL.put("skiej", "ska"); // krakowskiej
        MODAL_NORMAL.put("ckiej", "cka"); // tynieckiej
        MODAL_NORMAL.put("nach", "ny"); // czyżynach
        MODAL_NORMAL.put("cznym", "czne"); // słonecznym
        
        s = s.toLowerCase();
        for (Entry<String, String> modal : MODAL_NORMAL.entrySet()) {
            String key = modal.getKey();
			if (s.contains(key)){
                String value = modal.getValue();
				return s.replaceAll(key, value);
            }
        }
        return s;
    }
    
    private void generateDeclinations() {
        Map<String, String> NORMAL_MODAL = new HashMap<>();
        NORMAL_MODAL.put("ska", "skiej"); // krakowskiej
        NORMAL_MODAL.put("cka", "ckiej"); // tynieckiej
        NORMAL_MODAL.put("ny", "nach"); // czyżynach
        NORMAL_MODAL.put("czne", "cznym"); // słonecznym
        List<String> mutations = new LinkedList<String>();

        for (String s : this) {
            for (Entry<String, String> modal : NORMAL_MODAL.entrySet()) {
                String key = modal.getKey();
				if (s.endsWith(key)) {
                    String value = modal.getValue();
					mutations.add(s.replaceAll(key, value));
                }
            }
        }
        
        this.addAll(mutations);
    }

    public void print() {
        for (String entry : this) {
            System.out.println(entry);
        }
    }

	@Override
	public boolean add(String s) {
		return super.add(s.toLowerCase());
	}

}
