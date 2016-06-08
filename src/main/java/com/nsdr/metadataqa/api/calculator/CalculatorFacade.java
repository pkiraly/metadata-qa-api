package com.nsdr.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import com.nsdr.metadataqa.api.counter.Counters;
import com.nsdr.metadataqa.api.interfaces.Calculator;
import com.nsdr.metadataqa.api.schema.EdmSchema;
import com.nsdr.metadataqa.api.model.JsonPathCache;
import com.nsdr.metadataqa.api.model.XmlFieldInstance;
import com.nsdr.metadataqa.api.problemcatalog.EmptyStrings;
import com.nsdr.metadataqa.api.problemcatalog.LongSubject;
import com.nsdr.metadataqa.api.problemcatalog.ProblemCatalog;
import com.nsdr.metadataqa.api.problemcatalog.TitleAndDescriptionAreSame;
import com.nsdr.metadataqa.api.uniqueness.TfIdf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CalculatorFacade implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(CalculatorFacade.class.getCanonicalName());

	protected boolean runFieldExistence = true;
	protected boolean runFieldCardinality = true;
	protected boolean runCompleteness = true;
	protected boolean runTfIdf = false;
	protected boolean runProblemCatalog = false;
	protected boolean runLanguage = false;
	protected boolean collectTfIdfTerms = false;
	protected boolean verbose = false;
	private boolean changed = false;

	protected List<Calculator> calculators;
	protected FieldExtractor fieldExtractor;
	protected CompletenessCalculator completenessCalculator;
	protected TfIdfCalculator tfidfCalculator;
	protected LanguageCalculator languageCalculator;
	protected Counters counters;

	public CalculatorFacade() {
	}

	public CalculatorFacade(boolean runFieldExistence, boolean runFieldCardinality,
			boolean runCompleteness, boolean runTfIdf, boolean runProblemCatalog) {
		this.runFieldExistence = runFieldExistence;
		this.runFieldCardinality = runFieldCardinality;
		this.runCompleteness = runCompleteness;
		this.runTfIdf = runTfIdf;
		this.runProblemCatalog = runProblemCatalog;
	}

	protected void changed() {
		if (changed == true) {
			configure();
			changed = false;
		}
	}

	public void configure() {
		calculators = new ArrayList<>();
		fieldExtractor = new FieldExtractor();
		EdmSchema schema = new EdmSchema();

		if (runCompleteness) {
			completenessCalculator = new CompletenessCalculator(schema);
			completenessCalculator.setVerbose(verbose);
			calculators.add(completenessCalculator);
		}

		if (runTfIdf) {
			tfidfCalculator = new TfIdfCalculator(schema);
			tfidfCalculator.setDoCollectTerms(collectTfIdfTerms);
			calculators.add(tfidfCalculator);
		}

		if (runProblemCatalog) {
			ProblemCatalog problemCatalog = new ProblemCatalog();
			LongSubject longSubject = new LongSubject(problemCatalog);
			TitleAndDescriptionAreSame titleAndDescriptionAreSame = new TitleAndDescriptionAreSame(problemCatalog);
			EmptyStrings emptyStrings = new EmptyStrings(problemCatalog);
			calculators.add(problemCatalog);
		}

		if (runLanguage) {
			languageCalculator = new LanguageCalculator(schema);
			calculators.add(languageCalculator);
		}
	}

	public void configureCounter(Counters counters) {
		counters.returnFieldExistenceList(runFieldExistence);
		counters.returnFieldInstanceList(runFieldCardinality);
		counters.returnTfIdfList(runTfIdf);
		counters.returnProblemList(runProblemCatalog);
		counters.returnLanguage(runLanguage);
	}

	public String measure(String jsonRecord) throws InvalidJsonException {
		return this.<XmlFieldInstance>measureWithGenerics(jsonRecord);
	}

	protected <T extends XmlFieldInstance> String measureWithGenerics(String jsonRecord) 
			throws InvalidJsonException {
		changed();
		counters = new Counters();
		configureCounter(counters);

		JsonPathCache<T> cache = new JsonPathCache<>(jsonRecord);

		for (Calculator calculator : getCalculators()) {
			calculator.measure(cache, counters);
		}

		return counters.getFullResults(false, true);
	}

	/**
	
	@return 
	*/
	public boolean runFieldExistence() {
		return runFieldExistence;
	}

	public void runFieldExistence(boolean runFieldCardinality) {
		if (this.runFieldExistence != runFieldCardinality) {
			this.runFieldExistence = runFieldCardinality;
			changed = true;
		}
	}

	public boolean runFieldCardinality() {
		return runFieldCardinality;
	}

	public void runFieldCardinality(boolean runFieldCardinality) {
		if (this.runFieldCardinality != runFieldCardinality) {
			this.runFieldCardinality = runFieldCardinality;
			changed = true;
		}
	}

	public boolean runCompleteness() {
		return runCompleteness;
	}

	public void runCompleteness(boolean runCompleteness) {
		if (this.runCompleteness != runCompleteness) {
			this.runCompleteness = runCompleteness;
			changed = true;
		}
	}

	public boolean runLanguage() {
		return runLanguage;
	}

	public void runLanguage(boolean runLanguage) {
		this.runLanguage = runLanguage;
	}

	public boolean runTfIdf() {
		return runTfIdf;
	}

	public void runTfIdf(boolean runTfIdf) {
		if (this.runTfIdf != runTfIdf) {
			this.runTfIdf = runTfIdf;
			changed = true;
		}
	}

	public boolean runProblemCatalog() {
		return runProblemCatalog;
	}

	public void runProblemCatalog(boolean runProblemCatalog) {
		if (this.runProblemCatalog != runProblemCatalog) {
			this.runProblemCatalog = runProblemCatalog;
			changed = true;
		}
	}

	public List<Calculator> getCalculators() {
		return calculators;
	}

	public boolean collectTfIdfTerms() {
		return collectTfIdfTerms;
	}

	public void collectTfIdfTerms(boolean collectTfIdfTerms) {
		if (this.collectTfIdfTerms != collectTfIdfTerms) {
			this.collectTfIdfTerms = collectTfIdfTerms;
			changed = true;
		}
	}

	public boolean verbose() {
		return verbose;
	}

	public void verbose(boolean verbose) {
		LOGGER.info("verbose is " + verbose);
		if (this.verbose != verbose) {
			this.verbose = verbose;
			changed = true;
		}
		LOGGER.info("this.verbose is " + this.verbose);
	}

	public List<String> getExistingFields() {
		return completenessCalculator.getExistingFields();
	}

	public List<String> getEmptyFields() {
		return completenessCalculator.getEmptyFields();
	}

	public List<String> getMissingFields() {
		return completenessCalculator.getMissingFields();
	}

	public Map<String, List<TfIdf>> getTermsCollection() {
		return tfidfCalculator.getTermsCollection();
	}

	public Counters getCounters() {
		return counters;
	}
}