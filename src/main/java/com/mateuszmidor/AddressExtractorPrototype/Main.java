package com.mateuszmidor.AddressExtractorPrototype;

import java.io.IOException;

import com.mateuszmidor.AddressExtractorPrototype.contextbasedextractor.ContextBasedExtractor;
import com.mateuszmidor.AddressExtractorPrototype.dictionarybasedextractor.DictionaryBasedExtractor;

public class Main {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        TestSamples samples = TestSamples.fromFile("data/learning_samples_krakow.txt");
        testContextBasedExtractor(samples);
        testDictionaryBasedExtractor(samples);
    }

    private static void testContextBasedExtractor(TestSamples samples) {
        System.out.println("Context based extractor");
        Extractor e = new ContextBasedExtractor();
        testExtractor(samples, e);
        
    }

    private static void testDictionaryBasedExtractor(TestSamples samples) {
        System.out.println("Dictionary based extractor");
        Extractor e = new DictionaryBasedExtractor();
        testExtractor(samples, e);
    }

    private static void testExtractor(TestSamples samples, Extractor e) {
        int num_found = 0;
        for (TestSample sample : samples) {
            String address = e.extract(sample.sources);
//            System.out.println(sample);
//            System.out.println("Found address: " + address);
//            System.out.println();

            if (!address.startsWith("[")) {
                num_found++;
            }

        }
        System.out.println("Num samples - " + samples.size());
        System.out.println("Num found - " + num_found);
        System.out.println();
    }

}
