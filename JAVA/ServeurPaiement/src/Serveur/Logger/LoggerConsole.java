package Serveur.Logger;

public class LoggerConsole implements Logger {

    public LoggerConsole()
    {

    }
    @Override
    public void Trace(String message) {
        System.out.println(message);
    }
}
