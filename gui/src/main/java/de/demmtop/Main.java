package de.demmtop;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application
{
	public final static String VERSION = "0.0.1-SNAPSHOT";

	public static void main(String[] args)
	{
		launch(args);

	}

	@Override
	public void init()
	{
	}

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			showSplashScreen(primaryStage);

			PauseTransition pause = new PauseTransition(Duration.seconds(5));
			pause.setOnFinished(event -> {
				Stage mainStage = new Stage();

				try
				{
					StackPane root = (StackPane) FXMLLoader.load(getClass().getResource("fxml/MainFrame.fxml"));
					Scene scene = new Scene(root, 0, 0);
					mainStage.setMaximized(true);
					mainStage.setScene(scene);
					mainStage.setTitle("KiddyWatch " + VERSION);
					mainStage.show();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				primaryStage.hide();
			});
			pause.play();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Jenkins schreibt seine eigene Buildnummer in die Date "build" unter
	 * Resources. Diese Datei wird dann in Anwendung mit eingebunden.
	 * 
	 * @return die Build-Nummer die Jenkins geschrieben hat
	 */
	private int readBuildNumber()
	{
		int build = 0;
		try
		{
			String className = getClass().getSimpleName() + ".class";
			String classPath = getClass().getResource(className).toString();

			URL url = new URL(classPath);
			JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
			Manifest manifest = jarConnection.getManifest();
			Attributes attributes = manifest.getMainAttributes();
			build = Integer.parseInt(attributes.getValue("build"));
		} catch (Exception ex)
		{
			System.out.println(ex.getLocalizedMessage());
		}

		return build;
	}

	/**
	 * Jenkins schreibt die Maven Buildnummer in die Date "version" unter
	 * Resources. Diese Datei wird dann in Anwendung mit eingebunden.
	 * 
	 * @return die Version-Nummer die Jenkins geschrieben hat
	 */
	private String readVersionNumber()
	{
		String version = "1.0";
		try
		{
			String className = getClass().getSimpleName() + ".class";
			String classPath = getClass().getResource(className).toString();

			URL url = new URL(classPath);
			JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
			Manifest manifest = jarConnection.getManifest();
			Attributes attributes = manifest.getMainAttributes();
			version = attributes.getValue("version");
		} catch (Exception ex)
		{
		}

		return version;
	}

	/**
	 * Setzt den Border des SplashScreens.
	 * 
	 * @param pane
	 *            Das Pane auf dem der Border gesetzt wird
	 */
	private void setBorder(BorderPane pane)
	{
		final float hue = readBuildNumber() % 360;
		final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
		Color color = Color.hsb(hue, saturation, luminance);

		pane.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(2.0))));
	}

	/**
	 * Setzt die Jenkins-Build-Nummer in die GUI.
	 * 
	 * @param text
	 *            {@link Text} Feld in der die Build-Nummer geschrieben wird
	 */
	private void setBuild(Text text)
	{
		text.setText(String.valueOf(readBuildNumber()));
	}

	/**
	 * Setzt die Maven-Version-Nummer in die GUI.
	 * 
	 * @param t
	 *            Schrift mit der die Nummer dargstellt werden soll
	 * @param text
	 *            {@link Text} Feld in die Version-Nummer geschrieben wird
	 */
	private void setVersion(Font t, Text text)
	{
		final float hue = readBuildNumber() % 360;
		final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
		Color color = Color.hsb(hue, saturation, luminance);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

		text.setEffect(ds);
		text.setText(readVersionNumber());
		text.setFill(color);
		text.setFont(t);
	}

	private void showSplashScreen(Stage primaryStage) throws Exception
	{
		Font t = Font.loadFont(getClass().getResource("font/NEORD.ttf").toExternalForm(), 80);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SplashScreen.fxml"));
		BorderPane root = (BorderPane) loader.load();
		// setVersion(t, (Text) loader.getNamespace().get("version"));
		// setBuild((Text) loader.getNamespace().get("build"));
		setBorder((BorderPane) loader.getNamespace().get("mainPane"));

		Scene scene = new Scene(root, 300, 200);
		scene.getStylesheets().add(getClass().getResource("css/splashScreen.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
	}
}
