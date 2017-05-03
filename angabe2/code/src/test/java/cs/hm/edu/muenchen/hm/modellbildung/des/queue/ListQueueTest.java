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
        final ListQueue queue = new ListQueue();
        final Person person = new Person();
        queue.enqueue(person);
        if (queue.count() != 1 || queue.isEmpty()) {
            Assert.fail("Person was not added!");
        }
        final Person personFromQueue = queue.dequeue();
        if (!person.equals(personFromQueue)) {
            Assert.fail("Different Person was added!");
        }
    }

    @Test
    public void dequeue() throws Exception {
        final ListQueue queue = new ListQueue();
        final Person p1 = queue.dequeue();
        if (p1 != null) {
            Assert.fail("Person should be null!");
        }

        final Person p2 = new Person();
        final Person p3 = new Person();
        final Person p4 = new Person();
        queue.enqueue(p2);
        queue.enqueue(p3);
        queue.enqueue(p4);
        if (!p2.equals(queue.dequeue())) {
            Assert.fail("Queue does not dequeue in FIFO Order!");
        }
        if (!p3.equals(queue.dequeue())) {
            Assert.fail("Queue does not dequeue in FIFO Order!");
        }
        if (!p4.equals(queue.dequeue())) {
            Assert.fail("Queue does not dequeue in FIFO Order!");
        }
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

        for (int i=AMOUNT - 1 ; i >= 0; i--) {
            queue.dequeue();
            if (queue.count() != i) {
                Assert.fail("Queue count should be " + i + " but is: " + queue.count());
            }
        }
    }

    @Test
    public void isEmpty() throws Exception {
        final ListQueue queue = new ListQueue();
        if (!queue.isEmpty()) {
            Assert.fail("Queue must be empty!");
        }
        queue.enqueue(new Person());
        if (queue.isEmpty()) {
            Assert.fail("Queue must not be empty!");
        }
    }
}