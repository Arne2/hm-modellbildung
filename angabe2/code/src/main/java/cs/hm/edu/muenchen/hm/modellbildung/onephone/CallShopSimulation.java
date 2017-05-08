package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.ArrivalEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

    private void run() {
        init();

        while (state.clock.systemTime() < DURATION) {
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

    /**
     * Sets the constants of the Configuration at the begin of the program.
     * @param args array of strings
     */
    private static void loadConfiguration(String[] args){
        List<String> arguments = Arrays.asList(args);
        int index = arguments.indexOf("-a");
        if (index >= 0 && arguments.size() > index){
            String s = arguments.get(index+1);
            MEAN_ARRIVAL = Integer.parseInt(s);
        }
        index = arguments.indexOf("-c");
        if (index >= 0 && arguments.size() > index){
            String s = arguments.get(index+1);
            MEAN_CALL = Integer.parseInt(s);
        }
        index = arguments.indexOf("-d");
        if (index >= 0 && arguments.size() > index){
            String s = arguments.get(index+1);
            DURATION = Integer.parseInt(s);
        }
        index = arguments.indexOf("-vip");
        if (index >= 0 && arguments.size() > index){
            String s = arguments.get(index+1);
            VIP_PERCENTAGE = Integer.parseInt(s);
        }
        index = arguments.indexOf("-conf");
        if (index >= 0 && arguments.size() > index){
            String s = arguments.get(index+1);
            CONFIGURATION = Integer.parseInt(s);
        }
    }

    public static void main(String[] args) {
        loadConfiguration(args);

        final Path folder = Paths.get("../data/");
        try (final SimulationState state = new SimulationState(folder)) {
            final CallShopSimulation callShopSimulation = new CallShopSimulation(state);
            callShopSimulation.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




