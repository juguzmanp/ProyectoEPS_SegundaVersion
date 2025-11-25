public class NodoArbol {
    //El dato que guardamos (El objeto Usuario completo)
    Usuario usuario;

    //Los "hijos" del nodo
    NodoArbol izquierdo; //Para documentos menores
    NodoArbol derecho;   //Para documentos mayores

    public NodoArbol(Usuario usuario) {
        this.usuario = usuario;
        this.izquierdo = null;
        this.derecho = null;
    }
}
