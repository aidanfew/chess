package dataaccess;

import java.util.Objects;

public class DataAccessException extends RuntimeException {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataAccessException that = (DataAccessException) o;
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
    public DataAccessException(String message, int status) {
        super(message);
        this.status = status;
    }
}
