package ldauthor.wpt.ontology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class LearningDesign {

	private static LearningDesign instance;
	public static LearningDesign getInstance() {
		if (null == instance) instance = new LearningDesign();
		return instance;
	}

	private OntModel m;
	private static ArrayList<String> concepts;
	private String base = "http://www.cc.uah.es/ie/ont/imsLd#";

	protected LearningDesign() {
		try {
			m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
			m.read(
				this.getClass().getResource("eume_imsld.owl").openStream(), base);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void prepare_cache() {
		this.dump();
	}

	public Individual createIndividual(String concept) {
		return m.createIndividual(m.getOntClass(base + concept));
	}

	public String getOntClass(Individual individual) {
		return m.shortForm(individual.getOntClass().getURI())
				.replaceFirst(":", "");
	}

	public List<String> dump()
	{
		if (null == concepts) {
			concepts = new ArrayList<String>();

	        Iterator<OntClass> i = m.listHierarchyRootClasses();

	        HashSet<OntClass> visited = new HashSet<OntClass>();
	        while (i.hasNext()) {
	        	dump_r(i.next(), visited);
	        }
		}

        return concepts;
    }

	public void dump_r(OntClass cls, HashSet<OntClass> visited)
	{
		String prefix = "";
		if (!cls.isAnon()) {

			for (Iterator<OntClass> i = cls.listSuperClasses(true); i.hasNext();) {
				OntClass cc = i.next();
				if (!cc.isRestriction() && !visited.contains(cc)) return;
			}

			visited.add(cls);

			// render no sub-class only
			if (false == cls.listSubClasses(true).hasNext()) render(cls, "");
			prefix = cls.getModel().shortForm(cls.getURI());
		}

		// list restrictions (properties)
//		for (Iterator<OntClass> i = cls.listSuperClasses(true); i.hasNext();) {
//			OntClass cc = i.next();
//			if (cc.isRestriction()) render(cc, prefix);
//		}

		// traverse to sub-classes
		for (Iterator<OntClass> i = cls.listSubClasses(true); i.hasNext();) {
			OntClass cc = i.next();
			if (!visited.contains(cc)) dump_r(cc, visited);
		}
	}

	public void render(OntClass c, String prefix)
	{
    	String text = null;
		if (c.isRestriction()) {

			Restriction r = c.asRestriction();
			OntProperty p = r.getOnProperty();

			if (p.isDatatypeProperty()) {
				text = prefix + m.shortForm(p.getURI());
			}

    	} else if (!c.isAnon()) {
			text = prefix + m.shortForm(c.getURI());
    	}

		if (null != text) {
			text = text.replaceFirst(":", "").replaceAll(":", ".");
    		concepts.add(text);

//    		System.out.println(text);
		}
	}

}
