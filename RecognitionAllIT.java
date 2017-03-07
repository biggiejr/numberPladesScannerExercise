package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

/**
 *
 * @author Mato
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private File input;
    private String expectedResult;

    Intelligence intelligence;

    @Before
    public void initialize() {
        try {
            intelligence = new Intelligence();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public RecognitionAllIT(File input, String expectedResult) {
        this.input = input;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testingData() {
        String images = "src/test/resources/snapshots";
        String results = "src/test/resources/results.properties";
        InputStream resultsStream = null;
        try {
            resultsStream = new FileInputStream(new File(results));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties properties = new Properties();

        try {
            properties.load(resultsStream);
        } catch (IOException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            resultsStream.close();
        } catch (IOException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        }

        File snapshotDir = new File(images);
        File[] snaps = snapshotDir.listFiles();

        Collection<Object[]> imageData = new ArrayList();
        for (File file : snaps) {
            String name = file.getName();
            String plateNo = properties.getProperty(name);
            imageData.add(new Object[]{file, plateNo});
        }
        return imageData;

    }

    @Test
    public void tester() {
        try {
            String plate = expectedResult;
            CarSnapshot carSnapshot = new CarSnapshot(new FileInputStream(input));
            String actualResult = intelligence.recognize(carSnapshot, false);
            System.out.println("spz...............==...............   " + actualResult);
            assertThat(actualResult, is(equalTo(plate)));
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecognitionAllIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
