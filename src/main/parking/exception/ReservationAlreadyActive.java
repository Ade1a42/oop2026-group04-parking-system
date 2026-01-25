package parking.exception;

public class ReservationAlreadyActive extends Exception {
    public ReservationAlreadyActive(String message) {
        super(message);
    }
}
