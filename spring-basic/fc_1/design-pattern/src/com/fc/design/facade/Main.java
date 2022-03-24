package com.fc.design.facade;

/*
    - Facade 패턴은 여러개의 객체와 실제 사용하는 서브 객체의 사이에 복잡한 의존관계가 있을 때 중간에 두고 사용하는 방식.
    - 각각에 객체에 의존하는게 아닌 퍼사드 객체만 바라볼 수 있다.
    - 퍼사드 객체로 복잡한 의존성을 가진 객체들을 하나로 새로운 인터페이스 제공
 */
public class Main {

    public static void main(String[] args) {

        FTP ftp = new FTP("www.fc.co.kr", 22, "/home/etc");
//        ftp.connect();
//        ftp.moveDirectory();

        Reader reader = new Reader("text.tmp");
//        reader.fileConnect();
//        reader.fileRead();

        Writer writer = new Writer("text.tmp");
//        writer.fileConnect();
//        writer.write();
//
//        ftp.disConnect();
//        reader.fileDisConnect();
//        writer.fileDisConnect();

        SftpClient sftpClient = new SftpClient(ftp, reader, writer);
        sftpClient.connect();
        sftpClient.write();
        sftpClient.read();
        sftpClient.disConnect();

    }
}
