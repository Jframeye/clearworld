/**
 * 
 */
package com.clearworld.works.desktop.init.ui.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.clearworld.works.desktop.init.ui.MainFrame;
import com.clearworld.works.desktop.init.ui.component.DesktopHeader;
import com.clearworld.works.desktop.xmlbean.PluginBean;
import com.clearworld.works.desktop.xmlbean.PluginBeans;
import com.xiaoye.clearworld.desktop.common.action.CustomFocusListenerAdapter;
import com.xiaoye.clearworld.desktop.common.base.AbstractPluginInterface;
import com.xiaoye.clearworld.desktop.common.base.Plugin;
import com.xiaoye.clearworld.desktop.common.component._Button;
import com.xiaoye.clearworld.desktop.common.layout.CustomFlowLayout;
import com.xiaoye.clearworld.desktop.common.utils.DialogUtils;
import com.xiaoye.clearworld.utils.FileUtils;
import com.xiaoye.clearworld.utils.JAXBXmlUtils;
import com.xiaoye.clearworld.utils.StringUtils;

/**
 * @desc 工具栏按钮事件
 * @author mubreeze
 * @date 2017年11月4日
 */
public class DesktopHeaderToggleButtonAction implements ActionListener {

	public DesktopHeaderToggleButtonAction() {
	}

	private PluginBean plugin;
	private List<PluginBean> remainingPlugins;
	private DesktopHeader desktopHeader;
	private JDesktopPane desktopPane;
	private MainFrame mainFrame;

	public DesktopHeaderToggleButtonAction(DesktopHeader desktopHeader, PluginBean plugin) {
		this.desktopHeader = desktopHeader;
		this.plugin = plugin;
		this.remainingPlugins = desktopHeader.getRemainingPlugins();
		this.mainFrame = desktopHeader.getMainFrame();
		this.desktopPane = mainFrame.getDesktopPane();
	}

	private JInternalFrame internalFrame;

	private JDialog pluginDialog;

	private JDialog installDialog;

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			String command = event.getActionCommand();
			switch (command) {
			case "more_plugins":
				pluginDialog = DialogUtils.showDialog(mainFrame, "插件列表", pluginManagerContainer(), false);
				pluginDialog.setVisible(true);
				break;
			case "install_plugin_dialog":
				installDialog = DialogUtils.showDialog(mainFrame, "插件安装对话框", pluginInstallContainer());
				installDialog.setVisible(true);
				break;
			case "install_plugin":
				installPlugin();
				break;
			default:
				if (internalFrame == null) {
					internalFrame = new JInternalFrame();
					BasicInternalFrameUI ui = (BasicInternalFrameUI) internalFrame.getUI();
					ui.setNorthPane(null);
					internalFrame.add(initPlugin(plugin)); // 动态加载面板
					desktopPane.add(internalFrame);
				}
				internalFrame.setMaximum(true); // 最大化
				internalFrame.setVisible(true);
				internalFrame.setSelected(true);
				mainFrame.welcome(false);
				if (remainingPlugins.contains(plugin)) {
					pluginDialog.setVisible(false);
				}
				break;
			}
		} catch (Exception e) {
			DialogUtils.showExceptionMsg(mainFrame, e);
		}
	}

	/**
	 * 插件管理器
	 * @return
	 */
	private Component pluginManagerContainer() {
		JPanel container = new JPanel(new BorderLayout());
		container.setSize(600, 350);

		JPanel pluginsPanel = new JPanel(new CustomFlowLayout(FlowLayout.LEFT, 10, 10));
		for (PluginBean plugin : remainingPlugins) {
			JButton button = new JButton(plugin.getPluginName());
			button.addActionListener(new DesktopHeaderToggleButtonAction(desktopHeader, plugin));
			pluginsPanel.add(button);
		}
		// 增加安装插件按钮
		JButton button = new JButton("+ 安装插件");
		button.setActionCommand("install_plugin_dialog");
		button.addActionListener(new DesktopHeaderToggleButtonAction(desktopHeader, null));
		pluginsPanel.add(button);
		container.add(pluginsPanel, BorderLayout.CENTER);
		return new JScrollPane(container);
	}

	/** 新插件信息 **/
	private PluginBean installPlugin = new PluginBean();
	/** 插件包文件 **/
	private File resourcePluginFile;
	private JTextField pluginNameField;
	private JTextField pluginPackageField;
	private JTextField resourceField;
	private JProgressBar progressBar;
	private JButton installButton;
	/**
	 * 插件安装对话框
	 * @return
	 */
	private Component pluginInstallContainer() {
		JPanel container = new JPanel(new GridLayout(5, 1));

		JPanel temp1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
		temp1.add(new JLabel("插件名称："));
		pluginNameField = new JTextField(40);
		temp1.add(pluginNameField);
		container.add(temp1);

		JPanel temp2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
		temp2.add(new JLabel("  执行类："));
		pluginPackageField = new JTextField(40);
		temp2.add(pluginPackageField);
		container.add(temp2);

		JPanel temp3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
		temp3.add(new JLabel("  插件包："));
		resourceField = new JTextField(40);
		resourceField.setEditable(false);
		temp3.add(resourceField);
		container.add(temp3);

		JPanel temp4 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
		progressBar = new JProgressBar() {
			/** **/
			private static final long serialVersionUID = 1L;
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(resourceField.getSize().width, super.getPreferredSize().height);
			}
		};
		progressBar.setStringPainted(true);
		temp4.add(progressBar);
		container.add(temp4);

		JPanel temp5 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
		installButton = new _Button("安装插件");
		installButton.setEnabled(false);
		installButton.setActionCommand("install_plugin");
		installButton.addActionListener(this);
		temp5.add(installButton);

		container.add(temp5);

		// 设置事件监听器
		pluginNameField.addFocusListener(new CustomFocusListenerAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				installPlugin.setPluginName(pluginNameField.getText().trim());
				installButton.setEnabled(validate(pluginNameField, pluginPackageField, resourceField));
			}
		});

		pluginPackageField.addFocusListener(new CustomFocusListenerAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				installPlugin.setPluginPackage(pluginPackageField.getText().trim());
				installButton.setEnabled(validate(pluginNameField, pluginPackageField, resourceField));
			}
		});

		resourceField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(mainFrame);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(new FileNameExtensionFilter("*.jar", "jar"));
				File selectFile = fileChooser.getSelectedFile();
				if (selectFile == null) {
					return;
				}
				resourcePluginFile = selectFile;
				resourceField.setText(selectFile.getAbsolutePath());
				installButton.setEnabled(validate(pluginNameField, pluginPackageField, resourceField));
			}
		});

		return container;
	}

	/**
	 * 校验必填项
	 * @param field1
	 * @param field2
	 * @param field3
	 * @return
	 */
	private boolean validate(JTextField field1, JTextField field2, JTextField field3) {
		return StringUtils.isNotEmpty(field1.getText()) && StringUtils.isNotEmpty(field2.getText()) && StringUtils.isNotEmpty(field3.getText());
	}

	javax.swing.Timer timer = new javax.swing.Timer(18, installAction());
	/**
	 * 安装新插件
	 */
	private void installPlugin() {
		try {
			if (resourcePluginFile == null) {
				DialogUtils.showMessageDialog(desktopPane, "安装包不存在，请选择插件安装包。");
			}
			// 显示安装进度条
			timer.start();

			// 设置状态
			pluginNameField.setEnabled(false);
			pluginPackageField.setEnabled(false);
			resourceField.setEnabled(false);
			installButton.setEnabled(false);

			// 保存文件到resource目录下
			String resource = FileUtils.save(FileUtils.getRelativeFilePath("plugins"), resourcePluginFile);
			installPlugin.setResource(resource);
			
			// 校验
			if (checkJar(installPlugin)) {

				// 修改文件
				List<PluginBean> allPlugins = desktopHeader.getAllPlugins();
				allPlugins.add(installPlugin);
				PluginBeans pluginBeans = new PluginBeans();
				pluginBeans.setPlugins(allPlugins);
				JAXBXmlUtils.convertBeanToXml(DesktopHeader.plugins_xml_path, pluginBeans);
			}
		} catch (Exception e) {
			DialogUtils.showExceptionMsg(mainFrame, e);
			pluginNameField.setEnabled(true);
			pluginPackageField.setEnabled(true);
			resourceField.setEnabled(true);
			installButton.setEnabled(true);
		}
	}
	
	/**
	 * 安装进度条
	 * @return
	 */
	public Action installAction() {
		return new AbstractAction("plugin installing") {
			/** **/
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				if (progressBar.getValue() < progressBar.getMaximum()) {
					progressBar.setValue(progressBar.getValue() + 10);
				} else {
					timer.stop();
					installDialog.setVisible(false); // 关闭对话框
					// TODO 重启软件
					int result = DialogUtils.showConfirmDialog(desktopPane, "是否重启软件？");
					System.out.println("you choice is " + result);
				}
			}
		};
	}

	/**
	 * 实例化插件
	 * @param plugin
	 * @return
	 * @throws Exception
	 */
	private Plugin initPlugin(PluginBean plugin) throws Exception {
		AbstractPluginInterface action = pluginInterface(plugin);
		return action.initPlugin();
	}

	/**
	 * 校验插件信息
	 * 
	 * @param pluginBean
	 * @return
	 */
	private boolean checkJar(PluginBean plugin) {
		try {
			AbstractPluginInterface action = pluginInterface(plugin);
			String pluginPackage = action.getPluginPackage();
			return plugin.getPluginPackage().equals(pluginPackage);
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showExceptionMsg(mainFrame, e);
			timer.stop();
			return false;
		}
	}

	@SuppressWarnings("resource")
	private AbstractPluginInterface pluginInterface(PluginBean plugin) throws Exception {
		URL url = this.getClass().getClassLoader().getResource("plugins/" + plugin.getResource()).toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[] { url }, Thread.currentThread().getContextClassLoader());
		Class<?> clazz = classLoader.loadClass(plugin.getPluginPackage());
		AbstractPluginInterface action = (AbstractPluginInterface) clazz.newInstance();
		return action;
	}
}
