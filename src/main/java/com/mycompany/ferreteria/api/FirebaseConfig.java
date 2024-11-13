package com.mycompany.ferreteria.api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public void firebaseInitialization() {
        try {
            // Cargar el archivo de credenciales
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/Mayoristav2.json");

            // Configurar las opciones de Firebase
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://mayorista-89074.firebaseio.com") // Asegúrate de colocar el nombre correcto de tu base de datos
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
