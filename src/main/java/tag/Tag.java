package tag;

public class Tag {

    /**
     * Проверка, что строка содержит тэг #clientsCount#
     * @param string входящая строка
     * @return boolean
     */
    public static boolean isClientsCount(String string){
        return string.contains("#clientsCount#");
    }

    /**
     * Проверка, что строка содержит тэг #name#
     * @param string входящая строка
     * @return boolean
     */
    public static boolean isName(String string){
        return string.contains("#name#");
    }

    /**
     * Проверка, что строка содержит тэг #exit#
     * @param string входящая строка
     * @return boolean
     */
    public static boolean isExit(String string){
        return string.contains("#exit#");
    }

    /**
     * Проверка, что строка содержит тэг #msg#
     * @param string входящая строка
     * @return boolean
     */
    public static boolean isMsg(String string){
        return string.contains("#msg#");
    }

    /**
     * Проверка, что строка содержит тэг #individualMsg#
     * @param string входящая строка
     * @return boolean
     */
    public static boolean isIndividualMsg(String string){
        return string.contains("#individualMsg#");
    }
}
