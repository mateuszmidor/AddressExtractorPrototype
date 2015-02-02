package com.mateuszmidor.AddressExtractorPrototype.extractor.dictionarybased;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.extractor.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.extractor.Extractor;

/**
 * 
 * @author m.midor This Extractor uses dictionaries to look up most precise
 *         address in provided strings
 */
public class DictionaryBasedExtractor implements Extractor {

    private Dictionary streets = new Dictionary();
    private Dictionary districts = new Dictionary();

    /**
     * Constructor, takes street list filename and district list filename
     * 
     * @param streets_filename
     *            - file with list of known streets to look for
     * @param districts_filename
     *            - file with list of known districts to look for (if no street
     *            found)
     * 
     */
    public DictionaryBasedExtractor(String streets_filename, final String districts_filename) {

        streets = Dictionary.fromFile(streets_filename);
        generateMutations(streets);
        streets.sortLongestToShortest();
        districts = Dictionary.fromFile(districts_filename);
        generateMutations(districts);
        districts.sortLongestToShortest();
    }

    private void generateMutations(Dictionary d) {
        List<String> mutations = new LinkedList<String>();
        for (String s : d) {
            if (s.contains(" ")) {
                String mutation = s.substring(s.lastIndexOf(" ") + 1);
                if (mutation.length() > 2) {
                    mutations.add(mutation);
                }
            }
        }
        d.addAll(mutations);
    }

    @Override
    /**
     * Returns lowercase address, if any found
     */
    public String extract(final AddressSources sources) {
        String street = extractUsingDict(sources, streets);
        if (!street.isEmpty()) {
            return street;
        }

        String dictrict = extractUsingDict(sources, districts);
        if (!dictrict.isEmpty()) {
            return dictrict;
        }

        return "[nothing found]";
    }

    /**
     * Looks up dictionary keywords in sources
     * 
     * @param sources
     *            Where to look for address
     * @param dict
     *            Dictionary with known addresses to look up
     * @return Address from dictionary, along with number that follows (if any)
     */
    private String extractUsingDict(final AddressSources sources, final Dictionary dict) {

        for (String source : sources) {
            source = source.toLowerCase();
            for (String key : dict) {
                if (source.contains(key)) {
                	
                    String OPTIONAL_NUMBER = "(?:[ ]{0,5}\\d{1,5})?";
                    Pattern p = Pattern.compile("\\b"+key + OPTIONAL_NUMBER+"\\b", Pattern.UNICODE_CHARACTER_CLASS);
                    Matcher m = p.matcher(source);
                    if (m.find()) {
                        return m.group();
                    }
                }

            }
        }
        return "";
    }

}
