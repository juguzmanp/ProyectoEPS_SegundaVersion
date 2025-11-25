public class ArbolBinarioUsuarios {

    private NodoArbol raiz; //El inicio del árbol

    public ArbolBinarioUsuarios() {
        this.raiz = null;
    }

    //--- MÉTODOS DE INSERCIÓN ---

    //Metodo publico que llama al recursivo
    public void insertar(Usuario usuario) {
        raiz = insertarRec(raiz, usuario);
    }

    //Metodo recursivo
    private NodoArbol insertarRec(NodoArbol actual, Usuario usuario) {
        // 1. Caso Base: Si llegamos a un hueco vacío, ahí va el nuevo nodo
        if (actual == null) {
            return new NodoArbol(usuario);
        }

        // 2. Comparación: Usamos el Número de Documento como criterio de orden
        // compareTo devuelve < 0 si es menor, > 0 si es mayor

        String docNuevo = usuario.getNumeroDocumento();
        String docActual = actual.usuario.getNumeroDocumento();

        if (docNuevo.compareTo(docActual) < 0) {
            // Si es MENOR, va a la IZQUIERDA
            actual.izquierdo = insertarRec(actual.izquierdo, usuario);
        } else if (docNuevo.compareTo(docActual) > 0) {
            // Si es MAYOR, va a la DERECHA
            actual.derecho = insertarRec(actual.derecho, usuario);
        }
        // (Si son iguales no hacemos nada o podríamos actualizar, aquí lo ignoramos para no duplicar)

        return actual;
    }

    //Recorrido In-Order
    //Esto garantiza que los datos salgan ordenados de menor a mayor
    public void mostrarInOrder() {
        if (raiz == null) {
            System.out.println("   (El directorio está vacío)");
        } else {
            mostrarInOrderRec(raiz);
        }
    }

    private void mostrarInOrderRec(NodoArbol nodo) {
        if (nodo != null) {
            mostrarInOrderRec(nodo.izquierdo);     //1. Visitar hijos menores
            System.out.println(" - " + nodo.usuario); //2. Mostrar datos del nodo actual
            mostrarInOrderRec(nodo.derecho);       //3. Visitar hijos mayores
        }
    }
}
