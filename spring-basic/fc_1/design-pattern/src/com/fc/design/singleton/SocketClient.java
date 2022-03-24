package com.fc.design.singleton;

/*
    - 싱글톤 패턴은 어떠한 클래스가 유일하게 1개만 존재할 때 (1개만 존재하는게 유용할 때) 사용한다.
    - 불필요한 객체 생성을 하지 않음으로써 메모리 부하를 줄인다.
    - ex. printer, TCP Socket 통신에서 서버와 연결된 connect 객체, DAO 등
    - 스프링에서 빈 객체들은 기본적으로 싱글톤으로 관리가 된다.
*/
public class SocketClient {

    private static SocketClient socketClient;

    private SocketClient() {}

    public static SocketClient getInstance() {
        if(socketClient == null) {
            socketClient = new SocketClient();
        }
        return socketClient;
    }

}
