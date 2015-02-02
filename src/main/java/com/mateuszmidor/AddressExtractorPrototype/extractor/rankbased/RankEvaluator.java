package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

/**
 * Interface for evaluators that rank ExtractionResult's precisionRank and correctnessRank
 * based on criteria like capital letter (eg. "Wielicka" vs "wielicka", 
 * context (eg. "ulica Wielicka"), presence of number (eg. "Wielicka 23")
 * @author m.midor
 *
 */
public interface RankEvaluator {
	void evaluate(ExtractionResults r);
}
