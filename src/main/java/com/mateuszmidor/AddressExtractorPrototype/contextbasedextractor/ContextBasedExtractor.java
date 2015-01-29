package com.mateuszmidor.AddressExtractorPrototype.contextbasedextractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.Extractor;

public class ContextBasedExtractor implements Extractor{

    public String extract(final AddressSources sources) {
        final String[] CONTEXT_LEFT_KEYWORDS = {"ulica", "ulicy", "ul ", "ul.", "aleja", "alei", "al ", "al."};
        for (String s : sources) {
            for (String k : CONTEXT_LEFT_KEYWORDS) {
                String lowercase_source = s.toLowerCase();
                if (lowercase_source.contains(k)) {
                    return extractAddressAfterKeyword(lowercase_source, k);
                }
            }
        }
        return "[nothing found]";
    }

    private String extractAddressAfterKeyword(String lowercase_source, String k) {
        k = k.replace(".", "\\."); // escape special regex character - period
        Pattern p = Pattern.compile(k + "( |\\w{2,}|\\d)+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher m = p.matcher(lowercase_source);
        if (m.find()) {
            return m.group();
        }
        
        return "[nothing matched]";
    }

}
