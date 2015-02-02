package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankCapitalLetter implements RankEvaluator {

    @Override
    public void evaluate(ExtractionResults results) {
        for (ExtractionResult r : results) {
            Pattern p = Pattern.compile("^[A-ZĄĆŁŚŹŻÓŃ]", Pattern.UNICODE_CHARACTER_CLASS);
            Matcher m = p.matcher(r.address);
            if (m.find()) {
                r.correctnessRank++;
            }

        }
    }

}
