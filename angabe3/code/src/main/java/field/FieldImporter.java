package field;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import config.Configuration;
import field.location.Location;
import org.w3c.dom.css.RGBColor;
import person.Person;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dani on 07.06.2017.
 */
public class FieldImporter {

    public enum ColorOfPixel{
        colorBlack(-16777216),
        colorBlue(-12629812),
        colorGreen(-14503604),
        colorWhite(-1);

        private int value;

        ColorOfPixel(int value) {
            this.value = value;
        }

        private static final Map<Integer, ColorOfPixel> intToTypeMap = new HashMap<Integer, ColorOfPixel>();
        static {
            for (ColorOfPixel type : ColorOfPixel.values()) {
                intToTypeMap.put(type.value, type);
            }
        }

        public static ColorOfPixel fromInt(int i) {
            ColorOfPixel type = intToTypeMap.get(Integer.valueOf(i));
            if (type == null)
                return ColorOfPixel.colorWhite;
            return type;
        }

        public int getValue() {
            return value;
        }
    }
    private final String path;
    private final Configuration configuration;
    public FieldImporter(String filepath, Configuration config){
        this.configuration = config;
        this.path = filepath;
    }

    public Field getField(){

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(this.path));
        } catch (IOException e) {
        }
        int width = img.getWidth();
        int height = img.getHeight();
        Field returnField = new Field(width, height);

        for(int x = 0; x < width; x++){

            for(int y  = 0; y < height; y++){
                int pixelColor = img.getRGB(x, y);
                switch (ColorOfPixel.fromInt(pixelColor)){
                    case colorBlack:
                        break;
                    case colorBlue:
                        returnField.setTarget(Location.of(x, y));
                        break;
                    case colorWhite:
                        returnField.addLocation(Location.of(x, y));
                        break;
                    case colorGreen:
                        Location loc = Location.of(x, y);
                        returnField.addLocation(loc);
                        returnField.putPerson(new Person(configuration.getVelocity()), loc);
                        break;
                    default:
                        break;
                }
            }
        }



        return returnField;
    }

}
