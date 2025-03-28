package service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document; 
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Anexo;
import model.ScrapingResult;

public class ScrapingService {
    private static final String TARGET_URL = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String DOWNLOAD_DIR = "downloads";
    private static final String ZIP_FILE = "anexos.zip";
    private static final int DOWNLOAD_TIMEOUT_SECONDS = 30;

    private final FileService fileService;
    private final ExecutorService executor;

    public ScrapingService() {
        this.fileService = new FileService();
        this.executor = Executors.newFixedThreadPool(4); 
    }

    public ScrapingResult executeFullProcess() {
        long startTime = System.currentTimeMillis();
        try {
            // 1. 
            fileService.createDirectoryIfNotExists(DOWNLOAD_DIR);
            
            // 2. 
            List<Anexo> attachments = findAttachmentsOnWebsite();
            downloadAttachments(attachments);
            
            // 3. 
            String fullZipPath = Paths.get(DOWNLOAD_DIR, ZIP_FILE).toString();
            fileService.zipAttachments(attachments, fullZipPath);
            
            // 4. 
            long executionTime = System.currentTimeMillis() - startTime;
            return new ScrapingResult(true, "Process completed successfully", 
                                    attachments, fullZipPath, executionTime);
        } catch (Exception e) {
            // 5. Retornar erro
            return new ScrapingResult(false, "Error during process: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private List<Anexo> findAttachmentsOnWebsite() throws IOException {
        Document document = Jsoup.connect(TARGET_URL)
                               .timeout(DOWNLOAD_TIMEOUT_SECONDS * 1000)
                               .get();
        Elements pdfLinks = document.select("a[href$=.pdf]");
        
        List<Anexo> attachments = new ArrayList<>();
        for (Element link : pdfLinks) {
            String url = link.attr("abs:href");
            String name = extractAttachmentName(link);
            
            if (name.contains("Anexo I") || name.contains("Anexo II")) {
                attachments.add(new Anexo(name, url));
            }
        }
        
        if (attachments.isEmpty()) {
            throw new IOException("No attachments I or II found on website");
        }
        
        return attachments;
    }

    private String extractAttachmentName(Element link) {
        String linkText = link.text().trim();
        if (linkText.isEmpty()) {
            String url = link.attr("abs:href");
            return url.substring(url.lastIndexOf('/') + 1);
        }
        return linkText + ".pdf";
    }

    private void downloadAttachments(List<Anexo> attachments) 
            throws InterruptedException, ExecutionException, TimeoutException {
        List<Future<?>> futures = new ArrayList<>();
        
        for (Anexo attachment : attachments) {
            futures.add(executor.submit(() -> downloadAndSetPath(attachment)));
        }
        
        for (Future<?> future : futures) {
            future.get(DOWNLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }
    }

    private void downloadAndSetPath(Anexo attachment) {
        try {
            String filePath = Paths.get(DOWNLOAD_DIR, attachment.getName()).toString();
            downloadFile(attachment.getUrl(), filePath);
            attachment.setPath(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file: " + attachment.getUrl(), e);
        }
    }

    private void downloadFile(String fileUrl, String destination) throws IOException {
        URL url = new URL(fileUrl);
        try (ReadableByteChannel channel = Channels.newChannel(url.openStream());
             FileOutputStream fos = new FileOutputStream(destination)) {
            fos.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
        }
    }

    public FileService getFileService() {
        return fileService;
    }
}