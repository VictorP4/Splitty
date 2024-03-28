package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class EmailRequestBodyTest {

    /**
     * Tests the default constructor of the EmailRequestBody class.
     * It should initialize the email addresses and the code as null values.
     */
    @Test
    public void testEmptyConstructor() {
        EmailRequestBody erb = new EmailRequestBody();
        assertNull(erb.getEmailAddresses());
        assertNull(erb.getCode());
    }

    /**
     * Tests the default constructor of the EmailRequestBody class.
     * It should initialize the email addresses and the code as the inputted values in the constructor.
     */
    @Test
    public void testNonEmptyConstructor() {
        EmailRequestBody erb = new EmailRequestBody(new ArrayList<>(), "666");
        assertEquals(0, erb.getEmailAddresses().size());
        assertEquals("666", erb.getCode());
    }

    /**
     * Test all the setters and getters in the EmailRequestBody class.
     * It should set the emailAddresses and then return an equal value as the one set.
     */
    @Test
    public void testSettersAndGetters() {
        EmailRequestBody erb = new EmailRequestBody();
        erb.setEmailAddresses(new ArrayList<>());
        erb.setCode("666");
        assertEquals(0, erb.getEmailAddresses().size());
        assertEquals("666", erb.getCode());
    }

}
