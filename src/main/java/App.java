package main.java;

import main.java.controller.ScrapingController;
import main.java.model.ScrapingResult;

public class App {
    public static void main(String[] args) {
        System.out.println("Iniciando processo de web scraping...");
        
        ScrapingController controller = new ScrapingController();
        ScrapingResult result = controller.executeWebScrapingProcess();
        
        // Exibe resultados 
        controller.displayResults(result);
        
        // Encerra o programa
        System.exit(result.isSucess() ? 0 : 1);
    }
}