//package com.wildbeeslabs.jentle.collections.list;
//
//class ListStrings {
//    public static ListString wrap(final List<String> list) {
//        class Random extends AbstractList<String>
//            implements ListString, RandomAccess {
//            public int size() {
//                return list.size();
//            }
//
//            public String get(int i) {
//                return list.get(i);
//            }
//
//
//            public String set(int i, String s) {
//                return list.set(i, s);
//            }
//
//            public String remove(int i) {
//                return list.remove(i);
//            }
//
//            public void add(int i, String s) {
//                list.add(i, s);
//            }
//        }
//        class Sequential extends AbstractSequentialList<String>
//            implements ListString {
//            public int size() {
//                return list.size();
//            }
//
//            public ListIterator<String> listIterator(int index) {
//                final ListIterator<String> it = list.listIterator(index);
//                return new ListIterator<String>() {
//                    public void add(String s) {
//                        it.add(s);
//                    }
//
//                    public boolean hasNext() {
//                        return it.hasNext();
//                    }
//
//                    public boolean hasPrevious() {
//                        return it.hasPrevious();
//                    }
//
//                    public String next() {
//                        return it.next();
//                    }
//
//                    public int nextIndex() {
//                        return it.nextIndex();
//                    }
//
//                    public String previous() {
//                        return it.previous();
//                    }
//
//                    public int previousIndex() {
//                        return it.previousIndex();
//                    }
//
//                    public void remove() {
//                        it.remove();
//                    }
//
//                    public void set(String s) {
//                        it.set(s);
//                    }
//                };
//            }
//        }
//        return list instanceof RandomAccess ? new Random() : new Sequential();
//    }
//}
//
//class ArrayListString extends ArrayList<String> implements ListString {
//    public ArrayListString() {
//        super();
//    }
//
//    public ArrayListString(Collection<? extends String> c) {
//        super(c);
//    }
//
//    public ArrayListString(int capacity) {
//        super(capacity);
//    }
//}
//
