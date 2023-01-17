import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import repository.MergeRepository;

import static org.junit.Assert.*;


public class MergeRepositoryTest {

        @ParameterizedTest
        @CsvSource({
                "-d, -i, test\\out.txt, test\\in1.txt, test\\in3.txt",
                "-d, -s, test\\out.txt, test\\in1.txt, test\\in3.txt",
        })
        void testFromArgsDescending(String a, String b, String c, String d, String e) {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {a, b, c, d, e});
            assertTrue(mergeRepository.isDescendingMerge());
        }

        @ParameterizedTest
        @CsvSource({
                "-a, -i, test\\out.txt, test\\in1.txt, test\\in3.txt",
                "-a, -s, test\\out.txt, test\\in1.txt, test\\in3.txt",
                "-i, test\\out.txt, test\\in1.txt, test\\in3.txt, test\\in3.txt",
                "-s, test\\out.txt, test\\in1.txt, test\\in3.txt, test\\in3.txt",
        })
        void testFromArgsAscending(String a, String b, String c, String d, String e) {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {a, b, c, d, e});
            Assertions.assertFalse(mergeRepository.isDescendingMerge());
        }

        @Test
        void testIfDescendingMerge() {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {"-i", "something", "something"});
            Assertions.assertFalse(mergeRepository.isDescendingMerge());

            mergeRepository = MergeRepository.fromArgs(new String[] {"-a", "something", "something"});
            Assertions.assertFalse(mergeRepository.isDescendingMerge());

            mergeRepository= MergeRepository.fromArgs(new String[] {"-d", "something", "something"});
            Assertions.assertTrue(mergeRepository.isDescendingMerge());
        }

        @Test
        void isDataTypeInt() {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {"-i", "something", "something"});
            Assertions.assertTrue(mergeRepository.isDataTypeInt());

            mergeRepository = MergeRepository.fromArgs(new String[] {"-s", "something", "something"});
            Assertions.assertFalse(mergeRepository.isDataTypeInt());

            mergeRepository = MergeRepository.fromArgs(new String[] {"-a", "-i", "something"});
            Assertions.assertTrue(mergeRepository.isDataTypeInt());

            mergeRepository = MergeRepository.fromArgs(new String[] {"-d", "-s", "something"});
            Assertions.assertFalse(mergeRepository.isDataTypeInt());
        }

        @ParameterizedTest
        @CsvSource({
                "-a, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-a, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-d, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-d, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
        })
        void getInputFileNamesWithFirstArgument(String a, String b, String c, String d, String e) {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {a, b, c, d, e});
            Assertions.assertEquals(2, mergeRepository.getInputFileNames().size());
        }

        @ParameterizedTest
        @CsvSource({
                "-i, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-s, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-i, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-s, test\\out.txt, test\\in1.txt, test\\in2.txt",
        })
        void getInputFileNamesWithoutFirstArgument(String a, String b, String c, String d) {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {a, b, c, d});
            Assertions.assertEquals(2, mergeRepository.getInputFileNames().size());
        }

        @ParameterizedTest
        @CsvSource({
                "-a, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-a, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-d, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
                "-d, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
        })
        void getOutputFileName(String a, String b, String c, String d, String e) {
            MergeRepository mergeRepository = MergeRepository.fromArgs(new String[] {a, b, c, d, e});
            Assertions.assertEquals("test\\out.txt", mergeRepository.getOutputFileName());
        }

    }
