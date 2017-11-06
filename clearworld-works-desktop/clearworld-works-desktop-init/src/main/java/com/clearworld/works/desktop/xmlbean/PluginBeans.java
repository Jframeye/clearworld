/**
 * 
 */
package com.clearworld.works.desktop.xmlbean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @desc 插件
 * @author mubreeze
 * @date 2017年11月4日
 */
@XmlRootElement(name = "plugins")
@XmlAccessorType(XmlAccessType.FIELD)
public class PluginBeans {

	@XmlElement(name = "plugin")
	private List<PluginBean> plugins;

	/**
	 * @return the plugins
	 */
	public List<PluginBean> getPlugins() {
		return plugins;
	}

	/**
	 * @param plugins
	 *            the plugins to set
	 */
	public void setPlugins(List<PluginBean> plugins) {
		this.plugins = plugins;
	}
}
