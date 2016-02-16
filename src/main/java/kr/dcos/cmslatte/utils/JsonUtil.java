package kr.dcos.cmslatte.utils;

import kr.dcos.cmslatte.exception.CmsLatteException;
import kr.dcos.cmslatte.field.MatrixField;
import kr.dcos.cmslatte.functions.LatteFunctionBase;

public class JsonUtil extends LatteFunctionBase {
	
	public static String tableToValueTextJsonArray(MatrixField mf) throws CmsLatteException{

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0;i<mf.getRowCount();i++){
			sb.append("{value:\"" + mf.getValue(i, 0).toString()+"\"");
			sb.append(",");
			sb.append("text:\"" + mf.getValue(i, 1).toString()+"\"},");
		}
		if(sb.toString().endsWith(",")){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}
}
