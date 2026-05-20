package service;

public class ClearService {
    public void clear(UserService userService, GameService gameService) {
        userService.clear();
        gameService.clear();
    }
}
