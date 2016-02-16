package kr.dcos.cmslatte.external.db;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import kr.dcos.cmslatte.exception.CmsLatteFunctionException;
import kr.dcos.cmslatte.field.MatrixField;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MySqlTest {

//private static Logger logger = LoggerFactory.getLogger(MySqlTest.class);


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMySqlSelect() throws CmsLatteFunctionException {
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://220.76.203.236:3306/cmsnew";
		String id = "cms";
		String pw = "cms987";
		String sql = "select * from wcm_config ";
		MatrixField mf = DbOperation.dbSelect(driverName,url,id,pw,sql);
		assertNotNull(mf);
		System.out.println(mf.toString());
	}
//	@Test
//	public void testSelectOracle() throws CmsLatteFunctionException {
//		String driverName = "oracle.jdbc.driver.OracleDriver";
//		String url = "jdbc:oracle:thin:@220.76.203.237:1521:SMSD";
//		String id = "cms";
//		String pw = "cms987";
//		String sql = "select * from wcm_user ";
//		MatrixField mf = MySql.mySqlSelect(driverName,url,id,pw,sql);
//		assertNotNull(mf);
//		System.out.println(mf.toString());
//		
//	}
	@Test
	public void testSelectMsSql() throws CmsLatteFunctionException {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://220.76.203.236:1433;DatabaseName=DVD_DB";
		String id = "kalpadb";
		String pw = "kalpadb987";
		String sql = "select id,name from movieCategory ";

		MatrixField mf = DbOperation.dbSelect(driverName,url,id,pw,sql);
		assertNotNull(mf);
		System.out.println(mf.toString());
		
	}
	@Test
	public void testdbExecute() throws CmsLatteFunctionException {
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://220.76.203.236:3306/cmsnew";
		String id = "cms";
		String pw = "cms987";
		String sql = "insert wcm_config values('a','b','c','d')";
		String s  = DbOperation.dbExecute(driverName,url,id,pw,sql);
		assertEquals(s,"OK");
		sql = "delete from  wcm_config where name='a'";
		s  = DbOperation.dbExecute(driverName,url,id,pw,sql);
		assertEquals(s,"OK");
	}
	
}
