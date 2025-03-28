import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScrapingServiceTest {
    
    @Test
    void testFindAttachments() {
        ScrapingService service = new ScrapingService();
        assertDoesNotThrow(() -> {
            List<Anexo> anexos = service.findAttachmentsOnWebsite();
            assertFalse(anexos.isEmpty(), "Deveria encontrar anexos");
        });
    }
}