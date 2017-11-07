/**
 * 
 */
package com.clearworld.works.desktop.init.ui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.clearworld.works.desktop.init.ui.MainFrame;
import com.clearworld.works.desktop.init.ui.action.DesktopHeaderToggleButtonAction;
import com.clearworld.works.desktop.xmlbean.PluginBean;
import com.clearworld.works.desktop.xmlbean.PluginBeans;
import com.xiaoye.clearworld.desktop.common.utils.DialogUtils;
import com.xiaoye.clearworld.utils.XmlParserWithJAXBUtils;

/**
 * @desc 工具栏
 * @author mubreeze
 * @date 2017年11月4日
 */
public class DesktopHeader extends JToolBar implements ActionListener {

	/** **/
	private static final long serialVersionUID = 1L;

	public static final String plugins_xml_path = "clearworld.plugins.xml";

	private static final String index_command = "index_command";

	/** 主窗口 **/
	private MainFrame mainFrame;

	/** 工具栏按钮组 **/
	private ButtonGroup buttonGroup = new ButtonGroup();

	public DesktopHeader(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		this.setFloatable(true); // 工具栏可浮动

		this.initContainer();
	}

	/**
	 * 初始化面板
	 */
	private void initContainer() {
		JToggleButton indexButton = new JToggleButton("首页");
		buttonGroup.add(indexButton);
		indexButton.setActionCommand(index_command);
		indexButton.addActionListener(this);
		this.add(indexButton);

		loadingPlugins();
	}

	/**
	 * 加载插件
	 */
	private void loadingPlugins() {
		try {
			PluginBeans plugin = XmlParserWithJAXBUtils.convertXmlToBean(plugins_xml_path, PluginBeans.class);
			// 异步加载插件
			SwingUtilities.invokeLater(() -> {
				loadingPlugins(plugin.getPlugins());
			});
		} catch (Exception e) {
			DialogUtils.showExceptionMsg(mainFrame, e);
		}
	}

	/** 所有的插件 **/
	private List<PluginBean> allPlugins = new ArrayList<PluginBean>();
	/** 剩余的插件 **/
	private List<PluginBean> remainingPlugins = new ArrayList<PluginBean>();
	/**
	 * 初始化常用插件
	 * @param plugins
	 */
	private void loadingPlugins(List<PluginBean> plugins) {
		int count = 0;
		for (PluginBean plugin : plugins) {
			allPlugins.add(plugin);
			if (count > 7) {
				remainingPlugins.add(plugin);
			} else {
				JToggleButton button = new JToggleButton(plugin.getPluginName());
				button.addActionListener(new DesktopHeaderToggleButtonAction(DesktopHeader.this, plugin));
				buttonGroup.add(button);
				this.add(button);
			}
			count++;
		}
		JToggleButton button = new JToggleButton();
		button.addActionListener(new DesktopHeaderToggleButtonAction(DesktopHeader.this, null));
		if (plugins.size() > 8) {
			button.setText(">>");
			button.setActionCommand("more_plugins");
		} else {
			button.setText("+");
			button.setActionCommand("install_plugin_dialog");
			button.setToolTipText("安装插件");
		}
		buttonGroup.add(button);
		this.add(button);
	}

	/**
	 * @return the allPlugins
	 */
	public List<PluginBean> getAllPlugins() {
		return allPlugins;
	}

	/**
	 * @return the remainingPlugins
	 */
	public List<PluginBean> getRemainingPlugins() {
		return remainingPlugins;
	}

	/**
	 * @return the mainFrame
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		switch (command) {
		case index_command:
			mainFrame.welcome(true);
			break;

		default:
			break;
		}
	}
}
