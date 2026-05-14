public class DeathCauseStatistic {
    private String kod;
    private int[] cases;

    public DeathCauseStatistic(String kod, int[] cases){
        this.kod = kod;
        this.cases = cases;
    }
    public static DeathCauseStatistic fromCsvLine(String line) {
        String[] parts = line.split(",");
        //A02.1          ,5,-,-,-,-,-,-,-,-,-,-,-,-,1,2,-,1,1,-,-,-
        String code= parts[0].trim().replace("\t", "");//убираем таб к чертям собачьим
        String[] part = code.split(".");
        int[] deaths = new int[parts.length - 2];
        for(int i = 2; i< parts.length; i++){
            String value = parts[0].trim();
            if("-".equals(value) || value.isEmpty()){
                deaths[i-2] = 0;
            } else {
                deaths[i-2] = Integer.parseInt(value);
            }
        }
        return new DeathCauseStatistic(code, deaths);
    }

    public String getKod() {
        return kod;
    }

    public class AgeBracketDeaths{
        public final int young;
        public final int old;
        public final int deathCount;
        public AgeBracketDeaths(int young, int old, int deathCount){
            this.young = young;
            this.old = old;
            this.deathCount = deathCount;
        }
    }

    public AgeBracketDeaths ageGroup(int age){
        int young = age/10 * 10;
        int old = young + 9;
        int index = age/10;
        if(index < 0 || index > cases.length){
            return new AgeBracketDeaths(young, old, 0);
        }
        return new AgeBracketDeaths(young, old, cases[index]);
    }
}
