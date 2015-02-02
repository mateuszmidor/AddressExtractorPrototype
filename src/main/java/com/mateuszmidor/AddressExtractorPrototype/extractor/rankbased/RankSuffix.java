package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankSuffix implements RankEvaluator {

    @Override
    public void evaluate(ExtractionResults results) {

        for (ExtractionResult r : results) {
            Pattern p = Pattern.compile("\\w+\\s{0,3}\\d{1,3}\\w?\\b", Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(r.address);
            if (m.find()) {
                r.correctnessRank++;
            }

        }

    }

}
