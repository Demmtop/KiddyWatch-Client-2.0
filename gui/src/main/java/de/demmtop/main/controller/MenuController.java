package de.demmtop.main.controller;

import de.demmtop.calendar.view.CalendarView;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MenuController
{
	final private ToggleGroup	bt_group		= new ToggleGroup();
	@FXML
	private ToggleButton		bt_masterData;
	@FXML
	private ToggleButton		bt_financeData;
	@FXML
	private ToggleButton		bt_documentManagement;
	@FXML
	private ToggleButton		bt_systemSettings;
	@FXML
	private ToggleButton		bt_groupData;
	@FXML
	private VBox				vbox_menu;
	@FXML
	private HBox				hbox_menu;
	@FXML
	private StackPane			stackPane;
	@FXML
	private Button				bt_calendar;

	private boolean				masterMenuSwap	= false;
	private Timeline			timeline_masterVBox;
	private Timeline			timeline_mainMenu;
	private Timeline			timeline_lastActiveVBox;
	private Timeline			timeline_nextActiveVBox;
	private VBox				lastActiveVBox;
	private VBox				nextActiveVBox;
	private DoubleProperty		translateXProperty;

	/**
	 * Verschiebt die komplette VBox mit dem Hauptmenü nach rechts. So wird es
	 * dann sichtbar.
	 */
	public void moveMasterMenu()
	{
		if (masterMenuSwap)
		{
			playAnimationForMasterVBox(0);
		}
		else
		{
			playAnimationForMasterVBox(-105);

		}
	}

	public void setTranslateXProperty(DoubleProperty translateXProperty)
	{
		this.translateXProperty = translateXProperty;
	}

	/**
	 * Stoppt die Animation des Hauptmenüs, wenn es gerade ausgeführt wird.
	 */
	public void stopAnimation()
	{
		if (timeline_masterVBox != null && timeline_masterVBox.getStatus() == Animation.Status.RUNNING)
		{
			timeline_masterVBox.stop();
		}

		playAnimationForMasterVBox(-215);
	}

	@FXML
	private void initialize()
	{
		bt_documentManagement.setToggleGroup(bt_group);
		bt_financeData.setToggleGroup(bt_group);
		bt_groupData.setToggleGroup(bt_group);
		bt_masterData.setToggleGroup(bt_group);
		bt_systemSettings.setToggleGroup(bt_group);
	}

	/**
	 * Action Event der einzelnen Menübuttons. Alle Menübuttons verwenden die
	 * gleiche Methode.
	 * 
	 * @param event
	 */
	@FXML
	private void menuAction(ActionEvent event)
	{
		swapMasterMenu();
		moveMasterMenu();

		ToggleButton button = (ToggleButton) event.getSource();
		String buttonId = button.getId();
		String vboxId = "vbox_" + buttonId.substring(3);
		nextActiveVBox = (VBox) stackPane.lookup("#" + vboxId);

		if (button.isSelected())
		{
			playAnimationForNextActiveVBox();
		}
		else
		{
			playAnimationForLastActiveVBox();
		}
	}

	@FXML
	private void openCalendar(ActionEvent event)
	{
		playAnimationForMasterVBox(-215);
		SplitPane centerPane = (SplitPane) stackPane.getParent().getParent().lookup("#sp_pane");
		CalendarView view = new CalendarView();
		view.showDayPage();
		centerPane.getItems().add(view);
	}

	/**
	 * Blendet das Submenü, welche gerade offen ist, aus.
	 * 
	 */
	private void playAnimationForLastActiveVBox()
	{
		if (lastActiveVBox != null && (timeline_lastActiveVBox == null || timeline_lastActiveVBox.getStatus() == Animation.Status.STOPPED))
		{
			timeline_lastActiveVBox = new Timeline();
			timeline_lastActiveVBox.setCycleCount(1);
			final KeyValue kv = new KeyValue(lastActiveVBox.translateXProperty(), -215, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
			timeline_lastActiveVBox.getKeyFrames().add(kf);
			timeline_lastActiveVBox.play();
		}

	}

	/**
	 * Verschiebt das Menü nach links oder rechts. Je nachdem ob ein Submenü
	 * angezeigt wird.
	 * 
	 * @param endValue
	 *            Postion an die das Hauptmenü verschoben wird
	 */
	private void playAnimationForMainMenu(int endValue)
	{
		if (timeline_mainMenu == null || timeline_mainMenu.getStatus() == Animation.Status.STOPPED)
		{
			timeline_mainMenu = new Timeline();
			timeline_mainMenu.setCycleCount(1);
			final KeyValue kv = new KeyValue(vbox_menu.translateXProperty(), endValue, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
			timeline_mainMenu.getKeyFrames().add(kf);
			timeline_mainMenu.play();
		}

	}

	/**
	 * Öffnet oder schließt das Menü.
	 * 
	 * @param endValue
	 *            Position an der das Menü verschoben wird
	 */
	private void playAnimationForMasterVBox(int endValue)
	{
		if (timeline_masterVBox == null || timeline_masterVBox.getStatus() == Animation.Status.STOPPED)
		{
			timeline_masterVBox = new Timeline();
			timeline_masterVBox.setCycleCount(1);
			final KeyValue kv = new KeyValue(translateXProperty, endValue, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
			timeline_masterVBox.getKeyFrames().add(kf);
			timeline_masterVBox.play();
		}

	}

	/**
	 * Blendet das ausgewählte Submenü ein.
	 * 
	 */
	private void playAnimationForNextActiveVBox()
	{
		if (lastActiveVBox != null)
		{
			playAnimationForLastActiveVBox();
		}

		if (nextActiveVBox != null && (timeline_nextActiveVBox == null || timeline_nextActiveVBox.getStatus() == Animation.Status.STOPPED))
		{
			lastActiveVBox = nextActiveVBox;
			timeline_nextActiveVBox = new Timeline();
			timeline_nextActiveVBox.setCycleCount(1);
			final KeyValue kv = new KeyValue(nextActiveVBox.translateXProperty(), 0, Interpolator.EASE_BOTH);
			final KeyFrame kf = new KeyFrame(Duration.millis(250), kv);
			timeline_nextActiveVBox.getKeyFrames().add(kf);
			timeline_nextActiveVBox.play();
		}

	}

	/**
	 * Verschiebt das Hauptmenü nach links, wenn ein Submenübutton ausgewählt
	 * wurde. Das Menü ist in diesem Moment nicht sichtbar. Die nächste
	 * Animation verschiebt dann die komplette VBox nach rechts.
	 */
	private void swapMasterMenu()
	{
		if (bt_group.getSelectedToggle() != null && !masterMenuSwap)
		{
			playAnimationForMainMenu(-105);
			masterMenuSwap = !masterMenuSwap;
		}
		else if (bt_group.getSelectedToggle() == null)
		{
			playAnimationForMainMenu(0);
			masterMenuSwap = !masterMenuSwap;
		}
	}

	@FXML
	void actionRegister(ActionEvent event)
	{
		System.out.println("Click");
	}
}
