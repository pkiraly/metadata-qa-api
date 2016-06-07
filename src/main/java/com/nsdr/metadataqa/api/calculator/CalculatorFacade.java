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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 * @param <T>
 */
public class CalculatorFacade {

	protected boolean runFieldExistence = true;
	protected boolean runFieldCardinality = true;
	protected boolean runCompleteness = true;
	protected boolean runTfIdf = false;
	protected boolean runProblemCatalog = false;
	private boolean changed = false;

	protected List<Calculator> calculators;
	protected FieldExtractor fieldExtractor;
	protected CompletenessCalculator completenessCalculator;
	protected TfIdfCalculator tfidfCalculator;
	protected LanguageCalculator languageCalculator;
	
	private XmlFieldInstance xmlFieldInstance = new XmlFieldInstance();

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

		if (runCompleteness) {
			completenessCalculator = new CompletenessCalculator(new EdmSchema());
			calculators.add(completenessCalculator);
		}

		if (runTfIdf) {
			tfidfCalculator = new TfIdfCalculator(new EdmSchema());
			calculators.add(tfidfCalculator);
		}

		if (runProblemCatalog) {
			ProblemCatalog problemCatalog = new ProblemCatalog();
			LongSubject longSubject = new LongSubject(problemCatalog);
			TitleAndDescriptionAreSame titleAndDescriptionAreSame = new TitleAndDescriptionAreSame(problemCatalog);
			EmptyStrings emptyStrings = new EmptyStrings(problemCatalog);
			calculators.add(problemCatalog);
		}
	}

	public void configureCounter(Counters counters) {
		counters.doReturnFieldExistenceList(runFieldExistence);
		counters.doReturnFieldInstanceList(runFieldCardinality);
		counters.doReturnTfIdfList(runTfIdf);
		counters.doReturnProblemList(runProblemCatalog);
	}

	public String calculate(String jsonRecord) throws InvalidJsonException {
		return this.<XmlFieldInstance>genericCalculate(jsonRecord);
	}

	protected <T extends XmlFieldInstance> String genericCalculate(String jsonRecord) 
			throws InvalidJsonException {
		changed();
		Counters counters = new Counters();
		configureCounter(counters);

		JsonPathCache<T> cache = new JsonPathCache<>(jsonRecord);

		for (Calculator calculator : getCalculators()) {
			calculator.calculate(cache, counters);
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



}
