package controller;
import model.ScrapingResult;
import service.ScrapingService;
public class ScrapingController {
    private final ScrapingService scrapingService;

    public ScrapingController() {
        this.scrapingService = new ScrapingService();
    }

    public ScrapingResult executeWebScrapingProcess() {
        return scrapingService.executeFullProcess();
    }

    //Método p/ exibir os resultados formatado
    public void displayResults(ScrapingResult result) {
        System.out.println("\n<--RESULTADO DO PROCESSAMENTO-->");
        System.out.println("Status: " + (result.isSucess() ? "SUCESSO" : "FALHA"));
        System.out.println("Mensagem: " + result.getMessage());
        
        if(result.isSucess()) {
            System.out.println("\nDetalhes:");
            System.out.println("- Arquivos baixados: " + result.getDowloadedCount());
            System.out.println("- Caminho do ZIP: " + result.getPathZip());
            System.out.println("- Tempo de execução: " + result.getExecutionTime() + "ms");
            
            System.out.println("\nAnexos processados:");
            result.getDownAttachments().forEach(anexo -> 
                System.out.println("  • " + anexo.getName() + " | URL: " + anexo.getUrl()));
        }
        
        System.out.println("\n<--FIM DO PROCESSO-->");
    }
}