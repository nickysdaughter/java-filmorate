package ru.yandex.practicum.filmorate.model;

public class Friendship {

    private Long idInviter;
    private Long idAcceptor;
    private boolean isConfirmed;

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public boolean isInitiatedBy(long userId) {
        return idInviter == userId;
    }
}
