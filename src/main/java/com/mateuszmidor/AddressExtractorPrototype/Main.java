package com.mateuszmidor.AddressExtractorPrototype;

import java.io.IOException;

import com.mateuszmidor.AddressExtractorPrototype.extractor.Extractor;
import com.mateuszmidor.AddressExtractorPrototype.extractor.TestSample;
import com.mateuszmidor.AddressExtractorPrototype.extractor.TestSamples;
import com.mateuszmidor.AddressExtractorPrototype.extractor.contextbased.ContextBasedExtractor;
import com.mateuszmidor.AddressExtractorPrototype.extractor.dictinarybased.DictionaryBasedExtractor;

public class Main {

    private static boolean printFailedExtractions = true;
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        TestSamples samples = TestSamples.fromFile("data/learning_samples_krakow.txt");
//        testContextBasedExtractor(samples);
        testDictionaryBasedExtractor(samples);
    }

    private static void testContextBasedExtractor(TestSamples samples) {
        System.out.println("Context based extractor");
        Extractor e = new ContextBasedExtractor();
        testExtractor(samples, e);
    }

    private static void testDictionaryBasedExtractor(TestSamples samples) throws IOException {
        System.out.println("Dictionary based extractor");
        Extractor e = new DictionaryBasedExtractor("data/krakow_streets.txt", "data/krakow_districts.txt");
        testExtractor(samples, e);
    }

    private static void testExtractor(TestSamples samples, Extractor e) {
        int num_found = 0;
        for (TestSample sample : samples) {
            String address = e.extract(sample.sources);

            if ((printFailedExtractions) && (!sample.expected_result.contains(address))) {
                System.out.println(sample);
                System.out.println("Found address: " + address);
                System.out.println();
            }
            
            if (sample.expected_result.contains(address)) {
                num_found++;
            }

        }
        System.out.println("Num samples - " + samples.size());
        System.out.println("Num found - " + num_found);
        System.out.println("Efficiency: " + 100 * num_found / samples.size() + "%");
        System.out.println();
    }

}
