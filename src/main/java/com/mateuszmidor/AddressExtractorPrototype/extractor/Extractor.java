package com.mateuszmidor.AddressExtractorPrototype.extractor;

/**
 * 
 * @author m.midor
 * This Extractor interface is for deriving specialized AddressExtractors
 */
public interface Extractor {

    public abstract String extract(AddressSources sources);
}