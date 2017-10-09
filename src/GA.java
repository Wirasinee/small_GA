
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
    private double sumFitness;               //ผลรวม Fitness
    private double sumPercentage = 0;       //ผลรวม %
    private int[] indiv;
    private final double popCrossover ;     //ความน่าจะเป็นในCrossover
    private final double popMutation ;      //ความน่าจะเป็นในการผ่าเหล่า
    private double numRandom;               //เก็บเลขที่สุ่มมาได้
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

    public Map<String, Chromosome> createTable(Map<String, Chromosome> mapChromosome) {

        for (int i = 0; i < populationSize; i++) {
            String dec = "";
            for (int j = 0; j < chromosomeSize; j++) {
                indiv[j] = m.randomZeroOrOne();         //สุ่มเลข 0 หรือ 1
                dec += indiv[j];                        //เก็บเลข0หรือ1ไว้ที่decเพือนำไปแปลงเป็นฐาน10
            }
            Chromosome c = new Chromosome(indiv.clone());
            int value = Integer.parseInt(dec, 2);       //แปลงเป็นเลขฐาน10
            c.setValue(value);
            c.setFitness((2 * c.getValue()) + 1);       //หาค่าFitness
            sumFitness += c.getFitness();               //+เก็บค่าผลรวมของFitness

            mapChromosome.put("ch" + (i + 1), c);

        }
        //System.out.println("sumFitness:" + sumFitness);
        return mapChromosome;
    }

    public Map<String, Chromosome> createTableParent(Map<String, Chromosome> mapChromosome) {
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
    
    public void printTableChromosome(Map<String, Chromosome> mapChromosome){
    System.out.println("Table Chromosome (indiv|chromosome|value|fitness)");
        for (int i=1;i<=mapChromosome.size();i++) {
            String key="ch"+i;
            System.out.println(key + "  " + Arrays.toString(mapChromosome.get(key).getChromosome()) + "  " + mapChromosome.get(key).getValue()
                    + "   " + mapChromosome.get(key).getFitness() );
        }
    }

    public void printTableChromosomeAll(Map<String, Chromosome> mapChromosome) {
        System.out.println("Table Chromosome (indiv|chromosome|value|fitness|percentage|sumPercentage)");
        for (int i=1;i<=mapChromosome.size();i++) {
            String key="ch"+i;
            System.out.println(key + "  " + Arrays.toString(mapChromosome.get(key).getChromosome()) + "  " + mapChromosome.get(key).getValue()
                    + "   " + mapChromosome.get(key).getFitness() + "   " + mapChromosome.get(key).getPercentage() + "   " + mapChromosome.get(key).getSumPercentage());
        }
    }
    /*สร้าง invividual และ matingPool*/
    public String[] createMatingPool(Map<String, Chromosome> mapChromosome) { 
        System.out.println("invividual=>matingPool");

        for (int i = 0; i < populationSize; i++) {
            int individual = m.RandomTo(0, 100);            //สุ่มค่า invividual ตั้งแต่0-100
            System.out.print(individual+" ");
            for (int j = populationSize; j > 0; j--) {

                if (individual == 100) {                    //ถ้าinvividualแสดงว่าอยู่ช่วงchสุดท้ายแน่นอน
                    matingPool[i] = "ch" + j;
                    break;
                } else if (individual > mapChromosome.get("ch" + j).getSumPercentage()) { //ถ้าinvividualอยู่ช่วงของchไหน
                    matingPool[i] = " ch" + (j + 1);        //ให้เก็บchนั้นไว้
                    break;
                } else {                                    //อื่นๆ ให้อยู่ ch แรก
                    matingPool[i] = " ch" + 1;
                }
            }

        }
        System.out.println("\n"+Arrays.toString(matingPool));   //แสดง matingPoolเช่น[ ch9,  ch12,  ch12,  ch10,  ch11]
        return matingPool;
    }
    /*การ crossover*/
    public Map<String, Chromosome> crossover(Map<String, Chromosome> mapChromosome, Map<String, Chromosome> mapNewChromosome) {
        int z = 0;
        int ch = 1;
        for (int i = 0; i < populationSize / 2; i++) {

            numRandom = Double.parseDouble(FormatPercentage.format(Math.random())); //สุ่มตัวเลขจำนวนจริงตั้งแต่0-1
            System.out.print("Random: " + (numRandom));   

            int[] parent1 = mapChromosome.get(matingPool[z++].trim()).getChromosome().clone();  //เอาchromosomeของแต่ลลคู่เก็บไว้ในparent1,2
            int[] parent2 = mapChromosome.get(matingPool[z++].trim()).getChromosome().clone();
            //System.out.println(Arrays.toString(parent1) + " " + Arrays.toString(parent2) + " " + z);
            if (popCrossover > numRandom) {                //ถ้าค่าที่สุ่มมา<popCrossover(0.75)
                int lk = m.RandomTo(0, chromosomeSize);         //สุ่มตำแหน่งจุดที่ทำการ crossover 
                System.out.print(" crossover");
                System.out.print(matingPool[ch-1]);
                mapNewChromosome.put("ch" + (ch++), new Chromosome(m.combine(parent1, parent2, lk))); //สลับตำแหน่ง parent1 กับ parent2 ตามlk แล้วเก็บไว้ใน mapอันใหม่
                System.out.println(matingPool[ch-1]+" Index: "+(lk+1) );
                mapNewChromosome.put("ch" + (ch++), new Chromosome(m.combine(parent2, parent1, lk))); //สลับตำแหน่ง parent2 กับ parent1 ตามlk แล้วเก็บไว้ใน mapอันใหม่ 
                //System.out.print(">" + Arrays.toString(new Chromosome(m.combine(parent1, parent2, lk)).getChromosome()) + " \n>" + Arrays.toString(new Chromosome(m.combine(parent2, parent1, lk)).getChromosome()));

            } else {                                            //ถ้าค่าที่สุ่มมา>=popCrossover(0.75) ก็เก็บ chromosome เดิม ไว้ใน map อันใหม่
                System.out.println("");
                mapNewChromosome.put("ch" + (ch++), new Chromosome(parent1));
                mapNewChromosome.put("ch" + (ch++), new Chromosome(parent2));
            }
        }
        
        return mapNewChromosome;
    }
    /*ผ่าเหล่าลูก*/
    public void onePointMutation(Map<String, Chromosome> mapNewChromosome) {
        for (int i = 1; i <= populationSize; i++) {
            int lk = m.RandomTo(0, chromosomeSize); //สุ่มตำแหน่ง
            //System.out.println(lk);
            numRandom = Double.parseDouble(FormatPercentage.format(Math.random())); //สุ่มตัวเลขจำนวนจริงตั้งแต่0-1
            System.out.print("Random: " + (numRandom));
            int z = 0;
            if (popMutation > numRandom) {          //ถ้า เลขที่สุ่มมา <popMutation(0.5) แสดงว่าต้องทำMutation
                System.out.println(" Mutation ch"+i+" Index: "+(lk+1) );
                int[] a = mapNewChromosome.get("ch" + i).getChromosome();
                //System.out.println(Arrays.toString(a));
                if (a[lk] == 1) {                   //ถ้าค่าchromosomeตำแหน่งที่สุ่มมามีค่าเป็น1 จะสลับเป็น 0
                    a[lk] = 0;
                } else {                            //ถ้าค่าchromosomeตำแหน่งที่สุ่มมามีค่าเป็น0 จะสลับเป็น 1
                    a[lk] = 1;
                }
                
                //System.out.println(Arrays.toString(a));
                mapNewChromosome.get("ch" + i).setChromosome(a);    //setค่าChromosome ใหม่
            }else{System.out.println("");}

        }
        
    }
    /*หาผมลรวม Fitness ของลูก*/
    public void sumFitnessOffspring(Map<String, Chromosome> mapNewChromosome) {
        //offspring
        indiv = new int[chromosomeSize];
        sumFitness = 0;
        for (int i = 1; i <= populationSize; i++) {
            String dec = Arrays.toString(mapNewChromosome.get("ch" + i).getChromosome());       //เก็บChromosomeของchนั้นๆ
            dec = dec.substring(1, dec.length() - 1).replaceAll(", ", "");          //ทำให้อยู่ในรูปstring

            int value = Integer.parseInt(dec, 2);                   //นำไปแปลงให้เป็นเลขฐาน10
            mapNewChromosome.get("ch" + i).setValue(value);
            mapNewChromosome.get("ch" + i).setFitness((2 * mapNewChromosome.get("ch" + i).getValue()) + 1); //หาค่าFitness
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
        return numRandom;
    }

    public void setArrCrossRandom(double arrCrossRandom) {
        this.numRandom = arrCrossRandom;
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
