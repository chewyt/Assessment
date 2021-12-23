package chewyt;

// import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.time.LocalDate;
// import java.util.ArrayList;
// 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Unit test for simple Main.
 */
public class MainTest {

    @Test
    public void assertEqualcondition() {

        String expected = "ABC";
        String actual = "ABC"; // Calling Object.method

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void assertTrueconditions() {

        // sysout function for validation
        // other operations, testing method, making an instance of an object
        // running different methods, checking arithmetic sum
        // checking void --> boolean methods to check for successful run

        assertTrue(true && !false);
    }
}
