package com.tusak.tusak.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/api/tickets/create")
    public ResponseEntity<?> createTicketAndSendEmail(@RequestBody TicketRequest request) {
        try {
            // Проверка статуса платежа
            if ("paid".equals(request.getStatus())) {
                // Создание билета и отправка его на email
                Ticket ticket = ticketService.createTicket(request.getEmail(), request.getQuantity());
                return ResponseEntity.ok().body(ticket);
            } else {
                return ResponseEntity.badRequest().body("Платеж не был успешно завершен.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при создании билета: " + e.getMessage());
        }
    }
}
