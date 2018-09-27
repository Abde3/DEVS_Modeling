package Util;

import Model.NOCUnit.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NocUtil {

    public static <E> Stream<List<E>> combinationsDupl(List<E> list, int size) {
        if (size == 0) {
            return Stream.of(Collections.emptyList());
        } else {
            return list.stream().flatMap( head -> combinationsDupl(list, size - 1).map(
                        tail -> {
                            List<E> newList = new ArrayList(tail);
                            newList.add(0, head);
                            return newList;
                        }
                    )
            );
        }
    }

    public static <E> Stream<List<List<E>>> combinationsNoDupl(List<E> list) {
        return IntStream.rangeClosed(2, list.size()).mapToObj(i -> combinations(list, i));
    }

    private static <E> List<List<E>> combinations(List<E> motList, int longueur) {

        List<List<E>> resultat = new ArrayList<>();

        for (int i = 0; i < motList.size() ; i++) {
            if (longueur == 1)
                resultat.add(Collections.singletonList(motList.get(i)));
            else {

                List<E> listIntermediaire = motList.subList(i + 1, motList.size()  );
                List<List<E>> combiList = combinations(listIntermediaire, longueur - 1);
                for (List<E> s : combiList) {
                    List<E> newList = new ArrayList(s);
                    newList.add(0, motList.get(i));
                    resultat.add(newList);
                }
            }
        }
        return resultat;

    }

    public static List<String> directionsToPortsName(List<String> directions, boolean isInput, Type dataType) {
        return directions
                .stream()
                .map( direction -> directionsToPortsName( direction, isInput, dataType ) )
                .collect( Collectors.toList() );
    }

    public static String directionsToPortsName(String direction, boolean isInput, Type dataType) {
        return  (isInput ? "INPUT-" : "OUTPUT-") + dataType.toString() + "-" + direction;
    }

    public static String portsNameToDirection(String portName) {
        return  portName.split("-", -1)[2];
    }

    public static boolean isCorrespondingPort(String fistPortName, String secondPortName) {
        return  portsNameToDirection(fistPortName).equals(portsNameToDirection(secondPortName));
    }

    public static Boolean alwaysTrue(Object o) {
        return true;
    }

    public static Boolean alwaysFalse(Object o) {
        return false;
    }

}
