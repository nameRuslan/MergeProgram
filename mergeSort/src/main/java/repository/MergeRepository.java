package repository;

import java.util.ArrayList;
import java.util.List;

import static services.LogService.log;


//DAO и по сути DTO, но пока не знаю имеет ли смысл создавать DTO
public class MergeRepository {

        //информация о вход.файлах
        private final List<String> inputFileNames;
        private final String outputFileName;
        //если true, то сортировка по убыванию
        private final boolean descendingMerge;
        //если false, то тип данных String
        private final boolean dataTypeInt;


        private MergeRepository(boolean descendingMerge, boolean dataTypeInt, List<String> inputFileNames,
                                String outputFileName) {

            this.descendingMerge = descendingMerge;
            this.dataTypeInt = dataTypeInt;
            this.inputFileNames = inputFileNames;
            this.outputFileName = outputFileName;
        }

    public static MergeRepository fromArgs(String[] args) {
            if (args.length < 3) {
                log().severe("Аргументы должны содержать следующую информацию: \"-i, output.txt, input.txt\"");
                System.exit(1);
            }

            //для сортировки в обратном порядке
            boolean descendingMerge;
            descendingMerge = args[0].equals("-d");

            //Проверка на содержания агрумента -a или -d:
            //Если аргумента нет, то на его место встанет следующий аргумент в индекс 0
            int IndexOfARGS = args[0].matches("^-[a|d]$") ? 1 : 0;
            boolean dataTypeInt = false;
            String outputFileLinks = "";

            if (args[IndexOfARGS].matches("^-[i|s]$")) {
                dataTypeInt = (args[IndexOfARGS].equals("-i"));
                IndexOfARGS++;

                outputFileLinks = args[IndexOfARGS];
                IndexOfARGS++;
            } else {
                log().severe("Содержится некорректный аргумент: " + args[IndexOfARGS] + " введите \"-i or -s\"");
                System.exit(1);
            }

            List<String> inputFileNames = new ArrayList<>();
            //тут значение индексу не задаем, иначе первые аргументы будут восприниматься как входящией файлы
            for (; IndexOfARGS < args.length; IndexOfARGS++) {
                inputFileNames.add(args[IndexOfARGS]);
            }

            return new MergeRepository(descendingMerge, dataTypeInt, inputFileNames, outputFileLinks);
        }


        public List<String> getInputFileNames() { return inputFileNames; }

        public String getOutputFileName() {
            return outputFileName;
        }

        public boolean isDescendingMerge() {
        return descendingMerge;
    }

        public boolean isDataTypeInt() {
        return dataTypeInt;
    }
}
