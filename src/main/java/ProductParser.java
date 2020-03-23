import java.util.regex.Pattern;

public class ProductParser {

    private int errorCount = 0;

    public Product parseLine(String line) {
        int check = errorCount;
        String[] product = keyValuesSeparated(line);
        String name = product[1];
        String price = product[3];
        String type = product[5];
        String expiration = product[7];
        name = patternMatcherName(name);
        price = exceptionCheckPrice(price);
        type = exceptionCheckType(type);
        expiration = exceptionCheckExpiration(expiration);
        if (errorCount > check){
            return null;
        }
        return new Product(name, price, type, expiration);
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

    public String patternMatcherName(String name){
        //ignore caveman regexs
        String milk = "[Mm][Ii][Ll][Kk]";
        String bread = "[Bb][Rr][Ee][Aa][Dd]";
        String cookies = "[Cc][0Oo][0Oo][Kk][Ii][Ee][Ss]";
        String apples = "[Aa][Pp][Pp][Ll][Ee][Ss]";

        if (Pattern.matches(milk, name))
            return "Milk";
        else if (Pattern.matches(bread, name))
            return "Bread";
        else if (Pattern.matches(cookies, name))
            return "Cookies";
        else if (Pattern.matches(apples, name))
            return "Apples";
        else{
            errorCount++;
        }
        return name;
    }

    public String exceptionCheckPrice(String price){
        String pricePattern = "[0-9]+[.][0-9]+";
        if (!Pattern.matches(pricePattern, price)){
            errorCount++;
        }
        return price;
    }

    public String exceptionCheckType(String type){
        String typePattern = "[Ff][Oo][Oo][Dd]";
        if (!Pattern.matches(typePattern, type)){
            errorCount++;
        }
        return type;
    }

    public String exceptionCheckExpiration(String expiration){
        String expirationPattern = "[0-9]+[/][0-9]+[/][0-9]+";
        if (!Pattern.matches(expirationPattern, expiration)){
            errorCount++;
        }
        return expiration;
    }

    public int getErrorCount() {
        return errorCount;
    }
}
