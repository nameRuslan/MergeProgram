package scanners;

import services.ReverseInputStreamService;

import java.io.*;
import java.math.BigInteger;

import static services.LogService.log;


//Данный класс работает со сканнерами
public class FileScanner {

    private BufferedReader bufferedReader;
    private String currentValue;
    public String source;

    public FileScanner(File source, boolean descendingSortOrder) throws Exception {
        this.source = source.toString();
        if (descendingSortOrder) {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new ReverseInputStreamService(source)));
        } else {
            this.bufferedReader = new BufferedReader(new FileReader(source));
        }
        scanNextValue();
    }
    public FileScanner() {}
    public String getValue() {
        return currentValue;
    }
    public void setValue(String currentValue) {
        this.currentValue = currentValue;
    }
    private void scanNextValue() {

        try {
            currentValue = (bufferedReader.readLine());
            if (currentValue != null) currentValue = currentValue.trim();
        } catch (IOException e) {
            log().warning(e.getMessage());
        }
    }

    public boolean hasNext() {
        return (currentValue != null);
    }

    public String getValueAndReadNext() {
        String scannedValue = currentValue;
        scanNextValue();

        return scannedValue;
    }

    //Сравнение для значений(Value) int и String
    public int compareTo(FileScanner other) {
        String thisValue = this.getValue();
        String otherValue = other.getValue();
        if (thisValue.matches("\\d+") && otherValue.matches("\\d+")) {
            BigInteger bigIntegerThisValue = new BigInteger(thisValue);
            BigInteger bigIntegerOtherValue = new BigInteger(otherValue);
            return bigIntegerThisValue.compareTo(bigIntegerOtherValue);
        } else {
            return thisValue.compareTo(otherValue);
        }
    }
}