package hello.core.lifecycle;

/**
 * Created by Hunseong on 2022/03/23
 */
public class ConfigurationNetworkClient {

    private String url;

    public ConfigurationNetworkClient() {
        System.out.println("생성자 호출, url = " + url);
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

    // Configuration 콜백 설정을 통해 의존관계 주입 후 호출
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    // Configuration 콜백 설정을 통해 빈이 소멸되기 직전 호출
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
