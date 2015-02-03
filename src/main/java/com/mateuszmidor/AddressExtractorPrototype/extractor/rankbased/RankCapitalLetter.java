package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankCapitalLetter implements RankEvaluator {

	@Override
	public void evaluate(AddressCandidates results) {
		for (AddressCandidate r : results) {
			Pattern p = Pattern.compile("^[A-ZĄĆĘŁŃÓŚŹŻ]",
					Pattern.UNICODE_CHARACTER_CLASS);
			
			Matcher m = p.matcher(r.address);
			if (m.find()) {
				r.correctnessRank++;
			}

		}
	}

}
