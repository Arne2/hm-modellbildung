package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.ArrivalEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.*;

/**
 * @author peter-mueller
 */
public class CallShopSimulation {

    private final SimulationState state;

    public CallShopSimulation(SimulationState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null!");
        }
        this.state = state;
    }

    private void run(double duration) {
        init();

        while (state.clock.systemTime() < duration) {
            final Event event = state.events.nextEvent();
            state.clock.advanceTo(event.getTimeStamp());

            event.execute(state);

            final long phonesInUse = state.phones.stream()
                    .filter(Phone::isOccupied)
                    .count();
            state.queueLog.log(
                    state.clock.systemTime(),
                    state.queue.count(),
                    state.queue.count() + phonesInUse
            );
        }
    }

    private void init() {
        state.phones.add(new Phone(CONFIGURATION > 1));
        if (CONFIGURATION == 3) {
            state.phones.add(new Phone(false));
        }
        Event event = new ArrivalEvent(state.clock.systemTime());
        state.events.add(event);
    }

    public static void main(String[] args) {
        if (args.length == 4) {
            MEAN_ARRIVAL = Integer.parseInt(args[0]);
            MEAN_CALL = Integer.parseInt(args[1]);
            VIP_PERCENTAGE = Integer.parseInt(args[2]);
            CONFIGURATION = Integer.parseInt(args[3]);
        }

        final Path folder = Paths.get("../data/");
        try (final SimulationState state = new SimulationState(folder)) {
            final CallShopSimulation callShopSimulation = new CallShopSimulation(state);
            callShopSimulation.run(100000000.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




