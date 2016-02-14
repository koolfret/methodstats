package net.highersoft.mstats.dao;



import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.highersoft.mstats.model.VisitorData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * 
 * @author chengzhong
 *
 */
public class VisitorDataDao  implements  Serializable{
	private static Log log=LogFactory.getLog(VisitorDataDao.class);
	private JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdfdate=new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat sdfhour=new SimpleDateFormat("HH");
	
	private String insertSql="insert into visitor_data(sys_id,menu_path,erp_id,create_time,create_date,create_hour,path_type) values(?, ?, ?, ?,?,?,?)";
	/*查询访问记录*/
	private String queryDateCountSql="select create_date,count(id) num,count(distinct menu_path) pathNum,count(distinct erp_id) erpNum from visitor_data where create_date>=? and create_date<=?  group by create_date";
	private String queryPathDateCountSql="select create_date,count(id) num,count(distinct menu_path) pathNum,count(distinct erp_id) erpNum from visitor_data where  menu_path=? and create_date>=? and create_date<=?  group by create_date";
	/*查询某用户的访问记录*/
	private String queryDateCountErpSql="select erp_id,create_date,count(id) num from visitor_data where sys_id=?  and create_date>=? and create_date<=?  group by erp_id,create_date";
	private String queryPathDateCountErpSql="select erp_id,create_date,count(id) num from visitor_data where sys_id=? and path_type=? and menu_path=? and create_date>=? and create_date<=?  group by erp_id,create_date";
	/*系统信息*/
	private String querySysInfoSql="select * from visitor_system where system_id=?";
	private String delSysInfoSql="delete from visitor_system where system_id=?";
	private String addSysInfoSql="insert into visitor_system(system_id,system_name,path,path_name,path_type,create_time,update_time,updator) values(?,?,?,?,?,?,?,?)";
	private String querySysNames="select distinct system_id,system_name from visitor_system";
    public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public String getQueryDateCountSql() {
		return queryDateCountSql;
	}
	
	public String getQuerySysInfoSql() {
		return querySysInfoSql;
	}

	public void setQuerySysInfoSql(String querySysInfoSql) {
		this.querySysInfoSql = querySysInfoSql;
	}

	public String getQueryPathDateCountSql() {
		return queryPathDateCountSql;
	}

	public void setQueryPathDateCountSql(String queryPathDateCountSql) {
		this.queryPathDateCountSql = queryPathDateCountSql;
	}

	public void setQueryDateCountSql(String queryDateCountSql) {
		this.queryDateCountSql = queryDateCountSql;
	}
	
	public String getQueryDateCountErpSql() {
		return queryDateCountErpSql;
	}

	public void setQueryDateCountErpSql(String queryDateCountErpSql) {
		this.queryDateCountErpSql = queryDateCountErpSql;
	}

	public String getQueryPathDateCountErpSql() {
		return queryPathDateCountErpSql;
	}

	public void setQueryPathDateCountErpSql(String queryPathDateCountErpSql) {
		this.queryPathDateCountErpSql = queryPathDateCountErpSql;
	}
	
	public String getDelSysInfoSql() {
		return delSysInfoSql;
	}

	public void setDelSysInfoSql(String delSysInfoSql) {
		this.delSysInfoSql = delSysInfoSql;
	}

	public String getAddSysInfoSql() {
		return addSysInfoSql;
	}

	public void setAddSysInfoSql(String addSysInfoSql) {
		this.addSysInfoSql = addSysInfoSql;
	}
	
	public String getQuerySysNames() {
		return querySysNames;
	}

	public void setQuerySysNames(String querySysNames) {
		this.querySysNames = querySysNames;
	}
	
    

	public void addVisitorData(final VisitorData visitorData)
    {
		if(visitorData!=null){
			//sysId,erpId,menuPath,visitDate,type,remark			
			jdbcTemplate.update(getInsertSql(),   
	                new PreparedStatementSetter() {
	            	public void setValues(PreparedStatement ps) throws SQLException {   
		                ps.setInt(1, visitorData.getSystemId());	                          
		                ps.setString(2, visitorData.getMenuPath());
		                ps.setString(3, visitorData.getErpId());  
		                ps.setTimestamp(4, new Timestamp(visitorData.getVisitDate().getTime()));   
		                ps.setInt(5, Integer.parseInt(sdfdate.format(visitorData.getVisitDate())));
		                ps.setInt(6, Integer.parseInt(sdfhour.format(visitorData.getVisitDate())));
		                ps.setInt(7, visitorData.getPathType());
		            }   
	        });			
		}		
    }
	
	public List<Map<String,Object>> queryTotalVisitorData(final Map<String,Object> param){
		if(param!=null){			
			return jdbcTemplate.queryForList(getQueryDateCountSql(),param.get("startDate"),param.get("endDate"));
		}
		return null;
		       
    }
	public List<Map<String,Object>> queryPathVisitorData(final Map<String,Object> param){
		if(param!=null){			
			//return jdbcTemplate.queryForList(getQueryPathDateCountSql(),param.get("systemId"),param.get("pathType"),param.get("url"),param.get("startDate"),param.get("endDate"));
			return jdbcTemplate.queryForList(getQueryPathDateCountSql(),param.get("url"),param.get("startDate"),param.get("endDate"));
		}
		return null;
    }
	
	public List<Map<String,Object>> queryTotalVisitorErpData(final Map<String,Object> param){
		if(param!=null){			
			return jdbcTemplate.queryForList(getQueryDateCountErpSql(),param.get("systemId"),param.get("startDate"),param.get("endDate"));
		}
		return null;
		       
    }
	public List<Map<String,Object>> queryPathVisitorErpData(final Map<String,Object> param){
		if(param!=null){			
			return jdbcTemplate.queryForList(getQueryPathDateCountErpSql(),param.get("systemId"),param.get("pathType"),param.get("url"),param.get("startDate"),param.get("endDate"));
		}
		return null;
    }
	
	public List<Map<String,Object>> querySystemInfos(int sysId){				
		return jdbcTemplate.queryForList(getQuerySysInfoSql(),sysId);	
	}
	public void delSystemInfos(int sysId){
		jdbcTemplate.update(getDelSysInfoSql(),sysId);	
	}
	public void addSystemInfos(Map<String,String> pathInfos,int sysId,String sysName){
		if(pathInfos!=null){
			Calendar cal=Calendar.getInstance();
			for(Entry<String,String> entry:pathInfos.entrySet()){
				String path=entry.getKey();
				String pathName=entry.getValue();
				jdbcTemplate.update(getAddSysInfoSql(),sysId,sysName,path,pathName,0,cal.getTime(),cal.getTime(),sysId);
				
			}
		}
			
	}
	public List<Map<String,Object>> querySystemNames(){
		return jdbcTemplate.queryForList(getQuerySysNames());
	}
}
