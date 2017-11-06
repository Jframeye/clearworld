/**
 * 
 */
package com.clearworld.works.desktop.init.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.clearworld.works.desktop.init.ui.component.DesktopHeader;
import com.clearworld.works.desktop.init.ui.component.WelcomeClearworld;
import com.xiaoye.clearworld.desktop.common.utils.ImageUtils;

/**
 * @desc 主窗口
 * @author mubreeze
 * @date 2017年11月4日
 */
public class MainFrame extends JFrame {

	/** **/
	private static final long serialVersionUID = 1L;

	public MainFrame() {
		this.setTitle("我的世界");
		this.setIconImage(ImageUtils.getImage("images/clearworld.png"));
		this.setMinimumSize(new Dimension(800, 600));

		// 加载内容
		this.initContainer();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.desktopInCenter(); // 窗体居中显示
		this.setVisible(true);
	}

	private CardLayout cardLayout = new CardLayout(0, 0);
	private JPanel container = null;
	JDesktopPane desktopPane = null;

	/**
	 * 内容面板
	 */
	private void initContainer() {
		this.setLayout(new BorderLayout());

		// 工具栏面板
		DesktopHeader header = new DesktopHeader(this);
		this.add(header, BorderLayout.NORTH);

		// 主内容面板
		container = new JPanel(cardLayout);
		container.add(new WelcomeClearworld());

		desktopPane = new JDesktopPane();
		container.add(desktopPane);

		this.add(container, BorderLayout.CENTER);
	}

	/**
	 * @return the desktopPane
	 */
	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	private boolean isWelcome = true;

	/**
	 * 页面切换
	 * @param towelcome
	 */
	public void welcome(boolean towelcome) {
		if (towelcome) {
			if (!isWelcome) {
				isWelcome = true;
				cardLayout.next(container);
			}
		} else {
			if (isWelcome) {
				isWelcome = false;
				cardLayout.next(container);
			}
		}
	}

	/**
	 * @return the cardLayout
	 */
	public CardLayout getCardLayout() {
		return cardLayout;
	}

	/**
	 * the cardLayout to set
	 * @param cardLayout
	 */
	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	/**
	 * 窗口居中
	 */
	private void desktopInCenter() {
		// 窗体居中
		Rectangle screen = this.getGraphicsConfiguration().getBounds(); // 屏幕相关尺寸
		int x = screen.x; // x 坐标
		int screen_width = screen.width; // 屏幕宽度
		int desktop_width = this.getSize().width; // 窗口的宽度
		if (screen_width > desktop_width) {
			x = x + (screen_width - desktop_width) / 2;
		}
		int y = screen.y;
		int screen_height = screen.height;
		int desktop_height = this.getSize().height;
		if (screen_height > desktop_height) {
			y = y + (screen_height - desktop_height) / 2;
		}
		this.setLocation(x, y);
	}
}
