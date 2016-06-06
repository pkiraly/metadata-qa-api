package com.nsdr.metadataqa.api;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import net.minidev.json.JSONArray;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class TestJsonPaths {

	Object document;

	public TestJsonPaths() {
	}

	@Before
	public void setUp() throws URISyntaxException, IOException {
	}

	public String readFirstLine(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		return lines.get(0);
	}

	public String readContent(String fileName) throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		return StringUtils.join(lines, "");
	}

	@Test
	public void testJsonPathManual() throws URISyntaxException, IOException {
		document = Configuration.defaultConfiguration()
				  .jsonProvider().parse(readContent("general/book.json"));

		List<String> authors = JsonPath.read(document, "$.store.book[*].author");
		assertEquals(
				  Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien"),
				  authors);

		assertEquals("Nigel Rees", JsonPath.read(document, "$.store.book[0].author"));
		assertEquals("Evelyn Waugh", JsonPath.read(document, "$.store.book[1].author"));

		List<Map<String, Object>> expensiveBooks = JsonPath
				  .using(Configuration.defaultConfiguration())
				  .parse(readContent("general/book.json"))
				  .read("$.store.book[?(@.price > 10)]", List.class);
		assertEquals(2, expensiveBooks.size());

		String json = "{\"date_as_long\" : 1411455611975}";
		Date date = JsonPath.parse(json).read("$['date_as_long']", Date.class);
		assertEquals("23 Sep 2014 07:00:11 GMT", date.toGMTString());

		List<Map<String, Object>> books = JsonPath.read(document, "$.store.book[?(@.price < 10)]");
		assertEquals(2, books.size());

		Filter cheapFictionFilter = filter(where("category").is("fiction").and("price").lte(10D));

		books = JsonPath.read(document, "$.store.book[?]", cheapFictionFilter);
		assertEquals(1, books.size());

		Filter fooOrBar = filter(where("category").exists(true)).or(where("price").exists(true));
		books = JsonPath.read(document, "$.store.book[?]", fooOrBar);
		assertEquals(4, books.size());

		Filter fooAndBar = filter(where("category").exists(true))
		                   .and(where("price").exists(true));
		books = JsonPath.read(document, "$.store.book[?]", fooAndBar);
		assertEquals(4, books.size());
	}

	@Test
	public void testOr() throws URISyntaxException, IOException {
		document = Configuration.defaultConfiguration()
				  .jsonProvider().parse(readContent("general/test.json"));
		String providerProxy = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')][?]";

		String idPath = "$.identifier";
		String dataProviderPath = "$.['ore:Aggregation'][0]['edm:dataProvider'][0]";
		String datasetPath = "$.sets[0]";

		Filter fooOrBar = filter(where("identifier").exists(true)).or(Criteria.where("sets").exists(true));
		Map<String, Object> books = JsonPath.read(document, "$", fooOrBar);
		assertEquals(9, books.size());

		Filter titleOrDescription = filter(where("dc:title").exists(true)).or(Criteria.where("dc:description").exists(true));
		JSONArray proxy = JsonPath.read(document, providerProxy, titleOrDescription);
		assertEquals(1, proxy.size());

		Filter titleAndDescription = filter(where("dc:title").exists(true)).and(Criteria.where("dc:description").exists(true));
		proxy = JsonPath.read(document, providerProxy, titleAndDescription);
		assertEquals(0, proxy.size());

		proxy = JsonPath.read(document, providerProxy, filter(where("['dc:title1']").exists(true)));
		assertEquals(0, proxy.size());

		titleOrDescription = filter(where("['dc:title1']").exists(true)).and(Criteria.where("['dc:description']").exists(true));
		proxy = JsonPath.read(document, providerProxy, titleOrDescription);
		assertEquals(0, proxy.size());
	}
}
