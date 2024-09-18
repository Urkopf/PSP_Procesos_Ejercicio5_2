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

    private Process calculatorProcess;

    @FXML
    private void handleOpenCalc(ActionEvent event) throws IOException {
        // Iniciar la calculadora
        if (calculatorProcess == null || !calculatorProcess.isAlive()) {
            ProcessBuilder builder = new ProcessBuilder("calc.exe");
            calculatorProcess = builder.start();
        }
    }

    @FXML
    private void handleCloseCalc(ActionEvent event) {
        try {
            // Usa el nombre del proceso en lugar del PID
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


    private String getCalculatorPID() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("tasklist", "/FI", "IMAGENAME eq calc.exe", "/FO", "CSV");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("calc.exe")) {
                String[] parts = line.split(",");
                return parts[1].replaceAll("\"", "");  // Extraer el PID
            }
        }
        return null;  // No se encontró el PID
    }
}
