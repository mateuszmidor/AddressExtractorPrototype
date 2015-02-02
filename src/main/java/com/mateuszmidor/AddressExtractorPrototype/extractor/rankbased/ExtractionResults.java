package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * List of addresses extracted from a source.
 * @author m.midor
 *
 */

class CorrectnessComparator implements Comparator<ExtractionResult> {

    @Override
    public int compare(ExtractionResult a, ExtractionResult b) {

        return new Integer(b.correctnessRank).compareTo(a.correctnessRank);
    }
}

public class ExtractionResults extends LinkedList<ExtractionResult>{
    public static ExtractionResults instance = new ExtractionResults();
	private static final long serialVersionUID = 3788081570798145609L;
	
	public void sortByCorrectness() {
	    Collections.sort(this, new CorrectnessComparator());
	    instance = this;
	}
	
	public void printOut() {
	    for (ExtractionResult r : this) {
	        System.out.print(r.address + "["+r.correctnessRank+"],");
	    }
	    System.out.println();
	}

    @Override
    public boolean add(ExtractionResult r) {
//        for (ExtractionResult item : this) {
//            if (item.address.equals(r.address)) {
//                return false;
//            }
//        }
        return super.add(r);
    }
}
