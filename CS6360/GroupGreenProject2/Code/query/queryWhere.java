package query;

import java.util.ArrayList;
import java.util.Map;

public class queryWhere {
    public static void main( ArrayList<String> colList, Map<Integer, ArrayList<Object>> dataMap, String whereString) {
        String whereCondition = whereString.trim().replace(" ", "");
        // the kind of operator
        String operator;
        String[] conditionArr;
        int leftKeyIndex = 0;
        boolean belongTo = false;
        if(whereCondition.contains("=")){
            operator = "=";
            conditionArr = whereCondition.split("=");
        } else if (whereCondition.contains(">")){
            operator = ">";
            conditionArr = whereCondition.split(">=");
        } else if (whereCondition.contains(">=")){
            operator = ">=";
            conditionArr = whereCondition.split(">");
        } else if (whereCondition.contains("<")){
            operator = "<";
            conditionArr = whereCondition.split("<");
        } else if (whereCondition.contains("<=")){
            operator = "<=";
            conditionArr = whereCondition.split("<=");
        } else{
            System.out.println("WHERE condition is unsupported or the grammar is wrong.");
            System.out.println("Only support =,<,> three operators");
            colList.clear();
            dataMap.clear();
            return;
        }
        // if left key doesn't belong to colList
        for(String colName:colList){
            if(conditionArr[0].equalsIgnoreCase(colName)) {
                leftKeyIndex = colList.indexOf(colName);
                belongTo = true;
            }
        }
        if(!belongTo){
            System.out.println("WHERE condition grammar is wrong. Please make sure put attribute name to the left side of operator.");
            colList.clear();
            dataMap.clear();
            return;
        }
        // now we do the operation
        if (operator.equalsIgnoreCase("=")) {
            for(int n=1; n<= dataMap.keySet().size(); n++) {
                if (!(dataMap.get(n).get(leftKeyIndex).toString().trim().equalsIgnoreCase(conditionArr[1].trim())))
                    dataMap.remove(n);
            }
        } else {
            // to do comparison, the left and right must be a NUM!
            if (isNumeric(conditionArr[1])) {
                Double rightDouble = Double.parseDouble(conditionArr[1]);
                Double leftDouble;
                for (int n = 1; n <= dataMap.keySet().size(); n++) {
                    if (isNumeric(dataMap.get(n).get(leftKeyIndex).toString())) {
                        // we cannot do comparison except both of them are numbers
                        leftDouble = Double.parseDouble(dataMap.get(n).get(leftKeyIndex).toString());
                        if (operator.equalsIgnoreCase(">")) {
                            if (!(leftDouble > rightDouble))
                                dataMap.remove(n);
                        } else if (operator.equalsIgnoreCase(">=")) {
                            if (!(leftDouble >= rightDouble))
                                dataMap.remove(n);
                        } else if (operator.equalsIgnoreCase("<")) {
                            if (!(leftDouble < rightDouble))
                                dataMap.remove(n);
//                }else if (operator.equalsIgnoreCase("<=")){
                        } else {
                            if (!(leftDouble <= rightDouble))
                                dataMap.remove(n);
                        }

                    }
                }
            } else{
                // right is not a num, cannot operate comparison
                System.out.println("WHERE condition is unsupported or the grammar is wrong.");
                colList.clear();
                dataMap.clear();
                return;
            }
        }
    }




    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
//        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }


}
