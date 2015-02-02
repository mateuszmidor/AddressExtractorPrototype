package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.extractor.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.extractor.Extractor;

public class RankBasedExtractor implements Extractor {
    private Dictionary streets = new Dictionary();
    private Dictionary districts = new Dictionary();

    public RankBasedExtractor(String streets_filename, final String districts_filename) {
        streets = Dictionary.fromFile(streets_filename);
        streets.generateMutations();
        districts = Dictionary.fromFile(districts_filename);
        districts.generateMutations();
    }

    @Override
    public String extract(AddressSources sources) {
        ExtractionResults results = new ExtractionResults();

        extractUsingDict(sources, streets, results);
        extractUsingDict(sources, districts, results);
        
        RankEvaluator prefixEvaluator = new RankPrefix();
//        prefixEvaluator.evaluate(results);
        
        RankEvaluator suffixEvaluator = new RankSuffix();
//        suffixEvaluator.evaluate(results);
        
        RankEvaluator capitalEvaluator = new RankCapitalLetter();
        capitalEvaluator.evaluate(results);
        
        results.sortByCorrectness();
        if (results.size() > 0) {
            return results.getFirst().address;
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
    private void extractUsingDict(final AddressSources sources, final Dictionary dict, ExtractionResults results) {

        for (String source : sources) {
             String lowercase_source = source.toLowerCase();
            for (String key : dict) {
                if (lowercase_source.contains(key)) {

                    String OPTIONAL_NUMBER = "(?:[ ]{0,5}\\d{1,5}\\w?)?"; // number
                                                                          // can
                                                                          // be
                                                                          // followed
                                                                          // by
                                                                          // letter
                    Pattern p = Pattern.compile("\\b" + key + OPTIONAL_NUMBER + "\\b", Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(source);
                    if (m.find()) {
                        ExtractionResult r = new ExtractionResult();
                        r.address = m.group();
                        r.context = source;
                        results.add(r);
                    }
                }

            }
        }
    }

}
