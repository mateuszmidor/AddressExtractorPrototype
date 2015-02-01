package com.mateuszmidor.AddressExtractorPrototype.extractor.contextbased;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.extractor.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.extractor.Extractor;

public class ContextBasedExtractor implements Extractor{

	@Override
    public String extract(final AddressSources sources) {
        String withPrefix = extractPrefixBased(sources);
        if (!withPrefix.isEmpty()) {
        	return withPrefix.toLowerCase();
        }
        
        // experimentational - still catches many unwanted patterns
//        String withSuffix = extractSuffixBased(sources);
//        if (!withSuffix.isEmpty()) {
//        	return "{suffix based} " +withSuffix;
//        }
        
        return "[nothing found]";
    }

	private String extractSuffixBased(AddressSources sources) {
		for (String s : sources) {
			Pattern p = Pattern.compile("\\w{3,}\\s+\\d{1,3}[^-]", Pattern.UNICODE_CHARACTER_CLASS);
			Matcher m = p.matcher(s);
			if (m.find()) {
				return m.group();
			}
		}
		return "";
	}

	private String extractPrefixBased(final AddressSources sources) {
		final String[] CONTEXT_LEFT_KEYWORDS = {"ulica", "ulicy", "ul ", "ul.", "aleja", "alei", "al ", "al.", 
                "os ", "os.", "osiedle", "osiedlu"};
        for (String s : sources) {
            for (String k : CONTEXT_LEFT_KEYWORDS) {
                String lowercase_source = s;//.toLowerCase();
                if (lowercase_source.contains(k)) {
                    return normalizeTail(extractAddressAfterKeyword(lowercase_source, k));
                }
            }
        }
        return "";
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
        Pattern p = Pattern.compile( k + "((\\s{0,3}[A-Z]\\w+)+(\\s\\d+)?)", Pattern.UNICODE_CHARACTER_CLASS);
//        Pattern p = Pattern.compile( k + "(\\s{0,3}\\w{2,})", Pattern.UNICODE_CHARACTER_CLASS);
//        Pattern p = Pattern.compile( k + "((\\w{1,4}\\.)?( |\\w{2,}|\\d)+)", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher m = p.matcher(lowercase_source);
        if (m.find()) {
            return m.group(1).trim();
        }
        
        return "[nothing matched]";
    }

}
