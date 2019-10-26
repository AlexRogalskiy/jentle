//package com.wildbeeslabs.jentle.collections.array.impl;
//
////
//// To quickly find an index of an entry in the dynamic table with the given
//// contents an effective inverse mapping is needed. Here's a simple idea
//// behind such a mapping.
////
//// # The problem:
////
//// We have a queue with an O(1) lookup by index:
////
////     get: index -> x
////
//// What we want is an O(1) reverse lookup:
////
////     indexOf: x -> index
////
//// # Solution:
////
//// Let's store an inverse mapping in a Map<x, Integer>. This have a problem
//// that when a new element is added to the queue, all indexes in the map
//// become invalid. Namely, the new element is assigned with an index of 1,
//// and each index i, i > 1 becomes shifted by 1 to the left:
////
////     1, 1, 2, 3, ... , n-1, n
////
//// Re-establishing the invariant would seem to require a pass through the
//// map incrementing all indexes (map values) by 1, which is O(n).
////
//// The good news is we can do much better then this!
////
//// Let's create a single field of type long, called 'counter'. Then each
//// time a new element 'x' is added to the queue, a value of this field gets
//// incremented. Then the resulting value of the 'counter_x' is then put as a
//// value under key 'x' to the map:
////
////    map.put(x, counter_x)
////
//// It gives us a map that maps an element to a value the counter had at the
//// time the element had been added.
////
//// In order to retrieve an index of any element 'x' in the queue (at any
//// given time) we simply need to subtract the value (the snapshot of the
//// counter at the time when the 'x' was added) from the current value of the
//// counter. This operation basically answers the question:
////
////     How many elements ago 'x' was the tail of the queue?
////
//// Which is the same as its index in the queue now. Given, of course, it's
//// still in the queue.
////
//// I'm pretty sure in a real life long overflow will never happen, so it's
//// not too practical to add recalibrating code, but a pedantic person might
//// want to do so:
////
////     if (counter == Long.MAX_VALUE) {
////         recalibrate();
////     }
////
//// Where 'recalibrate()' goes through the table doing this:
////
////     value -= counter
////
//// That's given, of course, the size of the table itself is less than
//// Long.MAX_VALUE :-)
////
//private static final class Table {
//
//    private final Map<String, Map<String, Long>> map;
//    private final CircularBuffer<HeaderField> buffer;
//    private long counter = 1;
//
//    Table(int capacity) {
//        buffer = new CircularBuffer<>(capacity);
//        map = new HashMap<>(capacity);
//    }
//
//    void add(HeaderField f) {
//        buffer.add(f);
//        Map<String, Long> values = map.computeIfAbsent(f.name, k -> new HashMap<>());
//        values.put(f.value, counter++);
//    }
//
//    HeaderField get(int index) {
//        return buffer.get(index - 1);
//    }
//
//    int indexOf(String name, String value) {
//        Map<String, Long> values = map.get(name);
//        if (values == null) {
//            return 0;
//        }
//        Long index = values.get(value);
//        if (index != null) {
//            return (int) (counter - index);
//        } else {
//            assert !values.isEmpty();
//            Long any = values.values().iterator().next(); // Iterator allocation
//            return -(int) (counter - any);
//        }
//    }
//
//    HeaderField remove() {
//        HeaderField f = buffer.remove();
//        Map<String, Long> values = map.get(f.name);
//        Long index = values.remove(f.value);
//        assert index != null;
//        if (values.isEmpty()) {
//            map.remove(f.name);
//        }
//        return f;
//    }
//
//    int size() {
//        return buffer.size;
//    }
//
//    public void setCapacity(int capacity) {
//        buffer.resize(capacity);
//    }
//}


/*
// Convert to a Value Object (JDK-8046159)?
    static final class HeaderField {

        final String name;
        final String value;

        public HeaderField(String name) {
            this(name, "");
        }

        public HeaderField(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return value.isEmpty() ? name : name + ": " + value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HeaderField that = (HeaderField) o;
            return name.equals(that.name) && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + value.hashCode();
        }
    }
 */
