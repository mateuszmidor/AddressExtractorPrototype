package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

/**
 * This class represents address extraction single result.
 * Extraction can yield many results, such as city name, district name, street name.
 * Street with number has highest precisionRank.
 * City has lowest precisionRank.
 * Based on eg. context, the correctnessRank is evaluated, eg "ul. Wielicka 23" will have 
 * higher correctnessRank than just "wielicka" (based on keyword "ul.", number, capital letter)
 * @author m.midor
 *
 */
public class ExtractionResult {
	public String address = "";
	public String context = "";
	public int precisionRank = 0;
	public int correctnessRank = 0;
}
