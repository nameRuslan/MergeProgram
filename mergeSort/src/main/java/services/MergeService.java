package services;

import scanners.FileScanner;
import repository.MergeRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
import java.util.logging.Level;
import java.nio.charset.StandardCharsets;

import static services.LogService.log;

//Сервис по слиянию входящих и отсортированных файлов
public class MergeService {

        private ArrayList<FileScanner> bufferedReaders = new ArrayList<>();
        private final boolean descendingMerge;
        private final List<String> inputFileNames;
        private boolean dataTypeInt;


        public MergeService(MergeRepository mergeRepository) {
            this.descendingMerge = mergeRepository.isDescendingMerge();
            this.inputFileNames = mergeRepository.getInputFileNames();
            this.dataTypeInt = mergeRepository.isDataTypeInt();
        }

    //запись выходного файла
    public void writeMergedFiles(String outputFileName) {
            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName),
                        StandardCharsets.UTF_8));
                mergeData(writer);
                writer.close();
            } catch(Exception e) {
                log().warning(e.getMessage());
                e.printStackTrace();
            }
        }

        //определяем метод сортировки и возвращаем сканнер с бОльшим или меньшим значением
        //сортируем в зависимости от значения descendingMerge
        private FileScanner getReaderWithHighestOrLowestValue() {
            if (bufferedReaders.size() == 0) return null;

            FileScanner readerWithHighestOrLowestValue = bufferedReaders.get(0);
            for (FileScanner reader : bufferedReaders) {

                if ((readerWithHighestOrLowestValue.compareTo(reader) >= 0) && !descendingMerge)
                    readerWithHighestOrLowestValue = reader;
                if ((readerWithHighestOrLowestValue.compareTo(reader) <= 0) && descendingMerge)
                    readerWithHighestOrLowestValue = reader;

            }
            return readerWithHighestOrLowestValue;
        }

        //Обработка ошибки если файл пуст или его нет
        private void makeAListOfReaders(List<String> inputFileNames) {
            for (String filename : inputFileNames) {
                File sourceFileName = new File(filename);
                if (sourceFileName.length() > 0) {
                    try {
                        bufferedReaders.add(new FileScanner((sourceFileName), descendingMerge));
                    } catch (Exception e) {
                        e.printStackTrace();
                        log().severe(e.getMessage());
                    }
                } else {
                    log().warning("Ошибка открытия входящего файла : \"" +
                            sourceFileName.getName() + "\" : файл пуст или не существует");
                }
            }
        }

        private void removeFromReaderLists(FileScanner readerToRemove) {
            bufferedReaders.remove(readerToRemove);
        }

        //отслеживаем когда файл пустой  (readerLastValue)
        //проходим по сканнерам и пишем max/min значения
        private void mergeData(Writer writer) throws IOException {
            makeAListOfReaders(inputFileNames);
            FileScanner readerWithGoalValue = getReaderWithHighestOrLowestValue();
            FileScanner readerLastValue = new FileScanner();
            readerLastValue.setValue(Objects.requireNonNull
                    (getReaderWithHighestOrLowestValue()).getValue());
            while(readerWithGoalValue != null) {
                if((readerWithGoalValue.hasNext())) {
                    if (isInputValueIncorrect(readerWithGoalValue, readerLastValue)) {
                        skipReaderValueAndLogIt(readerWithGoalValue);
                    } else {
                        readerLastValue.setValue(readerWithGoalValue.getValue());
                        writer.write(readerWithGoalValue.getValueAndReadNext() + "\n");
                    }
                    if (readerWithGoalValue.hasNext()) {
                        readerWithGoalValue = getReaderWithHighestOrLowestValue();
                    }
                } else {
                    removeFromReaderLists(readerWithGoalValue);
                    readerWithGoalValue = getReaderWithHighestOrLowestValue();
                }
            }
        }

        //Если считанное значение либо из неотсортированного списка, либо имеет пробелы (пункт задания про пробелы)
        //либо программа, запущенная с аргументом -i(для сортировки чисел) получает из входного файла строки
        private boolean isInputValueIncorrect(FileScanner readerWithGoalValue,
                                              FileScanner previouslyAddedReader) {
            return readerWithGoalValue.getValue().matches("^(.*)(\\s+)(.*)$") ||
                    (isInputFileUnsorted(descendingMerge, previouslyAddedReader, readerWithGoalValue)) ||
                    ((readerWithGoalValue.getValue().matches("\\D+")) && (dataTypeInt));
        }

        //Не записываем значение сканнера, выводим информацию в лог и сканируем следующее значение.
        private void skipReaderValueAndLogIt(FileScanner readerWithHighestOrLowestValue) {
            log().log(Level.WARNING, "Символ(число) \"" + readerWithHighestOrLowestValue.getValue() + "\" в файле " +
                    readerWithHighestOrLowestValue.source + " был пропущен(игнорирова) т.к. невалидный и записан в log файл");
            readerWithHighestOrLowestValue.getValueAndReadNext();
        }

        //Нарушен ли порядок сортировки во входном файле
        private boolean isInputFileUnsorted(boolean descendingSortOrder, FileScanner previouslyAddedReader,
                                            FileScanner readerWithGoalValue) {
            if (descendingSortOrder && previouslyAddedReader.compareTo(readerWithGoalValue) < 0) {
                return true;
            }
            return !descendingSortOrder && previouslyAddedReader.compareTo(readerWithGoalValue) > 0;
        }
    }


