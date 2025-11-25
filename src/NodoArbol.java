class NodoArbol {
    public Usuario usuario;
    NodoArbol izquierdo;
    NodoArbol derecho;

    public NodoArbol(Usuario usuario) {
        this.usuario = usuario;
        this.izquierdo = null;
        this.derecho = null;
    }
}