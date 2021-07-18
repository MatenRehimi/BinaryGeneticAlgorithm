import java.util.ArrayList;

public class BGA {

  private int[][] goalRedPixels;
  private int[][] goalGreenPixels;
  private int[][] goalBluePixels;
  private int[][][] testRedPixels;
  private int[][][] testGreenPixels;
  private int[][][] testBluePixels;

  public BGA(int[][] goalRedPixels, int[][] goalGreenPixels, int[][] goalBluePixels, int n) {
    this.testRedPixels = new int[n][goalRedPixels.length][goalRedPixels[0].length];
    this.testBluePixels = new int[n][goalBluePixels.length][goalBluePixels[0].length];
    this.testGreenPixels = new int[n][goalGreenPixels.length][goalGreenPixels[0].length];
    this.goalRedPixels = goalRedPixels;
    this.goalGreenPixels = goalGreenPixels;
    this.goalBluePixels = goalBluePixels;
    //Population initialisation
    randomPixels();
  }

  public void randomPixels() {
    for (int n=0;n<testRedPixels.length;n++) {
      for (int w=0;w<testRedPixels[0].length;w++) {
        for (int h=0;h<testRedPixels[0][0].length;h++) {
          testRedPixels[n][w][h]= (int)(Math.random()*256);
          testBluePixels[n][w][h]=(int)(Math.random()*256);
          testGreenPixels[n][w][h]=(int)(Math.random()*256);
        }
      }
    }
  }

  public void iteration() {
    testRedPixels = reorderUsingCostFunction(testRedPixels,goalRedPixels);
    findProbabilityAndCrossover(testRedPixels,goalRedPixels);
    mutateAndKeepLowerCost(5000,testRedPixels,goalRedPixels,false);

    testGreenPixels = reorderUsingCostFunction(testGreenPixels,goalGreenPixels);
    findProbabilityAndCrossover(testGreenPixels,goalGreenPixels);
    mutateAndKeepLowerCost(5000,testGreenPixels,goalGreenPixels,false);

    testBluePixels = reorderUsingCostFunction(testBluePixels,goalBluePixels);
    findProbabilityAndCrossover(testBluePixels,goalBluePixels);
    mutateAndKeepLowerCost(5000,testBluePixels,goalBluePixels,false);
  }

  public ArrayList<Integer> printCosts(int[][][] testPixels,int[][] goalPixels) {
    int cost;
    ArrayList<Integer> costs = new ArrayList<Integer>();
    for (int n=0;n<testPixels.length;n++) {
      cost = 0;
      for (int w=0;w<testPixels[0].length;w++) {
        for (int h=0;h<testPixels[0][0].length;h++) {
          cost+=(int)Math.abs(goalPixels[w][h]-testPixels[n][w][h]);
        }
      }
      System.out.println(cost);
      costs.add(cost);
    }
    return costs;
  }

  public ArrayList<Integer> findCosts(int[][][] testPixels,int[][] goalPixels) {
    int cost;
    ArrayList<Integer> costs = new ArrayList<Integer>();
    for (int n=0;n<testPixels.length;n++) {
      cost = 0;
      for (int w=0;w<testPixels[0].length;w++) {
        for (int h=0;h<testPixels[0][0].length;h++) {
          cost+=(int)Math.abs(goalPixels[w][h]-testPixels[n][w][h]);
        }
      }
      // System.out.println(cost);
      costs.add(cost);
    }
    return costs;
  }

  public int[][][] reorderUsingCostFunction(int[][][] testPixels, int[][] goalPixels) {

    System.out.println("original");
    printCosts(testPixels,goalPixels);
    System.out.println();
    ArrayList<Integer> costs = findCosts(testPixels,goalPixels);

    ArrayList<Integer> orderedCostsIndexes = new ArrayList<Integer>();

    int iterations = costs.size();
    while(iterations > 0) {
      int minValue = Integer.MAX_VALUE;
      int minIndex = 0;
      for (int index=0; index < costs.size();index++) {
        if (costs.get(index) < minValue) {
          minValue = costs.get(index);
          minIndex = index;
        }
      }
      orderedCostsIndexes.add(minIndex);
      costs.set(minIndex,Integer.MAX_VALUE);
      iterations--;
    }

    int[][][] orderedTestPixels = new int[testPixels.length][testPixels[0].length][testPixels[0][0].length];

    int index = 0;
    for(int n : orderedCostsIndexes) {
      System.out.println(n);
      orderedTestPixels[index]=testPixels[n];
      index++;
    }

    System.out.println("UPDATED");
    printCosts(orderedTestPixels,goalPixels);

    return orderedTestPixels;
  }

  public void findProbabilityAndCrossover(int[][][] testPixels, int[][] goalPixels) {
    // System.out.println("findProbabilityAndCrossover");
    ArrayList<Integer> costs = findCosts(testPixels,goalPixels);
    // System.out.println("dingo");
    ArrayList<Double> probabilities = findProbability(costs,testRedPixels.length-1);
    ArrayList<Integer> selected = selection(probabilities,2);
    crossOver(testPixels, goalPixels, selected);
    // System.out.println("bingo");
  }

  public ArrayList<Double> findProbability(ArrayList<Integer> array,int nKeep) {
    if (array.size() == 0) return null;
    double total = 0;
    for (int index=0;index<nKeep; index++) {
      total += array.get(index);
    }
    // System.out.println("total: "+total);
    ArrayList<Double> probabilities = new ArrayList<Double>();

    // System.out.println("size:"+array.size());

    for (int index = 0; index < nKeep; index++) {
      double probability = array.get(index)/total;
      // System.out.println(probability);
      probabilities.add(probability);
    }
    return probabilities;
  }

  public ArrayList<Integer> selection(ArrayList<Double> probabilities, int numberOfChromosomes) {
    ArrayList<Integer> pickedChromosomes = new ArrayList<Integer>();

    for (int iteration = 0; iteration < numberOfChromosomes; iteration++) {
      double random = Math.random();
      double total = 0;
      for (int chromosome = 0; chromosome < probabilities.size(); chromosome++) {
        total +=probabilities.get(chromosome);
        if (random <= total) {
          pickedChromosomes.add(chromosome);
          // System.out.println("picked: "+chromosome);
          break;
        }
      }
    }

    return pickedChromosomes;
  }

  public void crossOver(int[][][] testPixels, int[][] goalPixels, ArrayList<Integer> selected) {

    // System.out.println(selected.size());
    if (selected.get(0) == selected.get(1)) return;
    for (int w = 0; w < goalPixels.length; w++) {
      for (int h = 0; h < goalPixels[0].length;h++) {
        int selected1 = testPixels[selected.get(0)][w][h];
        int selected2 = testPixels[selected.get(1)][w][h];
        int crossover1 = (selected1 & 240) | (selected2 & 15);
        int crossover2 = (selected1 & 15) | (selected2 & 240);
        int difference1 = Math.abs(goalPixels[w][h]-crossover1);
        int difference2 = Math.abs(goalPixels[w][h]-crossover2);
        testPixels[3][w][h] = (difference1 < difference2) ? crossover1 : crossover2;
      }
    }
  }

  public void mutateAndKeepLowerCost(int iterations,int[][][] testPixels,int[][] goalPixels,boolean elitism) {

    int[][][] testPixelsCopy = copy(testPixels);
    int random;
    int mask;

    while (iterations > 0) {
      for (int n=0;n<testRedPixels.length;n++) {
        if (elitism && n == 0) n++;
        random = (int)(Math.random()*8);
        mask = 1 << random;
        testPixelsCopy[n][getRandomWidth()][getRandomHeight()] ^= mask ;
      }
      iterations--;
    }

    // System.out.println("ordered");
    ArrayList<Integer> costs=findCosts(testPixels,goalPixels);
    // System.out.println("mutated");
    ArrayList<Integer> mutatedCosts=findCosts(testPixelsCopy,goalPixels);
    for (int n = 0; n<testRedPixels.length;n++) {
      if (mutatedCosts.get(n)<costs.get(n)) {
        testPixels[n]=testPixelsCopy[n];
      }
    }

    // System.out.println("updated");
    findCosts(testPixels,goalPixels);

  }

  public int[][][] copy(int[][][] array) {
    int[][][] newArray = new int[array.length][array[0].length][array[0][0].length];
    for (int n=0;n<array.length;n++) {
      for (int w=0; w<array[0].length;w++) {
        for (int h=0; h<array[0][0].length;h++) {
          newArray[n][w][h]=array[n][w][h];
        }
      }
    }
    return newArray;
  }

  public int getRandomWidth() {
    int random = (int)(Math.random()*testRedPixels[0].length);
    return random;
  }

  public int getRandomHeight() {
    int random = (int)(Math.random()*testRedPixels[0][0].length);
    return random;
  }

  public int[][] getRedPixels() {
    return testRedPixels[0];
  }

  public int[][] getGreenPixels() {
    return testGreenPixels[0];
  }

  public int[][] getBluePixels() {
    return testBluePixels[0];
  }

}
