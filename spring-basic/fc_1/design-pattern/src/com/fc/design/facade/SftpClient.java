package com.fc.design.facade;

public class SftpClient {
    private FTP ftp;
    private Reader reader;
    private Writer writer;

    public SftpClient(FTP ftp, Reader reader, Writer writer) {
        this.ftp = ftp;
        this.reader = reader;
        this.writer = writer;
    }

    public SftpClient(String host, int port, String path, String fileName) {
        this.ftp = new FTP(host, port, path);
        this.reader = new Reader(fileName);
        this.writer = new Writer(fileName);
    }

    public void connect() {
        ftp.connect();
        ftp.moveDirectory();
        writer.fileConnect();
        reader.fileConnect();
    }

    public void disConnect() {
        writer.fileDisConnect();
        reader.fileDisConnect();
        ftp.disConnect();
    }

    public void read() {
        reader.fileRead();
    }

    public void write() {
        writer.write();
    }
}
