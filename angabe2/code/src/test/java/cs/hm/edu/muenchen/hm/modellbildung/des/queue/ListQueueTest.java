package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Person;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author peter-mueller
 */
public class ListQueueTest {

    private static final int AMOUNT = 100;

    @Test
    public void enqueue() throws Exception {

    }

    @Test
    public void dequeue() throws Exception {

    }

    @Test
    public void count() throws Exception {
        final Queue queue = new ListQueue();
        if (queue.count() != 0) {
            Assert.fail("Queue count should be zero but is: " + queue.count());
        }
        for (int i=1; i <= AMOUNT; i++) {
            queue.enqueue(new Person());
            if (queue.count() != i) {
                Assert.fail("Queue count should be " + i + " but is: " + queue.count());
            }
        }
    }

    @Test
    public void isEmpty() throws Exception {

    }
}