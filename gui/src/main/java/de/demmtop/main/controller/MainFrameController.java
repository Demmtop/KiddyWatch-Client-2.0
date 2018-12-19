package de.demmtop.main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainFrameController
{
	@FXML
	private HBox			hBox;
	@FXML
	private StackPane		stackPane;
	@FXML
	private MenuController	menuController;
	@FXML
	private BorderPane		centerPane;
	@FXML
	Rectangle				rect_tab;
	@FXML
	Text					tab_font;
	@FXML
	TextArea				textArea;

	@FXML
	private void closeMenu(MouseEvent event)
	{
		menuController.stopAnimation();
	}

	@FXML
	private void initialize()
	{
		menuController.setTranslateXProperty(hBox.translateXProperty());
	}

	@FXML
	private void openMenu(MouseEvent event)
	{
		menuController.moveMasterMenu();
	}
}
