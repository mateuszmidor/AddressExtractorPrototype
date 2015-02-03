package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankPrefix implements RankEvaluator {

    @Override
    public void evaluate(AddressCandidates results) {
        final String[] HI_PRECISION_PREFIX = {"ulica", "ulicy", "ul ", "ul.", "aleja", "alei", "al ", "al."};
        final String[] MEDIUM_PRECISION_PREFIX = {"os ", "os.", "osiedle", "osiedlu"};
        final String[] LOW_PRECISION_PREFIX = {"przy", "na", "obok", "w"};
        rankByPrefix(results, HI_PRECISION_PREFIX, 3);
        rankByPrefix(results, MEDIUM_PRECISION_PREFIX, 2);
        rankByPrefix(results, LOW_PRECISION_PREFIX, 0);
    }

    private void rankByPrefix(AddressCandidates results, final String[] prefixes, int precision) {
        for (AddressCandidate r : results) {
            for (String prefix : prefixes) {
            	prefix = prefix.replace(".", "\\."); // escape special regex character - period
            	
            	// prefix space address, eg. "ul. Wielicka"
                Pattern p = Pattern.compile("\\b"+ prefix + "\\s{0,3}"+ r.address, 
                		Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
                
                Matcher m = p.matcher(r.source);
                if (m.find()) {
                    r.correctnessRank++;
                    r.precisionRank+= precision;
                    break;
                }
            }
        }
    }

}
