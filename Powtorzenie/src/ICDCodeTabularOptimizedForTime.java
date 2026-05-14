import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ICDCodeTabularOptimizedForTime implements ICDCodeTabular{
    public HashMap<String, String> diseases;

    public ICDCodeTabularOptimizedForTime(String path) throws IOException {
        this.diseases = new HashMap<>();
        BufferedReader bf = new BufferedReader(new FileReader(path));
        String line;
        bf.readLine();
        bf.readLine();
        int lineNum = 0;
        while((line = bf.readLine()) != null){
            lineNum++;
            if(lineNum < 88){
                continue;
            }
            line = line.trim();
            if(line.isEmpty()){
                continue;
            }
            String[] parts = line.split(" ", 2);
            if(parts.length < 2){
                continue;
            }
            String code = parts[0];
            if(!code.matches("[A-Z][0-9][0-9].*")) {
                continue;
            }
            String description = parts[1];
            diseases.put(code, description);
        }
        bf.close();
    }


    @Override
    public String getDescription(String code) {
        if(!diseases.containsKey(code)){
            throw new IndexOutOfBoundsException("No such code" + code);
        }
        return diseases.get(code);
    }
}
