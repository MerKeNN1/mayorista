package apii;

public class ProductoDTO {
    private String CodigoBarras;
    private String Nombre;
    private String Descripción;
    private String Marca;
    private String Categoría;
    private Integer Inventario;
    private Double Precio2;

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor con los campos necesarios
    public ProductoDTO(String CodigoBarras, String Nombre, String Descripción, String Marca, String Categoría, Integer Inventario, Double Precio2) {
        this.CodigoBarras = CodigoBarras;
        this.Nombre = Nombre;
        this.Descripción = Descripción;
        this.Marca = Marca;
        this.Categoría = Categoría;
        this.Inventario = Inventario;
        this.Precio2 = Precio2;
    }

    // Getters y Setters
    public String getCodigoBarras() {
        return CodigoBarras;
    }

    public void setCodigoBarras(String CodigoBarras) {
        this.CodigoBarras = CodigoBarras;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDescripción() {
        return Descripción;
    }

    public void setDescripción(String Descripción) {
        this.Descripción = Descripción;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    public String getCategoría() {
        return Categoría;
    }

    public void setCategoría(String Categoría) {
        this.Categoría = Categoría;
    }

    public Integer getInventario() {
        return Inventario;
    }

    public void setInventario(Integer Inventario) {
        this.Inventario = Inventario;
    }

    public Double getPrecio2() {
        return Precio2;
    }

    public void setPrecio2(Double Precio2) {
        this.Precio2 = Precio2;
    }
}
