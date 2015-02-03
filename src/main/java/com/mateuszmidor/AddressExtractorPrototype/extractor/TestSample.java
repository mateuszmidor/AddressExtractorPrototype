package com.mateuszmidor.AddressExtractorPrototype.extractor;

import java.util.Date;

/**
 * 
 * This class represents a test sample ie. a bunch of address sources and the
 * best address string that can be extracted from the sources
 * 
 * @author mateusz
 */
public class TestSample implements Comparable<TestSample> {
	public Date date = new Date();
	public String expected_result = new String();
	public AddressSources sources = new AddressSources();

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String s : sources) {
			sb.append(s);
			sb.append("\n");
		}
		sb.append("expected: ");
		sb.append(expected_result);
		return sb.toString();
	}

	@Override
	public int compareTo(TestSample b) {
		return this.date.compareTo(b.date);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sources == null) ? 0 : sources.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestSample other = (TestSample) obj;
		if (sources == null) {
			if (other.sources != null)
				return false;
		} else if (!sameContents(other))
			return false;
		return true;
	}

	private boolean sameContents(TestSample other) {
		return sources.equals(other.sources);
	}
}
