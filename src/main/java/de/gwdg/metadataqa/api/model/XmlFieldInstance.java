package de.gwdg.metadataqa.api.model;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * Instance of an XML field
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class XmlFieldInstance implements Serializable {

  private static final long serialVersionUID = 4355586872066308913L;
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
    return "XmlFieldInstance{"
      + "value=" + value
      + ", language=" + language
      + '}';
  }

  @Override
  public int hashCode() {
    var hash = 5;
    hash = 19 * hash + Objects.hashCode(this.value);
    hash = 19 * hash + Objects.hashCode(this.language);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof XmlFieldInstance))
      return false;

    if (this == obj)
      return true;

    final XmlFieldInstance other = (XmlFieldInstance) obj;
    return    Objects.equals(this.value,    other.value)
           && Objects.equals(this.language, other.language);
  }
}
