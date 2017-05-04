package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.ListQueue;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.Clock;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.EventList;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.ArrivalEvent;

import java.io.IOException;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.*;

/**
 * @author peter-mueller
 */
public class CallShopSimulation {
    private final SimulationState state = new SimulationState() {
        private final Clock clock = new Clock();
        private final Queue queue = new ListQueue();

        private final Phone phone = new Phone();
        private final EventList eventList = new EventList();

        public Phone phone() {
            return phone;
        }
        public Queue queue() {
            return queue;
        }
        public Clock clock() {
            return clock;
        }
        public EventList events() {
            return eventList;
        }
    };

    public void run(long duration) {
        init();

        System.out.println(state);
        while ( state.clock().systemTime() < duration) {
            final Event event = state.events().nextEvent();
            state.clock().advanceTo(event.getTimeStamp());
            event.execute(state);

            System.out.print("    " + event + ":\n");
            System.out.println(state);
        }
        try {
            arrivalLog.close();
            serveLog.close();
            finishLog.close();
            queueLog.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        Event event = new ArrivalEvent(state.clock().systemTime());
        state.events().add(event);
    }

    public static void main(String[] args) {
        if(args.length == 2){
            MEAN_ARRIVAL = Integer.parseInt(args[0]);
            MEAN_CALL = Integer.parseInt(args[1]);
        }

        arrivalLog = new Log("../data/Arrival" + MEAN_ARRIVAL +"/" ,"arrival.csv");
        serveLog = new Log("../data/Arrival" + MEAN_ARRIVAL + "/","serve.csv");
        finishLog = new Log("../data/Arrival" + MEAN_ARRIVAL + "/","finish.csv");
        queueLog = new Log("../data/Arrival" + MEAN_ARRIVAL + "/", "queue.csv");


        final CallShopSimulation callShopSimulation = new CallShopSimulation();
        callShopSimulation.run(100000);
    }
}




