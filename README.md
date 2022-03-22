# spring-core               
스프링 핵심원리 강의 내용정리                    
강의 - [스프링 핵심 원리 - 기본편, 김영한 강사님](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/dashboard)                            

## README 정리 방식                 
강의 내용을 해당 README에 정리 & 실습한 내용은 커밋 링크를 통해 함께 정리하여 참고할 수 있도록 함                    

## #1 객체지향원리 적용               

### #1-1 OCP, DIP의 위반             
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
이를 해결하기 위해서는 누군가가 OrderServiceImpl에 DiscountPolicy의 구현객체를 대신 생성, 주입해주어야 한다.           



