package com.example.DoencaChagas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<JavaFxApplication.StageReadyEvent> {

    @Value("classpath:/fxml/main.fxml")
    private org.springframework.core.io.Resource mainResource;

    private final ApplicationContext applicationContext;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(JavaFxApplication.StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(mainResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
            Parent parent = fxmlLoader.load();

            Stage stage = event.getStage();
            stage.setTitle("Sistema de Monitoramento - Doença de Chagas");
            
            // Set minimum width and height
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            Scene scene = new Scene(parent, 1024, 768);
            
            // Load custom CSS
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS não encontrado. Continuando sem estilos customizados.");
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar main.fxml", e);
        }
    }
}
