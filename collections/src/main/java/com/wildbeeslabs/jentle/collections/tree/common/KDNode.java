//package com.wildbeeslabs.jentle.collections.tree.common;
//
//import java.awt.Point;
//
//public class KDNode {
//  final Point point;
//  final int direction;
//  Region region;
//  KDNode above;
//  KDNode below;
//  boolean deleted;
//
//  public static final int HORIZONTAL = 0;
//  public static final int VERTICAL = 1;
//
//  public KDNode(Point p, int dir, Region r) {
//    this.point = new Point (p);
//    this.direction = dir;
//
//    this.region = new Region(r);
//  }
//
//  public KDNode(Point p, int dir) {
//    this (p, dir, Region.max);
//  }
//
//  public boolean isBelow(Point p) {
//    if (direction == VERTICAL) {
//      return p.x < point.x;
//    } else {
//      return p.y < point.y;
//    }
//  }
//
//  public boolean isAbove(Point p) {
//    if (direction == VERTICAL) {
//      return p.x >= point.x;
//    } else {
//      return p.y >= point.y;
//    }
//  }
//
//  public boolean isDeleted() { return deleted; }
//
//  public voidboolean add (Point p) {
//    if (p.equals(point)) {
//      if (deleted) {
//        deleted = false;
//        return true;
//      }
//      return false;
//    }
//
//    if (isBelow(p)) {
//      if (below == null) {
//        below = createChild (p, true);
//        return true;
//      } else {
//        return below.add(p);
//      }
//    } else {
//      if (above == null) {
//        above = createChild (p, false);
//        return true;
//      } else {
//        return above.add(p);
//      }
//    }
//  }
//
//  KDNode createChild (Point p, boolean below) {
//    Region r = new Region (region);
//    if (direction == VERTICAL) {
//      if (below) {
//        r.x_max = point.x;
//      } else {
//        r.x_min = point.x;
//      }
//    } else {
//      if (below) {
//        r.y_max = point.y;
//      } else {
//        r.y_min = point.y;
//      }
//    }
//    return new KDNode(p, 1-direction, r);
//  }
//}
