package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Helper enumeration class to process Plants
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
public class Plant {
    public enum LifeCycle {ANNUAL, PERENNIAL, BIENNIAL}

    private final String name;
    private final LifeCycle lifeCycle;

    Plant(final String name, final LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return this.name;
    }

//    public static void main(String[] args) {
//        Plant[] garden = {
//            new Plant("Basil", LifeCycle.ANNUAL),
//            new Plant("Carroway", LifeCycle.BIENNIAL),
//            new Plant("Dill", LifeCycle.ANNUAL),
//            new Plant("Lavendar", LifeCycle.PERENNIAL),
//            new Plant("Parsley", LifeCycle.BIENNIAL),
//            new Plant("Rosemary", LifeCycle.PERENNIAL)
//        };
//
//        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
//            new EnumMap<>(Plant.LifeCycle.class);
//        for (Plant.LifeCycle lc : Plant.LifeCycle.values())
//            plantsByLifeCycle.put(lc, new HashSet<>());
//        for (Plant p : garden)
//            plantsByLifeCycle.get(p.lifeCycle).add(p);
//        System.out.println(plantsByLifeCycle);
//    }
}
