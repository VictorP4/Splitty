package client.services;

import commons.Tag;
import javax.inject.Inject;

public class TagService {


    /**
     * Creates a new OverviewService.
     */
    @Inject
    public TagService() {}

    /**
     * Returns the color of a cell to the color of the tag added.
     *
     * @param tag The tag of which we will set the cell color to.
     */
    public String getCellColor(Tag tag) {
        return String.format("-fx-background-color: rgba(%d, %d, %d, 1);", tag.getRed(),
                tag.getGreen(), tag.getBlue());
    }

    /**
     * Returns the brightness the color of a cell should have.
     *
     * @param tag The tag of which we will set the cell color to.
     */
    public String getCellBrightness(Tag tag) {
        double brightness = (tag.getRed() * 0.299 + tag.getGreen() * 0.587
                + tag.getBlue() * 0.114) / 255;
        return brightness < 0.5 ? "white" : "black";
    }
}
