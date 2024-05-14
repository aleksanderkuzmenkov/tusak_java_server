package com.tusak.tusak.ticket;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TicketOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private int quantity;
    private double totalAmount; // Общая сумма заказа
    private String status; // Статус заказа, например, "pending", "paid", "failed"
}