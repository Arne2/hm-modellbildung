package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.XLog;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.XYLog;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class contains all Logs for the documentation of the results.
 * @author peter-mueller
 */
public class CallShopLogs implements AutoCloseable {

    public CallShopLogs(Path folder) throws IOException {
        queueSizeNormal = new XYLog(folder.resolve("queue-size-normal.csv"));
        queueSizeResident = new XYLog(folder.resolve("queue-size-resident.csv"));
        queueSizeAll = new XYLog(folder.resolve("queue-size-all.csv"));

        arrivalDelta = new XLog(folder.resolve("arrival-delta.csv"));
        serveDelta = new XLog(folder.resolve("serve-delta.csv"));

        meanQueueTimeNormal = new XYLog(folder.resolve("mean-queue-time-normal.csv"));
        meanQueueTimeResident = new XYLog(folder.resolve("mean-queue-time-resident.csv"));
        meanQueueTimeAll = new XYLog(folder.resolve("mean-queue-time-all.csv"));

        meanSystemTimeNormal = new XYLog(folder.resolve("mean-system-time-normal.csv"));
        meanSystemTimeResident = new XYLog(folder.resolve("mean-system-time-resident.csv"));
        meanSystemTimeAll = new XYLog(folder.resolve("mean-system-time-all.csv"));

        meanQueueSizeNormal = new XYLog(folder.resolve("mean-queue-size-normal.csv"));
        meanQueueSizeResident = new XYLog(folder.resolve("mean-queue-size-resident.csv"));
        meanQueueSizeAll = new XYLog(folder.resolve("mean-queue-size-all.csv"));

        meanPhoneSizeNormal = new XYLog(folder.resolve("mean-phone-size-normal.csv"));
        meanPhoneSizeResident = new XYLog(folder.resolve("mean-phone-size-resident.csv"));
        meanPhoneSizeAll = new XYLog(folder.resolve("mean-phone-size-all.csv"));

        meanSystemSizeNormal = new XYLog(folder.resolve("mean-system-size-normal.csv"));
        meanSystemSizeResident = new XYLog(folder.resolve("mean-system-size-resident.csv"));
        meanSystemSizeAll = new XYLog(folder.resolve("mean-system-size-all.csv"));

        littleQueueNormal = new XYLog(folder.resolve("little-queue-normal.csv"));
        littleQueueResident = new XYLog(folder.resolve("little-queue-resident.csv"));
        littleQueueAll = new XYLog(folder.resolve("little-queue-all.csv"));

        littleSystemNormal = new XYLog(folder.resolve("little-system-normal.csv"));
        littleSystemResident = new XYLog(folder.resolve("little-system-resident.csv"));
        littleSystemAll = new XYLog(folder.resolve("little-system-all.csv"));
    }

    public final XYLog queueSizeNormal;
    public final XYLog queueSizeResident;
    public final XYLog queueSizeAll;

    public final XLog arrivalDelta;
    public final XLog serveDelta;

    public final XYLog meanQueueTimeNormal;
    public final XYLog meanQueueTimeResident;
    public final XYLog meanQueueTimeAll;

    public final XYLog meanSystemTimeNormal;
    public final XYLog meanSystemTimeResident;
    public final XYLog meanSystemTimeAll;
    
    public final XYLog meanQueueSizeNormal;
    public final XYLog meanQueueSizeResident;
    public final XYLog meanQueueSizeAll;

    public final XYLog meanPhoneSizeNormal;
    public final XYLog meanPhoneSizeResident;
    public final XYLog meanPhoneSizeAll;

    public final XYLog meanSystemSizeNormal;
    public final XYLog meanSystemSizeResident;
    public final XYLog meanSystemSizeAll;

    public final XYLog littleQueueNormal;
    public final XYLog littleQueueResident;
    public final XYLog littleQueueAll;

    public final XYLog littleSystemNormal;
    public final XYLog littleSystemResident;
    public final XYLog littleSystemAll;

    @Override
    public void close() throws IOException {
        queueSizeNormal.close();
        queueSizeResident.close();
        queueSizeAll.close();

        arrivalDelta.close();
        serveDelta.close();

        meanQueueTimeNormal.close();
        meanQueueTimeResident.close();
        meanQueueTimeAll.close();

        meanSystemTimeNormal.close();
        meanSystemTimeResident.close();
        meanSystemTimeAll.close();

        meanQueueSizeNormal.close();
        meanQueueSizeResident.close();
        meanQueueSizeAll.close();

        meanPhoneSizeNormal.close();
        meanPhoneSizeResident.close();
        meanPhoneSizeAll.close();

        meanSystemSizeNormal.close();
        meanSystemSizeResident.close();
        meanSystemSizeAll.close();

        littleQueueNormal.close();
        littleQueueResident.close();
        littleQueueAll.close();

        littleSystemNormal.close();
        littleSystemResident.close();
        littleSystemAll.close();
    }
}
