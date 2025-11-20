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
public class Nodo {
    int dato;
    Nodo izq;
    Nodo der;
    
    public Nodo (int dato){
        this.dato = dato;
        this.izq = null;
        this.der = null;
    }
    
    public void imprimirNodo(){
    Queue<Nodo> colaImprimir = new LinkedList<>();
    colaImprimir.add(this);

    while (!colaImprimir.isEmpty()) {
        Nodo actualImprimir = colaImprimir.poll();
        System.out.print(actualImprimir.dato + " ");

        if (actualImprimir.izq != null) colaImprimir.add(actualImprimir.izq);
        if (actualImprimir.der != null) colaImprimir.add(actualImprimir.der);
        }
    } 
}
