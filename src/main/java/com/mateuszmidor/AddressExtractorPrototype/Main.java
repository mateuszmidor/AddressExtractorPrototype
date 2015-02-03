package com.mateuszmidor.AddressExtractorPrototype;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import com.mateuszmidor.AddressExtractorPrototype.extractor.Extractor;
import com.mateuszmidor.AddressExtractorPrototype.extractor.TestSample;
import com.mateuszmidor.AddressExtractorPrototype.extractor.TestSamples;
import com.mateuszmidor.AddressExtractorPrototype.extractor.contextbased.ContextBasedExtractor;
import com.mateuszmidor.AddressExtractorPrototype.extractor.dictionarybased.DictionaryBasedExtractor;
import com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased.AddressCandidates;
import com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased.RankBasedExtractor;

public class Main {

    private static boolean printFailedExtractions = true;
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//        TestSamples samples = TestSamples.fromFile("data/non_working_samples.txt");
        TestSamples samples = TestSamples.fromFile("data/learning_samples_krakow.txt");
//        testContextBasedExtractor(samples);
//        testDictionaryBasedExtractor(samples);
        testRankBasedExtractor(samples);
   
    }

    private static void testRankBasedExtractor(TestSamples samples) {
        System.out.println("Rank based extractor");
        Extractor e = new RankBasedExtractor("data/krakow_streets.txt", "data/krakow_districts.txt", "data/cities.txt");
        testExtractor(samples, e);
    }

    private static void removeDuplicatesAndNonKrakowSamples(TestSamples samples) {
        System.out.println(samples.size());
        samples.removeDuplicates();
        System.out.println(samples.size());
        samples.removeNotMatching("Kraków|kraków|Krakow|krakow");
        System.out.println(samples.size());
        
        // samples with empty "expected=" go last
        Collections.sort(samples, new Comparator<TestSample>() {

            @Override
            public int compare(TestSample o1, TestSample o2) {
                if (!(o1.expected_result.length() <2) && o2.expected_result.length() <2)
                return -1;
                else return 1;
            }
        });
        samples.saveToFile("data/learning_samples_krakow_nodup.txt");
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
            String address = e.extract(sample.sources).toLowerCase();
            String expected = sample.expected_result.toLowerCase();
            
            if (expected.contains(address)) {
                num_found++;
            } else
                if (printFailedExtractions) {
                    AddressCandidates.lastProcessedCandidates.printOut();
                    System.out.println(sample);
                    System.out.println("Found address: " + address);
                    System.out.println();
                }

        }
        
        System.out.println("Num samples - " + samples.size());
        System.out.println("Num found - " + num_found);
        System.out.println("Effectiveness: " + 100 * num_found / samples.size() + "%");
        System.out.println();
    }

}
