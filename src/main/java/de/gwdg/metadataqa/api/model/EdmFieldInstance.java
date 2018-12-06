package de.gwdg.metadataqa.api.model;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class EdmFieldInstance extends XmlFieldInstance {

  private String resource;

  public EdmFieldInstance() {
    super();
  }

  public EdmFieldInstance(String value) {
    super(value);
  }

  public EdmFieldInstance(String value, String language) {
    super(value, language);
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public boolean hasResource() {
    return StringUtils.isNotBlank(resource);
  }

  public boolean isEmpty() {
    return !hasValue() && !hasLanguage() && !hasResource();
  }

  public boolean isUrl() {
    return (hasResource() || (hasValue() && (getValue().startsWith("http://") || getValue().startsWith("https://"))));
  }

  public String getUrl() {
    if (hasResource()) {
      return getResource();
    }

    if (isUrl()) {
      return getValue();
    }

    return null;
  }

  @Override
  public String toString() {
    return "EdmFieldInstance{" + "value=" + getValue() + ", language=" + getLanguage() + ", resource=" + resource + '}';
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 19 * hash + Objects.hashCode(getValue());
    hash = 19 * hash + Objects.hashCode(getLanguage());
    hash = 19 * hash + Objects.hashCode(getResource());
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
    final EdmFieldInstance other = (EdmFieldInstance) obj;
    if (!Objects.equals(this.getValue(), other.getValue())) {
      return false;
    }
    if (!Objects.equals(this.getLanguage(), other.getLanguage())) {
      return false;
    }
    if (!Objects.equals(this.getResource(), other.getResource())) {
      return false;
    }
    return true;
  }
}
