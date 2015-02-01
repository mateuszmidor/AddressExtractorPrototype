package com.mateuszmidor.AddressExtractorPrototype.extractor.dictionarybased;

import java.util.Comparator;

/**
 * 
 * @author m.midor
 * Comparator used to sort strings in longest to shortest order
 */
class LongToShortComparator implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) {
		return new Integer(o2.length()).compareTo(o1.length());
	}
}