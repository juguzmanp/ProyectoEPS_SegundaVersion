/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.arbolescomplementarios;

/**
 *
 * @author Juanjo
 */
public class ArbolesComplementarios {

    public static void main(String[] args) {
        int[] arreglo1 = {5,2,1,4,6,9};
        ArbolBinario arbol1 = new ArbolBinario(arreglo1);
        arbol1.imprimirArbol();
        int[] arreglo2 = {5,2,1,4,8,9};
        ArbolBinario arbol2 = new ArbolBinario(arreglo2);
        arbol2.imprimirArbol();
        IgualarArboles.igualarHojas(arbol1, arbol2);
    }
}
