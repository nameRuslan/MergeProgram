package services;

import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;


//нужно переделать через зависимость -> меньше кода, а читаемость лучше
public class LogService {

        public static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        private static final String logFile = "Log.txt";
        private static FileHandler fileHandler;


        static {
            try {
                fileHandler = new FileHandler(logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
        }
        public static Logger log() {
            return log;
        }

        private LogService() {
        }


    }



