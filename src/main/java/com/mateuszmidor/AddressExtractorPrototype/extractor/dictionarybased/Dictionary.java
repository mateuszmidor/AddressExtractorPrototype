package com.mateuszmidor.AddressExtractorPrototype.extractor.dictionarybased;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Dictionary extends LinkedList<String> {

	private static final long serialVersionUID = 7903221359652849151L;

	public static Dictionary fromFile(String filename) {

		Dictionary result = new Dictionary();
		try (Scanner s = new Scanner(Paths.get(filename))) {
			result.readLines(s);
		} catch (IOException e) {
			System.out.println("Could not load dictionary " + filename);
		}

		return result;
	}

	public void sortLongestToShortest() {
		Collections.sort(this, new LongToShortComparator());
	}

	private void readLines(Scanner s) {
		while (s.hasNextLine()) {
			String line = s.nextLine().trim().toLowerCase();
			if (!line.isEmpty()) {
				add(line);
			}
		}
	}
	
	public void print() {
		for (String key : this) {
			System.out.println(key);
		}
	}

}