import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

public class Main {

    private static ProductParser parser = new ProductParser();

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) {
        hurtLocker();
    }

    public static void hurtLocker(){
        StringBuilder builder = new StringBuilder();
        buildOutput(convertToMap(products()), builder);
        System.out.println(builder.toString());
        myOutputTxt(builder.toString());
    }

    public static ArrayList<Product> products(){
        String[] lines = parser.lines();
        ArrayList<Product> productsList = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            Product tempProduct = parser.parseLine(lines[i]);
            if (tempProduct != null)
                productsList.add(tempProduct);
        }
        return productsList;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, Integer>> convertToMap(ArrayList<Product> productsList){
        LinkedHashMap<String, LinkedHashMap<String, Integer>> productMap = new LinkedHashMap<>();
        for (Product p : productsList) {
            if (!productMap.containsKey(p.getName())) {
                addNewKey(productMap, p);
            } else {
                addToExistingKey(productMap, p);
            }
        }
        return productMap;
    }

    public static void addNewKey(LinkedHashMap<String, LinkedHashMap<String, Integer>> productMap, Product p){
        LinkedHashMap<String, Integer> prices = new LinkedHashMap<>();
        prices.put(p.getPrice(), 1);
        productMap.put(p.getName(), prices);
    }

    public static void addToExistingKey(LinkedHashMap<String, LinkedHashMap<String, Integer>> productMap, Product p){
        LinkedHashMap<String, Integer> prices = productMap.get(p.getName());
        prices.put(p.getPrice(), prices.getOrDefault(p.getPrice(), 0) + 1);
    }

    public static void buildOutput(LinkedHashMap<String, LinkedHashMap<String, Integer>> productMap, StringBuilder builder){
        for (String name : productMap.keySet()) {
            LinkedHashMap<String, Integer> priceMap = productMap.get(name);
            appendNameAndSum(priceMap, name, builder);
            buildPriceOutput(priceMap, builder);
            builder.append("\n");
        }
        builder.append(String.format("Errors \t\t\t\t seen: %d times", parser.getErrorCount()));
    }

    public static void appendNameAndSum(LinkedHashMap<String, Integer> priceMap, String name, StringBuilder builder){
        Integer sum = priceMap.values().stream().reduce(0, Integer::sum);
        builder.append(String.format("name:%8s \t\t seen: %d times\n", name, sum));
        builder.append(String.format("%s \t"+" "+"\t %s\n", equalBreak(), equalBreak()));
    }

    public static void buildPriceOutput(LinkedHashMap<String, Integer> priceMap, StringBuilder builder){
        int dashCount = 0;
        for(String price : priceMap.keySet()){
            int sumPrice = priceMap.get(price);
            if (sumPrice > 1)
                builder.append(String.format("Price: \t %4s\t\t seen: %d times\n", price, sumPrice));
            else
                builder.append(String.format("Price:   %4s\t\t seen: %d time\n", price, sumPrice));
            dashCount = appendDashAndCheckCount(builder, dashCount);
        }
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

    public static int appendDashAndCheckCount(StringBuilder builder, int dashCount){
        if (dashCount < 1) {
            builder.append(String.format("%s\t\t %s\n", dashBreak(), dashBreak()));
            dashCount++;
        }
        return dashCount;
    }

    public static void myOutputTxt(String output){
        try (PrintStream out = new PrintStream(new FileOutputStream("MyOutput.txt"))) {
            out.print(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
