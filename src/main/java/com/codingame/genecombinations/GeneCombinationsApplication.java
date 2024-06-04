package com.codingame.genecombinations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@SpringBootApplication
public class GeneCombinationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneCombinationsApplication.class, args);
    }

    public static List<String> getGenotypes(String phenotype) {
        return switch (phenotype) {
            case "A" -> List.of("AA", "AO");
            case "B" -> List.of("BB", "BO");
            case "AB" -> List.of("AB");
            case "O" -> List.of("OO");
            default -> List.of("");
        };
    }

    private static boolean canProduceChild(String parent1, String parent2, String childPhenotype) {
        Map<String, String> genotypeToPhenotype = new HashMap<>();
        genotypeToPhenotype.put("AA", "A");
        genotypeToPhenotype.put("AO", "A");
        genotypeToPhenotype.put("OA", "A");
        genotypeToPhenotype.put("BB", "B");
        genotypeToPhenotype.put("BO", "B");
        genotypeToPhenotype.put("OB", "B");
        genotypeToPhenotype.put("AB", "AB");
        genotypeToPhenotype.put("BA", "AB");
        genotypeToPhenotype.put("OO", "O");

        for (char g1 : parent1.toCharArray()) {
            for (char g2 : parent2.toCharArray()) {
                String childGenotype = "" + g1 + g2;
                if (genotypeToPhenotype.getOrDefault(childGenotype, "").equals(childPhenotype)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<List<String>> getPossibleParentGenotypes(String parent1Phenotype, String parent2Phenotype, String childPhenotype) {
        List<String> parent1Genotypes = getGenotypes(parent1Phenotype);
        List<String> parent2Genotypes = getGenotypes(parent2Phenotype);

        List<List<String>> possibleCombinations = new ArrayList<>();

        for (String g1 : parent1Genotypes) {
            for (String g2 : parent2Genotypes) {
                if (canProduceChild(g1, g2, childPhenotype)) {
                    possibleCombinations.add(Arrays.asList(g1, g2));
                }
            }
        }

        // Sort the results
        possibleCombinations.sort(Comparator.comparing((List<String> combo) -> combo.get(0)).thenComparing(combo -> combo.get(1)));

        // If no valid combination, return [["--", "--"]]
        if (possibleCombinations.isEmpty()) {
            possibleCombinations.add(Arrays.asList("--", "--"));
        }

        return possibleCombinations;
    }

    // Public static method to get possible parent genotypes
    public static List<List<String>> computeBloodGenes(String parent1Phenotype, String parent2Phenotype, String childPhenotype) {
        List<String> parent1Genotypes = getGenotypes(parent1Phenotype);
        List<String> parent2Genotypes = getGenotypes(parent2Phenotype);

        List<List<String>> possibleCombinations = new ArrayList<>();

        for (String g1 : parent1Genotypes) {
            for (String g2 : parent2Genotypes) {
                if (canProduceChild(g1, g2, childPhenotype)) {
                    possibleCombinations.add(Arrays.asList(g1, g2));
                }
            }
        }

        // Sort the results
        possibleCombinations.sort((combo1, combo2) -> {
            int cmp = combo1.get(0).compareTo(combo2.get(0));
            if (cmp != 0) return cmp;
            return combo1.get(1).compareTo(combo2.get(1));
        });

        // If no valid combination, return [["--", "--"]]
        if (possibleCombinations.isEmpty()) {
            possibleCombinations.add(Arrays.asList("--", "--"));
        }

        return possibleCombinations;
    }

    private static void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Parent 1: ");
        String parent1 = scanner.nextLine();
        System.out.print("Parent 2: ");
        String parent2 = scanner.nextLine();
        System.out.print("Child: ");
        String child = scanner.nextLine();

        List<List<String>> result = computeBloodGenes(parent1, parent2, child);

        for (List<String> combination : result) {
            System.out.println(combination);
        }
    }

    @Bean
    CommandLineRunner runner() {
        return GeneCombinationsApplication::run;
    }

}
