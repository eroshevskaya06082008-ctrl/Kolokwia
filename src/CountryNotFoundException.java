public class CountryNotFoundException extends Exception {
    private final String countryName;
    public CountryNotFoundException(String countryName) {
        this.countryName = countryName;
    }
    public String getMessage(){
        return countryName;
    }
}
