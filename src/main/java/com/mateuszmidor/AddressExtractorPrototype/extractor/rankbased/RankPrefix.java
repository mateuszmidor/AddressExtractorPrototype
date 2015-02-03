package com.mateuszmidor.AddressExtractorPrototype.extractor.rankbased;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ranks up correctness and precission if street/alley/district prefix is
 * present before address
 * @author m.midor
 *
 */
public class RankPrefix implements RankEvaluator {
	final static int HI_PRECISION_RANK = 3;
	final static int MEDIUM_PRECISION_RANK = 2;
	final static int LOW_PRECISION_RANK = 0;

	@Override
	public void evaluate(AddressCandidates results) {
		final String[] HI_PRECISION_PREFIX = { "ulica", "ulicy", "ul ", "ul.", "aleja", "alei", "al ", "al." };
		final String[] MEDIUM_PRECISION_PREFIX = { "os ", "os.", "osiedle",	"osiedlu" };
		final String[] LOW_PRECISION_PREFIX = { "przy", "na", "obok", "w" };
		rankByPrefix(results, HI_PRECISION_PREFIX, HI_PRECISION_RANK);
		rankByPrefix(results, MEDIUM_PRECISION_PREFIX, MEDIUM_PRECISION_RANK);
		rankByPrefix(results, LOW_PRECISION_PREFIX, LOW_PRECISION_RANK);
	}

	private void rankByPrefix(AddressCandidates results,
			final String[] prefixes, int precision) {
		for (AddressCandidate r : results) {
			rankCandidate(r, prefixes, precision);
		}
	}

	private void rankCandidate(AddressCandidate r, final String[] prefixes,
			int precision) {

		for (String prefix : prefixes) {
			prefix = escapeRegexSpecialCharacters(prefix);
			Pattern p = composePattern(prefix, r.address);
			Matcher m = p.matcher(r.source);
			if (m.find()) {
				r.correctnessRank++;
				r.precisionRank += precision;
				break;
			}
		}
	}

	private Pattern composePattern(String prefix, String address) {
		// prefix space address, eg. "ul. Wielicka"
		return Pattern.compile("\\b" + prefix + "\\s{0,3}" + address,
				Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);
	}

	private String escapeRegexSpecialCharacters(String regex) {
		return regex.replace(".", "\\.");
	}

}
