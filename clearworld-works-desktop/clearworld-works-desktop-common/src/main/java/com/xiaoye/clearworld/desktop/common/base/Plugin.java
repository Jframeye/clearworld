/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.base;

import javax.swing.JPanel;

/**
 * @desc 插件基类
 * @author mubreeze
 * @date 2017年11月4日
 */
public abstract class Plugin extends JPanel {

	/** **/
	private static final long serialVersionUID = 1L;

	public abstract void initPlugin();
}
