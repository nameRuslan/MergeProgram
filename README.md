# MergeProgram
Task from Shift



Инструкция по запуску: 
Для запуска программы необходимо передать минимум 3 аргумента: 
- Тип данных
- Наименования выходного файла(результат слияния). Выходной файл только один.
- Имя(имена) входящих файлов (минимум один)

Параметры программы задаются при запуске через аргументы командной строки, по порядку:
1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию;
2. тип данных (-s или -i), обязательный;
3. имя выходного файла, обязательное;
4. остальные параметры – имена входных файлов, не менее одного.


Примеры по указанию аргументов:
sort-it.exe -i -a out.txt in.txt (для целых чисел по возрастанию)
sort-it.exe -s out.txt in1.txt in2.txt in3.txt (для строк по возрастанию)
sort-it.exe -d -s out.txt in1.txt in2.txt (для строк по убыванию)

Java : SDK 18.0.2

Использовал систему сборки Maven 4.0.0 (для возможного масштабирования и удобства внедерния зависимостей)

Добавлены зависимоти JUNIT:


<dependency>
        
    <dependency>
        
        <groupId>junit</groupId>
        
        <artifactId>junit</artifactId>
        
        <version>4.13.2</version>
        
        <scope>test</scope>
        
    </dependency>
  
    <dependency>
    
        <groupId>org.junit.jupiter</groupId>
        
        <artifactId>junit-jupiter-params</artifactId>
        
        <version>5.9.2</version>
        
        <scope>test</scope>
        
    </dependency>


Логика работы программы:
Входные файлы содержат данные одного из двух видов: целые числа или строки. Данные записаны
в столбик (каждая строка файла – новый элемент). Строки могут содержать любые не пробельные
символы, строки с пробелами считаются ошибочными. Также файлы должны быть предварительно
отсортированы.
Результатом работы программы является новый файл с объединенным содержимым
входных файлов(имя файлов задается в аргументах), отсортированным по возрастанию или убыванию путем сортировки слиянием.


Принцип работы:

1. main передаёт аргументы в MergeRepository где вызывается метод fromArgs и тут же присваивается значения полям которые
отвечают за информацию о типе сортировки(по возрастанию или убыванию), вид данных, путь к входящему и исходящим файлам.

2. main передает экземпляр класса MergeRepository в new MergeService, где он через метод makeAListOfReaders создает список экземпляров класса FileScanner (экземпляры имеют makeAListOfReaders для чтения данных из вход.файла).

3. Метод getReaderWithHighestOrLowestValue находит max/min значение currentValue по всем ридерам.

4. Для сортировки по возрастанию - читаем каждым bufferedReader и проверяем где currenValue min и записываем.  
Перед тем как произвести запись делаем проверку:
 - элемент это строка или число
 - Есть ли пробелы и больше ли он, чем ранее записанное в выходной файл значение (т.к. есть вероятность, что файлы не всегда отсортированы).

4.1 Для сортировки по убыванию, считываем файлы в обратном порядке(т.к. подразумевается, что файлы отсортированы по возрастанию) с помощью класса ReverseInputStream.

5. При возникновении ошибок, или элементы несоответствуют ТЗ (файл не отсортирован, присутствуют пробелы и т.д.) данные не будут записываться в выходной файл, а залогируются в log.txt.

P.S. 
- В Случае ошибок программа не падает. 
- Программа устойчива к большим файлам.
- Присутствуют тесты 
