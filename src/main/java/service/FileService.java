package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.List;

import model.Anexo;

public class FileService {
    private static final int BUFFER_SIZE = 1024;

    public void zipAttachments (List<Anexo> attachments, String zipPath) throws IOException{
        
        if(attachments == null || attachments.isEmpty()){
            throw new IllegalArgumentException("Attachment list cannot be empty");
        }
        
        try(ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath))){
            byte[] buffer = new byte[BUFFER_SIZE];
            for(Anexo attachment : attachments){
                if(attachment.getPath() != null){
                    File file = new File(attachment.getPath());
                    addFileToZip(file, zipOut, buffer);
                }
            }
        }
    }

    private void addFileToZip(File file, ZipOutputStream zipOut, byte[] buffer) throws IOException{
        
        try(FileInputStream fis = new FileInputStream(file)){
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);
            int lenght;
            
            while((lenght = fis.read(buffer)) > 0){
                zipOut.write(buffer, 0, lenght);
            }
            
            zipOut.closeEntry();
        }
    }

    public void createDirectoryIfNotExists(String directoryPath) throws IOException{
        
        File directory = new File(directoryPath);
        
        if(!directory.exists()){
            if(!directory.mkdirs()){
                throw new IOException("Failed to create directory " + directoryPath);
            }
        }
    }
    
}
