package de.gwdg.metadataqa.api.interfaces;

/**
 * Implementation of the Observer design pattern (https://en.wikipedia.org/wiki/Observer_pattern)
 * There is a core Java implementation (java.util.Observable), but that is not
 * an interface but class, so it breaks some OO design principles.
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public interface Observable {

	/**
	 * Notify observers about a change.
	 */
	void notifyObservers();

	/**
	 * Add an observer object to the subject.
	 *
	 * @param observer
	 *   An observer object
	 */
	void addObserver(Observer observer);

	/**
	 * Removes an observer object from the list of registered observers.
	 *
	 * @param observer
	 *   An observer object
	 */
	void deleteObserver(Observer observer);

}
