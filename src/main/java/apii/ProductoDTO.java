package apii;

public class ProductoDTO {
    private String CodigoBarras;
    private String Categoria;
    private String Descripcion;
    private String Marca;
    private String Nombre;
    private Double Precio1;
    private Integer Inventario;
    private Double Costo;
    private Double Precio2;
    private Double Precio3;
    private String Proveedor;

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor con todos los campos
    public ProductoDTO(String CodigoBarras, String Categoria, String Descripcion, String Marca, String Nombre, Double Precio1, Integer Inventario, Double Costo, Double Precio2, Double Precio3, String Proveedor) {
        this.CodigoBarras = CodigoBarras;
        this.Categoria = Categoria;
        this.Descripcion = Descripcion;
        this.Marca = Marca;
        this.Nombre = Nombre;
        this.Precio1 = Precio1;
        this.Inventario = Inventario;
        this.Costo = Costo;
        this.Precio2 = Precio2;
        this.Precio3 = Precio3;
        this.Proveedor = Proveedor;
    }

    // Getters y Setters
    public String getCodigoBarras() {
        return CodigoBarras;
    }

    public void setCodigoBarras(String CodigoBarras) {
        this.CodigoBarras = CodigoBarras;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public Double getPrecio1() {
        return Precio1;
    }

    public void setPrecio1(Double Precio1) {
        this.Precio1 = Precio1;
    }

    public Integer getInventario() {
        return Inventario;
    }

    public void setInventario(Integer Inventario) {
        this.Inventario = Inventario;
    }

    public Double getCosto() {
        return Costo;
    }

    public void setCosto(Double Costo) {
        this.Costo = Costo;
    }

    public Double getPrecio2() {
        return Precio2;
    }

    public void setPrecio2(Double Precio2) {
        this.Precio2 = Precio2;
    }

    public Double getPrecio3() {
        return Precio3;
    }

    public void setPrecio3(Double Precio3) {
        this.Precio3 = Precio3;
    }

    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String Proveedor) {
        this.Proveedor = Proveedor;
    }
}
