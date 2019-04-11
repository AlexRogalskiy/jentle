/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.genetics.knapsack;

import com.wildbeeslabs.jentle.algorithms.random.CRandom;
import com.wildbeeslabs.jentle.algorithms.matrix.CMatrixUtils;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import io.jenetics.*;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import static io.jenetics.engine.Limits.bySteadyFitness;

/**
 * Custom genetics algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
public final class CGenetics {

    private CGenetics() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CMemberGenetics<T extends CMember> {

        private final double uniformRate = 0.5;
        private final double mutationRate = 0.025;
        private final int tournamentSize = 5;
        private final boolean elitism = true;
        private static long[] solution;

        public void runTask(int populationSize, final String solution) {
            setSolution(solution);
            CMemberSet<T> myPop = new CMemberSet<>(populationSize, true);
            int generationCount = 1;
            while (myPop.getFittest().getFitness() < getMaxFitness()) {
                log.debug(String.format("Generation: %s, Correct genes found: %s", generationCount, myPop.getFittest().getFitness()));
                myPop = evolvePopulation(myPop);
                generationCount++;
            }
            log.debug("Generation: " + generationCount);
            log.debug("Genes: ");
            log.debug(myPop.getFittest().toString());
        }

        public CMemberSet<T> evolvePopulation(final CMemberSet<T> pop) {
            int elitismOffset;
            CMemberSet<T> newPopulation = new CMemberSet<>(pop.getMembers().size(), false);

            if (elitism) {
                newPopulation.getMembers().add(0, pop.getFittest());
                elitismOffset = 1;
            } else {
                elitismOffset = 0;
            }

            for (int i = elitismOffset; i < pop.getMembers().size(); i++) {
                final T indiv1 = tournamentSelection(pop);
                final T indiv2 = tournamentSelection(pop);
                T newIndiv = crossover(indiv1, indiv2);
                newPopulation.getMembers().add(i, newIndiv);
            }

            for (int i = elitismOffset; i < newPopulation.getMembers().size(); i++) {
                mutate(newPopulation.getMember(i));
            }
            return newPopulation;
        }

        private T crossover(final T indiv1, final T indiv2) {
            final Class<? extends T> clazz = (Class<? extends T>) indiv1.getClass();
            final T newSol = CUtils.getInstance(clazz);
            for (int i = 0; i < newSol.getGenLength(); i++) {
                if (Math.random() <= uniformRate) {
                    newSol.setSingleGene(i, indiv1.getSingleGene(i));
                } else {
                    newSol.setSingleGene(i, indiv2.getSingleGene(i));
                }
            }
            return newSol;
        }

        private void mutate(final T indiv) {
            for (int i = 0; i < indiv.getGenLength(); i++) {
                if (Math.random() <= mutationRate) {
                    indiv.setSingleGene(i, CRandom.generateRandomLong());
                }
            }
        }

        private T tournamentSelection(final CMemberSet<T> pop) {
            final CMemberSet<T> tournament = new CMemberSet<>(tournamentSize, false);
            for (int i = 0; i < tournamentSize; i++) {
                int randomId = (int) (Math.random() * pop.getMembers().size());
                tournament.getMembers().add(i, pop.getMember(randomId));
            }
            return tournament.getFittest();
        }

        protected static int getFitness(final CMember member) {
            int fitness = 0;
            for (int i = 0; i < member.getGenLength() && i < solution.length; i++) {
                if (member.getSingleGene(i) == solution[i]) {
                    fitness++;
                }
            }
            return fitness;
        }

        protected int getMaxFitness() {
            int maxFitness = solution.length;
            return maxFitness;
        }

        protected void setSolution(final String newSolution) {
            solution = new long[newSolution.length()];
            for (int i = 0; i < newSolution.length(); i++) {
                final String character = newSolution.substring(i, i + 1);
                if (character.contains("0") || character.contains("1")) {
                    solution[i] = Long.valueOf(character);
                } else {
                    solution[i] = 0;
                }
            }
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CActorGenetics<T extends CActor> {

        /**
         * Default max number of iterations
         */
        private static final int DEFAULT_MAX_ITERATIONS = 1000;

        private static final double c = 1.0;
        private static final double alpha = 1;
        private static final double beta = 5;
        private static final double evaporation = 0.5;
        private static final double Q = 500;
        private static final double actorFactor = 0.8;
        private static final double randomFactor = 0.01;

        private final int numOfIterations;
        private final int numOfPlaces;
        private final int numberOfActors;
        private final double[][] graph;
        private final double[][] trails;
        private final List<T> actors = new ArrayList<>();
        private final double[] probabilities;

        private int currentIndex;
        private int[] bestTrailOrder;
        private double bestTrailLength;

        public CActorGenetics(int numOfPlaces) {
            this(numOfPlaces, CActorGenetics.DEFAULT_MAX_ITERATIONS);
        }

        public CActorGenetics(int numOfPlaces, int numOfIterations) {
            this(numOfPlaces, numOfIterations, 0, 100);
        }

        public CActorGenetics(int numOfPlaces, int numOfIterations, int lowerLimit, int upperLimit) {
            assert (numOfPlaces > 0);
            assert (lowerLimit >= 0 && upperLimit >= 0 && lowerLimit > upperLimit);

            this.numOfPlaces = numOfPlaces;
            this.numOfIterations = numOfIterations;

            this.graph = CMatrixUtils.generateRandomDoubleMatrix(numOfPlaces, lowerLimit, upperLimit);
            this.numberOfActors = (int) (numOfPlaces * CActorGenetics.actorFactor);
            this.trails = new double[numOfPlaces][numOfPlaces];
            this.probabilities = new double[numOfPlaces];
            IntStream.range(0, this.numberOfActors).forEach(i -> actors.add((T) new CActor(numOfPlaces)));
        }

        public void runTask(int numOfAttempts) {
            assert (numOfAttempts > 0);
            IntStream.rangeClosed(1, numOfAttempts)
                .forEach(i -> {
                    log.debug("Attempt #" + i);
                    optimize();
                });
        }

        public int[] optimize() {
            setup();
            clearTrails();
            IntStream.range(0, this.numOfIterations)
                .forEach(i -> {
                    moveActors();
                    updateTrails();
                    updateBestTrail();
                });
            log.debug("Best tour length: " + (this.bestTrailLength - this.numOfPlaces));
            log.debug("Best tour order: " + Arrays.toString(this.bestTrailOrder));
            return SerializationUtils.clone(this.bestTrailOrder);
        }

        private void setup() {
            IntStream.range(0, this.numberOfActors)
                .forEach(i -> {
                    this.actors.forEach(actor -> {
                        actor.clear();
                        actor.visit(-1, CRandom.generateRandomInt(0, this.numOfPlaces));
                    });
                });
            this.currentIndex = 0;
        }

        private void moveActors() {
            IntStream.range(currentIndex, this.numOfPlaces - 1)
                .forEach(i -> {
                    this.actors.forEach(actor -> actor.visit(this.currentIndex, getNextPlace(actor)));
                    this.currentIndex++;
                });
        }

        private Integer getNextPlace(final T actor) {
            int t = CRandom.generateRandomInt(0, this.numOfPlaces - this.currentIndex);
            if (CRandom.generateRandomDouble() < CActorGenetics.randomFactor) {
                OptionalInt cityIndex = IntStream.range(0, this.numOfPlaces)
                    .filter(i -> i == t && !actor.visited(i))
                    .findFirst();
                if (cityIndex.isPresent()) {
                    return cityIndex.getAsInt();
                }
            }
            calculateProbabilities(actor);
            double r = CRandom.generateRandomDouble();
            double total = 0;
            for (int i = 0; i < this.numOfPlaces; i++) {
                total += this.probabilities[i];
                if (total >= r) {
                    return i;
                }
            }
            return null;
        }

        public void calculateProbabilities(final T actor) {
            int i = actor.trail[this.currentIndex];
            double denominator = 0.0;
            for (int l = 0; l < this.numOfPlaces; l++) {
                if (!actor.visited(l)) {
                    denominator += Math.pow(this.trails[i][l], CActorGenetics.alpha) * Math.pow(1.0 / this.graph[i][l], CActorGenetics.beta);
                }
            }
            for (int j = 0; j < this.numOfPlaces; j++) {
                if (actor.visited(j)) {
                    this.probabilities[j] = 0.0;
                } else {
                    double numerator = Math.pow(this.trails[i][j], CActorGenetics.alpha) * Math.pow(1.0 / this.graph[i][j], CActorGenetics.beta);
                    this.probabilities[j] = numerator / denominator;
                }
            }
        }

        private void updateTrails() {
            for (int i = 0; i < this.numOfPlaces; i++) {
                for (int j = 0; j < this.numOfPlaces; j++) {
                    this.trails[i][j] *= CActorGenetics.evaporation;
                }
            }
            this.actors.stream().forEach((actor) -> {
                double contribution = CActorGenetics.Q / actor.trailLength(this.graph);
                for (int i = 0; i < this.numOfPlaces - 1; i++) {
                    this.trails[actor.trail[i]][actor.trail[i + 1]] += contribution;
                }
                this.trails[actor.trail[this.numOfPlaces - 1]][actor.trail[0]] += contribution;
            });
        }

        private void updateBestTrail() {
            if (Objects.isNull(this.bestTrailOrder)) {
                this.bestTrailOrder = this.actors.get(0).trail;
                this.bestTrailLength = this.actors.get(0).trailLength(this.graph);
            }
            this.actors.stream().filter((actor) -> (actor.trailLength(this.graph) < this.bestTrailLength)).map((actor) -> {
                this.bestTrailLength = actor.trailLength(this.graph);
                return actor;
            }).forEach((actor) -> {
                this.bestTrailOrder = SerializationUtils.clone(actor.trail);
            });
        }

        private void clearTrails() {
            IntStream.range(0, this.numOfPlaces)
                .forEach(i -> {
                    IntStream.range(0, this.numOfPlaces)
                        .forEach(j -> this.trails[i][j] = CActorGenetics.c);
                });
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CTourGenetics<T extends CPlace> {

        public void runTask() {
            final CTour<T> travel = new CTour<>(10);
            travel.getBestDistance(100, 10.0, 0.5);
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CSimpleGenetics {

        private static Integer eval(final Genotype<BitGene> gt) {
            return gt.getChromosome().as(BitChromosome.class).bitCount();
        }

        public void runTask() {
            final Factory<Genotype<BitGene>> genotypeStart = Genotype.of(BitChromosome.of(10, 0.5));
            log.debug("Before:" + genotypeStart);
            final Engine<BitGene, Integer> engine = Engine.builder(CSimpleGenetics::eval, genotypeStart)
                .build();
            final Genotype<BitGene> result = engine.stream()
                .limit(500)
                .collect(EvolutionResult.toBestGenotype());
            log.debug("After:" + result);
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CBagGenetics {

        /**
         * Default items number
         */
        public static final int DEFAULT_ITEMS_NUMBER = 150;

        public void runTask() {
            double size = CBagGenetics.DEFAULT_ITEMS_NUMBER * 100.0 / 3.0;
            final CBagWrapper wrapper = new CBagWrapper(Stream.generate(() -> CBagItem.random(0, 100)).limit(CBagGenetics.DEFAULT_ITEMS_NUMBER).toArray(CBagItem[]::new), size);
            final Engine<BitGene, Double> engine = Engine.builder(wrapper, BitChromosome.of(CBagGenetics.DEFAULT_ITEMS_NUMBER, 0.5))
                .populationSize(500)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                .build();
            final EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();
            final Phenotype<BitGene, Double> best = engine.stream()
                .limit(bySteadyFitness(7))
                .limit(100)
                .peek(statistics)
                .collect(toBestPhenotype());
            log.debug(statistics.toString());
            log.debug(best.toString());
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CSalesmanGenetics {

        public void runTask() {
            final CSalesman data = new CSalesman(50, 200.0);
            final Engine<EnumGene<Integer>, Double> engine = Engine.builder((path) -> data.distance(path), Codecs.ofPermutation(data.getNumOfPlaces()))
                .optimize(Optimize.MINIMUM)
                .maximalPhenotypeAge(11)
                .populationSize(500)
                .alterers(new SwapMutator<>(0.2), new PartiallyMatchedCrossover<>(0.35))
                .build();
            final EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();
            final Phenotype<EnumGene<Integer>, Double> best = engine.stream()
                .limit(bySteadyFitness(15))
                .limit(250)
                .peek(statistics)
                .collect(toBestPhenotype());
            log.debug("Evolution statistics: " + statistics);
            log.debug("Best phenotype" + best);
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CSpringsteenGenetics {

        public void runTask() {
            double maxPricePerUniqueSong = 2.5;
            final CSpringsteen springsteen = new CSpringsteen(
                ISeq.of(new CSpringsteenItem("Item1", 25, ISeq.of("Song1", "Song2", "Song3", "Song4", "Song5", "Song6")), new CSpringsteenItem("Item2", 15, ISeq.of("Song2", "Song3", "Song4", "Song5", "Song6", "Song7")),
                    new CSpringsteenItem("Item3", 35, ISeq.of("Song5", "Song6", "Song7", "Song8", "Song9", "Song10")), new CSpringsteenItem("Item4", 17, ISeq.of("Song9", "Song10", "Song12", "Song4", "Song13", "Song14")),
                    new CSpringsteenItem("Item5", 29, ISeq.of("Song1", "Song2", "Song13", "Song14", "Song15", "Song16")), new CSpringsteenItem("Item6", 5, ISeq.of("Song18", "Song20", "Song30", "Song40"))),
                maxPricePerUniqueSong);

            final Engine<BitGene, Double> engine = Engine.builder(springsteen).build();
            final ISeq<CSpringsteenItem> result = springsteen.codec()
                .decoder()
                .apply(engine.stream().limit(10).collect(EvolutionResult.toBestGenotype()));
            double cost = result.stream()
                .mapToDouble(r -> r.price)
                .sum();
            int uniqueSongCount = result.stream()
                .flatMap(r -> r.songs.stream())
                .collect(Collectors.toSet())
                .size();
            double pricePerUniqueSong = cost / uniqueSongCount;
            log.debug("Total cost: " + cost);
            log.debug("Unique songs:  " + uniqueSongCount);
            log.debug("Price per song: " + pricePerUniqueSong);
            log.debug("Items: " + result.map(r -> r.name).toString(", "));
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class CSumGenetics {

        public static <T, R extends Number> CSum<R> of(int limit, int groupSize, final Supplier<T> supplier, final Function<? super T, ? extends R> mapper) {
            assert (limit > 0);
            assert (groupSize > 0);
            return new CSum(Stream.generate(supplier)
                .limit(limit)
                .map(mapper)
                .collect(ISeq.toISeq()), groupSize);
        }

        public void runTask() {
            final CSum<Integer> task = CSumGenetics.of(500, 15, CRandom::generateRandomDouble, d -> (int) ((d - 0.5) * 500));
            final Engine<EnumGene<Integer>, Integer> engine = Engine.builder(task)
                .minimizing()
                .maximalPhenotypeAge(5)
                .alterers(new PartiallyMatchedCrossover<>(0.4), new Mutator<>(0.3))
                .build();
            final Phenotype<EnumGene<Integer>, Integer> result = engine.stream()
                .limit(bySteadyFitness(55))
                .collect(EvolutionResult.toBestPhenotype());
            log.debug(result.toString());
        }
    }
}
