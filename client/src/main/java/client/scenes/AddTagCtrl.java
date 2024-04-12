package client.scenes;

import client.Main;
import client.services.TagService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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
    private final TagService tagService;
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
    private Expense expense;

    /**
     * Constructs a new instance of AddTagCtrl.
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl, TagService tagService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagService = tagService;
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
                abort();
            }
        });

        setInstructions();
        buttonSetup();
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
     * Sets the expense.
     *
     * @param expense The expense to set.
     */
    public void setExpense(Expense expense){
        this.expense = expense;
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
            tagNameError("NameCannotBeEmpty");
        }

        if (tagService.doesTagNameExist(event, name)) {
            tagNameError("tagNameExist");
        }

        Color color = colorPicker.getValue();
        Tag saved = server.addTag(tagService.createNewTag(name, color));
        event.getTags().add(saved);
        this.event = server.updateEvent(event);

        clearFields();
        mainCtrl.showAddExpenseFromTag(event, expense);
    }

    /**
     * Edits the selected tag.
     */
    public void edit() {
        String name = nameTextField.getText().trim();
        if (name.isEmpty()) {
            tagNameError("NameCannotBeEmpty");
        }

        Color color = colorPicker.getValue();

        Tag temp = tagService.createNewTag(name, color);

        selectedTag.setName(temp.getName());
        selectedTag.setRed(temp.getRed());
        selectedTag.setGreen(temp.getGreen());
        selectedTag.setBlue(temp.getBlue());

        server.updateTag(selectedTag);
        this.event = server.updateEvent(event);

        clearFields();
        mainCtrl.showAddExpenseFromTag(event, expense);
    }

    /**
     * Shows an error popup when a user tried to create/edit a tag with no name.
     */
    private void tagNameError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Main.getLocalizedString("Error"));
        alert.setHeaderText(Main.getLocalizedString("Error"));
        alert.setContentText(Main.getLocalizedString(message));
        alert.showAndWait();
    }

    /**
     * Removes the tag.
     */
    public void remove() {
        String name = nameTextField.getText();
        Tag tag1 = tagService.removeTag(event, name);
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
        mainCtrl.showAddExpenseFromTag(event, expense);
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
        mainCtrl.showAddExpenseFromTag(event, expense);
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

    /**
     * Sets the hover over and focus 'look' of the buttons.
     */
    public void buttonSetup(){
        mainCtrl.buttonFocus(this.abortButton);
        mainCtrl.buttonFocus(this.addButton);
        mainCtrl.buttonFocus(this.backButton);
        mainCtrl.buttonFocus(this.removeButton);
        mainCtrl.buttonFocus(this.editButton);
        mainCtrl.buttonShadow(this.removeButton);
        mainCtrl.buttonShadow(this.editButton);

        this.colorPicker.focusedProperty().addListener( (obs, old, newV) -> {
            if(newV){
                Color b = Color.rgb(0, 150, 230);
                this.colorPicker.setEffect(new DropShadow(10, b));
            }
            else {
                this.colorPicker.setEffect(null);
            }
        });
    }

}
