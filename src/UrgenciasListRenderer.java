import javax.swing.*;
import java.awt.*;

// Clase que decide cómo se dibuja cada elemento en la lista de urgencias
class UrgenciasListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof UsuarioUrgencias) {
            UsuarioUrgencias u = (UsuarioUrgencias) value;
            int triage = u.getNivelTriage();

            // Asignación de colores según la prioridad (Triage)
            Color colorFondo;
            switch (triage) {
                case 1: colorFondo = new Color(255, 100, 100); break; // Rojo (Máxima)
                case 2: colorFondo = new Color(255, 200, 100); break; // Naranja
                case 3: colorFondo = new Color(255, 255, 100); break; // Amarillo
                case 4: colorFondo = new Color(200, 255, 200); break; // Verde
                case 5: colorFondo = new Color(200, 200, 255); break; // Azul (Mínima)
                default: colorFondo = Color.WHITE;
            }

            // Si el elemento no está seleccionado, aplicamos el color de fondo
            if (!isSelected) {
                label.setBackground(colorFondo);
            }
            label.setText("Triage " + triage + " - " + u.toString());
        }
        return label;
    }
}