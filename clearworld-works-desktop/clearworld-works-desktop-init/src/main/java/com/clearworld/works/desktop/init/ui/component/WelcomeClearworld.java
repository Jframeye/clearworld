/**
 * 
 */
package com.clearworld.works.desktop.init.ui.component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.xiaoye.clearworld.desktop.common.utils.ImageUtils;

/**
 * @desc 欢迎界面
 * @author mubreeze
 * @date 2017年11月4日
 */
public class WelcomeClearworld extends JPanel {

	/** **/
	private static final long serialVersionUID = 1L;

	public WelcomeClearworld() {
		this.initContainer();
	}

	/**
	 * 初始化
	 */
	private void initContainer() {
		this.add(new JLabel(ImageUtils.getImageIcon("images/test.jpg")));
	}
}
