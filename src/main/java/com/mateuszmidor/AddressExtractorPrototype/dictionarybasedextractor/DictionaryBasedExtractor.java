package com.mateuszmidor.AddressExtractorPrototype.dictionarybasedextractor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.Extractor;

public class DictionaryBasedExtractor implements Extractor {

    private List<String> streets;
    private Comparator<String> LONG_TO_SHORT = new Comparator<String>() {

        @Override
        public int compare(String o1, String o2) {
            return new Integer(o2.length()).compareTo(o1.length());
        }
    };

    public DictionaryBasedExtractor(final String street_dictionary_filename) throws IOException {
        streets = loadDictionary(street_dictionary_filename);
    }

    private List<String> loadDictionary(String street_dictionary_filename) throws IOException {
        Scanner s = new Scanner(Paths.get(street_dictionary_filename));
        List<String> result = new LinkedList<String>();
        while (s.hasNextLine()) {
            String line = s.nextLine().trim().toLowerCase();
            if (!line.isEmpty()) {
                result.add(line);
            }
        }
        s.close();
        
        Collections.sort(result, LONG_TO_SHORT);
        return result;
    }

    @Override
    public String extract(final AddressSources sources) {
        for (String s : sources) {
            s = s.toLowerCase();
            for (String street : streets) {
                if (s.contains(street)) {
                    String OPTIONAL_NUMBER = "([ ]{0,5}\\d{1,5})?";
                    Pattern p = Pattern.compile(street + OPTIONAL_NUMBER, Pattern.UNICODE_CHARACTER_CLASS);
                    Matcher m = p.matcher(s);
                    if (m.find()) {
                        return m.group();
                    }
                }

            }
        }
        return "[nothing found]";
    }

}
