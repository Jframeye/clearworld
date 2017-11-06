/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.component;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * @desc 按钮类
 * @author mubreeze
 * @date 2017年11月4日
 */
public class _Button extends JButton {

	/** **/
	private static final long serialVersionUID = 1L;

	public _Button() {
		super();
		this.setMargin(new Insets(4, 4, 4, 4));
		this.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
	}

	public _Button(String text) {
		super(text);
		this.setMargin(new Insets(4, 4, 4, 4));
		this.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
	}

	public _Button(Icon icon) {
		super(icon);
		this.setMargin(new Insets(4, 4, 4, 4));
		this.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
	}

	public _Button(String text, Icon icon) {
		super(text, icon);
		this.setMargin(new Insets(4, 4, 4, 4));
		this.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
	}
}
