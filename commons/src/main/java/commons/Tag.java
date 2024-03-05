package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * Represents a tag for categorizing expenses.
 */
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int red;
    private int green;
    private int blue;

    /**
     * Constructs a Tag with the specified name and RGB color values.
     *
     * @param name  the name of the tag
     * @param red   the red component of the tag's color (0-255)
     * @param green the green component of the tag's color (0-255)
     * @param blue  the blue component of the tag's color (0-255)
     */
    public Tag(String name, int red, int green, int blue) {
        this.name = name;
        setColor(red, green, blue);
    }

    public Tag() {
        //for JPA
    }

    /**
     * Retrieves the ID of the tag.
     *
     * @return the ID of the tag
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the tag.
     *
     * @param id the ID of the tag to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the tag.
     *
     * @return the name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     *
     * @param name the name of the tag to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the RGB color components of the tag.
     *
     * @return an array containing the red, green, and blue components in that order
     */
    public int[] getColor() {
        return new int[]{red, green, blue};
    }

    /**
     * Sets the RGB color components of the tag.
     *
     * @param red   the red component of the tag's color (0-255)
     * @param green the green component of the tag's color (0-255)
     * @param blue  the blue component of the tag's color (0-255)
     */
    public void setColor(int red, int green, int blue) {
        this.red = Math.min(Math.max(0, red), 255);
        this.green = Math.min(Math.max(0, green), 255);
        this.blue = Math.min(Math.max(0, blue), 255);
    }

    /**
     * Gets the red component of the tag's color.
     *
     * @return the red component of the tag's color
     */
    public int getRed() {
        return red;
    }

    /**
     * Sets the red component of the tag's color.
     *
     * @param red the new red component of the tag's color
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * Gets the blue component of the tag's color.
     *
     * @return the blue component of the tag's color
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Sets the blue component of the tag's color.
     *
     * @param blue the new blue component of the tag's color
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }

    /**
     * Gets the green component of the tag's color.
     *
     * @return the green component of the tag's color
     */
    public int getGreen() {
        return green;
    }

    /**
     * Sets the green component of the tag's color.
     *
     * @param green the new green component of the tag's color
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return red == tag.red &&
                green == tag.green &&
                blue == tag.blue &&
                Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, red, green, blue);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
