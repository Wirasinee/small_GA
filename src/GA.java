
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wirasinee
 */
class GA {

    MainClass_GA m = new MainClass_GA();
    private int chromosomeSize;
    private int populationSize;
    private DecimalFormat FormatPercentage; //ฟอแมตเลขทศนิยม
    private double sumFitness;  //ผลรวม Fitness
    private double sumPercentage = 0; //ผลรวม %
    private int[] indiv;
    private final double popCrossover ;//ความน่าจะเป็นในCrossover
    private final double popMutation ;//ความน่าจะเป็นในการผ่าเหล่า
    private double arrCrossRandom;
    private String[] matingPool;
    
    public GA(int chromosomeSize, int populationSize,double popCrossover,double popMutation) {
        this.chromosomeSize = chromosomeSize;
        this.populationSize = populationSize;
        this.indiv = new int[chromosomeSize];
        this.matingPool = new String[populationSize];
        this.FormatPercentage = new DecimalFormat("0.00");
        this.popCrossover = popCrossover;
        this.popMutation = popMutation;
    }
    
    public GA(int chromosomeSize, int populationSize,double popCrossover,double popMutation, String format) {
        this.chromosomeSize = chromosomeSize;
        this.populationSize = populationSize;
        this.indiv = new int[chromosomeSize];
        this.matingPool = new String[populationSize];
        this.FormatPercentage = new DecimalFormat(format);
        this.popCrossover = popCrossover;
        this.popMutation = popMutation;
    }

    Map<String, Chromosome> createTable(Map<String, Chromosome> mapChromosome) {

        for (int i = 0; i < populationSize; i++) {
            String dec = "";
            for (int j = 0; j < chromosomeSize; j++) {
                indiv[j] = m.RandomZeroToOne();
                dec += indiv[j];
            }
            Chromosome c = new Chromosome(indiv.clone());
            int value = Integer.parseInt(dec, 2);
            c.setValue(value);
            c.setFitness((2 * c.getValue()) + 1);
            sumFitness += c.getFitness();

            mapChromosome.put("ch" + (i + 1), c);

        }
        //System.out.println("sumFitness:" + sumFitness);
        return mapChromosome;
    }

    Map<String, Chromosome> createTableParent(Map<String, Chromosome> mapChromosome) {
        sumPercentage = 0;
        for (int i = 1; i <= populationSize; i++) {
            String key = "ch" + i;
            double percentage = (mapChromosome.get(key).getFitness() * 100) / (double) sumFitness;
            percentage = Double.parseDouble(FormatPercentage.format(percentage));
            sumPercentage = Double.parseDouble(FormatPercentage.format((sumPercentage + percentage)));
            mapChromosome.get(key).setPercentage(percentage);
            mapChromosome.get(key).setSumPercentage(sumPercentage);
        }
        return mapChromosome;
    }
    
    void printTableChromosome(Map<String, Chromosome> mapChromosome){
    System.out.println("Table Chromosome (indiv|chromosome|value|fitness)");
        for (int i=1;i<mapChromosome.size();i++) {
            String key="ch"+i;
            System.out.println(key + "  " + Arrays.toString(mapChromosome.get(key).getChromosome()) + "  " + mapChromosome.get(key).getValue()
                    + "   " + mapChromosome.get(key).getFitness() );
        }
    }

    void printTableChromosomeAll(Map<String, Chromosome> mapChromosome) {
        System.out.println("Table Chromosome (indiv|chromosome|value|fitness|percentage|sumPercentage)");
        for (int i=1;i<mapChromosome.size();i++) {
            String key="ch"+i;
            System.out.println(key + "  " + Arrays.toString(mapChromosome.get(key).getChromosome()) + "  " + mapChromosome.get(key).getValue()
                    + "   " + mapChromosome.get(key).getFitness() + "   " + mapChromosome.get(key).getPercentage() + "   " + mapChromosome.get(key).getSumPercentage());
        }
    }

    String[] createMatingPool(Map<String, Chromosome> mapChromosome) {
        System.out.println("invividual=>matingPool");

        for (int i = 0; i < populationSize; i++) {
            int individual = m.RandomTo(0, 100);
            System.out.print(individual+" ");
            for (int j = populationSize; j > 0; j--) {

                if (individual == 100) {
                    matingPool[i] = "ch" + j;
                    break;
                } else if (individual > mapChromosome.get("ch" + j).getSumPercentage()) {
                    matingPool[i] = " ch" + (j + 1);
                    break;
                } else {
                    matingPool[i] = " ch" + 1;
                }
            }

        }
        System.out.println("\n"+Arrays.toString(matingPool));
        return matingPool;
    }

    Map<String, Chromosome> crossover(Map<String, Chromosome> mapChromosome, Map<String, Chromosome> mapNewChromosome) {
        int z = 0;
        int ch = 1;
        for (int i = 0; i < populationSize / 2; i++) {
           

            arrCrossRandom = Double.parseDouble(FormatPercentage.format(Math.random()));
            System.out.print("Random: " + (arrCrossRandom+1));

            int[] parent1 = mapChromosome.get(matingPool[z++].trim()).getChromosome().clone();
            int[] parent2 = mapChromosome.get(matingPool[z++].trim()).getChromosome().clone();
            //System.out.println(Arrays.toString(parent1) + " " + Arrays.toString(parent2) + " " + z);
            if (popCrossover > arrCrossRandom) {
                int lk = m.RandomTo(0, chromosomeSize);
                System.out.print(" crossover");
                System.out.print(matingPool[ch-1]);
                mapNewChromosome.put("ch" + (ch++), new Chromosome(m.combine(parent1, parent2, lk)));
                System.out.println(matingPool[ch-1]+" Index: "+(lk+1) );
                mapNewChromosome.put("ch" + (ch++), new Chromosome(m.combine(parent2, parent1, lk)));
                //System.out.print(">" + Arrays.toString(new Chromosome(m.combine(parent1, parent2, lk)).getChromosome()) + " \n>" + Arrays.toString(new Chromosome(m.combine(parent2, parent1, lk)).getChromosome()));

            } else {
                System.out.println("");
                mapNewChromosome.put("ch" + (ch++), new Chromosome(parent1));
                mapNewChromosome.put("ch" + (ch++), new Chromosome(parent2));
            }
        }
        
        return mapNewChromosome;
    }

    void onePointMutation(Map<String, Chromosome> mapNewChromosome) {
        for (int i = 1; i <= populationSize; i++) {
            int lk = m.RandomTo(0, chromosomeSize); //สุ่มตำแหน่ง
            //System.out.println(lk);
            arrCrossRandom = Double.parseDouble(FormatPercentage.format(Math.random()));
            System.out.print("Random: " + (arrCrossRandom+1));
            int z = 0;
            if (popMutation > arrCrossRandom) {
                System.out.println(" Mutation ch"+i+" Index: "+(lk+1) );
                String dec = "";
                int[] a = mapNewChromosome.get("ch" + i).getChromosome();
                //System.out.println(Arrays.toString(a));
                if (a[lk] == 1) {
                    a[lk] = 0;
                } else {
                    a[lk] = 1;
                }
                
                //System.out.println(Arrays.toString(a));
                mapNewChromosome.get("ch" + i).setChromosome(a);
            }else{System.out.println("");}

        }
        
    }

    void sumFitnessOffspring(Map<String, Chromosome> mapNewChromosome) {
        //offspring
        indiv = new int[chromosomeSize];
        sumFitness = 0;
        for (int i = 1; i <= populationSize; i++) {
            String dec = Arrays.toString(mapNewChromosome.get("ch" + i).getChromosome());
            dec = dec.substring(1, dec.length() - 1).replaceAll(", ", "");

            int value = Integer.parseInt(dec, 2);
            mapNewChromosome.get("ch" + i).setValue(value);
            mapNewChromosome.get("ch" + i).setFitness((2 * mapNewChromosome.get("ch" + i).getValue()) + 1);
            sumFitness += mapNewChromosome.get("ch" + i).getFitness();

        }
        //System.out.println("sumFitness:" + sumFitness);
    }

    //----------------get set------------------
    public double getPopMutation() {
        return popMutation;
    }


    public double getPopCrossover() {
        return popCrossover;
    }


    public double getArrCrossRandom() {
        return arrCrossRandom;
    }

    public void setArrCrossRandom(double arrCrossRandom) {
        this.arrCrossRandom = arrCrossRandom;
    }

    public String[] getMatingPool() {
        return matingPool;
    }

    public void setMatingPool(String[] matingPool) {
        this.matingPool = matingPool;
    }

    public DecimalFormat getFormatPercentage() {
        return FormatPercentage;
    }

    public void setFormatPercentage(DecimalFormat FormatPercentage) {
        this.FormatPercentage = FormatPercentage;
    }

    public double getSumPercentage() {
        return sumPercentage;
    }

    public void setSumPercentage(double sumPercentage) {
        this.sumPercentage = sumPercentage;
    }

    public int[] getIndiv() {
        return indiv;
    }

    public void setIndiv(int[] indiv) {
        this.indiv = indiv;
    }

    public int getChromosomeSize() {
        return chromosomeSize;
    }

    public void setChromosomeSize(int chromosomeSize) {
        this.chromosomeSize = chromosomeSize;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getSumFitness() {
        return sumFitness;
    }

    public void setSumFitness(double sumFitness) {
        this.sumFitness = sumFitness;
    }

}
