package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * This class represents list of known addresses. It allows to load from file.
 * It also allows to generate mutations of address, that can be found in source
 * texts It enforces that entries added with "add" method are always lowercase.
 * 
 * @author m.midor
 *
 */
public class Dictionary extends LinkedList<String> {

	private static final long serialVersionUID = 1018433824638220292L;

	public static Dictionary fromFile(String filename) {

		Dictionary dictionary = new Dictionary();
		try (Scanner s = new Scanner(Paths.get(filename), "UTF8")) {
			dictionary.readLines(s);
		} catch (IOException e) {
			System.out.println("Could not load dictionary " + filename);
		}

		return dictionary;
	}

	private void readLines(Scanner s) {
		while (s.hasNextLine()) {
			String line = s.nextLine().trim().toLowerCase();
			addLineIfNotEmpty(line);
		}
	}

	private void addLineIfNotEmpty(String line) {
		if (!line.isEmpty()) {
			add(line);
		}
	}

	public void generateMutations() {
		generateSurnames();
		generateDeclinations();
		generateWithNoPolishLetters();
	}

	private void generateWithNoPolishLetters() {
		List<String> mutations = new LinkedList<String>();

		for (String original : this) {
			String mutated = normalizeString(original);
			addMutationIfDiffers(mutated, original, mutations);
		}
		this.addAll(mutations);
	}

	// Change polish characters to ascii characters, eg π->a, Ê->c, ..., ø->z
	private String normalizeString(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
	}

	private void addMutationIfDiffers(String mutated, String original,
			List<String> mutations) {
		if (!mutated.equals(original)) {
			mutations.add(mutated);
		}
	}

	private void generateSurnames() {
		List<String> mutations = new LinkedList<String>();

		for (String s : this) {
			if (hasSurname(s)) {
				String mutation = extractSurname(s);
				addMutationIfLongEnaugh(mutation, mutations);
			}
		}
		this.addAll(mutations);
	}
	
	private boolean hasSurname(String s) {
		return s.contains(" ");
	}

	private String extractSurname(String s) {
		return s.substring(s.lastIndexOf(" ") + 1);
	}

	private void addMutationIfLongEnaugh(String mutation, List<String> mutations) {
		if (mutation.length() > 2) {
			mutations.add(mutation);
		}
	}

	private void generateDeclinations() {
		Map<String, String> NORMAL_MODAL = new HashMap<>();
		NORMAL_MODAL.put("ska", "skiej"); // krakowskiej
		NORMAL_MODAL.put("cka", "ckiej"); // tynieckiej
		NORMAL_MODAL.put("ny", "nach"); // czy≈ºynach
		NORMAL_MODAL.put("czne", "cznym"); // s≈Çonecznym
		List<String> mutations = new LinkedList<String>();

		for (String s : this) {
			generateDeclination(s, NORMAL_MODAL, mutations);
		}

		this.addAll(mutations);
	}

	private void generateDeclination(String s,
			Map<String, String> normal_modal, List<String> mutations) {

		for (Entry<String, String> entry : normal_modal.entrySet()) {
			String normal = entry.getKey();
			String modal = entry.getValue();
			declinateIfPossible(s, normal, modal, mutations);
		}
	}

	private void declinateIfPossible(String s, String normal, String modal,
			List<String> mutations) {
		if (s.endsWith(normal)) {
			mutations.add(s.replaceAll(normal, modal));
		}
	}
	
	public static String removeDeclination(String s) {
		Map<String, String> MODAL_NORMAL = new HashMap<>();
		MODAL_NORMAL.put("skiej", "ska"); // krakowskiej
		MODAL_NORMAL.put("ckiej", "cka"); // tynieckiej
		MODAL_NORMAL.put("nach", "ny"); // czy≈ºynach
		MODAL_NORMAL.put("cznym", "czne"); // s≈Çonecznym
		
		s = s.toLowerCase();
		for (Entry<String, String> modal : MODAL_NORMAL.entrySet()) {
			String key = modal.getKey();
			
			// if contains modal part - replace it for normal part and return
			if (s.contains(key)) {
				String value = modal.getValue();
				return s.replaceAll(key, value);
			}
		}
		// no modal part - return original string
		return s;
	}

	public void print() {
		for (String entry : this) {
			System.out.println(entry);
		}
	}

	@Override
	public boolean add(String s) {
		return super.add(s.toLowerCase());
	}

}
