public class ArbolBinarioUsuarios {

    private NodoArbol raiz;

    public ArbolBinarioUsuarios() {
        this.raiz = null;
    }

    public void insertar(Usuario usuario) {
        raiz = insertarRec(raiz, usuario);
    }

    private NodoArbol insertarRec(NodoArbol raiz, Usuario usuario) {
        if (raiz == null) {
            return new NodoArbol(usuario);
        }
        if (usuario.getNumeroDocumento().compareTo(raiz.usuario.getNumeroDocumento()) < 0) {
            raiz.izquierdo = insertarRec(raiz.izquierdo, usuario);
        } else if (usuario.getNumeroDocumento().compareTo(raiz.usuario.getNumeroDocumento()) > 0) {
            raiz.derecho = insertarRec(raiz.derecho, usuario);
        }
        return raiz;
    }

    public void obtenerReporteInOrder(StringBuilder sb) {
        if (raiz == null) {
            return; // El árbol está vacío, no hace nada
        }
        // Llamamos al metodo recursivo, pasandole la raiz y el string builder
        obtenerInOrderRec(this.raiz, sb);
    }

    private void obtenerInOrderRec(NodoArbol nodo, StringBuilder sb) {
        if (nodo != null) {
            obtenerInOrderRec(nodo.izquierdo, sb);     // 1. Izquierda (Menores)
            sb.append(" - ").append(nodo.usuario).append("\n"); // 2. Raíz (Actual)
            obtenerInOrderRec(nodo.derecho, sb);       // 3. Derecha (Mayores)
        }
    }
}