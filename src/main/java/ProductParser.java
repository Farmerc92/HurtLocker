import java.util.regex.Pattern;

public class ProductParser {

    private int errorCount = 0;

    public Product parseLine(String line) {
        try {
            String[] product = keyValuesSeparated(line);
            String name = product[1];
            String price = product[3];
            String type = product[5];
            String expiration = product[7];
            name = patternMatcherName(name);
            price = exceptionCheckPrice(price);
            type = exceptionCheckType(type);
            expiration = exceptionCheckExpiration(expiration);
            return new Product(name, price, type, expiration);
        }catch(IllegalArgumentException e){
            errorCount++;
        }
        return null;
    }

    public String[] lines(){
        try {
            return new Main().readRawDataToString().split("##");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public String[] keyValuesSeparated(String line){
        return line.split("[^a-zA-Z0-9/.]");
    }

    public String patternMatcherName(String name) throws IllegalArgumentException{
        if (Pattern.matches("[Mm][Ii][Ll][Kk]", name))
            return "Milk";
        else if (Pattern.matches("[Bb][Rr][Ee][Aa][Dd]", name))
            return "Bread";
        else if (Pattern.matches("[Cc][0Oo][0Oo][Kk][Ii][Ee][Ss]", name))
            return "Cookies";
        else if (Pattern.matches("[Aa][Pp][Pp][Ll][Ee][Ss]", name))
            return "Apples";
        else
            throw new IllegalArgumentException();
    }

    public String exceptionCheckPrice(String price) throws IllegalArgumentException{
        String pricePattern = "[0-9]+[.][0-9]+";
        if (!Pattern.matches(pricePattern, price)){
            throw new IllegalArgumentException();
        }
        return price;
    }

    public String exceptionCheckType(String type) throws IllegalArgumentException{
        String typePattern = "[Ff][Oo][Oo][Dd]";
        if (!Pattern.matches(typePattern, type)){
            throw new IllegalArgumentException();
        }
        return type;
    }

    public String exceptionCheckExpiration(String expiration) throws IllegalArgumentException{
        String expirationPattern = "[0-9]+[/][0-9]+[/][0-9]+";
        if (!Pattern.matches(expirationPattern, expiration)){
            throw new IllegalArgumentException();
        }
        return expiration;
    }

    public int getErrorCount() {
        return errorCount;
    }
}
