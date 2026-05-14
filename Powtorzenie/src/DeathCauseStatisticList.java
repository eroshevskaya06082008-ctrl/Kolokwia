import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeathCauseStatisticList {
    public List<DeathCauseStatistic> statistics;
    public DeathCauseStatisticList() {
        this.statistics = new ArrayList<>();
    }
    public void repopulate(String path){
        statistics.clear();
        try(BufferedReader bf = new BufferedReader(new FileReader(path))){
            String line;
            bf.readLine();
            bf.readLine();
            while((line = bf.readLine()) != null){
                DeathCauseStatistic p = DeathCauseStatistic.fromCsvLine(line);
                statistics.add(p);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DeathCauseStatistic> mostDeadlyDiseases(int age, int n){
//        List<DeathCauseStatistic> copy = new ArrayList<>(statistics);
//        List<DeathCauseStatistic> result = new ArrayList<>();
//        for(int i = 0; i< n; i++){
//            DeathCauseStatistic max = copy.get(0);
//            for(DeathCauseStatistic stat : copy){
//                if(stat.ageGroup(age).deathCount > max.ageGroup(age).deathCount){
//                    max =stat;
//                }
//            }
//
//        }

        List<DeathCauseStatistic> copy = new ArrayList<>(statistics);
        for(int  i = 0; i< copy.size(); i++){
            for(int j = i+ 1; j < copy.size(); j++){
                int deaths1 = copy.get(i).ageGroup(age).deathCount;
                int deaths2 = copy.get(j).ageGroup(age).deathCount;

                if(deaths2 > deaths1){
                    DeathCauseStatistic x = copy.get(i);
                    copy.set(i, copy.get(j));
                    copy.set(j, x);

                }
            }
        }
        List<DeathCauseStatistic> result = new ArrayList<>();
        for(int k = 0; k< n; k++){
            result.add(copy.get(k));
        }
        return result;
    }
}
