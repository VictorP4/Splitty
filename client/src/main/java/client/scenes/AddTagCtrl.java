package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private Button addButton;
    @FXML
    private Button abortButton;
    @FXML
    private Button backButton;
    @FXML
    private Button removeButton;

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
        removeButton.setVisible(false);
    }

    /**
     * Updates the tag name.
     *
     * @param newValue The new value of the tag name.
     */
    private void updateTagName(String newValue) {
        tagName = newValue;
        colorFiller();
        updateRemoveButtonVisibility(newValue);
        updateAddButtonName(newValue);
    }

    /**
     * Fills the RGB text fields with values based on the selected tag.
     */
    private void colorFiller() {
        for (Tag tag : event.getTags()) {
            if (tag.getName().equals(tagName)) {
                colorPicker.setValue(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
                removeButton.setVisible(true);
                break;
            }
        }
    }

    /**
     * Checks if the tag name exists.
     *
     * @param tagName The tag name to check.
     * @return True if the tag name exists, false otherwise.
     */
    private boolean doesTagNameExist(String tagName) {
        for (Tag tag : event.getTags()) {
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the visibility of the remove button.
     *
     * @param newTagName The new tag name.
     */
    private void updateRemoveButtonVisibility(String newTagName) {
        boolean isValidTagName = doesTagNameExist(newTagName);
        removeButton.setVisible(isValidTagName);
    }

    /**
     * Updates the name of the add button.
     *
     * @param newTagName The new tag name.
     */
    private void updateAddButtonName(String newTagName) {
        boolean isValidTagName = doesTagNameExist(newTagName);
        addButton.setText(isValidTagName ? Main.getLocalizedString("edit") : Main.getLocalizedString("add"));
    }

    /**
     * Adds or updates the tag.
     */
    public void add() {
        String name = nameTextField.getText().trim();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Main.getLocalizedString("Error"));
            alert.setHeaderText(Main.getLocalizedString("Error"));
            alert.setContentText(Main.getLocalizedString("NameCannotBeEmpty"));
            alert.showAndWait();
            return;
        }

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
                    server.updateTag(tag);
                    this.event=server.updateEvent(event);
                }
            }
        } else {
            Tag tag = new Tag(name,red,green,blue);
            Tag saved = server.addTag(tag);
            event.getTags().add(saved);
            this.event=server.updateEvent(event);

        }
        clearFields();
        mainCtrl.showAddExpenseFromTag(event);
    }

    /**
     * Removes the tag.
     */
    public void remove() {
        String name = nameTextField.getText();
        Tag tag1=null;
        for(Tag tag : event.getTags()){
            if(name.equals(tag.getName())){

                tag1=tag;
            }
        }
        event.getTags().remove(tag1);
        this.event = server.updateEvent(event);
        for(Expense expense: event.getExpenses()){
            if(expense.getTag().equals(tag1)){
                expense.setTag(null);
                server.updateExpense(event.getId(), expense);
            }
        }
        server.removeTag(tag1);
        clearFields();
        mainCtrl.showAddExpenseFromTag(event);
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
        mainCtrl.showAddExpenseFromTag(event);
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
        abortButton.setText(Main.getLocalizedString("abort"));
        addButton.setText(Main.getLocalizedString("add"));
        backButton.setText(Main.getLocalizedString("back"));
    }
}
