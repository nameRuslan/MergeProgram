package services;

import java.io.*;



public class ReverseInputStreamService extends InputStream {

    private RandomAccessFile in;

    private long currentLineStart = -1;
    private long currentLineEnd = -1;
    private long currentPos = -1;
    private long lastPosInFile = -1;
    private int lastChar = -1;

    public ReverseInputStreamService(File file) throws FileNotFoundException {
        in = new RandomAccessFile(file, "r");
        currentLineStart = file.length();
        currentLineEnd = file.length();
        lastPosInFile = file.length() - 1;
        currentPos = currentLineEnd;
    }

    private void findPrevLine() throws IOException {
        if (lastChar == -1) {
            in.seek(lastPosInFile);
            lastChar = in.readByte();
        }
        currentLineEnd = currentLineStart;

        //Больше нет строк, так как мы находимся в начале файла
        if (currentLineEnd == 0) {
            currentLineEnd = -1;
            currentLineStart = -1;
            currentPos = -1;
            return;
        }

        long filePointer = currentLineStart -1;

        while (true) {
            filePointer--;

            // Начало файла
            if (filePointer < 0) {
                break;
            }

            in.seek(filePointer);
            int readByte = in.readByte();

            //Игнорируем last newLine в файле
            if (readByte == 0xA && filePointer != lastPosInFile ) {
                break;
            }
        }
        //для пропуска newLine стартуем с позиции filePointer + 1
        currentLineStart = filePointer + 1;
        currentPos = currentLineStart;
    }

    public int read() throws IOException {

        if (currentPos < currentLineEnd ) {
            in.seek(currentPos++);
            return in.readByte();
        } else if (currentPos > lastPosInFile && currentLineStart < currentLineEnd) {
            // последняя строка в файле
            findPrevLine();
            if (lastChar != '\n' && lastChar != '\r') {
                // добавляем newLine
                return '\n';
            } else {
                return read();
            }
        } else if (currentPos < 0) {
            return -1;
        } else {
            findPrevLine();
            return read();
        }
    }
}