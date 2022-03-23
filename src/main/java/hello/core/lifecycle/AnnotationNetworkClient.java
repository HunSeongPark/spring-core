package hello.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Hunseong on 2022/03/23
 */
public class AnnotationNetworkClient {

    private String url;

    public AnnotationNetworkClient() {
        System.out.println("생성자 호출, url = " + url);
        connect();
        call("초기화 연결 메시지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message: " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    // 객체 생성, 의존관계 주입 완료 후 호출
    @PostConstruct
    public void init() {
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 소멸되기 직전 호출
    @PreDestroy
    public void close() {
        disconnect();
    }
}
