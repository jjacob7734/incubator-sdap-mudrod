package org.apache.sdap.mudrod.weblog.partition;

import java.util.*;

public class KGreedyPartitionSolver implements ThePartitionProblemSolver {

  public boolean bsorted = false;

  public KGreedyPartitionSolver() {
    // default constructor
  }

  public KGreedyPartitionSolver(boolean bsorted) {
    this.bsorted = true;
  }

  @Override
  public Map<String, Integer> solve(Map<String, Double> labelNums, int k) {
    List<Double> lista = null;
    List<String> months = null;

    if (!this.bsorted) {
      LinkedHashMap<String, Double> sortedMap = (LinkedHashMap<String, Double>) this.sortMapByValue(labelNums);
      lista = new ArrayList<>(sortedMap.values());
      months = new ArrayList<>(sortedMap.keySet());
    } else {
      lista = new ArrayList<>(labelNums.values());
      months = new ArrayList<>(labelNums.keySet());
    }

    List<List<Double>> parts = new ArrayList<>();
    List<List<String>> splitMonths = new ArrayList<>();

    for (int i = 0; i < k; i++) {
      List<Double> part = new ArrayList<>();
      parts.add(part);

      List<String> monthList = new ArrayList<>();
      splitMonths.add(monthList);
    }

    int j = 0;
    for (Double lista1 : lista) {

      Double minimalSum = -1.0;
      int position = 0;
      for (int i = 0; i < parts.size(); i++) {
        List<Double> part = parts.get(i);
        if (minimalSum == -1) {
          minimalSum = suma(part);
          position = i;
        } else if (suma(part) < minimalSum) {
          minimalSum = suma(part);
          position = i;
        }
      }

      List<Double> part = parts.get(position);
      part.add(lista1);
      parts.set(position, part);

      List<String> month = splitMonths.get(position);
      month.add(months.get(j));
      splitMonths.set(position, month);
      j++;
    }

    Map<String, Integer> labelGroups = new HashMap<>();
    for (int i = 0; i < splitMonths.size(); i++) {
      List<String> list = splitMonths.get(i);
      for (String aList : list) {
        labelGroups.put(aList, i);
      }
    }

    return labelGroups;
  }

  public Map<String, Double> sortMapByValue(Map<String, Double> passedMap) {
    List<String> mapKeys = new ArrayList<>(passedMap.keySet());
    List<Double> mapValues = new ArrayList<>(passedMap.values());
    Collections.sort(mapValues, Collections.reverseOrder());
    Collections.sort(mapKeys, Collections.reverseOrder());

    LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

    Iterator<Double> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
      Object val = valueIt.next();
      Iterator<String> keyIt = mapKeys.iterator();

      while (keyIt.hasNext()) {
        Object key = keyIt.next();
        String comp1 = passedMap.get(key).toString();
        String comp2 = val.toString();

        if (comp1.equals(comp2)) {
          passedMap.remove(key);
          mapKeys.remove(key);
          sortedMap.put((String) key, (Double) val);
          break;
        }

      }

    }
    return sortedMap;
  }

  private Double suma(List<Double> part) {
    Double ret = 0.0;
    for (Double aPart : part) {
      ret += aPart;
    }
    return ret;
  }

}
