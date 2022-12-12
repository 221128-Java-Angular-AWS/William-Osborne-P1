package com.revature.pojos;

public class Ticket {
    private Integer ticketId;
    private Double amount;
    private String description;
    private String status;
    private Integer userId;


    public Ticket () {

    }
    public Ticket (Double amount, String description, String status, Integer userId) {
        // this constructor probably will not get used since status defaults to pending
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }

    public Ticket (Double amount, String description, Integer userId) {
        // this constructor will be used for ticket creation
        this.amount = amount;
        this.description = description;
        this.userId = userId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}
