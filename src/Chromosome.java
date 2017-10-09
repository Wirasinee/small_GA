/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wirasinee
 */
class Chromosome {

    private int[] chromosome;
    private int value;
    private int fitness;
    private double percentage;
    private double sumPercentage;

    Chromosome(int[] chr) {
        chromosome = chr;
    }

    public Chromosome(int[] chromosome, int value, int fitness, double percentage, double sumPercentage) {
        this.chromosome = chromosome;
        this.value = value;
        this.fitness = fitness;
        this.percentage = percentage;
        this.sumPercentage = sumPercentage;
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getSumPercentage() {
        return sumPercentage;
    }

    public void setSumPercentage(double sumPercentage) {
        this.sumPercentage = sumPercentage;
    }

}
