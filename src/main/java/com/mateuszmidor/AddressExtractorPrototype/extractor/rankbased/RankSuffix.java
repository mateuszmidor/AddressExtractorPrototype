package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ranks up precision and correctness when street number is present in address
 * @author m.midor
 *
 */
public class RankSuffix implements RankEvaluator {

    @Override
    public void evaluate(AddressCandidates results) {

        for (AddressCandidate r : results) {
        	rankCandidate(r);
        }
    }

	private void rankCandidate(AddressCandidate candidate) {
		
		Pattern p = composePattern();
		Matcher m = p.matcher(candidate.address);
		if (m.find()) {
		    candidate.correctnessRank++;
		    candidate.precisionRank++; // street number is a level up precision
		}
	}

	private Pattern composePattern() {
		// word space number optional_letter eg. "Wielicka 23b"
		return Pattern.compile("\\w+\\s{0,3}\\d{1,3}\\w?\\b", 
				Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
	}
}
