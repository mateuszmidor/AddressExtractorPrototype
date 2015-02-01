package com.mateuszmidor.AddressExtractorPrototype.extractor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author mateusz This class is basically a collection of test samples. It
 *         allows to read test samples from file/save samples to file.
 */
public class TestSamples extends LinkedList<TestSample> {
    private static final long serialVersionUID = 2013396258330884057L;

    public static TestSamples fromFile(final String filename) throws IOException {
        TestSamples result = new TestSamples();
        TestSample current = new TestSample();
        Scanner s = new Scanner(Paths.get(filename));

        // SOURCE FILE STRUCTURE:
        // source=string1
        // source=string2
        // expected=string3
        // # comment
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String key = getKey(line);
            String value = getValue(line);

            if (key.equals("source")) {
                current.sources.addLast(value);
            } else if (key.equals("expected")) {
                current.expected_result = value;
                result.addLast(current);
                current = new TestSample();
            }
        }
        s.close();
        return result;
    }

    private static String getKey(String line) {
        if (line.startsWith("#")) {
            return "#";
        }
        if (line.contains("=")) {
            return line.split("=")[0].toLowerCase();
        }
        return "";
    }

    private static String getValue(String line) {
        if (line.startsWith("#")) {
            return line.substring(1, line.length());
        }
        if (line.contains("=")) {
            return line.split("=")[1].toLowerCase();
        }
        return "";
    }
    
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            printSamples(pw);
        } catch (IOException e) {
            System.out.println("Error while writing file " + filename);
        }
    }
    
    private void printSamples(PrintWriter pw) {
        int i = 1;
        for (TestSample s : this) {
            pw.println("# " + i + ".");
            for (String src : s.sources) {
                pw.println("source=" + src);
            }
            pw.println("expected=" + s.expected_result);
            i++;
        }
        
    }

    /**
     * Removes test samples that have same contents (fiels: sources)
     */
    public void removeDuplicates() {
        Collections.sort(this);
        Set<TestSample> noDuplicates = new HashSet<TestSample>(this);
        this.clear();
        this.addAll(noDuplicates);
    }
}
