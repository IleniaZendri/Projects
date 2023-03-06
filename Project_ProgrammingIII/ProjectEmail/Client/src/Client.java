import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage loginStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
        loginStage.setTitle("Client email - Login");
        loginStage.setScene(new Scene(root, 330, 130));
        loginStage.setMinHeight(170);
        loginStage.setMinWidth(330);
        loginStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
