/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arbolescomplementarios;
import java.util.*;
/**
 *
 * @author Juanjo
 */
public class IgualarArboles {
    public static void igualarHojas(ArbolBinario arbol1,ArbolBinario arbol2) {

        List<Integer> hojas1 = new ArrayList<>();
        List<Integer> hojas2 = new ArrayList<>();
        obtenerHojas(arbol1.raiz, hojas1);
        obtenerHojas(arbol2.raiz, hojas2);

        System.out.println("Hojas A: " + hojas1);
        System.out.println("Hojas B: " + hojas2);

        List<Integer> comun = lcs(hojas1, hojas2);
        Set<Integer> permitidas = new HashSet<>(comun);
        System.out.println("Hojas comunes: " + comun);

        podarArbol(arbol1.raiz, permitidas);
        podarArbol(arbol2.raiz, permitidas);

        System.out.println("Arbol 1 tras igualar:");
        arbol1.imprimirArbol();
        System.out.println("Arbol 2 tras igualar:");
        arbol1.imprimirArbol();
    }
    
    static void obtenerHojas(Nodo n, List<Integer> hojas) {
        if (n == null) return;
        if (n.izq == null && n.der == null) {
            hojas.add(n.dato);
        } else {
            obtenerHojas(n.izq, hojas);
            obtenerHojas(n.der, hojas);
        }
    }
    
    // Longest Common Subsequence (LCS)
    static List<Integer> lcs(List<Integer> a, List<Integer> b) {
        int n = a.size(), m = b.size();
        int[][] dp = new int[n+1][m+1];

        for (int i=1; i<=n; i++) {
            for (int j=1; j<=m; j++) {
                if (a.get(i-1).equals(b.get(j-1)))
                    dp[i][j] = dp[i-1][j-1] + 1;
                else
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }

        // reconstruir secuencia común
        List<Integer> res = new ArrayList<>();
        int i=n, j=m;
        while (i>0 && j>0) {
            if (a.get(i-1).equals(b.get(j-1))) {
                res.add(0, a.get(i-1));
                i--; j--;
            } else if (dp[i-1][j] > dp[i][j-1]) i--;
            else j--;
        }
        return res;
    }
    
    static Nodo podarArbol(Nodo n, Set<Integer> hojasPermitidas) {
        if (n == null) return null;

        n.izq = podarArbol(n.izq, hojasPermitidas);
        n.der = podarArbol(n.der, hojasPermitidas);

        // Si es hoja y no está permitida, eliminar
        if (n.izq == null && n.der == null && !hojasPermitidas.contains(n.dato)) {
            return null;
        }
        return n;
    }
}
