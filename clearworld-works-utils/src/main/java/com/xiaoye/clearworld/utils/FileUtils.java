/**
 * 
 */
package com.xiaoye.clearworld.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @desc 一般文件工具类
 * @author mubreeze
 * @date 2017年11月5日
 */
public class FileUtils {

	/**
	 * 获取相对根路径的文件路径
	 * @param packagePath 工程下的文件
	 * @return
	 */
	public static String getRelativeFilePath(String packagePath) {
		return FileUtils.class.getClassLoader().getResource(packagePath).getPath();
	}

	/**
	 * 保存文文件
	 * @param filePath 文件保存路径
	 * @param file 要保存的文件
	 * @return 返回文件名称
	 * @throws IOException 
	 */
	public static String save(String filePath, File file) throws IOException {
		String fileName = file.getName();
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = new FileOutputStream(filePath + File.separator + fileName);
		save(inputStream, outputStream);
		return fileName;
	}

	/**
	 * 保存文文件
	 * @param filePath 文件保存路径
	 * @param fileName 新文件名称（不带后缀）
	 * @param file 要保存的文件
	 * @return 返回文件名称
	 * @throws IOException 
	 */
	public static String save(String filePath, String fileName, File file) throws IOException {
		fileName += getSuffixOfFile(file.getName());
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = new FileOutputStream(filePath + File.separator + fileName);
		save(inputStream, outputStream);
		return fileName;
	}
	
	/**
	 * 保存文文件
	 * 
	 * @param filePath 文件保存路径
	 * @param file 要保存的文件
	 * @param randomFileName 随机重新命名文件
	 * @return 返回文件名称
	 * @throws IOException
	 */
	public static String save(String filePath, File file, boolean randomFileName) throws IOException {
		if(!randomFileName) {
			return save(filePath, file);
		}
		String fileName = getRandomFileName(file.getName(), null);
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = new FileOutputStream(filePath + File.separator + fileName);
		save(inputStream, outputStream);
		return fileName;
	}
	
	/**
	 * 生成随机的文件名称
	 * @param fileName 源文件名称或路径名称
	 * @param prefix 目标文件名称前缀
	 * @return
	 */
	public static String getRandomFileName(String fileName, String prefix) {
		String suffix = getSuffixOfFile(fileName);
		if (prefix == null) {
			fileName = prefix;
		}
		String random = StringUtils.randomNumber(6);
		fileName += random + suffix;
		return fileName;
	}

	/**
	 * 获取文件扩展名
	 * @param fileName 文件名称 或 带名称的文件路径
	 * @return
	 */
	public static String getSuffixOfFile(String fileName) {
		if (fileName == null || fileName.indexOf(".") == -1) {
			throw new IllegalArgumentException("无效的文件名称");
		}
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 保存文件
	 * @param inputStream
	 * @param outputStream
	 * @throws IOException
	 */
	private static void save(InputStream inputStream, OutputStream outputStream) throws IOException {
		// 1K的数据缓冲
		byte[] datacache = new byte[1024];
		// 读取到的数据长度
		int length;
		while ((length = inputStream.read(datacache)) != -1) {
			outputStream.write(datacache, 0, length);
		}
		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}
}
