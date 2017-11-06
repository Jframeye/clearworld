/**
 * 
 */
package com.clearworld.works.desktop.init;

import java.awt.EventQueue;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.clearworld.works.desktop.init.ui.MainFrame;

/**
 * @desc 主窗体程序
 * @author mubreeze
 * @date 2017年11月4日
 */
public class ClearworldDesktop {

	public static void main(String[] args) throws Exception {
		// 设置皮肤
		BeautyEyeLNFHelper.launchBeautyEyeLNF();
		UIManager.put("RootPane.setupButtonVisible", false);
		EventQueue.invokeLater(() -> {
			new MainFrame();
		});
	}
}
