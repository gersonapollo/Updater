package br.com.chaos.updater.view;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.chaos.updater.business.Updater;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdaterScreen extends Application {
	
	private final static Logger LOG = LoggerFactory.getLogger(UpdaterScreen.class);

	private Task copyWorker;
	private static DoubleProperty progress = new SimpleDoubleProperty(0.0);
	private static StringProperty taskName = new SimpleStringProperty("Looking for Updates");
	private static BooleanProperty hasToClose = new SimpleBooleanProperty(false);
	private final ProgressBar progressBar = new ProgressBar(0);
	//Text version = new Text("Updater Version: 01.00.00");
	private final Instant initTime = Instant.now();
	private static String[] args;

    @Override
    public void start(Stage stage) {
    	Group root = new Group();
    	Scene scene = new Scene(root);
    	stage.setScene(scene);
    	stage.setTitle("Client Updater");
    	stage.setResizable(false);
    	stage.initStyle(StageStyle.UNDECORATED);
    	scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);

    	progressBar.setPrefWidth(200.0);

    	Text taskInfo = new Text();
    	ImageView ico = new ImageView(new Image(UpdaterScreen.class.getResourceAsStream("/images/logo.png")));

    	taskInfo.setText(taskName.getValueSafe());


    	GridPane grid = new GridPane();
    	grid.setHgap(2);
    	grid.setVgap(2);

    	GridPane gridProgress = new GridPane();
    	gridProgress.setHgap(3);
    	gridProgress.setVgap(1);
    	gridProgress.add(taskInfo, 0, 0);
    	gridProgress.add(progressBar, 0, 1);
    	
//    	GridPane gridVersion = new GridPane();
//    	gridVersion.setHgap(0);
//    	gridVersion.setVgap(0);
//    	gridVersion.add(version, 0, 0);
//    	gridVersion.setStyle("-fx-font-size: 10px;");
    	
    	grid.add(ico, 0, 0);
    	grid.add(gridProgress, 1, 0);
    	//grid.add(gridVersion, 1, 3);

    	grid.setStyle("-fx-background-color: rgb(178,34,34); -fx-background-radius: 10px; paddin-right: 5px");
    	grid.setHgap(15);
    	grid.setPrefSize(400, 100);
    	grid.setAlignment(Pos.CENTER);

    	scene.setRoot(grid);
    	stage.show();

    	findUpdates(progressBar, taskInfo);
    }
    public static void main(String[] args) {
    	LOG.info("**##** Starting Updater Application **##**");
    	LOG.info("Updater Version: "+ Version.UPDATER_VERSION.getNumber());
    	List<String> arguments = Arrays.asList(args);
    	LOG.info("total of received arguments: " + arguments.size());
    	arguments.forEach(argument -> LOG.info("received argument: " + argument));
    	UpdaterScreen.args = args;
    	launch(args);
    }

    public static void setProgress(Number value) {
    	progress.setValue(value);
    }

    public static void setTaskName(String value) {
    	value = value.isEmpty()? "Looking for Updates" : value;
    	taskName.setValue(value);
    }

    public Task createWorker() {
    	return new Task() {
    		@Override
    		protected Object call() throws Exception {
    			progress.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
    				updateProgress(progress.getValue(), 1);
    			});
    			Thread.sleep(2000);
    			Updater updater = new Updater();
            	updater.update(args);


//    			for (int i = 0; i <= 10; i++) {
//    				Thread.sleep(2000);
//    				//updateMessage("2000 milliseconds");
//    				//updateProgress(i + 1, 10);
//    				setTaskName("Looking for Updates "+ i);
//    				setProgress(new Double(i*0.1));
//    			}
    			return true;
    		}
    	};
    }

    private void findUpdates(ProgressBar progressBar, Text taskInfo) {
    	copyWorker = createWorker();

		progressBar.setProgress(0);
		progressBar.progressProperty().unbind();
//		progressBar.progressProperty().bind(progress);
		progressBar.progressProperty().bind(copyWorker.progressProperty());

//		progress.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//			taskInfo.setText(taskName.getValueSafe());
//			System.out.println("Setting Progress to: "+newValue);
////			progressBar.setProgress(newValue.doubleValue());
//		});

		taskName.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			taskInfo.setText(taskName.getValueSafe());
			LOG.info("Setting task name to: "+newValue);
		});

		hasToClose.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
			if(Boolean.TRUE.equals(newValue)) {
				taskInfo.setText("Closing...");
				LOG.info("**##** Closing Updater **##**");
				LOG.info("Total execution time: " + Duration.between(initTime, Instant.now()));
				close();
			}

		});

		Thread copy = new Thread(copyWorker);
		copy.start();
    }

    private void close(){
    	try {
    		 Platform.exit();
		} catch (Exception e) {
			LOG.error("Error while closing Updater", e);
		}
    }

    public static void closeApplication() {
    	hasToClose.setValue(Boolean.TRUE);
    }
}