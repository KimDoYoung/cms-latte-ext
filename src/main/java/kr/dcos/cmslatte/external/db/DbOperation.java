package kr.dcos.cmslatte.external.db;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import kr.dcos.cmslatte.annotation.CmsLatteFunction;
import kr.dcos.cmslatte.exception.CmsLatteException;
import kr.dcos.cmslatte.exception.CmsLatteFunctionException;
import kr.dcos.cmslatte.field.MatrixField;
import kr.dcos.cmslatte.functions.LatteFunctionBase;

public class DbOperation  extends LatteFunctionBase {
	
	
	private static Connection getConnection(String driverName,String url, String id, String password){
		Connection connection = null;
		try{
			Class.forName(driverName);		
			connection = DriverManager.getConnection(url, id, password);
		}catch(Exception e){
			return null;
		}
		return connection;
	}
	
	@CmsLatteFunction(anotherName="executeQuery", desc="return matrix from selected table of db ",backArg="false")
	public static MatrixField dbSelect(Object... args) throws CmsLatteFunctionException{
		String functionName = "dbSelect";
		String protoType = String.format("matrix=%s(driverName,url,userId,userPassword,selectSql)",functionName);
		
				
		// argument length check
		if (argsLength(args.length, 5) == false) {
			throw new CmsLatteFunctionException(functionName, protoType,
					ERROR_ARGS_LENGTH);
		}
		
		
		String driverName = (String)args[0];
		String url = (String)args[1];
		String userId = (String)args[2];
		String userPassword = (String)args[3];
		String selectSql = (String)args[4];
		
		Connection conn = getConnection(driverName,url,userId,userPassword);
		if(conn == null){
			String s = String.format("driver:[%s],url:[%s],userId:[%s],userPassword:[%s]",driverName,url,userId,userPassword);
			throw new CmsLatteFunctionException("mySqlSelect","connection fail:"+s);
		}
		
		MatrixField matrixField = new MatrixField("tmp");
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			int rowIndex = 0;
			while (rs.next()) {
				//ZooE ::: jdbc column index start with 1 ^^ 
				for(int colIndex=0;colIndex<colCount;colIndex++){
					int type = rsmd.getColumnType(colIndex+1);
					Object value = getDbValue(type,rs.getObject(colIndex+1));
					matrixField.appendOrReplace(rowIndex, colIndex, value);						
				}
				rowIndex++;
			}
			return matrixField;
		}catch (SQLException e) {
			throw new CmsLatteFunctionException("mySqlSelect", e.getMessage());
		} catch (CmsLatteException e) {
			throw new CmsLatteFunctionException("mySqlSelect", e.getMessage());
		} catch (IOException e) {
			throw new CmsLatteFunctionException("mySqlSelect", e.getMessage());
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				;
			}
		}
	}
	@CmsLatteFunction(anotherName="executeUpdate", desc="execute DDL, if success return OK, if fail return error message",backArg="false")
	public static String dbExecute(Object... args) throws CmsLatteFunctionException{
		String functionName = "dbExecute";
		String protoType = String.format("boolean=%s(driverName,url,userId,userPassword,selectSql)",functionName);
		
				
		// argument length check
		if (argsLength(args.length, 5) == false) {
			throw new CmsLatteFunctionException(functionName, protoType,
					ERROR_ARGS_LENGTH);
		}
		String driverName = (String)args[0];
		String url = (String)args[1];
		String userId = (String)args[2];
		String userPassword = (String)args[3];
		String selectSql = (String)args[4];
		
		Connection conn = getConnection(driverName,url,userId,userPassword);
		if(conn == null){
			String s = String.format("driver:[%s],url:[%s],userId:[%s],userPassword:[%s]",driverName,url,userId,userPassword);
			throw new CmsLatteFunctionException("mySqlSelect","connection fail:"+s);
		}
		
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(selectSql);
			return "OK";
		}catch (SQLException e) {
			return e.getMessage();
		}finally{
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				;
			}
		}
	}
	/**
	 * type은 ResultSet의 ColumnType , object는 value이다.
	 * type에 따라서 해당하는 처리한다
	 * @param type
	 * @param object
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private static Object getDbValue(int type, Object object) throws SQLException, IOException {
		if(object==null) return "";
		if(type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP){
			return (Date)object;
		}else if(type == Types.LONGNVARCHAR || type == Types.CHAR || type == Types.VARCHAR){
			return (String)object;
		}else if(type == Types.CLOB){
		     StringBuffer sb = new StringBuffer();

		     Clob rd =  (Clob)object;
		     // CLOB column에 대한 스트림을 얻는다
		     Reader reader = rd.getCharacterStream() ;

            char[] buf = new char[1024];
            int readcnt;

            while((readcnt=reader.read(buf,0,1024))!=-1){
            // 스트림으로부터 읽어서 스트링 버퍼에 넣는다.
                sb.append(buf,0,readcnt);
            }

            if(reader != null)	reader.close();
            return sb.toString();
			
		}else {
			return object.toString();
		}

	}
}