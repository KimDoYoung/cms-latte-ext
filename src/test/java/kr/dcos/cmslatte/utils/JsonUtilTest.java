package kr.dcos.cmslatte.utils;

import static org.junit.Assert.*;
import kr.dcos.cmslatte.exception.CmsLatteException;
import kr.dcos.cmslatte.field.MatrixField;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtilTest {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtilTest.class);
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTableToValueTextJsonArray() {
		MatrixField mf = new MatrixField("tmp");
		try {
			mf.appendOrReplace(0, 0, "A");
			mf.appendOrReplace(0, 1, "AAAA");
			mf.appendOrReplace(1, 0, "B");
			mf.appendOrReplace(1, 1, "BBB");
			String s = JsonUtil.tableToValueTextJsonArray(mf);
			logger.debug(s);
		} catch (CmsLatteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
