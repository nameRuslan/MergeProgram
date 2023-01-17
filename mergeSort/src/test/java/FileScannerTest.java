import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.MergeRepository;
import scanners.FileScanner;
import services.MergeService;

import static org.junit.Assert.*;

public class FileScannerTest {

        private FileScanner fileScanner;

        @BeforeEach
        void initEach() {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {"-a", "-i", "JUNIToutFILE.txt", "JUNITinFILE.txt"});
            MergeService mergeService = new MergeService(mergeRepository);
            mergeService.writeMergedFiles(mergeRepository.getOutputFileName());
            FileScanner fileScanner = new FileScanner();
            fileScanner.setValue("firstValue");
            this.fileScanner = fileScanner;
        }

        @Test
        void getValue() {
            Assertions.assertEquals("firstValue", fileScanner.getValue());
        }

        @Test
        void setValue() {
            fileScanner.setValue(null);
            Assertions.assertNull(fileScanner.getValue());
        }

        @Test
        void compareTo() {
            FileScanner someFileScanner = new FileScanner();
            someFileScanner.setValue("ZZZ");
            Assertions.assertTrue(fileScanner.compareTo(someFileScanner) > 0);
            Assertions.assertFalse(fileScanner.compareTo(someFileScanner) < 0);
            fileScanner.setValue("ZZZ");
            Assertions.assertEquals(0, fileScanner.compareTo(someFileScanner));
            fileScanner.setValue("278");
            someFileScanner.setValue("277");
            Assertions.assertTrue(fileScanner.compareTo(someFileScanner) > 0);
            Assertions.assertFalse(fileScanner.compareTo(someFileScanner) < 0);
            someFileScanner.setValue("278");
            Assertions.assertEquals(0, fileScanner.compareTo(someFileScanner));
            someFileScanner.setValue("12478");
            Assertions.assertFalse(fileScanner.compareTo(someFileScanner) > 0);

        }
    }





