/**
 * @author c.skinner
 *
 */
module kiddywatch.client
{
	exports de.demmtop;

	opens de.demmtop.main.controller;

	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.web;
	requires kiddywatch.client.calendar;
	requires java.persistence;
	requires de.jensd.fx.glyphs.commons;
	requires controlsfx;
	requires de.demmtop.calendarfx.recurrence;
	requires de.demmtop.calendarfx.view;
}