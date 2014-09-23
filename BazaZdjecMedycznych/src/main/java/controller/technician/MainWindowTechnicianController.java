package controller.technician;

import controller.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Common;
import model.ResourceBundleMaster;
import model.StageMaster;
import model.db.DBConnector;

/**
 * FXML Controller class
 *
 * @author peer
 */
public class MainWindowTechnicianController extends Window {

    @FXML
    private Button manageUsersButton;
    @FXML
    private BorderPane borderPaneMainWindowTechnician;
    @FXML
    private Label loggedAsLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Button logoutButton;

    /**
     * Initializes the controller class.
     */
    public MainWindowTechnicianController() {
        super();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        initButtons();
        loggedAsLabel.setText(loggedAsLabel.getText() + " " + Common.COMMON.getLoggedUser());

    }

    private void initButtons() {
        manageUsersButton.setOnAction((event) -> {
            showManageUserWindow();
        });

        logoutButton.setOnAction((event) -> {
            logoutAndClose();
        });
    }

    private void showManageUserWindow() {
        Common.COMMON.setManageUsersStartController("MainWindowTechnicianController");
        Parent manageUsersRoot = StageMaster.master.getRoot("admin/ManageUsers.fxml");
        if (manageUsersRoot != null) {
            this.borderPaneMainWindowTechnician.setCenter(manageUsersRoot);
        }
    }

    private void showAddPictureController() {
        Parent manageUsersRoot = StageMaster.master.getRoot("technician/AddPicture.fxml");
        if (manageUsersRoot != null) {
            this.borderPaneMainWindowTechnician.setCenter(manageUsersRoot);
        }
    }

    public BorderPane getBorderPane() {
        return borderPaneMainWindowTechnician;
    }

    protected void closeWindow() {
        Stage stage = (Stage) this.manageUsersButton.getScene().getWindow();
        stage.close();//tu jest nadal problem bo nie wiem skat tego stage wziac
    }

    public void setWindowTitle(String title) {
        Stage stage = (Stage) this.manageUsersButton.getScene().getWindow();
        stage.setTitle(title);
    }

    public void logoutAndClose() {
        try {
            DBConnector.master.logout();
            showWindow("LoginWindow.fxml");
            Stage stage = (Stage) this.loggedAsLabel.getScene().getWindow();
            Common.COMMON.setManageUsersStartController("");
            stage.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowTechnicianController.class.getName()).log(Level.SEVERE, null, ex);
            infoLabel.setText(ResourceBundleMaster.TRANSLATOR.getTranslation("connectionError"));
        }
    }
}
