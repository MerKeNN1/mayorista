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
import com.google.cloud.firestore.WriteResult;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudDTO solicitud) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference productosRef = db.collection("Productos");
        CollectionReference solicitudes = db.collection("Solicitudes");

        // Validar el inventario para cada ítem solicitado
        for (SolicitudDTO.ItemSolicitud item : solicitud.getItems()) {
            // Consulta el producto por CodigoBarras en lugar de productoId
            Query query = productosRef.whereEqualTo("CodigoBarras", item.getCodigoBarras());
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El producto " + item.getCodigoBarras() + " no existe.");
            }

            DocumentSnapshot productoSnapshot = documents.get(0);
            Long inventarioActual = productoSnapshot.getLong("Inventario");
            if (inventarioActual == null || item.getCantidad() > inventarioActual) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: No hay suficiente inventario para el producto " + item.getCodigoBarras());
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
        return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud creada con éxito con ID: " + result.get().getId());
    }


    @PutMapping("/{id}/autorizar")
    public String autorizarSolicitud(@PathVariable String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference solicitudRef = db.collection("Solicitudes").document(id);
        ApiFuture<DocumentSnapshot> future = solicitudRef.get();
        DocumentSnapshot solicitudSnapshot = future.get();

        if (solicitudSnapshot.exists() && "PENDIENTE".equals(solicitudSnapshot.getString("estado"))) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) solicitudSnapshot.get("items");
            CollectionReference productosRef = db.collection("Productos");

            // Verificar y actualizar el inventario
            for (Map<String, Object> item : items) {
                String productoId = (String) item.get("productoId");
                int cantidadSolicitada = ((Long) item.get("cantidad")).intValue();
                DocumentReference productoRef = productosRef.document(productoId);
                DocumentSnapshot productoSnapshot = productoRef.get().get();
                Long inventarioActual = productoSnapshot.getLong("Inventario");

                if (inventarioActual == null || inventarioActual < cantidadSolicitada) {
                    return "Error: No hay suficiente inventario para el producto " + productoId;
                }

                productoRef.update("Inventario", inventarioActual - cantidadSolicitada);
            }

            solicitudRef.update("estado", "APROBADA");
            return "Solicitud " + id + " autorizada con éxito.";
        } else {
            return "Error: La solicitud no existe o ya ha sido procesada.";
        }
    }

    @PutMapping("/{id}/denegar")
    public String denegarSolicitud(@PathVariable String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference solicitudRef = db.collection("Solicitudes").document(id);
        Map<String, Object> updates = new HashMap<>();
        updates.put("estado", "DENEGADA");
        ApiFuture<WriteResult> writeResult = solicitudRef.update(updates);
        return "Solicitud " + id + " denegada con éxito: " + writeResult.get().getUpdateTime();
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<String> recibirPagoFicticio(@PathVariable String id, @RequestBody PaymentDTO payment) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference solicitudRef = db.collection("Solicitudes").document(id);
        ApiFuture<DocumentSnapshot> future = solicitudRef.get();
        DocumentSnapshot solicitudSnapshot = future.get();

        if (solicitudSnapshot.exists() && "APROBADA".equals(solicitudSnapshot.getString("estado"))) {
            // Simulamos la verificación del pago
            boolean pagoVerificado = verificarPagoFicticio(payment);

            if (pagoVerificado) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) solicitudSnapshot.get("items");
                CollectionReference productosRef = db.collection("Productos");

                // Descontar la cantidad del inventario
                for (Map<String, Object> item : items) {
                    String productoId = (String) item.get("productoId");
                    int cantidadSolicitada = ((Long) item.get("cantidad")).intValue();
                    DocumentReference productoRef = productosRef.document(productoId);
                    productoRef.update("Inventario", FieldValue.increment(-cantidadSolicitada));
                }

                // Actualizar el estado de la solicitud a "PAGADA"
                solicitudRef.update("estado", "PAGADA");

                // Enviar notificación a la aplicación que usa la API
                enviarNotificacionInventarioActualizado(solicitudSnapshot);

                return ResponseEntity.ok("Pago recibido y solicitud " + id + " procesada con éxito.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El pago no se pudo verificar.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: La solicitud no existe o no está en estado APROBADA.");
        }
    }

    // Método simulado para verificar el pago
    private boolean verificarPagoFicticio(PaymentDTO payment) {
        // Simulación de verificación del pago
        // Aquí puedes añadir lógica para validar los detalles del pago si es necesario
        return true; // Simulación de verificación exitosa
    }

    // Método simulado para enviar notificación de inventario actualizado
    private void enviarNotificacionInventarioActualizado(DocumentSnapshot solicitudSnapshot) {
        // Lógica para enviar notificación a la aplicación externa
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://miaplicacion.com/api/inventario/actualizado";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("solicitudId", solicitudSnapshot.getId());
        notificationData.put("estado", "PAGADA");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);
        restTemplate.postForEntity(url, request, String.class);

        System.out.println("Inventario actualizado para la solicitud: " + solicitudSnapshot.getId());
    }
}
