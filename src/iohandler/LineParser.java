package iohandler;

public interface LineParser<T> {
    T parse(String line);
}

