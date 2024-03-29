package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import commons.Event;
import commons.Expense;
import commons.Tag;

import java.util.HashMap;
import java.util.Map;

public class StatisticsCtrl implements Main.UpdatableUI {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public AnchorPane anchor;
    private Event event;
    @FXML
    public Text stats;
    @FXML
    public Text eventCost;
    @FXML
    public Button backButton;
    @FXML
    public Text total;
    @FXML
    public PieChart pieChart;
    Map<String, Double> expensesPerTag = new HashMap<>();
    Map<String, Color> tagColors = new HashMap<>();

    /**
     * Constructs a new instance of a StatisticsCtrl
     *
     * @param server   The utility class for server-related operations.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * initializes the scene
     */
    public void initialize(){
        anchor.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                back();
            }
        });
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void updateUI() {
        stats.setText(Main.getLocalizedString("statistics"));
        eventCost.setText(Main.getLocalizedString("statEventCost"));
        backButton.setText(Main.getLocalizedString("back"));
    }

    /**
     * Sets the event for which the statistics are to be displayed.
     *
     * @param event The event for which the statistics are to be displayed.
     */
    public void refresh(Event event) {
        this.event = event;
        getTotal();
        populatePieChart();
        paintChart();
        pieChart.getParent().layout();
    }

    /**
     * Calculates and displays the total cost of all expenses in the event.
     */
    public void getTotal() {
        double totalCost = event.getExpenses().stream()
                .mapToDouble(Expense::getAmount).sum();
        total.setText(String.format("%.2f \u20AC", totalCost));
    }

    /**
     * Populates the pie chart with expenses per tag.
     */
    private void populatePieChart() {
        expensesPerTag = new HashMap<>();
        tagColors = new HashMap<>();

        for (Expense expense : event.getExpenses()) {
            Tag tag = expense.getTag();
            String tagName;
            if (tag != null) {
                tagName = tag.getName();
            } else {
                tagName = "Other";
                tag = new Tag("Other", 255, 255, 255);
            }
            double amount = expense.getAmount();
            expensesPerTag.put(tagName, expensesPerTag.getOrDefault(tagName, 0.0) + amount);
            tagColors.putIfAbsent(tagName, Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double totalExpense = expensesPerTag.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : expensesPerTag.entrySet()) {
            String tagName = entry.getKey();
            double amount = entry.getValue();
            double relativeValue = amount / totalExpense;
            PieChart.Data data = new PieChart.Data(tagName + String.format("\n%.2f \u20AC (%.2f%%)", amount, relativeValue * 100), amount);
            pieChartData.add(data);
        }
        pieChart.setData(pieChartData);
    }


    /**
     * Paints the pie chart with the colors of the tags.
     */
    private void paintChart() {
        for (PieChart.Data data : pieChart.getData()) {
            String tagName = data.getName().split("\n")[0];
            Color color = tagColors.get(tagName);
            data.getNode().setStyle("-fx-pie-color: rgb(" + (int) (255 * color.getRed()) + "," + (int) (255 * color.getGreen()) + "," + (int) (255 * color.getBlue()) + ");");
            for (Node node : pieChart.lookupAll("Label.chart-legend-item")) {
                if (node instanceof Label) {
                    Label label = (Label) node;
                    if (label.getText().startsWith(tagName)) {
                        Node legendSymbol = label.getGraphic();
                        if (legendSymbol != null) {
                            legendSymbol.setStyle("-fx-background-color: rgb(" + (int) (255 * color.getRed()) + "," + (int) (255 * color.getGreen()) + "," + (int) (255 * color.getBlue()) + ");");
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns to the Overview scene.
     */
    public void back() {
        mainCtrl.showEventOverview(event);
    }
}

