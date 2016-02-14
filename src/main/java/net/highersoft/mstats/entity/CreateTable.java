package net.highersoft.mstats.entity;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable {

	

	public void createTable() throws Exception{
		Class.forName("org.sqlite.JDBC");

		Connection conn = DriverManager.getConnection("jdbc:sqlite:/d:/tmp/test.db");		

		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS visitor_data");
		stmt.executeUpdate("CREATE TABLE `visitor_data` ("+
	"`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
	"`sys_id` INT(10) NULL DEFAULT NULL ,"+
	"`menu_path` VARCHAR(500) NULL DEFAULT NULL ,"+
	"`erp_id` VARCHAR(100) NULL DEFAULT NULL ,"+
	"`create_time` DATETIME NULL DEFAULT NULL ,"+
	"`create_date` INT(11) NULL DEFAULT NULL ,"+
	"`create_hour` INT(11) NULL DEFAULT NULL ,"+
	"`path_type` INT(10) NULL DEFAULT NULL "+
	
")");
		
		stmt.execute("DROP TABLE IF EXISTS visitor_system");
		stmt.executeUpdate("CREATE TABLE `visitor_system` ("+
	"`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
	"`system_id` INT(11) NULL DEFAULT NULL ,"+
	"`system_name` VARCHAR(50) NULL DEFAULT NULL ,"+
	"`path` VARCHAR(500) NULL DEFAULT NULL ,"+
	"`path_name` VARCHAR(50) NULL DEFAULT NULL ,"+
	"`path_type` INT(11) NULL DEFAULT NULL ,"+
	"`create_time` TIMESTAMP NULL DEFAULT NULL ,"+
	"`update_time` TIMESTAMP NULL DEFAULT NULL ,"+
	"`updator` VARCHAR(50) NULL DEFAULT NULL"+
	
")");
		
		stmt.close();
		
		conn.close();
	}
	public static void main(String[] args) throws Exception {
		//classes ../../../../上面4层是tomcat目录
		File tomcat=new File(CreateTable.class.getResource("/").getFile()).getParentFile().getParentFile().getParentFile().getParentFile();
		File configFile=new File(tomcat.getAbsoluteFile()+File.separator+"methodstatis"+File.separator+"methodstatis.properties");
		if(!configFile.getParentFile().exists()){
			configFile.getParentFile().mkdirs();	
			File dbPath=new File(configFile.getParent()+File.separator+"db");
			dbPath.mkdir();
			if(!configFile.exists()){
				configFile.createNewFile();
			}
		}
		//System.out.println("CreateTable:"+);

		CreateTable conn = new CreateTable();

	
		//conn.createTable();

		System.out.print("Success!!");

	}
}
