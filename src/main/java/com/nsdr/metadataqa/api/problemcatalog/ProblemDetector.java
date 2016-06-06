package com.nsdr.metadataqa.api.problemcatalog;

import com.nsdr.metadataqa.api.interfaces.Observer;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public abstract class ProblemDetector implements Observer {

	protected ProblemCatalog problemCatalog;
}
