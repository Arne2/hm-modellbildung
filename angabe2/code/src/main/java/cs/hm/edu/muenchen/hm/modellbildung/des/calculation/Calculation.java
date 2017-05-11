package cs.hm.edu.muenchen.hm.modellbildung.des.calculation;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.ListQueue;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.ArrivalEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.BeginServeEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.FinishServeEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.CalculationLog;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Bidi;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.*;
import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.CONFIGURATION;

/**
 * Created by dima on 10.05.17.
 */
public final class Calculation {

    public static int DivisionAccuracy = 10;
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
    private static BigDecimal MeanSystemTime = new BigDecimal(0);


    private static BigDecimal LittleLaw = new BigDecimal(0);

    private Calculation(){
    }


    private static void CalcMeanQueueSize(SimulationState state){
        if(lastClockTime.equals(state.clock.systemTime())) return;
        if(lastClockTime.equals(state.clock.systemTime())) return;
        BigDecimal delta_t = state.clock.systemTime().subtract(lastClockTime);
        if(delta_t.equals(new BigDecimal(0))){

        }
        BigDecimal queuesize = new BigDecimal(state.queue.count());
        BigDecimal queuesizeTime = queuesize.multiply(delta_t);
        MeanQueueSizeSum = MeanQueueSizeSum.add(queuesizeTime);
        if (!state.clock.systemTime().equals(new BigDecimal(0))){
            MeanQueueSize = MeanQueueSizeSum.divide(state.clock.systemTime(),
                    DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        }
    }


    private static void CalcMeanSystemSize(SimulationState state){
        if(lastClockTime.equals(state.clock.systemTime())) return;
        if(lastClockTime.equals(state.clock.systemTime())){
            System.out.println("ZERO");
            return;
        }
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
            MeanSystemSize = SysSizeSum.divide(state.clock.systemTime(),
                    DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    private static void CalcMeanQueueTime(SimulationState state){
        if(lastClockTime.equals(state.clock.systemTime())) return;
        /* calculate Waiting Time of Persons in Queue*/
        BigDecimal waitTimeInQueueSum = new BigDecimal(0);
        for (Person person:state.queue.getList()) {
            BigDecimal waitTime = state.clock.systemTime().subtract(person.getArrivalTime());
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
            sum(waiting time of People in Queue) + sum(waiting time of people on phone) + sum(waiting time of People finished)
            -----------------------------------------------------------------------------------------------------------------
                                           people in queue + people on phone + people finished
         */

        BigDecimal a = waitTimeInQueueSum.add(waitTimeInPhoneSum).add(FinishedWaitingTimeSum);


        BigDecimal b = numOfFinishedPersons.add(numOfUsersOnPhone).
                add(new BigDecimal(state.queue.getList().size()));
        if(!b.equals(new BigDecimal(0))) {
            MeanWaitingTime = a.divide(b,
                    DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        }




    }

    private static void CalcMeanSystemTime(SimulationState state){
        if(lastClockTime.equals(state.clock.systemTime())) return;
        /* calculating mean systemTime of people in System */
        /*
        Time(now) - Arrival(Person) = WaitTime(Person) = SystemTime -> persons in Queue
        Sum up all WaitTimes of all Persons in queue
         */
        BigDecimal waitTimeInQueueSum = new BigDecimal(0);
        for (Person person:state.queue.getList()) {

            BigDecimal waitTime = state.clock.systemTime().subtract(person.getArrivalTime());
            waitTimeInQueueSum = waitTimeInQueueSum.add(waitTime);
        }

        /*
        Time(Now) - Arrival(Person) = SystemTime(Person) --> person on Phone
        Sum up all SystemTimes of all Person on ALL Phones
         */
        BigDecimal systemTimeInPhoneSum = new BigDecimal(0);
        BigDecimal numOfUsersOnPhone = new BigDecimal(0);
        for (Phone phone:state.phones) {
            if(phone.getUser()!= null) {
                BigDecimal TimeInPhone = state.clock.systemTime().subtract(phone.getUser().getArrivalTime());
                systemTimeInPhoneSum = systemTimeInPhoneSum.add(TimeInPhone);
                numOfUsersOnPhone = numOfUsersOnPhone.add(new BigDecimal(1));
            }
        }


        BigDecimal a = waitTimeInQueueSum.add(systemTimeInPhoneSum).add(FinishedSystemTimeSum);


        BigDecimal b = numOfFinishedPersons.add(numOfUsersOnPhone).
                add(new BigDecimal(state.queue.getList().size()));
        if(!b.equals(new BigDecimal(0))) {
            MeanSystemTime = a.divide(b, DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        }

    }

    private static void CalcLittle(){
        int meanArrival = MEAN_ARRIVAL;
        BigDecimal lambda = new BigDecimal(1).divide(new BigDecimal(meanArrival),
                DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        /* L = lambda * W

        lambda * W - L = 0 ---> For Big Numbers
         */
        LittleLaw = (lambda.multiply(MeanSystemTime)).subtract(MeanQueueSize);
    }

    public static void calculate(Event ev, SimulationState state){
        CalcMeanQueueSize(state);
        CalcMeanSystemSize(state);
        CalcMeanQueueTime(state);
        CalcMeanSystemTime(state);
        CalcLittle();

        lastClockTime = state.clock.systemTime();
        System.out.println(MeanQueueSize);
        /*count people on Phone: for systemsize*/
        int pplOnPhone = 0;
        for (Phone phone:state.phones) {
            if(phone.isOccupied())pplOnPhone++;
        }

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
                new BigDecimal(state.queue.count()),
                new BigDecimal(state.queue.count()+pplOnPhone),
                MeanQueueSize,
                MeanSystemSize,
                MeanWaitingTime,
                FinishedMeanCallingTime,
                MeanSystemTime,
                LittleLaw


        );
    }

    public static void calculate(Person person){

        numOfFinishedPersons = numOfFinishedPersons.add(new BigDecimal(1));

        BigDecimal waitingTime = person.getBeginTime().subtract(person.getArrivalTime());
        BigDecimal callingTime = person.getFinishTime().subtract(person.getBeginTime());
        BigDecimal systemTime = person.getFinishTime().subtract(person.getArrivalTime());
        FinishedWaitingTimeSum = FinishedWaitingTimeSum.add(waitingTime);
        FinishedCallingTimeSum = FinishedCallingTimeSum.add(callingTime);
        FinishedSystemTimeSum = FinishedSystemTimeSum.add(systemTime);
        FinishedMeanWaitingTime = FinishedWaitingTimeSum.divide(numOfFinishedPersons,
                DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        FinishedMeanCallingTime = FinishedCallingTimeSum.divide(numOfFinishedPersons,
                DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        FinishedMeanSystemTime = FinishedSystemTimeSum.divide(numOfFinishedPersons,
                DivisionAccuracy, BigDecimal.ROUND_HALF_EVEN);
        //System.out.println(MeanWaitingTime);*/
    }



}
