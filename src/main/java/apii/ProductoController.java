package apii;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public List<ProductoDTO> getProductos() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference productosRef = db.collection("Productos");
        ApiFuture<QuerySnapshot> querySnapshot = productosRef.get();

        List<ProductoDTO> productos = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            // Crear un ProductoDTO solo con los campos necesarios
            ProductoDTO producto = new ProductoDTO(
                document.getString("CodigoBarras"),
                document.getString("Nombre"),
                document.getString("Descripción"),
                document.getString("Marca"),
                document.getString("Categoría"),
                document.getLong("Inventario").intValue(),
                document.getDouble("Precio2")
            );
            productos.add(producto);
        }
        return productos;
    }
}



//    @GetMapping
//    public List<ProductoDTO> getAllProductos() throws ExecutionException, InterruptedException {
//        Firestore db = FirestoreClient.getFirestore();
//        CollectionReference productos = db.collection("Productos");
//        ApiFuture<QuerySnapshot> query = productos.get();
//        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
//
//        /// Convertir los documentos a DTOs
//        return documents.stream().map(document -> {
//            ProductoDTO productoDTO = new ProductoDTO();
//            productoDTO.setCodigoBarras(document.getString("CodigoBarras"));
//            productoDTO.setCategoria(document.getString("Categoría"));
//            productoDTO.setDescripcion(document.getString("Descripción"));
//            productoDTO.setMarca(document.getString("Marca"));
//            productoDTO.setNombre(document.getString("Nombre"));
//            productoDTO.setPrecio(document.getDouble("Precio1"));
//            productoDTO.setInventario(document.getLong("Inventario").intValue()); // Convertir Long a Integer
//            return productoDTO;
//        }).collect(Collectors.toList());
//    }
    
//}
