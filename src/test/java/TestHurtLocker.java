import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestHurtLocker {

    @Test
    public void testCompare(){
        try {
            BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
            BufferedReader myOutputReader = new BufferedReader(new FileReader("MyOutput.txt"));
            String outputLine;
            String myOutputLine;
            while ((outputLine = outputReader.readLine()) != null && (myOutputLine = myOutputReader.readLine()) != null){
                Assert.assertEquals(outputLine, myOutputLine);
            }
            outputReader.close();
            myOutputReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
