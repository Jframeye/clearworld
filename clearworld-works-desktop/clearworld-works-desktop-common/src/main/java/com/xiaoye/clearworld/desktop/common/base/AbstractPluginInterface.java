/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.base;

/**
 * @desc 抽象插件接口类
 * @author mubreeze
 * @date 2017年11月4日
 */
public abstract class AbstractPluginInterface {

	/**
	 * @return the pluginPackage
	 */
	public abstract String getPluginPackage();

	/**
	 * 初始化插件
	 */
	public abstract Plugin initPlugin();
}
