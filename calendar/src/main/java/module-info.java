/**
 * 
 */
/**
 * @author c.skinner
 *
 */
module kiddywatch.client.calendar
{
	exports de.demmtop.calendar.view;
	opens de.demmtop.calendar.controller;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.web;
	requires de.demmtop.calendarfx.view;
	requires org.apache.commons.lang3;
	requires java.naming;
	requires kiddywatch.server.jpa;
	requires kiddywatch.server.calendar.ejb.client;
	requires slf4j.api;
	requires com.jfoenix;
	requires kiddywatch.client.common.gui;
	requires kiddywatch.client.transfer;
}