package com.getabby.ap.util;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This enum represents the level of messages sent back to the client of the
 * Abby Rest API
 * 
 * @author pankaj
 *
 */
public enum TransmittalMessageLevel {
	I("Information", "Informational message"), W("Warning", "Warning message"), E("Error", "Error message"), A("Alert", "Alert message");

	private String description;
	private String name;

	private TransmittalMessageLevel(String name, String description) {
		this.description = description;
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).toString();
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

}
