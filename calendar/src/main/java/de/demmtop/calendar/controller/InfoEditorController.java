package de.demmtop.calendar.controller;

import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;

public class InfoEditorController
{
	@FXML
	private HTMLEditor editor;

	/**
	 * Setzt den aktuellen Html Content in den Editor
	 * 
	 * @param content
	 *            der Content der im Editor angezeigt werden soll
	 */
	public void setContent(String content)
	{
		editor.setHtmlText(content);
	}
}
