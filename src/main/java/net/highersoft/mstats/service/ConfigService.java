package net.highersoft.mstats.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigService {
	private static Log log=LogFactory.getLog(ConfigService.class);
	private static String  initConfig(File dbPath,File configFile) throws IOException{		
		if(!dbPath.exists()){
			dbPath.mkdirs();	
			if(!configFile.exists()){
				configFile.createNewFile();
				/*PrintWriter pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile),"UTF-8")));
				pw.println("db="+dbPath+File.separator+"highersoft.db");
				pw.close();*/
				File db=new File(dbPath+File.separator+"highersoft.db");
				if(!db.exists()){
					db.createNewFile();
				}
				return db.getAbsolutePath();
			}
		}
		return null;
	}
	/**
	 * 
	 * @param parentPath
	 * @return 是否执行了初始化
	 */
	public static boolean initFolderDB(String parentPath) {
		File parentFile=new File(parentPath);
		if(!parentFile.exists()) {
			parentFile.mkdirs();
		}
		File configFile=new File(getConfigPath(parentPath));
		File dbFolder=new File(configFile.getParent()+File.separator+"db");
		
		
		try{
			if(!dbFolder.exists()|| !configFile.exists()){
				//开始初始化				
				String db=initConfig(dbFolder,configFile);	
				if(db!=null){
					ConfigService.createSqliteTable(parentPath);
				}
				
				return true;
			}
			return false;
	      
			
			
		}catch(Exception e){
			log.error(e.getMessage(),e);
			return false;
		}
			
	}
	public static void createSqliteTable(String parentPath) throws Exception {
		/* /d:/tmp/test.db */
		Class.forName("org.sqlite.JDBC");

		Connection conn = DriverManager.getConnection(getDbPath(parentPath));

		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS visitor_data");
		stmt.executeUpdate("CREATE TABLE `visitor_data` ("
				+ "`id` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`sys_id` INT(10) NULL DEFAULT NULL ,"
				+ "`menu_path` VARCHAR(500) NULL DEFAULT NULL ,"
				+ "`erp_id` VARCHAR(100) NULL DEFAULT NULL ,"
				+ "`create_time` DATETIME NULL DEFAULT NULL ,"
				+ "`create_date` INT(11) NULL DEFAULT NULL ,"
				+ "`create_hour` INT(11) NULL DEFAULT NULL ,"
				+ "`path_type` INT(10) NULL DEFAULT NULL " +

				")");

		stmt.execute("DROP TABLE IF EXISTS visitor_system");
		stmt.executeUpdate("CREATE TABLE `visitor_system` ("
				+ "`id` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "`system_id` INT(11) NULL DEFAULT NULL ,"
				+ "`system_name` VARCHAR(50) NULL DEFAULT NULL ,"
				+ "`path` VARCHAR(500) NULL DEFAULT NULL ,"
				+ "`path_name` VARCHAR(50) NULL DEFAULT NULL ,"
				+ "`path_type` INT(11) NULL DEFAULT NULL ,"
				+ "`create_time` TIMESTAMP NULL DEFAULT NULL ,"
				+ "`update_time` TIMESTAMP NULL DEFAULT NULL ,"
				+ "`updator` VARCHAR(50) NULL DEFAULT NULL" +

				")");

		stmt.close();

		conn.close();
	}

	public static String getDbPath(String parentPath) {
		return "jdbc:sqlite:/" + parentPath + "/db"
				+ "/" + "highersoft.db";
	}
	public static String getConfigPath(String parentPath) {
		return parentPath+File.separator+"methodstatis.properties";
	}
}
