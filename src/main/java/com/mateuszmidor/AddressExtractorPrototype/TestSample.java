package com.mateuszmidor.AddressExtractorPrototype;

/**
 * 
 * @author mateusz
 * This class represents a test sample ie. a bunch of address sources
 * and the best address string that can be extracted from the sources
 */
public class TestSample {
    public String expected_result = new String();
    public AddressSources sources = new AddressSources();
    
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (String s : sources) {
            sb.append(s);
            sb.append("\n");
        }
        sb.append(expected_result);
        return sb.toString();
    }
}
