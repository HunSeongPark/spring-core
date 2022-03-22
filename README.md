# spring-core               
스프링 핵심원리 강의 내용정리                    
강의 - [스프링 핵심 원리 - 기본편, 김영한 강사님](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/dashboard)                            

## README 정리 방식                 
실습 내용 중 중요하다 생각하는 부분 / 새롭게 알게 된 부분에 대한 내용을 기록       
* 이론과 관련한 부분은 pdf 기반으로 공부. 해당 repository에는 실습 관련 개인 공부용 정리              

## #1 객체지향원리 적용               

### [#1-1 OCP, DIP의 위반](https://github.com/HunSeongPark/spring-core/commit/9e0f545d1fdb3bad89371f7802b34cd6d8380193)             
- OCP(Open/Closed Principle, 개방-폐쇄 원칙) : 소프트웨어 요소는 확장에는 열려있고, 변경에는 닫혀있어야 한다.          
- DIP(Dependency Inversion Principle, 의존관계 역전 원칙) : 추상화에 의존하고, 구체화에 의존하면 안된다. 즉, 구현 클래스가 아닌 인터페이스에 의존해야한다.          

다음 코드는 DiscountPolicy라는 인터페이스에 의존하므로 DIP, OCP를 지킨 것 같아보이지만, 다음과 같은 이유로 DIP, OCP를 위반하는 코드이다.
1. 해당 클라이언트는 new 키워드를 통해 실제 구현 클래스의 인스턴스를 생성하고 있으므로 구현 클래스에도 의존하고 있다. -> DIP 위반                 
2. 해당 클라이언트는 DiscountPolicy 기능을 확장하여 변경할 때, 클라이언트의 코드를 변경하여야 한다. -> OCP 위반

```java
public class OrderServiceImpl implements OrderService {

//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    
}
```               
이를 해결하기 위해서는 누군가가 OrderServiceImpl에 DiscountPolicy의 구현객체를 대신 생성, 주입해주어야 한다. (후에 정리할 내용인 DI 컨테이너를 통해 해결)       

### [#1-2 본 예제에서 SOLID 원칙의 활용](https://github.com/HunSeongPark/spring-core/commit/7ef823d15bf4a193449ef47e5a988ea95659240a)                  
- SRP : 기존 OrderService 클라이언트 객체는 직접 구현객체를 생성 / 의존성 연결 / 실행하는 다수의 책임을 가지고 있었으나, AppConfig를 통해 구현객체의 생성/연결 책임을 넘기고 클라이언트는 오직 실행만을 수행하는 단일 책임의 원칙(SRP)을 지키게 됨                     
- DIP :  기존 OrderService 클라이언트 객체는 직접 구현객체를 생성함으로써 구체화에도 의존하고 있었으나, AppConfig를 통해 클라이언트는 구현객체를 직접 생성하지 않고 의존성을 주입받으며 추상화에만 의존하는 의존관계 역전 원칙(DIP)을 지키게 됨                      
- OCP : AppConfig를 통해 애플리케이션의 구성 영역을 클라이언트가 맡지 않게 되며 DiscountPolicy를 변경할 때 클라이언트 코드가 아닌 AppConfig 클래스의 코드를 변경하게 된다. 이를 통해 개방-폐쇄의 원칙(OCP)을 지키게 됨                 
```java               
public class AppConfig {

    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
    
}
```





