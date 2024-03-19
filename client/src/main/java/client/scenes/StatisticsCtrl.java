package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
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
     * Initializes the controller.
     */
    @Override
    public void updateUI() {
        stats.setText(Main.getLocalizedString("statistics"));
        eventCost.setText(Main.getLocalizedString("statEventCost"));
        backButton.setText(Main.getLocalizedString("back"));
        getTotal();
        populatePieChart();
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
        Map<String, Double> expensesPerTag = new HashMap<>();
        Map<String, Color> tagColors = new HashMap<>();

        for (Expense expense : event.getExpenses()) {
            Tag tag = expense.getTag();
            if (tag != null) {
                String tagName = tag.getName();
                double amount = expense.getAmount();
                expensesPerTag.put(tagName, expensesPerTag.getOrDefault(tagName, 0.0) + amount);
                tagColors.putIfAbsent(tagName, Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double totalExpense = expensesPerTag.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : expensesPerTag.entrySet()) {
            String tagName = entry.getKey();
            double amount = entry.getValue();
            double relativeValue = amount / totalExpense;
            Color color = tagColors.get(tagName);
            PieChart.Data data = new PieChart.Data(tagName + String.format("\n%.2f \u20AC (%.2f%%)", amount, relativeValue * 100), amount);
            //data.getNode().setStyle("-fx-pie-color: rgb(" + (int) (255 * color.getRed()) + "," + (int) (255 * color.getGreen()) + "," + (int) (255 * color.getBlue()) + ");");
            pieChartData.add(data);
        }

        pieChart.setData(pieChartData);
    }

    /**
     * Returns to the Overview scene.
     */
    public void back() {
        mainCtrl.showEventOverview(event);
    }
}

