import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.*;

public class Main {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) {
        ProductParser parser = new ProductParser();
        String[] lines = parser.lines();
        ArrayList<Product> productsList = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            Product tempProduct = parser.parseLine(lines[i]);
            if (tempProduct != null)
                productsList.add(tempProduct);
        }
        LinkedHashMap<String, TreeMap<String, Integer>> productMap = new LinkedHashMap<>();
        for (Product p : productsList) {
            if (!productMap.containsKey(p.getName())) {
                TreeMap<String, Integer> tree = new TreeMap<>();
                tree.put(p.getPrice(), 1);
                productMap.put(p.getName(), tree);
            } else {
                TreeMap<String, Integer> tree = productMap.get(p.getName());
                if (tree.containsKey(p.getPrice()))
                    tree.put(p.getPrice(), tree.get(p.getPrice()) + 1);
                else
                    tree.put(p.getPrice(), 1);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String name : productMap.keySet()) {
            TreeMap<String, Integer> priceMap = productMap.get(name);
            int sum = 0;
            for(Map.Entry<String, Integer> entry : priceMap.entrySet()){
                sum += entry.getValue();
            }
            builder.append(String.format("name:%8s %6s seen: %d times\n", name, " ", sum));
            builder.append(String.format("%s %6s %s\n", equalBreak(), " ", equalBreak()));
            for(String price : priceMap.keySet()){
                int sumPrice = 0;
                for(Map.Entry<String, Integer> entry : priceMap.entrySet()){
                    sumPrice += entry.getValue();
                }
                if (sumPrice > 1)
                    builder.append(String.format("Price:%7s %6s seen: %d times\n", price, " ", sumPrice));
                else
                    builder.append(String.format("Price:%7s %6s seen: %d time\n", price, " ", sumPrice));
                builder.append(String.format("%s %6s %s\n", dashBreak(), " ", dashBreak()));
            }
            builder.append("\n");
        }
        builder.append(String.format("Errors%15s seen: %d times", " ", parser.getErrorCount()));
        System.out.println(builder.toString());
    }

    public static String equalBreak(){
        StringBuilder equal = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            equal.append("=");
        }
        return equal.toString();
    }

    public static String dashBreak(){
        StringBuilder dash = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            dash.append("-");
        }
        return dash.toString();
    }
}
