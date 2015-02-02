package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

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
		// TODO Auto-generated method stub
		return null;
	}

}
