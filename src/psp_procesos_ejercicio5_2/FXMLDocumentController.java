package psp_procesos_ejercicio5_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FXMLDocumentController {

    @FXML
    private Button openCalcButton;

    @FXML
    private Button closeCalcButton;

    @FXML
    private Button openOtroProgramaButton;

    @FXML
    private Button closeOtroProgramaButton;

    private Process calculatorProcess;
    private Process otroProgramaProcess;  // Para el otro programa

    // Método para abrir la calculadora
    @FXML
    private void handleOpenCalc(ActionEvent event) throws IOException {
        if (calculatorProcess == null || !calculatorProcess.isAlive()) {
            ProcessBuilder builder = new ProcessBuilder("calc.exe");
            calculatorProcess = builder.start();
        }
    }

    // Método para cerrar la calculadora
    @FXML
    private void handleCloseCalc(ActionEvent event) {
        try {
            ProcessBuilder builder = new ProcessBuilder("taskkill", "/IM", "CalculatorApp.exe", "/F");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Muestra la salida del comando
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para abrir OtroPrograma
    @FXML
    private void handleOpenOtroPrograma(ActionEvent event) {
        try {
            if (otroProgramaProcess == null || !otroProgramaProcess.isAlive()) {
                // Cambia "build/classes" a la carpeta donde están tus archivos .class compilados
                String classpath = "build/classes";

                // Usa ProcessBuilder para iniciar el otro programa
                ProcessBuilder builder = new ProcessBuilder("java", "-cp", classpath, "otroPrograma.OtroPrograma");
                otroProgramaProcess = builder.start(); // Inicia el proceso

                // Ejecuta la lectura de la salida en un hilo separado
                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(otroProgramaProcess.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);  // Muestra la salida del comando
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                System.out.println("OtroPrograma ya está en ejecución.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cerrar OtroPrograma
    @FXML
    private void handleCloseOtroPrograma(ActionEvent event) {
        if (otroProgramaProcess != null && otroProgramaProcess.isAlive()) {
            otroProgramaProcess.destroy();  // Cierra el proceso
            System.out.println("OtroPrograma cerrado.");
        } else {
            System.out.println("OtroPrograma no está en ejecución.");
        }
    }

}
