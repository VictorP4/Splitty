package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddTagCtrl implements Main.UpdatableUI{
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private Event event;
    private String tagName;
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField redTextField;

    @FXML
    private TextField greenTextField;

    @FXML
    private TextField blueTextField;


    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    public void initialize() {
        tagName = nameTextField.getText();
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> updateTagName(newValue));
    }

    private void updateTagName(String newValue) {
        tagName = newValue;
        colorFiller();
    }

    private void colorFiller() {
        for (Tag tag : event.getTags()) {
            if (tag.getName().equals(tagName)) {
                redTextField.setText(String.valueOf(tag.getRed()));
                greenTextField.setText(String.valueOf(tag.getGreen()));
                blueTextField.setText(String.valueOf(tag.getBlue()));
                break;
            }
        }
    }

    public void add() {
        String name = nameTextField.getText();
        int red = Integer.parseInt(redTextField.getText());
        int green = Integer.parseInt(greenTextField.getText());
        int blue = Integer.parseInt(blueTextField.getText());

        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Color values must be between 0 and 255.");
            alert.showAndWait();
            return;
        }

        Tag newTag = new Tag(name, red, green, blue);
        if (event.getTags().stream()
                .map(Tag::getName)
                .toList().contains(newTag.getName())) {
            for (Tag tag : event.getTags()) {
                if (tag.getName().equals(newTag.getName())) {
                    tag.setRed(newTag.getRed());
                    tag.setGreen(newTag.getGreen());
                    tag.setBlue(newTag.getBlue());
                }
            }
        } else {
            event.getTags().add(newTag);
        }
    }

    public void refresh(Event event){
        this.event = event;
    }

    public void abort() {
        clearFields();
    }

    public void back() {
        clearFields();
        mainCtrl.showAddExpense(event);
    }

    public void clearFields() {
        nameTextField.clear();
        redTextField.clear();
        greenTextField.clear();
        blueTextField.clear();
    }

    public void ok() {
        clearFields();
        mainCtrl.showAddExpense(event);
    }

    @Override
    public void updateUI() {

    }
}
