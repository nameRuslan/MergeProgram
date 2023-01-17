import repository.MergeRepository;
import services.LogService;
import services.MergeService;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        long programStart = System.currentTimeMillis();

        LogService.log().info("Логирование: " + LogService.log().getName() +
                "\nАргументы запущенной сортировки: " + Arrays.toString(args));

        MergeRepository mergeRepository = MergeRepository.fromArgs(args);
        //слияние
        MergeService mergeService = new MergeService(mergeRepository);
        mergeService.writeMergedFiles(mergeRepository.getOutputFileName());

        LogService.log().info("Время выполнения программы (сек): " + ((System.currentTimeMillis() - programStart) / 1000));
    }
}
