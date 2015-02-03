package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * List of addresses extracted from a source.
 * 
 * @author m.midor
 *
 */

class CorrectnessPrecisionComparator implements Comparator<AddressCandidate> {

	@Override
	public int compare(AddressCandidate a, AddressCandidate b) {

		return new Integer(b.correctnessRank + b.precisionRank)
				.compareTo(a.correctnessRank + a.precisionRank);
	}
}

public class AddressCandidates extends LinkedList<AddressCandidate> {

	public static AddressCandidates lastProcessedCandidates = new AddressCandidates();
	private static final long serialVersionUID = 3788081570798145609L;

	public void sortByCorrectnessAndPrecision() {
		Collections.sort(this, new CorrectnessPrecisionComparator());
		lastProcessedCandidates = this;
	}

	public void printOut() {
		for (AddressCandidate r : this) {
			System.out.format("%s[c:%d,p:%d] ", r.address, r.correctnessRank,
					r.precisionRank);
		}
		System.out.println();
	}

}
