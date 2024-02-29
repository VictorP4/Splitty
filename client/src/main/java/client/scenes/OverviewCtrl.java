package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class OverviewCtrl {
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Event event;
    @FXML
    private Text title;
    @FXML
    private TextField titleField;
    @FXML
    private AnchorPane anchorPane;

    @Inject
    public OverviewCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Event e) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.event = e;
    }

    public void initialize() {
        if(event.getTitle()!=null) title.setText(event.getTitle());
        else title.setText("Title");
        titleField.setText(title.getText());

        // Initially hide the TextField
        titleField.setVisible(false);

        // Set up event handling for switching between view and edit modes
        titleField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Focus lost
                updateTitle();
            }
        });

        // Double-click event handler to switch to edit mode
        title.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click detected
                startEditing();
            }
        });

        // Key pressed event handler to switch to view mode when Enter is pressed
        titleField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateTitle();
            }
        });
    }

    private void startEditing() {
        titleField.setText(title.getText()); // Set initial text of TextField
        title.setVisible(false); // Hide the Text node
        titleField.setVisible(true); // Show the TextField
        titleField.requestFocus(); // Set focus to TextField
    }

    private void updateTitle() {
        title.setText(titleField.getText()); // Update text of Text node
        title.setVisible(true); // Show the Text node
        titleField.setVisible(false); // Hide the TextField
        serverUtils.updateEvent(event);
    }

    public void showInvites() {
        mainCtrl.showInvitation();
    }
}
