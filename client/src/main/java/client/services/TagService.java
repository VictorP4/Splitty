package client.services;

import client.utils.ServerUtils;
import commons.Event;
import commons.Tag;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class TagService {
    ServerUtils serverUtils;

    /**
     * Creates a new OverviewService.
     */
    @Inject
    public TagService(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

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

    /**
     * Creates a new tag.
     *
     * @param name The name of the tag.
     * @param color The color of the tag.
     */
    public Tag createNewTag(String name, Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);

        return new Tag(name, red, green, blue);
    }

    /**
     * Adds a tag to an event.
     *
     * @param event The event to which we will add the tag.
     * @param tagName The name of the tag.
     */
    public boolean doesTagNameExist(Event event, String tagName) {
        for (Tag tag : event.getTags()) {
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a tag from an event.
     *
     * @param event The event from which we will remove the tag.
     * @param name The name of the tag.
     */
    public Tag removeTag(Event event, String name) {
        Tag tag1 = null;
        for(Tag tag : event.getTags()){
            if(name.equals(tag.getName())){
                tag1=tag;
            }
        }
        return tag1;
    }
}
