package de.gwdg.metadataqa.api.interfaces;

/**
 * Implementation of the Observer design pattern (https://en.wikipedia.org/wiki/Observer_pattern)
 * There is a core Java implementation (java.util.Observable), but that is not
 * an interface but class, so it breaks some OO design principles.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Observable {

	void notifyObservers();
	void addObserver(Observer observer);
	void deleteObserver(Observer observer);

}
