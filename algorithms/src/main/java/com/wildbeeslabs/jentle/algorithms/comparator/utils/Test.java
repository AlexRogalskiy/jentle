package com.wildbeeslabs.jentle.algorithms.comparator.utils;

import com.wildbeeslabs.jentle.algorithms.comparator.DiffComparator;
import com.wildbeeslabs.jentle.algorithms.comparator.entity.AddressInfo;
import com.wildbeeslabs.jentle.algorithms.comparator.entity.DeliveryInfo;
import com.wildbeeslabs.jentle.algorithms.comparator.factory.DefaultDiffComparatorFactory;

import java.util.ArrayList;

public class Test {

    public static void main(final String[] args) {
        final AddressInfo ai = new AddressInfo();
        ai.setCity("Test3");
        ai.setZipCode("1245r");

        final AddressInfo ai2 = new AddressInfo();
        ai2.setCity("Test3");
        ai2.setZipCode("1245rr");

        final DeliveryInfo di = new DeliveryInfo();
        di.setAddressInfo(ai);

        final DeliveryInfo di2 = new DeliveryInfo();
        di2.setAddressInfo(ai2);

        final DiffComparator<DeliveryInfo> dc = DefaultDiffComparatorFactory.create(DeliveryInfo.class);
        final DiffComparator<AddressInfo> dc2 = DefaultDiffComparatorFactory.create(AddressInfo.class);
        System.out.println(new ArrayList(dc.diffCompare(di, di2)));
        System.out.println(new ArrayList(dc2.diffCompare(ai, ai2)));
    }
}
