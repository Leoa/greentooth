package ws.pusher;

import junit.framework.Assert;
import org.junit.Test;

public class EventTest {

    @Test
    public void testTrue() {
        Event event = new Event();
        Assert.assertNotNull(event);
    }
}
