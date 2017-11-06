/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.action;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * @desc 焦点事件适配器
 * @author mubreeze
 * @date 2017年11月5日
 */
public abstract class CustomFocusListenerAdapter implements FocusListener {

	@Override
	public void focusGained(FocusEvent e) { }

	@Override
	public void focusLost(FocusEvent e) { }
}
