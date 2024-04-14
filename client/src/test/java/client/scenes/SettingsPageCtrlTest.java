package client.scenes;

import client.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SettingsPageCtrlTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        Parent mainNode = FXMLLoader.load(Main.class.getResource("/client/scenes/SettingsPage.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }


    @Test
    void constructor() {
        SettingsPageCtrl ctrl  = new SettingsPageCtrl(null,null,null);
        assertNotNull(ctrl);
    }

    @Test
    public void testLoginWithBlankPassword() throws IOException {
    /*    Stage stage = new Stage();
        start(stage);
        PasswordField passwordField = lookup("#adminPassword").query();
        passwordField.setText("");
        clickOn("#login");
        DialogPane dialogPane = lookup(".dialog-pane").query();
        assertEquals("Password needed to enter the admin overview", dialogPane.getHeaderText()); */   }
}