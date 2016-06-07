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
public class CalculatorFacade<T extends XmlFieldInstance> {

	protected final boolean doFieldExistence;
	protected final boolean doFieldCardinality;
	protected final boolean doCompleteness;
	protected final boolean doTfIdf;
	protected final boolean doProblemCatalog;
	protected boolean doAbbreviate = false;
	protected List<Calculator> calculators;
	protected FieldExtractor fieldExtractor;
	protected CompletenessCalculator completenessCalculator;
	protected TfIdfCalculator tfidfCalculator;
	protected LanguageCalculator languageCalculator;

	public CalculatorFacade(boolean doFieldExistence, boolean doFieldCardinality, 
			  boolean doCompleteness, boolean doTfIdf, boolean doProblemCatalog) {
		this.doFieldExistence = doFieldExistence;
		this.doFieldCardinality = doFieldCardinality;
		this.doCompleteness = doCompleteness;
		this.doTfIdf = doTfIdf;
		this.doProblemCatalog = doProblemCatalog;
		setupCalculators();
	}

	private void setupCalculators() {
		calculators = new ArrayList<>();
		fieldExtractor = new FieldExtractor();

		if (doCompleteness) {
			completenessCalculator = new CompletenessCalculator(new EdmSchema());
			calculators.add(completenessCalculator);
		}

		if (doTfIdf) {
			tfidfCalculator = new TfIdfCalculator(new EdmSchema());
			calculators.add(tfidfCalculator);
		}

		if (doProblemCatalog) {
			ProblemCatalog problemCatalog = new ProblemCatalog();
			LongSubject longSubject = new LongSubject(problemCatalog);
			TitleAndDescriptionAreSame titleAndDescriptionAreSame = new TitleAndDescriptionAreSame(problemCatalog);
			EmptyStrings emptyStrings = new EmptyStrings(problemCatalog);
			calculators.add(problemCatalog);
		}
	}
	
	public void configureCounter(Counters counters) {
		counters.doReturnFieldExistenceList(doFieldExistence);
		counters.doReturnFieldInstanceList(doFieldCardinality);
		counters.doReturnTfIdfList(doTfIdf);
		counters.doReturnProblemList(doProblemCatalog);
	}

	public String calculate(String jsonRecord) throws InvalidJsonException {
		Counters counters = new Counters();
		configureCounter(counters);

		JsonPathCache<T> cache = new JsonPathCache<>(jsonRecord);

		for (Calculator calculator : getCalculators()) {
			calculator.calculate(cache, counters);
		}

		// return the result of calculations
		return counters.getFullResults(false, true);
	}

	public boolean isDoFieldExistence() {
		return doFieldExistence;
	}

	public boolean isDoFieldCardinality() {
		return doFieldCardinality;
	}

	public boolean isDoCompleteness() {
		return doCompleteness;
	}

	public boolean isDoTfIdf() {
		return doTfIdf;
	}

	public boolean isDoProblemCatalog() {
		return doProblemCatalog;
	}

	public List<Calculator> getCalculators() {
		return calculators;
	}

	public void doAbbreviate(boolean doAbbreviate) {
		this.doAbbreviate = doAbbreviate;
	}

}
