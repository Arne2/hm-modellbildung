package cs.hm.edu.muenchen.hm.modellbildung.des.calculation;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.ListQueue;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.ArrivalEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.BeginServeEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.FinishServeEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.CalculationLog;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.*;
import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.CONFIGURATION;

/**
 * Created by dima on 10.05.17.
 */
public final class calculation {
    private static BigDecimal lastArrivalTime = new BigDecimal(0);
    private static BigDecimal lastBeginTime = new BigDecimal(0);
    private static BigDecimal lastFinishTime = new BigDecimal(0);
    private static BigDecimal lastClockTime = new BigDecimal(0);

    private static BigDecimal MeanQueueSize = new BigDecimal(0);
    private static BigDecimal MeanQueueSizeSum = new BigDecimal(0);

    private static BigDecimal MeanSystemSizeSum = new BigDecimal(0);
    private static BigDecimal MeanSystemSize = new BigDecimal(0);

    private static BigDecimal MeanSystemTime = new BigDecimal(0);
    private static BigDecimal MeanCallingTime = new BigDecimal(0);
    private static BigDecimal MeanWaitingTime = new BigDecimal(0);
    private static BigDecimal numOfPersons = new BigDecimal(0);
    private static BigDecimal SystemTimeSum = new BigDecimal(0);
    private static BigDecimal CallingTimeSum = new BigDecimal(0);
    private static BigDecimal WaitingTimeSum = new BigDecimal(0);
    private calculation(){
    }



    public static void calculate(Event ev, SimulationState state){

        BigDecimal delta_t = state.clock.systemTime().subtract(lastClockTime);
        BigDecimal queuesize = new BigDecimal(state.queue.count());
        BigDecimal queuesizeTime = queuesize.multiply(delta_t);
        MeanQueueSizeSum = MeanQueueSizeSum.add(queuesizeTime);
        if (!state.clock.systemTime().equals(new BigDecimal(0))){
            MeanQueueSize = MeanQueueSizeSum.divide(state.clock.systemTime(), 100, BigDecimal.ROUND_HALF_EVEN);
        }

        BigDecimal SystemSize = new BigDecimal(state.queue.count());
        final long phonesInUse = state.phones.stream()
                .filter(Phone::isOccupied)
                .count();
        SystemSize = SystemSize.add(new BigDecimal(phonesInUse));

        BigDecimal systemSizeTime = SystemSize.multiply(delta_t);
        MeanSystemSizeSum = MeanSystemSizeSum.add(systemSizeTime);
        BigDecimal SysSizeSum = MeanSystemSizeSum;
        if (!state.clock.systemTime().equals(new BigDecimal(0))) {
            MeanSystemSize = SysSizeSum.divide(state.clock.systemTime(), 100, BigDecimal.ROUND_HALF_EVEN);
        }
        //System.out.println(MeanSystemSize);
        lastClockTime = state.clock.systemTime();

        String event = "";
        if(ev instanceof ArrivalEvent){
            event = "arrive";
        }
        if(ev instanceof BeginServeEvent){
            event = "begin";
        }
        if (ev instanceof FinishServeEvent){
            event = "finish";
        }
        state.calculationLog.log(
                state.clock.systemTime(),
                event,
                queuesize,
                SystemSize,
                MeanQueueSize,
                MeanSystemSize,
                MeanWaitingTime,
                MeanCallingTime,
                MeanSystemTime


        );
    }

    public static void calculate(Person person){
        numOfPersons = numOfPersons.add(new BigDecimal(1));

        BigDecimal waitingTime = person.getBeginTime().subtract(person.getArrivalTime());
        BigDecimal callingTime = person.getFinishTime().subtract(person.getBeginTime());
        BigDecimal systemTime = person.getFinishTime().subtract(person.getArrivalTime());
        WaitingTimeSum = WaitingTimeSum.add(waitingTime);
        CallingTimeSum = CallingTimeSum.add(callingTime);
        SystemTimeSum = SystemTimeSum.add(systemTime);
        MeanWaitingTime = WaitingTimeSum.divide(numOfPersons, 100, BigDecimal.ROUND_HALF_EVEN);
        MeanCallingTime = CallingTimeSum.divide(numOfPersons, 100, BigDecimal.ROUND_HALF_EVEN);
        MeanSystemTime = SystemTimeSum.divide(numOfPersons, 100, BigDecimal.ROUND_HALF_EVEN);
        //System.out.println(MeanWaitingTime);
    }



}
