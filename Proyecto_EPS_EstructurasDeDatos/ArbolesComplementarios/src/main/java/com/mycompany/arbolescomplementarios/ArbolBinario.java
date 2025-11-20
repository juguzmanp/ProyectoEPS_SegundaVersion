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
public class ArbolBinario {
    Nodo raiz;
    
    public ArbolBinario (int[] arr){
        if (arr == null || arr.length == 0) {
        }
        else{
            Nodo raiz = new Nodo(arr[0]);
            this.raiz = raiz;
            Queue<Nodo> colaNodos = new LinkedList<>();
            colaNodos.add(raiz);
            int i = 1;
            while(!colaNodos.isEmpty() && i < arr.length){
                Nodo nodoActual = colaNodos.poll();
                if (i < arr.length) {
                    nodoActual.izq = new Nodo(arr[i]);
                    i++;
                    colaNodos.add(nodoActual.izq);
                }
                if (i < arr.length) {
                    nodoActual.der = new Nodo(arr[i]);
                    i++;
                    colaNodos.add(nodoActual.der);
                }
            }
        }
    }
    
    public void imprimirArbol(){
        if (raiz != null){
            raiz.imprimirNodo();
        }
    }
}
