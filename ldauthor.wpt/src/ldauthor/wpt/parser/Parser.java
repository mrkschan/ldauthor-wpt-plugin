package ldauthor.wpt.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import ldauthor.wpt.ontology.LearningDesign;

import org.cyberneko.html.parsers.DOMParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.xpath.XPath;
import org.tencompetence.jdom.JDOMXMLUtils;

import com.hp.hpl.jena.ontology.Individual;


public class Parser {

	public static class ElementPair {
		public Individual individual;
		public String     content;

		public ElementPair(Individual i, String c) {
			individual = i;
			content = c;
		}
	}

	private static class CacheWorker extends Thread {

		private String url;
		private HashMap<String, Document> cache;

		public CacheWorker(String url, HashMap<String, Document> cache)
		{
			this.url   = url;
			this.cache = cache;
		}

		public void run()
		{
			try {
				// parse HTML DOM by NekoHTML
				DOMBuilder builder = new DOMBuilder();
				DOMParser parser = new DOMParser();
				parser.parse(url);

				cache.put(url, builder.build(parser.getDocument()));

			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

// TODO: check icdl host?
	public static List<ElementPair>	parse(String[] urls, String icdl)
	{
		List<ElementPair> individuals = new ArrayList<ElementPair>();

		HashMap<String, Document>
			html_cache = new HashMap<String, Document>();

		// cache HTML JDOM
		for (String url : urls) {
			CacheWorker t = new CacheWorker(url, html_cache);
			t.start();
			try { t.join(); }
			catch (InterruptedException e) {}
		}

		try {
			Document icdl_doc = JDOMXMLUtils.readXMLFile(new File(icdl));
			Element icdl_root = icdl_doc.getRootElement();

			String icdl_host = icdl_root.getAttributeValue("host");
			Pattern icdl_host_regex = Pattern.compile(icdl_host);

			List<Element> icdl_url_sets = XPath.selectNodes(icdl_root, "//urls");

			for (String url : html_cache.keySet())
			{
				if (false == icdl_host_regex.matcher(url).find()) {
					System.err.println(
						"URL: " + url + " does not match ICDL host: " + icdl_host);
					continue;
				}

				Iterator<Element> iter = icdl_url_sets.iterator();

				while (iter.hasNext())
				{
					Element icdl_url_set  = iter.next();
					String parse_template = icdl_url_set.getAttributeValue("template");

					Iterator<Element> url_iter =
						XPath.selectNodes(icdl_url_set, "//url").iterator();

					while (url_iter.hasNext())
					{
						Element icdl_url = url_iter.next();
						Pattern regex = Pattern.compile(icdl_url.getAttributeValue("url"));

						if (regex.matcher(url).find()) {
							extractFromHtml(individuals,
								html_cache.get(url), parse_template, icdl_root);
							// continue with next template
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);

			return null;
		}

		return individuals;
	}


	private static
	void extractFromHtml(
		List<ElementPair> individuals,
		Document html_content,
		String parse_template,
		Element icdl_root)
	throws Exception
	{
		Iterator<Element> rule_iter = XPath.selectNodes(icdl_root,
			"//template[@name='" + parse_template + "']/html_tag").iterator();

		while (rule_iter.hasNext())
		{
			Element rule = rule_iter.next();
			String concept = rule.getAttributeValue("content");
			String xpath = rule.getAttributeValue("xpath");

			Element html_el = (Element) XPath.selectSingleNode(html_content, xpath);
			if (null != html_el) {
				individuals.add(new ElementPair(
					LearningDesign.getInstance().createIndividual(concept),
					html_el.getValue() ));
			}
		}


		Iterator<Element> container_iter = XPath.selectNodes(icdl_root,
			"//template[@name='" + parse_template + "']/container").iterator();

		while (container_iter.hasNext())
		{
			Element container = container_iter.next();
			String container_xpath = container.getAttributeValue("container_xpath");

			Element container_el = (Element) XPath.selectSingleNode(html_content, container_xpath);
			if (null == container_el) continue; // skip null container

			Iterator<Element> block_iter = XPath.selectNodes(
				container, "./repeatable_block").iterator();

			while (block_iter.hasNext())
			{
				Element block = block_iter.next();
				String block_xpath = block.getAttributeValue("block_xpath");

				List<Element> block_els = XPath.selectNodes(container_el, block_xpath);
				if (null == block_els || 0 == block_els.size()) {
					// re-try xpath by /html instead of container_el
					block_els = XPath.selectNodes(html_content, block_xpath);
				}

				rule_iter = XPath.selectNodes(block, "./html_tag").iterator();

				while (rule_iter.hasNext())
				{
					Element rule = rule_iter.next();
					String concept = rule.getAttributeValue("content");
					String xpath = rule.getAttributeValue("xpath");

					for (Element block_el : block_els)
					{
						Element html_el = (Element) XPath.selectSingleNode(block_el, xpath);
						if (null == html_el) {
							// re-try xpath by /html instead of block_el
							html_el = (Element) XPath.selectSingleNode(html_content, xpath);
						}

						if (null != html_el) {
							individuals.add(new ElementPair(
								LearningDesign.getInstance().createIndividual(concept),
								html_el.getValue() ));
						}
					}

				}
			}
		}
	}
}
