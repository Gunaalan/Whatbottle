package com.company.util;

import com.company.data.Location;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guna on 30/06/18.
 */
@Component
public class SwiggyUtils {
    public static double calculateDistance(Location orderLocation, Location daLocation) {
        return distance(orderLocation.getLat(), daLocation.getLat(), orderLocation.getLon(), daLocation.getLon(), 0, 0);

    }


    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }

//    public static void main(String args[]) {
//        String s ="aacecaaa";
//        int lastIndex =  s.length();
//
//        for(int i=lastIndex-1; i>=0; i--) {
//            if(isPalindrome(s,0,i)) {
//                String toAppend = s.substring(0,i+1);
//               String toreverse =  s.substring(i+1, s.length());
//               System.out.println(toAppend + "##");
//                System.out.println(reverseAString(toreverse) + "^^");
//
//            }
//        }
//    }
//
//    public static String reverseAString(String str) {
//        StringBuilder sb = new StringBuilder();
//
//        for(int i = str.length() - 1; i >= 0; i--)
//        {
//            sb.append(str.charAt(i));
//        }
//        return sb.toString();
//
//    }
//
//    public static boolean isPalindrome(String a, int start, int end) {
//        for (; start <= end; start++, end--) {
//            if (a.charAt(start) != a.charAt(end)) {
//                return false;
//            }
//        }
//        return true;
//    }

}
