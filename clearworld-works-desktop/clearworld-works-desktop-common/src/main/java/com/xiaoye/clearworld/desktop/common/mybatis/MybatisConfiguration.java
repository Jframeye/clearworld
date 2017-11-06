/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.mybatis;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.xiaoye.clearworld.utils.PropUtils;

/**
 * @desc TODO
 * @author mubreeze
 * @date 2017年11月6日
 */
public class MybatisConfiguration {

	private static SqlSessionFactory sessionFactory;

	private static boolean isInit = false;

	/**
	 * 初始化mybatis环境
	 * @param filePath
	 * @throws Exception
	 */
	public static void initMybatis(String filePath) throws Exception {
		if (!isInit) {
			Properties properties = PropUtils.loadProperties(filePath);
			check(properties);
			DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
			TransactionFactory transactionFactory = new JdbcTransactionFactory();
			Environment environment = new Environment("development", transactionFactory, dataSource);
			Configuration configuration = new Configuration(environment);

			// 加载注解类
			registerMybatis(properties.getProperty("basePackage"), configuration);

			sessionFactory = new SqlSessionFactoryBuilder().build(configuration);
			isInit = true;
		}
	}

	/**
	 * 注册mybatis注解
	 * @param basePackage 扫描包
	 * @param configuration
	 * @throws IOException 
	 */
	private static void registerMybatis(String basePackage, Configuration configuration) throws Exception {
		List<String> list = VFS.getInstance().list(basePackage);
		for (String str : list) {
			if (str.endsWith(".class")) {
				String externalName = str.substring(0, str.indexOf('.')).replace('/', '.');
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class<?> clazz = loader.loadClass(externalName);
				if (clazz.getAnnotation(Mapper.class) != null) {
					configuration.addMapper(clazz);
				}
			}
		}
	}

	/**
	 * 属性文件的基本检查
	 * @param properties
	 */
	private static void check(Properties properties) {
		if (properties == null) {
			throw new RuntimeException("clearword.jdbc.properties is not exist or load failure.");
		}
		if (properties.getProperty("basePackage") == null) {
			throw new RuntimeException("clearword.jdbc.properties has not basePackage, please set it a value.");
		}
		if (properties.getProperty("driverClassName") == null) {
			throw new RuntimeException("clearword.jdbc.properties has not driverClassName, please set it a value.");
		}
		if (properties.getProperty("url") == null) {
			throw new RuntimeException("clearword.jdbc.properties has not url, please set it a value.");
		}
		if (properties.getProperty("password") == null) {
			throw new RuntimeException("clearword.jdbc.properties has not password, please set it a value.");
		}
		if (properties.getProperty("username") == null) {
			throw new RuntimeException("clearword.jdbc.properties has not username, please set it a value.");
		}
	}

	/**
	 * 获取数据库session
	 * @return
	 */
	public static SqlSession getSession() {
		return sessionFactory.openSession();
	}
}
