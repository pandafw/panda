package panda.ex.wordpress;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.ex.wordpress.bean.Taxonomy;
import panda.ex.wordpress.bean.Term;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Numbers;

public class TaxonomyTest extends AbstractWordpressTest {

	@Test
	public void testGetTaxonomy() throws Exception {
		Taxonomy t = WP.getTaxonomy("category");

//		System.out.println(t);
		Assert.assertNotNull(t);
		Assert.assertEquals("category", t.name);
	}
	
	@Test
	public void testGetTaxonomies() throws Exception {
		List<Taxonomy> ts = WP.getTaxonomies();

//		System.out.println(ts);
		Assert.assertNotNull(ts);
		Assert.assertEquals(3, ts.size());

		List<String> ets = Arrays.toList( Taxonomy.CATEGORY, Taxonomy.POST_TAG, Taxonomy.POST_FORMAT );
		List<String> ats = new ArrayList<String>();
		for (Taxonomy t : ts) {
			ats.add(t.name);
		}
		Collections.sort(ets);
		Collections.sort(ats);
		Assert.assertEquals(ets, ats);
	}

	@Test
	public void testGetTerms() throws Exception {
		List<Term> result = WP.getTerms("category");

		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals(4, result.size());
		
		List<String> ecs = Arrays.toList( "Anime", "Misc", "Program", "Shopping" );
		List<String> acs = new ArrayList<String>();
		for (Term c : result) {
			acs.add(c.name);
		}
		Collections.sort(ecs);
		Collections.sort(acs);
		
		Assert.assertEquals(ecs, acs);
	}

	@Test
	public void testGetTerm() throws Exception {
		Term result = WP.getTerm("category", 1);

		System.out.print(result);
		Assert.assertNotNull(result);
		Assert.assertEquals("Misc", result.name);
	}


	@Test
	public void testEditTerm() throws Exception {
		Term term = new Term();
		
		term.taxonomy = "category";
		term.name = "New Term !";
		
		String tid = WP.newTerm(term);
		
		Assert.assertTrue(Numbers.toLong(tid) > 0);

		term.name = "Edit Term !";
		boolean re = WP.editTerm(term);
		Assert.assertTrue(re);

		boolean rd = WP.deleteTerm("category", tid);
		Assert.assertTrue(rd);
	}
}
