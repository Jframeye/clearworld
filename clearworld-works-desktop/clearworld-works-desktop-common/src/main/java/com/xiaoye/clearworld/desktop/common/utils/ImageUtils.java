/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.utils;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @desc 图片工具类
 * @author mubreeze
 * @date 2017年11月4日
 */
public class ImageUtils {

	/**
	 * 获取图片
	 * @param imagePath
	 * @return
	 */
	public static Image getImage(String imagePath) {
		return getImageIcon(imagePath).getImage();
	}

	/**
	 * 获取图片
	 * @param imagePath
	 * @return
	 */
	public static ImageIcon getImageIcon(String imagePath) {
		return new ImageIcon(getImageURL(imagePath));
	}

	private static URL getImageURL(String imagePath) {
		return ImageUtils.class.getClassLoader().getResource(imagePath);
	}
}
