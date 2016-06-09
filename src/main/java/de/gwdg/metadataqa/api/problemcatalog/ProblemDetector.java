package de.gwdg.metadataqa.api.problemcatalog;

import de.gwdg.metadataqa.api.interfaces.Observer;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public abstract class ProblemDetector implements Observer {

	protected ProblemCatalog problemCatalog;
}
