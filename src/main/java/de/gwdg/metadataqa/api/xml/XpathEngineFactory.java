package de.gwdg.metadataqa.api.xml;

import org.apache.ws.commons.util.NamespaceContextImpl;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.LinkedHashMap;
import java.util.Map;

public class XpathEngineFactory {

  private static final Map<String, String> defaultNamespaces = new LinkedHashMap<>();
  static {
    defaultNamespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    defaultNamespaces.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    defaultNamespaces.put("dc", "http://purl.org/dc/elements/1.1/");
    defaultNamespaces.put("dcterms", "http://purl.org/dc/terms/");
    defaultNamespaces.put("edm", "http://www.europeana.eu/schemas/edm/");
    defaultNamespaces.put("owl", "http://www.w3.org/2002/07/owl#");
    defaultNamespaces.put("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#");
    defaultNamespaces.put("skos", "http://www.w3.org/2004/02/skos/core#");
    defaultNamespaces.put("rdaGr2", "http://rdvocab.info/ElementsGr2/");
    defaultNamespaces.put("foaf", "http://xmlns.com/foaf/0.1/");
    defaultNamespaces.put("ebucore", "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#");
    defaultNamespaces.put("doap", "http://usefulinc.com/ns/doap#");
    defaultNamespaces.put("odrl", "http://www.w3.org/ns/odrl/2/");
    defaultNamespaces.put("cc", "http://creativecommons.org/ns#");
    defaultNamespaces.put("ore", "http://www.openarchives.org/ore/terms/");
    defaultNamespaces.put("svcs", "http://rdfs.org/sioc/services#");
    defaultNamespaces.put("oa", "http://www.w3.org/ns/oa#");
    defaultNamespaces.put("dqv", "http://www.w3.org/ns/dqv#");
    defaultNamespaces.put("xml", "http://www.w3.org/XML/1998/namespace");
    defaultNamespaces.put("oai", "http://www.openarchives.org/OAI/2.0/");
  }

  public static XPath initializeEngine() {
    return initializeEngine(null);
  }

  public static XPath initializeEngine(Map<String, String> customNamespaces) {
    var xPathfactory = XPathFactory.newInstance();
    XPath xpathEngine = xPathfactory.newXPath();
    xpathEngine.setNamespaceContext(createNamespaceContext(customNamespaces));
    return xpathEngine;
  }

  private static NamespaceContext createNamespaceContext(Map<String, String> customNamespaces) {
    var nsContext = new NamespaceContextImpl();
    for (Map.Entry<String, String> entry : defaultNamespaces.entrySet())
      nsContext.startPrefixMapping(entry.getKey(), entry.getValue());

    if (customNamespaces != null && !customNamespaces.isEmpty())
      for (Map.Entry<String, String> entry : customNamespaces.entrySet())
        nsContext.startPrefixMapping(entry.getKey(), entry.getValue());
    return nsContext;
  }
}
