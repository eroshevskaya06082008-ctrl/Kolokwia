import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ICDCodeTabularOptimizedForMemory implements ICDCodeTabular{

    public String path;

    public ICDCodeTabularOptimizedForMemory(String path){
        this.path = path;
    }


    @Override
    public String getDescription(String code) throws IndexOutOfBoundsException {
        try{
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line;
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
                String currentCode = parts[0];
                if(currentCode.equals(code)){
                    bf.close();
                    return parts[1];
                }
            }
            bf.close();
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono kodu: " + code);
        } catch (IOException e) {
            System.err.println("Blad pliku");
        }
        throw new IndexOutOfBoundsException();
    }
}
