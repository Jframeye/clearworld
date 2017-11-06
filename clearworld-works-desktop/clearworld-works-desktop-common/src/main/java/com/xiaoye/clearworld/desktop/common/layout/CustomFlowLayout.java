package com.xiaoye.clearworld.desktop.common.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/**
 * @desc 自定义流式布局管理器，使其能够自动换行
 * @author mubreeze
 * @date 2017年11月4日
 */
public class CustomFlowLayout extends FlowLayout {
  
	/** **/
	private static final long serialVersionUID = 1L;

	public CustomFlowLayout() {
        super();  
    }  
  
    public CustomFlowLayout(int align) {  
        super(align);  
    }  
  
    public CustomFlowLayout(int align, int hgap, int vgap) {  
        super(align, hgap, vgap);  
    }  
  
    public Dimension minimumLayoutSize(Container target) {  
        return computeSize(target, false);  
    }  
  
    public Dimension preferredLayoutSize(Container target) {  
        return computeSize(target, true);  
    }  
  
    private Dimension computeSize(Container target, boolean minimum) {  
        synchronized (target.getTreeLock()) {  
            int hgap = getHgap();  
            int vgap = getVgap();  
            int w = target.getWidth();  
  
            if (w == 0) {  
                w = Integer.MAX_VALUE;  
            }  
  
            Insets insets = target.getInsets();  
            if (insets == null) {  
                insets = new Insets(0, 0, 0, 0);  
            }  
            int reqdWidth = 0;  
  
            int maxwidth = w - (insets.left + insets.right + hgap * 2);  
            int n = target.getComponentCount();  
            int x = 0;  
            int y = insets.top;  
            int rowHeight = 0;  
  
            for (int i = 0; i < n; i++) {  
                Component c = target.getComponent(i);  
                if (c.isVisible()) {  
                    Dimension d =  
                            minimum ? c.getMinimumSize() : c.getPreferredSize();  
                    if ((x == 0) || ((x + d.width) <= maxwidth)) {  
                        if (x > 0) {  
                            x += hgap;  
                        }  
                        x += d.width;  
                        rowHeight = Math.max(rowHeight, d.height);  
                    } else {  
                        x = d.width;  
                        y += vgap + rowHeight;  
                        rowHeight = d.height;  
                    }  
                    reqdWidth = Math.max(reqdWidth, x);  
                }  
            }  
            y += rowHeight;  
            return new Dimension(reqdWidth + insets.left + insets.right, y);  
        }  
    }  
} 