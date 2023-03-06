import controller.ServerController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Server extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("serverView.fxml"));
        Parent root = (Parent) loader.load();
        ServerController serverController = loader.getController();
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.setTitle("Server");
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Devo chiudere1");
                serverController.serverStop();
                System.out.println("Devo chiudere2");
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
