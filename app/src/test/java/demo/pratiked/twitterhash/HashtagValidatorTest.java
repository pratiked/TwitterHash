package demo.pratiked.twitterhash;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HashtagValidatorTest {

    @Test
    public void hashtagIsValid_hash(){
        assertFalse(MainActivity.isValidSearch("#India"));
    }

    @Test
    public void hashtagIsValid_at(){
        assertFalse(MainActivity.isValidSearch("@India"));
    }

    @Test
    public void hashtagIsValid_space(){
        assertFalse(MainActivity.isValidSearch(" "));
    }

    @Test
    public void hashtagIsValid_simple(){
        assertTrue(MainActivity.isValidSearch("India"));
    }
}
