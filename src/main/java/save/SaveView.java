package save;

import java.nio.file.Path;

public interface SaveView {

    /**
     * Добавление элемента
     * @param path путь к файлу
     * @param content добавляемый элемент
     */
    void saveToFile(Path path, String content);

    /**
     * Удаление элемента
     * @param path путь к файлу
     * @param content удаляемый элемент
     */
    void delFromFile(Path path, String content);
}
