package kr.dcos.cmslatte.business;

import kr.dcos.cmslatte.annotation.CmsLatteFunction;
import kr.dcos.cmslatte.exception.CmsLatteException;
import kr.dcos.cmslatte.exception.CmsLatteFunctionException;
import kr.dcos.cmslatte.external.db.DbOperation;
import kr.dcos.cmslatte.field.MatrixField;
import kr.dcos.cmslatte.functions.LatteFunctionBase;
import kr.dcos.cmslatte.utils.JsonUtil;

public class Movie extends LatteFunctionBase {
	
	@CmsLatteFunction(anotherName="movieCategory", desc="make json array string ",backArg="false")
	public static String getMovieCategory(Object... args) throws CmsLatteFunctionException{
		
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://220.76.203.236:1433;DatabaseName=DVD_DB";
		String id = "kalpadb";
		String pw = "kalpadb987";
		String sql = "select id,name from movieCategory ";
		
		MatrixField mf = DbOperation.dbSelect(driverName,url,id,pw,sql);
		
		try {
			String s =  JsonUtil.tableToValueTextJsonArray(mf);
			return s;
		} catch (CmsLatteException e) {
			throw new CmsLatteFunctionException(e.getMessage());
		}
	}
}
