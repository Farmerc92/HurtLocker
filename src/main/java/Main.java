import org.apache.commons.io.IOUtils;

import java.io.*;
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
        LinkedHashMap<String, LinkedHashMap<String, Integer>> productMap = new LinkedHashMap<>();
        for (Product p : productsList) {
            if (!productMap.containsKey(p.getName())) {
                LinkedHashMap<String, Integer> prices = new LinkedHashMap<>();
                prices.put(p.getPrice(), 1);
                productMap.put(p.getName(), prices);
            } else {
                LinkedHashMap<String, Integer> prices = productMap.get(p.getName());
                if (prices.containsKey(p.getPrice()))
                    prices.put(p.getPrice(), prices.get(p.getPrice()) + 1);
                else
                    prices.put(p.getPrice(), 1);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String name : productMap.keySet()) {
            LinkedHashMap<String, Integer> priceMap = productMap.get(name);
            int sum = 0;
            for(Map.Entry<String, Integer> entry : priceMap.entrySet()){
                sum += entry.getValue();
            }
            builder.append(String.format("name:%8s \t\t seen: %d times\n", name, sum));
            builder.append(String.format("%s \t"+" "+"\t %s\n", equalBreak(), equalBreak()));
            int dashCount = 0;
            for(String price : priceMap.keySet()){
                int sumPrice = priceMap.get(price);
                if (sumPrice > 1)
                    builder.append(String.format("Price: \t %4s\t\t seen: %d times\n", price, sumPrice));
                else
                    builder.append(String.format("Price:   %4s\t\t seen: %d time\n", price, sumPrice));
                if (dashCount < 1) {
                    builder.append(String.format("%s\t\t %s\n", dashBreak(), dashBreak()));
                    dashCount++;
                }
            }
            builder.append("\n");
        }
        builder.append(String.format("Errors \t\t\t\t seen: %d times", parser.getErrorCount()));
        System.out.println(builder.toString());
        myOutputTxt(builder.toString());
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

    public static void myOutputTxt(String output){
        try (PrintStream out = new PrintStream(new FileOutputStream("MyOutput.txt"))) {
            out.print(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
