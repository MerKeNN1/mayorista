

package apii;

import com.google.cloud.Timestamp;
import java.util.List;


public class SolicitudDTO {
    private String clienteId;
    private List<ItemSolicitud> items;
    private Timestamp timestamp;
    private String estado; // Nuevo campo para el estado de la solicitud

    // Constructor vacío
    public SolicitudDTO() {}

    // Constructor con todos los campos
    public SolicitudDTO(String clienteId, List<ItemSolicitud> items, Timestamp timestamp, String estado) {
        this.clienteId = clienteId;
        this.items = items;
        this.timestamp = timestamp;
        this.estado = estado;
    }

    // Getters y Setters
    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemSolicitud> getItems() {
        return items;
    }

    public void setItems(List<ItemSolicitud> items) {
        this.items = items;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Clase interna para representar cada ítem de la solicitud
    public static class ItemSolicitud {
        private String CodigoBarras;
        private int cantidad;

        // Constructor vacío
        public ItemSolicitud() {}

        // Constructor con todos los campos
        public ItemSolicitud(String CodigoBarras, int cantidad) {
            this.CodigoBarras = CodigoBarras;
            this.cantidad = cantidad;
        }

        // Getters y Setters
        public String getCodigoBarras() {
            return CodigoBarras;
        }

        public void setCodigoBarras(String CodigoBarras) {
            this.CodigoBarras = CodigoBarras;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
    }
}
