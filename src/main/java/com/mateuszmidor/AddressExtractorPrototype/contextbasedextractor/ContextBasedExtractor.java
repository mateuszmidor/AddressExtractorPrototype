package com.mateuszmidor.AddressExtractorPrototype.contextbasedextractor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.Extractor;

public class ContextBasedExtractor implements Extractor{

    public String extract(final AddressSources sources) {
        final String[] CONTEXT_LEFT_KEYWORDS = {"ulica", "ulicy", "ul ", "ul.", "aleja", "alei", "al ", "al.", 
                "os ", "os.", "osiedle", "osiedlu"};
        for (String s : sources) {
            for (String k : CONTEXT_LEFT_KEYWORDS) {
                String lowercase_source = s.toLowerCase();
                if (lowercase_source.contains(k)) {
                    return normalizeTail(extractAddressAfterKeyword(lowercase_source, k));
                }
            }
        }
        return "[nothing found]";
    }

    private String normalizeTail(String s) {
        Map<String, String> MODAL_NORMAL = new HashMap<>();
        MODAL_NORMAL.put("skiej", "ska");
        
        for (Entry<String, String> modal : MODAL_NORMAL.entrySet()) {
            if (s.endsWith(modal.getKey())) {
                return replace(s, modal.getKey(), modal.getValue());
            }
        }
        return s;
    }

    private String replace(String s, String key, String value) {
        return s.replaceAll(key, value);
    }

    private String extractAddressAfterKeyword(String lowercase_source, String k) {
        k = k.replace(".", "\\."); // escape special regex character - period
        Pattern p = Pattern.compile( k + "(\\w{1,4}\\.)?( |\\w{2,}|\\d)+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher m = p.matcher(lowercase_source);
        if (m.find()) {
            return m.group().trim();
        }
        
        return "[nothing matched]";
    }

}
