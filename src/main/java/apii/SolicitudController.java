package apii;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @PostMapping
public ResponseEntity<SolicitudDTO> crearSolicitud(@RequestBody SolicitudDTO solicitud) throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference productosRef = db.collection("Productos");
    CollectionReference solicitudes = db.collection("Solicitudes");

    // Log de depuración para verificar el contenido del objeto solicitud
    System.out.println("Recibida solicitud de cliente: " + solicitud.getClienteId());
    for (SolicitudDTO.ItemSolicitud item : solicitud.getItems()) {
        System.out.println("Item - CodigoBarras: " + item.getCodigoBarras() + ", Cantidad: " + item.getCantidad());
    }

    // Validar el inventario para cada ítem solicitado
    for (SolicitudDTO.ItemSolicitud item : solicitud.getItems()) {
        Query query = productosRef.whereEqualTo("CodigoBarras", item.getCodigoBarras());
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        DocumentSnapshot productoSnapshot = documents.get(0);
        Long inventarioActual = productoSnapshot.getLong("Inventario");
        if (inventarioActual == null || item.getCantidad() > inventarioActual) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Si todo está bien, crea la solicitud
    Map<String, Object> solicitudMap = new HashMap<>();
    solicitudMap.put("clienteId", solicitud.getClienteId());
    solicitudMap.put("items", solicitud.getItems().stream().map(item -> {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("CodigoBarras", item.getCodigoBarras());
        itemMap.put("cantidad", item.getCantidad());
        return itemMap;
    }).collect(Collectors.toList()));
    solicitudMap.put("timestamp", Timestamp.now());
    solicitudMap.put("estado", "PENDIENTE"); // Estado inicial

    ApiFuture<DocumentReference> result = solicitudes.add(solicitudMap);
    String solicitudId = result.get().getId();
    solicitud.setId(solicitudId);

    return ResponseEntity.status(HttpStatus.CREATED).body(solicitud);
}


    @GetMapping("/{clienteId}")
public ResponseEntity<List<SolicitudDTO>> obtenerSolicitudesPorCliente(@PathVariable String clienteId) throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference solicitudesRef = db.collection("Solicitudes");
    Query query = solicitudesRef.whereEqualTo("clienteId", clienteId);
    ApiFuture<QuerySnapshot> querySnapshot = query.get();
    List<SolicitudDTO> solicitudes = new ArrayList<>();
    for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
        SolicitudDTO solicitud = document.toObject(SolicitudDTO.class);
        if (solicitud != null) {
            solicitud.setId(document.getId()); // Asegúrate de que el ID se está asignando
            solicitudes.add(solicitud);
        }
    }
    return ResponseEntity.ok(solicitudes);
}


    @PostMapping("/{id}/pagar")
    public ResponseEntity<String> procesarPago(@PathVariable String id, @RequestBody Map<String, String> pagoRequest) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference solicitudRef = db.collection("Solicitudes").document(id);
        ApiFuture<DocumentSnapshot> future = solicitudRef.get();
        DocumentSnapshot solicitudSnapshot = future.get();

        if (solicitudSnapshot.exists() && "APROBADA".equals(solicitudSnapshot.getString("estado"))) {
            // Simulación de la recepción y verificación del pago
            String montoPago = pagoRequest.get("monto");
            System.out.println("Monto recibido para el pago: " + montoPago);

            // Actualizar el estado de la solicitud a "PAGADA"
            solicitudRef.update("estado", "PAGADA");

            // Descontar la cantidad del inventario en Firebase
            List<Map<String, Object>> items = (List<Map<String, Object>>) solicitudSnapshot.get("items");
            CollectionReference productosRef = db.collection("Productos");

            for (Map<String, Object> item : items) {
                String codigoBarras = (String) item.get("CodigoBarras");
                int cantidadSolicitada = ((Long) item.get("cantidad")).intValue();
                Query productoQuery = productosRef.whereEqualTo("CodigoBarras", codigoBarras);
                ApiFuture<QuerySnapshot> productoSnapshot = productoQuery.get();

                for (DocumentSnapshot productoDoc : productoSnapshot.get().getDocuments()) {
                    DocumentReference productoRef = productoDoc.getReference();
                    productoRef.update("Inventario", FieldValue.increment(-cantidadSolicitada));
                }
            }

            return ResponseEntity.ok("Pago recibido y solicitud " + id + " procesada con éxito. Inventario actualizado.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: La solicitud no existe o no está en estado APROBADA.");
        }
    }
}
