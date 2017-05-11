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
import java.text.Bidi;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.*;
import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.CONFIGURATION;

/**
 * Created by dima on 10.05.17.
 */
public final class Calculation {

    private static BigDecimal lastClockTime = new BigDecimal(0);

    private static BigDecimal MeanQueueSize = new BigDecimal(0);
    private static BigDecimal MeanQueueSizeSum = new BigDecimal(0);

    private static BigDecimal MeanSystemSizeSum = new BigDecimal(0);
    private static BigDecimal MeanSystemSize = new BigDecimal(0);

    private static BigDecimal FinishedMeanSystemTime = new BigDecimal(0);
    private static BigDecimal FinishedMeanCallingTime = new BigDecimal(0);
    private static BigDecimal FinishedMeanWaitingTime = new BigDecimal(0);


    private static BigDecimal numOfFinishedPersons = new BigDecimal(0);
    private static BigDecimal FinishedSystemTimeSum = new BigDecimal(0);
    private static BigDecimal FinishedCallingTimeSum = new BigDecimal(0);
    private static BigDecimal FinishedWaitingTimeSum = new BigDecimal(0);

    private static BigDecimal MeanWaitingTime = new BigDecimal(0);
    private Calculation(){
    }


    private static void CalcMeanQueueSize(SimulationState state){
        BigDecimal delta_t = state.clock.systemTime().subtract(lastClockTime);
        BigDecimal queuesize = new BigDecimal(state.queue.count());
        BigDecimal queuesizeTime = queuesize.multiply(delta_t);
        MeanQueueSizeSum = MeanQueueSizeSum.add(queuesizeTime);
        if (!state.clock.systemTime().equals(new BigDecimal(0))){
            MeanQueueSize = MeanQueueSizeSum.divide(state.clock.systemTime(), 10, BigDecimal.ROUND_HALF_EVEN);
        }
    }


    private static void CalcMeanSystemSize(SimulationState state){
        BigDecimal delta_t = state.clock.systemTime().subtract(lastClockTime);
        BigDecimal SystemSize = new BigDecimal(state.queue.count());
        final long phonesInUse = state.phones.stream()
                .filter(Phone::isOccupied)
                .count();
        SystemSize = SystemSize.add(new BigDecimal(phonesInUse));

        BigDecimal systemSizeTime = SystemSize.multiply(delta_t);
        MeanSystemSizeSum = MeanSystemSizeSum.add(systemSizeTime);
        BigDecimal SysSizeSum = MeanSystemSizeSum;
        if (!state.clock.systemTime().equals(new BigDecimal(0))) {
            MeanSystemSize = SysSizeSum.divide(state.clock.systemTime(), 10, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    private static void CalcMeanQueueTime(SimulationState state){

        /* calculate Waiting Time of Persons in Queue*/
        BigDecimal waitTimeInQueueSum = new BigDecimal(0);
        for (Person person:state.queue.getList()) {
            BigDecimal waitTime = person.getArrivalTime().subtract(state.clock.systemTime());
            waitTimeInQueueSum = waitTimeInQueueSum.add(waitTime);
        }
        BigDecimal waitTimeInPhoneSum = new BigDecimal(0);
        BigDecimal numOfUsersOnPhone = new BigDecimal(0);
        for (Phone phone:state.phones) {
            if(phone.getUser()!= null) {
                BigDecimal TimeInPhone = phone.getUser().getBeginTime().subtract(phone.getUser().getArrivalTime());
                waitTimeInPhoneSum = waitTimeInPhoneSum.add(TimeInPhone);
                numOfUsersOnPhone = numOfUsersOnPhone.add(new BigDecimal(1));
            }
        }


        /*Formula:
            sum(WaitngTime of People in Queue) + sum(Waiting Time of People on Phone) + sum(wainting Time of People finished)
            -----------------------------------------------------------------------------------------------------------------
                                           people in Queue + people on Phone + people finished
         */

        BigDecimal a = waitTimeInQueueSum.add(waitTimeInPhoneSum).add(FinishedWaitingTimeSum);


        BigDecimal b = numOfFinishedPersons.add(numOfUsersOnPhone).
                add(new BigDecimal(state.queue.getList().size()));
        if(!b.equals(new BigDecimal(0))) {
            MeanWaitingTime = a.divide(b, 10, BigDecimal.ROUND_HALF_EVEN);
        }




    }

    private static void CalcMeanSystemTime(SimulationState state){

    }



    public static void calculate(Event ev, SimulationState state){
        CalcMeanQueueSize(state);
        CalcMeanSystemSize(state);
        CalcMeanQueueTime(state);
        CalcMeanSystemTime(state);

        System.out.println(MeanWaitingTime);
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
        /*state.calculationLog.log(
                state.clock.systemTime(),
                event,
                queuesize,
                SystemSize,
                MeanQueueSize,
                MeanSystemSize,
                MeanWaitingTime,
                MeanCallingTime,
                MeanSystemTime


        );*/
    }

    public static void calculate(Person person){

        numOfFinishedPersons = numOfFinishedPersons.add(new BigDecimal(1));

        BigDecimal waitingTime = person.getBeginTime().subtract(person.getArrivalTime());
        BigDecimal callingTime = person.getFinishTime().subtract(person.getBeginTime());
        BigDecimal systemTime = person.getFinishTime().subtract(person.getArrivalTime());
        FinishedWaitingTimeSum = FinishedWaitingTimeSum.add(waitingTime);
        FinishedCallingTimeSum = FinishedCallingTimeSum.add(callingTime);
        FinishedSystemTimeSum = FinishedSystemTimeSum.add(systemTime);
        FinishedMeanWaitingTime = FinishedWaitingTimeSum.divide(numOfFinishedPersons, 10, BigDecimal.ROUND_HALF_EVEN);
        FinishedMeanCallingTime = FinishedCallingTimeSum.divide(numOfFinishedPersons, 10, BigDecimal.ROUND_HALF_EVEN);
        FinishedMeanSystemTime = FinishedSystemTimeSum.divide(numOfFinishedPersons, 10, BigDecimal.ROUND_HALF_EVEN);
        //System.out.println(MeanWaitingTime);*/
    }



}
