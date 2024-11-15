package apii;

public class ProductoDTO {
    private String id;
    private String categoria;
    private String descripcion;
    private String marca;
    private String nombre;
    private Double precio;
    private Integer inventario;

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor con todos los campos
    public ProductoDTO(String id, String categoria, String descripcion, String marca, String nombre, Double precio,  Integer inventario) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.marca = marca;
        this.nombre = nombre;
        this.precio = precio;
        this.inventario = inventario;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }


    public Integer getInventario() {
        return inventario;
    }

    public void setInventario(Integer inventario) {
        this.inventario = inventario;
    }

}