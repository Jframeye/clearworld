/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/beautyeye
 * Version 3.6
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ProgressBarDemo.java at 2015-2-1 20:25:39, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */

/*
 * @(#)ProgressBarDemo.java	1.12 05/11/17
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * JProgressBar Demo.
 *
 * @version 1.12 11/17/05
 * @author Jeff Dinkins # @author Peter Korn (accessibility support)
 */
public class ProgressBarDemo extends DemoModule {

	public static void main(String[] args) {
		ProgressBarDemo demo = new ProgressBarDemo(null);
		demo.mainImpl();
	}

	public ProgressBarDemo(SwingSet2 swingset) {
		super(swingset);
		createProgressPanel();
	}

	/** The timer. */
	javax.swing.Timer timer = new javax.swing.Timer(18, createTextLoadAction());

	/** The load action. */
	Action loadAction;

	/** The stop action. */
	Action stopAction;

	/** The progress bar. */
	JProgressBar progressBar;

	/** The progress text area. */
	JTextArea progressTextArea;

	/*
	 * (non-Javadoc)
	 * 
	 * @see DemoModule#updateDragEnabled(boolean)
	 */
	void updateDragEnabled(boolean dragEnabled) {
		progressTextArea.setDragEnabled(dragEnabled);
	}

	/**
	 * Creates the progress panel.
	 */
	public void createProgressPanel() {
		getDemoPanel().setLayout(new BorderLayout());

		JPanel textWrapper = new JPanel(new BorderLayout());
		progressTextArea = new MyTextArea();

		textWrapper.add(new JScrollPane(progressTextArea), BorderLayout.CENTER);

		getDemoPanel().add(textWrapper, BorderLayout.CENTER);

		JPanel progressPanel = new JPanel();
		getDemoPanel().add(progressPanel, BorderLayout.SOUTH);

		progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, text.length()) {
			public Dimension getPreferredSize() {
				return new Dimension(270, super.getPreferredSize().height);
			}
		};

		JPanel p1 = new JPanel();
		p1.add(progressBar);
		p1.add(createLoadButton());

		progressPanel.add(p1);
	}

	/**
	 * Creates the load button.
	 *
	 * @return the j button
	 */
	public JButton createLoadButton() {
		loadAction = new AbstractAction(getString("ProgressBarDemo.start_button")) {
			public void actionPerformed(ActionEvent e) {
				loadAction.setEnabled(false);
				if (progressBar.getValue() == progressBar.getMaximum()) {
					progressBar.setValue(0);
				}
				timer.start();
			}
		};
		return createButton(loadAction);
	}

	public JButton createButton(Action a) {
		JButton b = new JButton();
		// setting the following client property informs the button to show
		// the action text as it's name. The default is to not show the
		// action text.
		b.putClientProperty("displayActionText", Boolean.TRUE);
		b.setAction(a);
		return b;
	}

	/** The text location. */
	int textLocation = 0;

	/** The text. */
	String text = getString("ProgressBarDemo.text");

	/**
	 * Creates the text load action.
	 *
	 * @return the action
	 */
	public Action createTextLoadAction() {
		return new AbstractAction("text load action") {
			public void actionPerformed(ActionEvent e) {
				if (progressBar.getValue() < progressBar.getMaximum()) {
					progressBar.setValue(progressBar.getValue() + 1);
				} else {
					timer.stop();
					loadAction.setEnabled(true);
				}
			}
		};
	}

	/**
	 * The Class MyTextArea.
	 */
	class MyTextArea extends JTextArea {

		/**
		 * Instantiates a new my text area.
		 */
		public MyTextArea() {
			super(null, 0, 0);
			// setEditable(false);
			setText("");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#getAlignmentX()
		 */
		public float getAlignmentX() {
			return LEFT_ALIGNMENT;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#getAlignmentY()
		 */
		public float getAlignmentY() {
			return TOP_ALIGNMENT;
		}
	}
}
