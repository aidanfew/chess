package dataaccess;

import java.util.Objects;

public class BadRequestException extends RuntimeException {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BadRequestException that = (BadRequestException) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(status);
    }

    int status;
    public int getStatus() {
        return status;
    }
    public BadRequestException(String message, int status) {
        super(message);
        this.status = status;
    }
}
