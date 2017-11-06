/**
 * 
 */
package com.xiaoye.clearworld.desktop.common.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.xiaoye.clearworld.desktop.common.component._Button;

/**
 * @desc 对话框工具类
 * @author mubreeze
 * @date 2017年11月4日
 */
public class DialogUtils {

	/**
	 * 提示框
	 * @param component
	 * @param message
	 */
	public static void showMessageDialog(Component component, String message) {
		JOptionPane.showMessageDialog(component, message);
	}

	/**
	 * 确认框
	 * @param component
	 * @param message
	 * @return
	 */
	public static int showConfirmDialog(Component component, String message) {
		return JOptionPane.showConfirmDialog(component, message, null, JOptionPane.YES_NO_OPTION);
	}

	/**
	 * 异常信息弹框
	 * 
	 * @param frame
	 * @param exception
	 */
	public static void showExceptionMsg(Frame frame, Exception exception) {
		JDialog dialog = new JDialog(frame);
		dialog.setModal(true);
		dialog.setTitle("好像出了问题了");
		dialog.setIconImage(ImageUtils.getImage("images/error.png"));
		dialog.setSize(600, 400);

		Container container = dialog.getContentPane();
		container.setLayout(new BorderLayout());

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		exception.printStackTrace(printWriter);
		printWriter.close();
		JTextArea textArea = new JTextArea(stringWriter.toString());
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		container.add(scrollPane, BorderLayout.CENTER);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		JButton confirmButton = new _Button("确定");
		confirmButton.setPreferredSize(new Dimension(100, 27));
		panel.add(confirmButton);
		container.add(panel, BorderLayout.SOUTH);

		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		setLocation(dialog, frame);
		dialog.setVisible(true);
	}

	/**
	 * 弹出对话框
	 * @param frame
	 * @param title 对话框标题
	 * @param container 对话框内容容器
	 * @return
	 */
	public static JDialog showDialog(Frame frame, String title, Component container) {
		JDialog dialog = new JDialog(frame);
		dialog.setTitle(title);
		dialog.setModal(true);
		dialog.getContentPane().add(container);
		dialog.pack();
		setLocation(dialog, frame);
		return dialog;
	}

	/**
	 * 弹出对话框
	 * @param frame
	 * @param title 对话框标题
	 * @param container 对话框内容容器
	 * @param pack 是否自动布局
	 * @return
	 */
	public static JDialog showDialog(Frame frame, String title, Component container, boolean pack) {
		JDialog dialog = new JDialog(frame);
		dialog.setTitle(title);
		dialog.setModal(true);
		dialog.getContentPane().add(container);
		dialog.setSize(container.getSize());
		setLocation(dialog, frame);
		return dialog;
	}

	public static void setLocation(JDialog dialog, Frame frame) {
		int x = frame.getLocation().x;
		int frame_width = frame.getSize().width;
		int dialog_width = dialog.getSize().width;
		x = x + (frame_width - dialog_width) / 2;

		int y = frame.getLocation().y;
		int frame_height = frame.getSize().height;
		int dialog_height = dialog.getSize().height + 50;
		y = y + (frame_height - dialog_height) / 2;
		dialog.setLocation(x, y);
	}
}
