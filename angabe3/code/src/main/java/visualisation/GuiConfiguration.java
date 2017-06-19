package visualisation;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * Created by Arne on 16.06.2017.
 * Configuration for the Visualization.
 */
public class GuiConfiguration {
    /**
     * Waiting time for the running simulation.
     */
    public static final int MILLIS = 5;

    // Default Sizes of Panels
    public static final int MENUSIZE = 75;
    public static final int STARTSIZE = 475;
    public static final int INFOSIZE = 275;

    // Colors
    public static final Color GOALCOLOR = Color.GREEN;
    public static final Color WALLCOLOR = Color.BLACK;
    public static final Color PERSONCOLOR = Color.HOTPINK;
    public static final Color MEASUREMENTCOLOR = Color.RED;

    /**
     * Value of a wall in the distance map.
     */
    public static final double WALLVALUE = -1.7976931348623157E308;

    /**
     * Minimal size of a cell.
     */
    public static final int MINCELLSIZE = 4;

    /**
     * Maximal size of a cell.
     */
    public static final int MAXCELLSIZE = 30;

    /**
     * For accessing the screen info.
     */
    public static final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    public static final boolean DEBUG = false;
}
