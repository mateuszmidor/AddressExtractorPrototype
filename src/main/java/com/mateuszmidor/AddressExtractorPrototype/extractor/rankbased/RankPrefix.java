package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankPrefix implements RankEvaluator {

    @Override
    public void evaluate(ExtractionResults results) {
        final String[] CONTEXT_LEFT_KEYWORDS = {"ulica", "ulicy", "ul ", "ul.", "aleja", "alei", "al ", "al.", 
                "os ", "os.", "osiedle", "osiedlu",
                "przy", "na", "obok", "w"};
        
        for (ExtractionResult r : results) {
            for (String prefix : CONTEXT_LEFT_KEYWORDS) {
                String k = prefix.replace(".", "\\."); // escape special regex character - period
                Pattern p = Pattern.compile("\\b"+ k + "\\s{0,3}"+r.address, Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(r.context);
                if (m.find()) {
                    r.correctnessRank++;
                    break;
                }
            }
            
        }
        
    }

}
