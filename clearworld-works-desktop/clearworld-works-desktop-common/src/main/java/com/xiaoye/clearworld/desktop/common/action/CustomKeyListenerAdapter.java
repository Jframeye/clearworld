/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.action;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @desc 键盘事件适配器
 * @author mubreeze
 * @date 2017年11月5日
 */
public abstract class CustomKeyListenerAdapter implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyReleased(e);
	}
}
