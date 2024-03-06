package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Controller class for the Add Tag scene.
 */
public class AddTagCtrl implements Main.UpdatableUI {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private Event event;
    private String tagName;
    @FXML
    private Text colorText;
    @FXML
    private Text nameText;
    @FXML
    private Text addEditText;
    @FXML
    private TextField nameTextField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button abort;
    @FXML
    private Button add;
    @FXML
    private Button back;

    /**
     * Constructs a new instance of AddTagCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        tagName = nameTextField.getText();
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> updateTagName(newValue));
    }

    /**
     * Updates the tag name.
     *
     * @param newValue The new value of the tag name.
     */
    private void updateTagName(String newValue) {
        tagName = newValue;
        colorFiller();
    }

    /**
     * Fills the RGB text fields with values based on the selected tag.
     */
    private void colorFiller() {
        for (Tag tag : event.getTags()) {
            if (tag.getName().equals(tagName)) {
                colorPicker.setValue(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
                break;
            }
        }
    }

    /**
     * Adds or updates the tag.
     */
    public void add() {
        String name = nameTextField.getText();
        Color color = colorPicker.getValue();
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        if (event.getTags().stream()
                .map(Tag::getName)
                .toList().contains(name)) {
            for (Tag tag : event.getTags()) {
                if (tag.getName().equals(name)) {
                    tag.setRed(red);
                    tag.setGreen(green);
                    tag.setBlue(blue);
                }
            }
        } else {
            event.getTags().add(new Tag(name, red, green, blue));
        }
    }

    /**
     * Refreshes the event.
     *
     * @param event The event to refresh.
     */
    public void refresh(Event event) {
        this.event = event;
    }

    /**
     * Clears all input fields.
     */
    public void abort() {
        clearFields();
    }

    /**
     * Returns to the Add Expense scene.
     */
    public void back() {
        clearFields();
        mainCtrl.showAddExpense(event);
    }

    /**
     * Clears all input fields and returns to the Add Expense scene.
     */
    public void ok() {
        clearFields();
        mainCtrl.showAddExpense(event);
    }

    /**
     * Clears all input fields.
     */
    public void clearFields() {
        nameTextField.clear();
        colorPicker.setValue(Color.WHITE);
    }

    /**
     * Updates the user interface.
     */
    @Override
    public void updateUI() {
        addEditText.setText(Main.getLocalizedString("Add/EditTag"));
        nameText.setText(Main.getLocalizedString("Name"));
        colorText.setText(Main.getLocalizedString("Color"));
        abort.setText(Main.getLocalizedString("abort"));
        add.setText(Main.getLocalizedString("add"));
        back.setText(Main.getLocalizedString("back"));
    }
}
