/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import mofokom.slee.SleeAnnotationProcessor;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.w3c.dom.Node;

/**
 *
 * @author wozza
 */
public class QueryParserTest {

    public QueryParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testQuery1() throws Exception {
        String query = "( X range-match 1 2 or X less-than#somecollator 0 ) and someParameter#otherCollator has-prefix 44 ";
        query="x less-than anotherParameter#oneCollator and another has-prefix something";
        String queryName = "myquery";
        Node e = new SleeAnnotationProcessor().parse(query, Arrays.asList(new String[] {"x","another"}), 0,Arrays.asList(new String[]{"something","anotherParameter"}));
        Transformer transform = TransformerFactory.newInstance().newTransformer();
        transform.transform(new DOMSource(e), new StreamResult(System.err));
    }
}
