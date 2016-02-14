package net.highersoft.mstats.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigService {
	private static Log log=LogFactory.getLog(ConfigService.class);
	public static String  initConfig(File dbPath,File configFile) throws IOException{		
		if(!dbPath.exists()){
			dbPath.mkdirs();	
			if(!configFile.exists()){
				configFile.createNewFile();
				PrintWriter pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile),"UTF-8")));
				pw.println("db="+dbPath+File.separator+"highersort.db");
				pw.close();
				File db=new File(dbPath+File.separator+"highersort.db");
				if(!db.exists()){
					db.createNewFile();
				}
				return db.getAbsolutePath();
			}
		}
		return null;
	}
	public static boolean checkConfig(boolean initFlag,String parentPath) {		
		File configFile=new File(getConfigPath(parentPath));
		File dbPath=new File(configFile.getParent()+File.separator+"db");
		
		
		try{
			if(!dbPath.exists()|| !configFile.exists()){
				//如果要初始化
				if(initFlag){
					String db=initConfig(dbPath,configFile);	
					if(db!=null){
						ConfigService.createSqliteTable(parentPath);
					}
				}
				
			}
			
	      
			return true;
			
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

	public static String getDbPath(String configPath) {
		return "jdbc:sqlite:/" + configPath + "/" + "methodstatis" + "/db"
				+ "/" + "highersort.db";
	}
	public static String getConfigPath(String configPath) {
		return configPath+File.separator+"methodstatis"+File.separator+"methodstatis.properties";
	}
}
