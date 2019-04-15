package com.wildbeeslabs.jentle.collections.set.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ObservableSet<E> extends ForwardingSet<E> {

    @FunctionalInterface
    public interface SetObserver<E> {
        void added(final ObservableSet<E> set, final E element);
    }

    public ObservableSet(final Set<E> set) {
        super(set);
    }

    private final List<SetObserver<E>> observers = new ArrayList<>();

    public void addObserver(final SetObserver<E> observer) {
        synchronized (this.observers) {
            this.observers.add(observer);
        }
    }

    public boolean removeObserver(final SetObserver<E> observer) {
        synchronized (this.observers) {
            return this.observers.remove(observer);
        }
    }

    private void notifyElementAdded(final E element) {
        synchronized (this.observers) {
            for (final SetObserver<E> observer : this.observers)
                observer.added(this, element);
        }
    }

//  private void notifyElementAdded(E element) {
//      List<SetObserver<E>> snapshot = null;
//      synchronized(observers) {
//          snapshot = new ArrayList<SetObserver<E>>(observers);
//      }
//      for (SetObserver<E> observer : snapshot)
//          observer.added(this, element);
//  }


//
//  private final List<SetObserver<E>> observers =
//      new CopyOnWriteArrayList<SetObserver<E>>();
//
//  public void addObserver(SetObserver<E> observer) {
//      observers.add(observer);
//  }
//  public boolean removeObserver(SetObserver<E> observer) {
//      return observers.remove(observer);
//  }
//  private void notifyElementAdded(E element) {
//      for (SetObserver<E> observer : observers)
//          observer.added(this, element);
//  }

    @Override
    public boolean add(final E element) {
        boolean added = super.add(element);
        if (added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean result = false;
        for (final E element : c) {
            result |= add(element);
        }
        return result;
    }
}
