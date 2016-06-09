package de.gwdg.metadataqa.api.calculator;

import com.jayway.jsonpath.InvalidJsonException;
import de.gwdg.metadataqa.api.counter.Counters;
import de.gwdg.metadataqa.api.interfaces.Calculator;
import de.gwdg.metadataqa.api.schema.EdmSchema;
import de.gwdg.metadataqa.api.model.JsonPathCache;
import de.gwdg.metadataqa.api.model.XmlFieldInstance;
import de.gwdg.metadataqa.api.problemcatalog.EmptyStrings;
import de.gwdg.metadataqa.api.problemcatalog.LongSubject;
import de.gwdg.metadataqa.api.problemcatalog.ProblemCatalog;
import de.gwdg.metadataqa.api.problemcatalog.TitleAndDescriptionAreSame;
import de.gwdg.metadataqa.api.uniqueness.TfIdf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The central entry point of the application. It provides a facade to the
 * subsystems.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CalculatorFacade implements Serializable {

	// private static final Logger LOGGER = Logger.getLogger(CalculatorFacade.class.getCanonicalName());

	/**
	 * Flag whether or not run the field existence measurement
	 * (default: true)
	 */
	protected boolean runFieldExistence = true;
	/**
	 * Flag whether or not run the field cardinality measurement
	 * (default: true)
	 */
	protected boolean runFieldCardinality = true;
	/**
	 * Flag whether or not run the completeness measurement
	 * (default: true)
	 */
	protected boolean runCompleteness = true;
	/**
	 * Flag whether or not run the uniqueness measurement
	 * (default: false)
	 */
	protected boolean runTfIdf = false;
	/**
	 * Flag whether or not run the problem catalog
	 * (default: false)
	 */
	protected boolean runProblemCatalog = false;
	/**
	 * Flag whether or not run the language detector
	 * (default: false)
	 */
	protected boolean runLanguage = false;
	/**
	 * Flag whether or not collect TF-IDF terms in uniqueness measurement
	 * (default: false)
	 */
	protected boolean collectTfIdfTerms = false;
	/**
	 * Flag whether or not run missing/empty/existing field collection in completeness
	 * (default: false)
	 */
	protected boolean verbose = false;
	/**
	 * Flag to detect status changes
	 * (default: false)
	 */
	private boolean changed = false;

	/**
	 * The list of registred calculator objects. Those will do the measurements
	 */
	protected List<Calculator> calculators;
	/**
	 * The field exctractor object
	 */
	protected FieldExtractor fieldExtractor;
	/**
	 * The completeness calculator
	 */
	protected CompletenessCalculator completenessCalculator;
	/**
	 * The TF-IDF calculator
	 */
	protected TfIdfCalculator tfidfCalculator;
	/**
	 * The language detector
	 */
	protected LanguageCalculator languageCalculator;
	/**
	 * The counters
	 */
	protected Counters counters;

	/**
	 * Create calculator facade with the default configuration
	 */
	public CalculatorFacade() {
	}

	/**
	 * Create calculator facade with configuration
	 * @param runFieldExistence
	 *   Flag whether or not run the field existence measurement
	 * @param runFieldCardinality
	 * @param runCompleteness
	 * @param runTfIdf
	 * @param runProblemCatalog 
	 */
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

	/**
	 * Run the configuration based on the previously set flags
	 */
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

	/**
	 * Configure the counters object
	 * @param counters
	 *   The Counters object
	 */
	protected void configureCounter(Counters counters) {
		counters.returnFieldExistenceList(runFieldExistence);
		counters.returnFieldInstanceList(runFieldCardinality);
		counters.returnTfIdfList(runTfIdf);
		counters.returnProblemList(runProblemCatalog);
		counters.returnLanguage(runLanguage);
	}

	/**
	 * Run the measurements with each Calculator then returns the result as CSV
	 * @param jsonRecord
	 *   The JSON record string
	 * @return
	 *   The result of measurements as a CSV string
	 * @throws InvalidJsonException 
	 */
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
	 * Returns whether or not to run the field existence measurement
	 * @return 
	 *   field existence measurement flag
	 */
	public boolean runFieldExistence() {
		return runFieldExistence;
	}

	/**
	 * Sets whether or not to run the field existence measurement
	 * @param runFieldExistence
	 *    field existence measurement flag
	 */
	public void runFieldExistence(boolean runFieldExistence) {
		if (this.runFieldExistence != runFieldExistence) {
			this.runFieldExistence = runFieldExistence;
			changed = true;
		}
	}

	/**
	 * Returns whether or not to run cardinality measurement
	 * @return 
	 */
	public boolean runFieldCardinality() {
		return runFieldCardinality;
	}

	/**
	 * configure to run the cardinality measurement
	 * @param runFieldCardinality
	 *    cardinality measurement flag
	 */
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

	/**
	 * Returns the flag whether or not run the language detector
	 * @return
	 *   language detector flag
	 */
	public boolean runLanguage() {
		return runLanguage;
	}

	/**
	 * Configure whether or not run the language detector
	 * @param runLanguage 
	 */
	public void runLanguage(boolean runLanguage) {
		this.runLanguage = runLanguage;
	}

	/**
	 * Returns whether or not run the uniqueness measurement
	 * @return
	 *   uniqueness measurement flag
	 */
	public boolean runTfIdf() {
		return runTfIdf;
	}

	/**
	 * Configure whether or not run the uniqueness measurement
	 * @param runTfIdf
	 *   uniqueness measurement flag
	 */
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
		if (this.verbose != verbose) {
			this.verbose = verbose;
			changed = true;
		}
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

	public Map<String, Double> getResults() {
		return counters.getResults();
	}
}