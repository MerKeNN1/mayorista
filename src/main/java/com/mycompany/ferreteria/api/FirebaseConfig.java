package com.mycompany.ferreteria.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.ByteArrayInputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public void firebaseInitialization() {
        try {
            // Obtener las credenciales desde la variable de entorno 
            String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS"); 
            InputStream serviceAccount = new ByteArrayInputStream(firebaseCredentials.getBytes());

            // Obtener la URL de la base de datos desde la variable de entorno 
            String databaseUrl = System.getenv("FIREBASE_DATABASE_URL");
            
            // Configurar las opciones de Firebase
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl) // Asegúrate de colocar el nombre correcto de tu base de datos
                    .build();

            // Inicializar la aplicación de Firebase
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase App initialized successfully!");

            // Verificar conexión a Firestore
            Firestore db = FirestoreClient.getFirestore();
            System.out.println("Connected to Firestore: " + db.getOptions().getProjectId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
