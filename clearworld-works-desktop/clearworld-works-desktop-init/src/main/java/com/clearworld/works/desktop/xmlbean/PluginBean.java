/**
 * 
 */
package com.clearworld.works.desktop.xmlbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @desc 插件详情
 * @author mubreeze
 * @date 2017年11月4日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PluginBean {

	@XmlAttribute(name = "pluginName", required = true)
	private String pluginName;

	@XmlAttribute(name = "pluginPackage", required = true)
	private String pluginPackage;

	@XmlAttribute(name = "resource", required = true)
	private String resource;

	/**
	 * @return the pluginName
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * @param pluginName
	 *            the pluginName to set
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	/**
	 * @return the pluginPackage
	 */
	public String getPluginPackage() {
		return pluginPackage;
	}

	/**
	 * @param pluginPackage
	 *            the pluginPackage to set
	 */
	public void setPluginPackage(String pluginPackage) {
		this.pluginPackage = pluginPackage;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
}
