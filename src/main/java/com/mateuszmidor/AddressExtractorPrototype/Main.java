package com.mateuszmidor.AddressExtractorPrototype;

import java.io.IOException;

import com.mateuszmidor.AddressExtractorPrototype.contextbased.Extractor;

public class Main {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        TestSamples samples = TestSamples.fromFile("data/input.txt");
        Extractor e = new Extractor();
        for (TestSample sample : samples) {
            System.out.println(sample);
            System.out.println("Found address: " + e.extract(sample.sources));
            System.out.println();
        }
    }

}
