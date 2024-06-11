package com.pcroom.pcproject.model.dto;

public class SeatDto{
    private int seat_id;
    private int seat_number;
    private int active;

    // 생성자
    public SeatDto(int seat_id, int seat_number, int active) {
        this.seat_id = seat_id;
        this.seat_number = seat_number;
        this.active = active;
    }

    public SeatDto() {

    }

    // Getter 및 Setter
    public int getSeatId() {
        return seat_id;
    }

    public void setSeatId(int seatId) {
        this.seat_id = seatId;
    }

    public int getSeatNumber() {
        return seat_number;
    }

    public void setSeatNumber(int seat_number) {
        this.seat_number = seat_number;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
