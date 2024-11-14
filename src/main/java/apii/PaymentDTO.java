

package apii;


//@author om899

public class PaymentDTO {
    private String solicitudId; // ID de la solicitud que se está pagando
    private double monto; // Monto del pago realizado

    // Constructor vacío
    public PaymentDTO() {}

    // Constructor con parámetros
    public PaymentDTO(String solicitudId, double monto) {
        this.solicitudId = solicitudId;
        this.monto = monto;
    }

    // Getters y Setters
    public String getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(String solicitudId) {
        this.solicitudId = solicitudId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "solicitudId='" + solicitudId + '\'' +
                ", monto=" + monto +
                '}';
    }
}

