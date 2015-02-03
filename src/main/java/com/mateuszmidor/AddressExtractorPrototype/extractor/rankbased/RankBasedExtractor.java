package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mateuszmidor.AddressExtractorPrototype.extractor.AddressSources;
import com.mateuszmidor.AddressExtractorPrototype.extractor.Extractor;

public class RankBasedExtractor implements Extractor {
	private Dictionary streets = new Dictionary();
	private Dictionary districts = new Dictionary();
	private Dictionary cities = new Dictionary();

	public RankBasedExtractor(String streets_filename,
			String districts_filename, String cities_filename) {

		streets = Dictionary.fromFile(streets_filename);
		streets.generateMutations();
		districts = Dictionary.fromFile(districts_filename);
		districts.generateMutations();
		cities = Dictionary.fromFile(cities_filename);
		cities.generateMutations();
	}

	@Override
	/**
	 * Extract address candidates based on known streets, districts and cities names,
	 * then rank the candidates correctness probability and precision
	 * using rank evaluators and return the highest-ranked one
	 */
	public String extract(AddressSources sources) {
		AddressCandidates addressCandidates = new AddressCandidates();

		extractFromSources(sources, streets, addressCandidates);
		extractFromSources(sources, districts, addressCandidates);
		extractFromSources(sources, cities, addressCandidates);

		RankEvaluator prefix = new RankPrefix();
		prefix.evaluate(addressCandidates);

		RankEvaluator suffix = new RankSuffix();
		suffix.evaluate(addressCandidates);

		RankEvaluator capitalLetter = new RankCapitalLetter();
		capitalLetter.evaluate(addressCandidates);

		addressCandidates.sortByCorrectnessAndPrecision();

		if (!addressCandidates.isEmpty()) {
			String address = addressCandidates.getFirst().address;
			return Dictionary.removeDeclination(address);
		}

		return "[nothing found]";
	}

	/**
	 * Looks up dictionary keywords in sources
	 *
	 * @param sources
	 *            Where to look for address
	 * @param dict
	 *            Dictionary with known addresses to look up. The Dictionary
	 *            entries MUST BE ALL LOWER CASE for the algorithm to work
	 * @return Address from dictionary, along with number that follows (if any)
	 */
	private void extractFromSources(final AddressSources sources,
			final Dictionary dict, AddressCandidates results) {

		for (String source : sources) {
			extractFromSource(source, dict, results);
		}
	}

	private void extractFromSource(String source, final Dictionary dict,
			AddressCandidates results) {

		String lowercaseSource = source.toLowerCase();
		for (String address : dict) {
			if (lowercaseSource.contains(address)) {
				extractAddressWithNumber(address, source, results);
			}
		}
	}

	private void extractAddressWithNumber(String address, String source,
			AddressCandidates results) {

		// Obligatory space, then number, then possibly a letter
		// eg. " 23a" in "Wieliczka 23a"
		// ?: is for non-capturing group
		final String OPTIONAL_NUMBER = "(?:[ ]{1,5}\\d{1,5}\\w?)?";

		// \\b is for word boundary
		Pattern p = Pattern.compile("\\b" + address + OPTIONAL_NUMBER + "\\b",
				Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);

		if (m.find()) {
			String addressWithNumber = m.group();
			addAddressCandidate(addressWithNumber, source, results);
		}
	}

	private void addAddressCandidate(String address, String source,
			AddressCandidates results) {
		AddressCandidate r = new AddressCandidate();
		r.address = address;
		r.source = source;
		results.add(r);
	}

}
