
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wirasinee
 */
public class MainClass_GA {

    public static void main(String[] args) {

        MainClass_GA mainClass = new MainClass_GA();
        int chromosomeSize = 16;
        int populationSize = 20;
        int numGenerations = 100;//จำนวนรุ่น
        double popCrossover = 0.75;//ความน่าจะเป็นในCrossover
        double popMutation = 0.5;//ความน่าจะเป็นในการผ่าเหล่า
        GA ga = new GA(chromosomeSize, populationSize, popCrossover, popMutation);
        Map<String, Chromosome> mapParent = new TreeMap<>(); //map ของพ่อแม่
        Map<String, Chromosome> mapOffspring = new TreeMap<>();  //map ของลูกคนใหม่
        
        System.out.println("---- Generation 0");
        //parent
        System.out.println("--Crossover Parent--");
        mapParent = ga.createTableParent(ga.createTable(mapParent)); //สร้างตาราง chromosome ของพ่อแม่
        ga.printTableChromosomeAll(mapParent); //แสดงตาราง พ่อแม่

        for (int gen = 1; gen <= numGenerations; gen++) {

            String[] matingPool = ga.createMatingPool(mapParent); //สร้าง invividual และ matingPool
            //offspring
            System.out.println("--Crossover Offspring--");
            mapOffspring = ga.crossover(mapParent, mapOffspring); //crossover พ่อแม่ กลายเป็น chromosome ลูก
            ga.sumFitnessOffspring(mapOffspring);
            ga.printTableChromosome(mapOffspring);
            System.out.println("--One Point Mutation offspring--");
            ga.onePointMutation(mapOffspring); //ผ่าเหล่าลูก
            ga.sumFitnessOffspring(mapOffspring); //หาผมลรวม Fitness ของลูก
            mapParent = ga.createTableParent(mapOffspring); //สร้างตารางของ ลูก และเป็นตารางของพ่อแม่ต่อรุ่นต่อไป
            
            System.out.println("------- Generation " + gen);
            ga.printTableChromosomeAll(mapOffspring); //แสดงตาราง ลูก
            System.out.println("______________________________");
            

        }
    }

    public static int[] combine(int[] a, int[] b, int lk) {
        int[] result = new int[a.length];
        System.arraycopy(a, 0, result, 0, lk);
        System.arraycopy(b, lk, result, lk, b.length - lk);
        return result;
    }

    public int RandomZeroToOne() {
        double r = Math.random();
        if (r > 0.5) {
            return 1;
        } else {
            return 0;
        }
    }

    public int RandomTo(int a, int b) {
        Random random = new Random();
        return random.nextInt(b) + a;
    }

}
