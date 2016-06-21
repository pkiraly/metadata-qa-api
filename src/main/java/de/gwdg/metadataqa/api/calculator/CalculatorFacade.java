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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

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
	protected boolean completenessCollectFields = false;
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
	 * Create calculator facade with the default configuration
	 */
	public CalculatorFacade() {
	}

	/**
	 * Create calculator facade with configuration
	 * @param runFieldExistence
	 *   Flag whether or not run the field existence measurement
	 * @param runFieldCardinality
	 *   Flag whether or not run the field cardinality measurement
	 * @param runCompleteness
	 *   Flag whether or not run the completeness measurement
	 * @param runTfIdf
	 *   Flag whether or not run the uniqueness measurement
	 * @param runProblemCatalog
	 *   Flag whether or not run the problem catalog
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
			completenessCalculator.collectFields(completenessCollectFields);
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

	/**
	 * The generic version of measure.
	 * The class to pass should define the individual field's representations, so
	 * it is bound to the schema the record in. The class should be the extension
	 * of XmlFieldInstance
	 * 
	 * @param <T>
	 *   A class defining the internal representation of a fiel. It should be
	 *   an extension of XmlFieldInstance
	 * @param jsonRecord
	 *   The JSON record
	 * @return
	 *   The result of measurements as a CSV string
	 * @throws InvalidJsonException 
	 */
	protected <T extends XmlFieldInstance> String measureWithGenerics(String jsonRecord) 
			throws InvalidJsonException {
		changed();

		JsonPathCache<T> cache = new JsonPathCache<>(jsonRecord);

		List<String> csvs = new ArrayList<>();
		for (Calculator calculator : getCalculators()) {
			calculator.measure(cache);
			csvs.add(calculator.getCsv(false, true));
		}

		return StringUtils.join(csvs, ",");
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
	 *   Flag to run cardinality measurement
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

	/**
	 * Returns the flag whether or not run the completeness measurement
	 * @return
	 *   Flag whether or not run the completeness measurement
	 */
	public boolean runCompleteness() {
		return runCompleteness;
	}

	/**
	 * Sets the flag whether or not run the completeness measurement
	 * @param runCompleteness
	 *    flag whether or not run the completeness measurement
	 */
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

	/**
	 * Gets flag whether to run the problem catalog measurement
	 * @return
	 *   problem catalog measurement flag
	 */
	public boolean runProblemCatalog() {
		return runProblemCatalog;
	}

	/**
	 * Configure to run the problem catalog measurement
	 * @param runProblemCatalog
	 *   problem catalog measurement flag
	 */
	public void runProblemCatalog(boolean runProblemCatalog) {
		if (this.runProblemCatalog != runProblemCatalog) {
			this.runProblemCatalog = runProblemCatalog;
			changed = true;
		}
	}

	/**
	 * Return the list of all registered calculators
	 * @return
	 *   The calculators
	 */
	public List<Calculator> getCalculators() {
		return calculators;
	}

	/**
	 * Returns the flag whether the measurement should collect each individual 
	 * terms with their Term Ferquency and Invers Document Frequency scores
	 * @return
	 *   The TF-IDF collector flag
	 */
	public boolean collectTfIdfTerms() {
		return collectTfIdfTerms;
	}

	/**
	 * Sets the flag whether the measurement should collect each individual 
	 * terms with their Term Ferquency and Invers Document Frequency scores
	 * @param collectTfIdfTerms
	 *   The TF-IDF collector flag
	 */
	public void collectTfIdfTerms(boolean collectTfIdfTerms) {
		if (this.collectTfIdfTerms != collectTfIdfTerms) {
			this.collectTfIdfTerms = collectTfIdfTerms;
			changed = true;
		}
	}

	/**
	 * Get the completenessCollectFields flag
	 * @return
	 *   completenessCollectFields flag
	 */
	public boolean completenessCollectFields() {
		return completenessCollectFields;
	}

	/**
	 * The completeness calculation will collect empty, existent and missing fields
	 * @param completenessCollectFields
	 *   The completenessCollectFields flag
	 */
	public void completenessCollectFields(boolean completenessCollectFields) {
		if (this.completenessCollectFields != completenessCollectFields) {
			this.completenessCollectFields = completenessCollectFields;
			changed = true;
		}
	}

	/**
	 * Returns the list of existing fields
	 * @return
	 *   The list of existing fields
	 */
	public List<String> getExistingFields() {
		return completenessCalculator.getExistingFields();
	}

	/**
	 * Returns the list of empty fields
	 * @return
	 *   The list of empty fields
	 */
	public List<String> getEmptyFields() {
		return completenessCalculator.getEmptyFields();
	}

	/**
	 * Returns the list of missing fields
	 * @return
	 *    The list of missing fields
	 */
	public List<String> getMissingFields() {
		return completenessCalculator.getMissingFields();
	}

	/**
	 * Returns the TF-IDF term map. The keys are the field names, the values are
	 * lists of TfIdf objects
	 * @return
	 *   TF-IDF term list
	 * @see
	 *   TfIdfCalculator#getTermsCollection()
	 */
	public Map<String, List<TfIdf>> getTermsCollection() {
		return tfidfCalculator.getTermsCollection();
	}

	/**
	 * Returns the result map
	 * @return
	 *   The result map
	 * @see
	 *   Counters#getResults() 
	 */
	public Map<String, Object> getResults() {
		Map<String, Object> results = new LinkedHashMap<>();
		for (Calculator calculator : calculators) {
			results.putAll(calculator.getResultMap());
		}
		return results;
	}

	public Map<String, Map<String, ? extends Object>> getLabelledResults() {
		Map<String, Map<String, ? extends Object>> results = new LinkedHashMap<>();
		for (Calculator calculator : calculators) {
			results.putAll(calculator.getLabelledResultMap());
		}
		return results;
	}
}