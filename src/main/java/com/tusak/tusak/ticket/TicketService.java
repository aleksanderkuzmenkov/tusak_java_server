package com.tusak.tusak.ticket;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public Ticket createTicket(String email, int quantity) throws Exception {
        String qrCode = generateQRCode(email, quantity);
        Ticket ticket = new Ticket(null, email, quantity, qrCode);
        ticketRepository.save(ticket);
        sendEmailWithTicket(email, qrCode);
        return ticket;
    }

    private String generateQRCode(String email, int quantity) throws Exception {
        String text = "Email: " + email + ", Quantity: " + quantity;
        int width = 300;
        int height = 300;

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        String filePath = "src/main/resources/qrcodes/" + email + ".png";
        Files.write(Paths.get(filePath), pngData);

        return filePath;
    }

    private void sendEmailWithTicket(String toEmail, String qrCodePath) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Ваш билет");
        helper.setText("<p>Спасибо за покупку! Ваш билет:</p><img src='cid:qrcode' alt='QR Code'/>", true);

        FileSystemResource qrCodeResource = new FileSystemResource(qrCodePath);
        helper.addInline("qrcode", qrCodeResource, "image/png");

        emailSender.send(message);
    }

    public TicketOrder createTicketOrder(String email, int quantity, double pricePerTicket) {
        double totalAmount = quantity * pricePerTicket;
        TicketOrder order = new TicketOrder(null, email, quantity, totalAmount, "pending");
        // Здесь может быть логика проверки доступности билетов
        return order; // Предполагаем, что этот объект будет сохранён в базе данных
    }
}