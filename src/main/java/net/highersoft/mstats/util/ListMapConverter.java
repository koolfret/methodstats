package net.highersoft.mstats.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author chengzhong
 *
 */
public class ListMapConverter {
	/**
	 * 
	 * @param head
	 * @param leftColKey
	 * @param headKey
	 * @param valKey
	 * @param src 这个数据源需是按leftColKey排序的
	 * @return
	 */
	public static  List<List<String>> rowtocol(LinkedHashMap<Object,String> head,String leftColKey,String headKey,String valKey,List<Map<String,Object>> src){
		List<List<String>> rst=new ArrayList<List<String>>();
		LinkedHashMap<Object,String> tmpHead=(LinkedHashMap<Object,String>)head.clone();
		if(src!=null){
			Object leftColVal=null;
			Object curLeftColVal=null;
			int addCount=0;
			for(Map<String,Object> m:src){
				if(leftColVal==null){
					leftColVal=m.get(leftColKey);
				}
				curLeftColVal=m.get(leftColKey);
				Object curVal=m.get(valKey);
				Object curHead=m.get(headKey);
				
				if(curLeftColVal!=null&&!curLeftColVal.equals(leftColVal)){
					List<String> row=new ArrayList();
					row.add(String.valueOf(leftColVal));
					row.addAll(tmpHead.values());
					rst.add(row);
					tmpHead=(LinkedHashMap<Object,String>)head.clone();
					leftColVal=curLeftColVal;
					addCount=0;
				}
				if(tmpHead.get(curHead)!=null){
					addCount++;
					tmpHead.put(curHead,String.valueOf(curVal));
				}
			}
			//最后如果有新加入tmp的，加入最终结果
			if(addCount>0){
				List<String> row=new ArrayList();
				row.add(String.valueOf(leftColVal));
				row.addAll(tmpHead.values());
				rst.add(row);
			}
			
		}
		return rst;
	}

}
