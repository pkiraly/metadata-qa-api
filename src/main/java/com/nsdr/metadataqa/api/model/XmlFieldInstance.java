package com.nsdr.metadataqa.api.model;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class XmlFieldInstance implements Serializable {

	private String value;
	private String language;

	public XmlFieldInstance() {
	}

	public XmlFieldInstance(String value) {
		this.value = value;
	}

	public XmlFieldInstance(String value, String language) {
		this.value = value;
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean hasValue() {
		return StringUtils.isNotBlank(value);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean hasLanguage() {
		return StringUtils.isNotBlank(language);
	}

	public boolean isEmpty() {
		return !hasValue() && !hasLanguage();
	}

	@Override
	public String toString() {
		return "EdmFieldInstance{" + "value=" + value + ", language=" + language+ '}';
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 19 * hash + Objects.hashCode(this.value);
		hash = 19 * hash + Objects.hashCode(this.language);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final XmlFieldInstance other = (XmlFieldInstance) obj;
		if (!Objects.equals(this.value, other.value)) {
			return false;
		}
		if (!Objects.equals(this.language, other.language)) {
			return false;
		}
		return true;
	}
}
