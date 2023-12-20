package save;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Save implements SaveView{

    @Override
    public void saveToFile(Path path, String content) {
        try {
            if(!Files.exists(path)) Files.createFile(path);
            Files.writeString(path, content + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void delFromFile(Path path, String content){
        try{
            List<String> inStr = Files.readAllLines(path);
            List<String> outStr = new ArrayList<>();
            for (String string : inStr){
                if(!string.equals(content)) {
                    outStr.add(string);
                }
            }
            Files.write(path, outStr);
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}

