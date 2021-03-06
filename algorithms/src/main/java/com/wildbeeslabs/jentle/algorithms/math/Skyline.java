//package com.wildbeeslabs.jentle.algorithms.math;
//
//import java.io.*;
//import java.util.*;
//import java.awt.Point;
//
//public class Skyline {
//
//  public static Collection<Building> retrieveInput(InputStream is) {
//    ArrayList<Building> buildings = new ArrayList<Building>();
//    Scanner sc = new Scanner (is);
//    while (sc.hasNextLine()) {
//      String s = sc.nextLine();
//      if (s.equals("")) { break; }
//
//      try {
//        StringTokenizer st = new StringTokenizer(s);
//        int left = Integer.valueOf(st.nextToken());
//        int right = Integer.valueOf(st.nextToken());
//        int height = Integer.valueOf(st.nextToken());
//
//        Building b = new Building (left, right, height);
//        buildings.add(b);
//      } catch (NumberFormatException nfe) {
//        System.err.println(" ** Ignoring " + s + ": all values must be integers.");
//      } catch (Exception e) {
//        System.err.println(" ** Ignoring " + s + ": " + e.getMessage());
//      }
//    }
//
//    return (buildings);
//  }
//
//  public static ArrayList<Edge> compute(Collection<Building> buildings) {
//    TreeSet<Integer> S = new TreeSet<Integer>();
//    HashMap<Integer,ArrayList<Building>> lefts = new HashMap<Integer,ArrayList<Building>>();
//    HashMap<Integer,ArrayList<Building>> rights = new HashMap<Integer,ArrayList<Building>>();
//    ArrayList<Building> list = null;
//    for (Building b : buildings) {
//      S.add(b.left);
//      list = lefts.get(b.left);
//      if (list == null) {
//        list = new ArrayList<Building>();
//        lefts.put(b.left, list);
//      }
//      list.add(b);
//
//      S.add(b.right);
//      list = rights.get(b.right);
//      if (list == null) {
//        list = new ArrayList<Building>();
//        rights.put(b.right, list);
//      }
//      list.add(b);
//    }
//
//    int left = 0, top = 0;
//    ArrayList<Edge>skyline = new ArrayList<Edge>();
//    ArrayList<Building> heightList = new ArrayList<Building>();
//    for (int x : S) {
//      if (heightList.isEmpty()) {
//        top = 0;
//      } else {
//        top = heightList.get(0).height;
//      }
//
//      list = rights.get(x);
//      if (list != null) {
//        for (Building b : list) {
//          heightList.remove(b);
//        }
//      }
//
//      list = lefts.get(x);
//      if (list != null) {
//        for (Building b : list) {
//          int i;
//          for (i = 0; i < heightList.size(); i++) {
//            if (heightList.get(i).height < b.height) {
//              heightList.add(i, b);
//              break;
//            }
//          }
//          if (i == heightList.size()) {
//            heightList.add(b);
//          }
//        }
//      }
//
//      int newTop;
//      if (heightList.isEmpty()) {
//        newTop = 0;
//      } else {
//        newTop = heightList.get(0).height;
//      }
//
//      if (top == 0) {
//        left = x;
//      } else if (top != newTop) {
//        Edge e = new Edge(new Point (left, top), new Point (x, top));
//        skyline.add(e);
//        left = x;
//      }
//    }
//    return (skyline);
//  }
//
//  public static Collection<Point> complete(ArrayList<Edge> skyline) {
//    ArrayList<Point> skylinepoints = new ArrayList<Point>();
//    if (skyline.isEmpty()) { return skylinepoints; }
//
//    int left = skyline.get(0).start.x;
//    int right = skyline.get(skyline.size()-1).end.x;
//
//    skylinepoints.add(new Point (left, 0));
//
//    for (int i = 0; i < skyline.size(); i++) {
//      Edge edge = skyline.get(i);
//      skylinepoints.add(edge.start);
//      skylinepoints.add(edge.end);
//
//      Edge nextEdge = null;
//      if (i+1 < skyline.size()) {
//        nextEdge = skyline.get(i+1);
//
//        if (edge.end.x != nextEdge.start.x) {
//          skylinepoints.add(new Point (edge.end.x, 0));
//          skylinepoints.add(new Point (nextEdge.start.x, 0));
//        }
//      }
//    }
//
//    skylinepoints.add(new Point (right, 0));
//    return (skylinepoints);
//  }
//
//  public static void main(String[] args) {
//    Collection<Building> buildings = retrieveInput(System.in);
//
//    ArrayList<Edge> skyline = compute(buildings);
//    Collection<Point> skylinepoints = complete(skyline);
//    for (Point p : skylinepoints) {
//      System.out.print("(" + p.x + "," + p.y + ") ");
//    }
//    for (Edge e : compute(buildings)) {
//       System.out.println(e);
//    }
//  }
//}
