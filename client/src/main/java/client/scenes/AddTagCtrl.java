package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;



/**
 * Controller class for the Add Tag scene.
 */
public class AddTagCtrl implements Main.UpdatableUI {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    public AnchorPane anchor;
    private Event event;
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
    private Button editButton;
    @FXML
    private Button abortButton;
    @FXML
    private Button backButton;
    @FXML
    private Button removeButton;
    private Tag selectedTag;

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
        modeChanger();
        anchor.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                if (selectedTag == null) {
                    add();
                } else {
                    edit();
                }
            }
            if(event.getCode() == KeyCode.ESCAPE){
                back();
            }
            if(event.isControlDown() && event.isAltDown() && event.getCode() == KeyCode.C){
                abort(); //TODO
            }
        });

        setInstructions();
    }

    /**
     * Sets the selected tag.
     *
     * @param tag The tag to set.
     */
    public void setSelectedTag(Tag tag){
        if (tag != null) {
            this.selectedTag = tag;
        }
        modeChanger();
    }

    /**
     * Changes the mode of the scene between edit and add.
     */
    public void modeChanger() {
        if (selectedTag != null) {
            nameTextField.setText(selectedTag.getName());
            colorPicker.setValue(Color.rgb(selectedTag.getRed(), selectedTag.getGreen(), selectedTag.getBlue()));
            editButton.setVisible(true);
            removeButton.setVisible(true);
            addButton.setVisible(false);
            addEditText.setText(Main.getLocalizedString("EditTag"));
        } else {
            editButton.setVisible(false);
            removeButton.setVisible(false);
            addButton.setVisible(true);
            addEditText.setText(Main.getLocalizedString("AddTag"));
        }
    }

    /**
     * Adds a new tag.
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

        if (doesTagNameExist(name)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Main.getLocalizedString("Error"));
            alert.setHeaderText(Main.getLocalizedString("Error"));
            alert.setContentText(Main.getLocalizedString("TagNameExists"));
            alert.showAndWait();
            return;
        }

        Color color = colorPicker.getValue();
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        Tag tag = new Tag(name, red, green, blue);
        Tag saved = server.addTag(tag);
        event.getTags().add(saved);
        this.event = server.updateEvent(event);

        clearFields();
        mainCtrl.showAddExpenseFromTag(event);
    }

    /**
     * Edits the selected tag.
     */
    public void edit() {
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

        selectedTag.setName(name);
        selectedTag.setRed(red);
        selectedTag.setGreen(green);
        selectedTag.setBlue(blue);
        server.updateTag(selectedTag);
        this.event = server.updateEvent(event);

        clearFields();
        mainCtrl.showAddExpenseFromTag(event);
    }

    /**
     * Checks if the tag name already exists.
     *
     * @param tagName The name of the tag to check.
     * @return True if the tag name already exists, false otherwise.
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
        addButton.setText(Main.getLocalizedString("add"));
        removeButton.setVisible(false);
        selectedTag = null;
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

    /**
     * Sets the instruction popups for shortcuts.
     */
    public void setInstructions(){
        mainCtrl.instructionsPopup(new Label(" press ESC to go back "), this.backButton);
        mainCtrl.instructionsPopup(new Label(" press ENTER \n add the tag "), this.addButton);
        mainCtrl.instructionsPopup(new Label(" press CTRL + ALT + C \n to clear fields "), this.abortButton);
    }

}
