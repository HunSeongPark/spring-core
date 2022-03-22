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

### [#1-2 본 예제에서 SOLID 원칙의 활용 / IoC, DI Container](https://github.com/HunSeongPark/spring-core/commit/7ef823d15bf4a193449ef47e5a988ea95659240a)                  
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
- 위 코드에서 AppConfig는 해당 클라이언트의 객체 생성 및 연결을 담당하며(제어 흐름 관리), 클라이언트는 오직 주어진 로직만을 수행한다. 
- 위와 같이 AppConfig가 클라이언트의 프로그램의 흐름을 제어하는 것, 즉, 프로그램 자신이 아닌 외부에서 제어 흐름을 관리하는 것을 **IoC(제어의 역전)** 이라고 한다.                  
- AppConfig와 같이 객체를 생성/관리하며 의존관계를 연결해주는 것을 **DI 컨테이너** 라고 한다.               

## #2 스프링 컨테이너와 스프링 빈               
### [#2-1 스프링 컨테이너](https://github.com/HunSeongPark/spring-core/commit/80b3f3880ef6f9de3e0295343ec50dbb963843ed)               
- `ApplicationContext`를 스프링 컨테이너라고 한다.              
- `@Configuration`이 붙은 AppConfig를 스프링의 구성정보로 사용한다. 이 때, `@Bean` 어노테이션이 붙어있는 메서드를 모두 호출하여 반환된 객체를 스프링 컨테이너에 등록한다.            
- `@Bean` 어노테이션을 통해 스프링 컨테이너에 등록된 스프링 빈은 메서드명을 이름으로 사용한다.           
- 스프링 빈은 `applicationContext.getBean()` 메서드를 통해 해당 객체를 찾을 수 있다.                     

### [#2-2 스프링 빈](https://github.com/HunSeongPark/spring-core/commit/80b3f3880ef6f9de3e0295343ec50dbb963843ed)                      
- `@Bean` 어노테이션을 통해 스프링 컨테이너에 등록된 스프링 빈은 메서드명을 이름으로 사용한다.           
- 빈 이름을 직접 부여할 수도 있다. `@Bean(name="hi")`
- 빈 이름이 중복된다면 다른 빈이 무시되거나, 기존 빈을 덮는 등 설정에 따라 오류가 발생한다.                          
- 스프링 빈은 `applicationContext.getBean()` 메서드를 통해 해당 객체를 찾을 수 있다.                         

### [#2-3 스프링 컨테이너와 싱글톤 1](https://github.com/HunSeongPark/spring-core/commit/d13210e445449ec3d4d8ae0974b276ebeac4efce)                            
- 싱글톤(Singleton) 패턴 : 클래스의 인스턴스가 1개만 생성되는 것을 보장하는 디자인 패턴                        
- 일반적인 싱글톤 패턴의 문제점 
1. 싱글톤 패턴을 구현하는 코드의 양이 증가
2. 의존관계 상 클라이언트가 구현 클래스에 의존 (내부적으로 instance를 생성하여 가지고 있어야 함) -> DIP, OCP 위반
3. 테스트의 어려움
4. 내부 속성의 변경 및 초기화가 어려움
5. 1개의 인스턴스만이 존재해야 하므로 외부에서 객체 생성을 막기 위해 private 생성자를 사용함으로써 자식 클래스의 생성이 어려움
- 스프링 컨테이너는 위 싱글톤 패턴의 문제점을 해결하면서 빈을 싱글톤으로 관리한다.         
```java
@Test
    @DisplayName("스프링 컨테이너")
    void springContainer() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // memberService1 == memberService2 (OK)
        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
```
결과                      
```java
memberService1 = hello.core.member.MemberServiceImpl@436390f4
memberService2 = hello.core.member.MemberServiceImpl@436390f4
```
bean을 2번 조회할 때 모두 같은 객체를 반환함을 알 수 있다.       
- 이렇게 싱글톤 객체를 생성/관리하는 기능을 **싱글톤 레지스트리** 라고 한다.
- 이러한 싱글톤 패턴을 제공하는 스프링 컨테이너로 인해 각 요청마다 새롭게 객체를 생성함으로써 생기는 메모리 낭비를 막고 효율적인 객체의 재사용이 가능하다.
- !! 싱글톤 방식은 여러 클라이언트가 하나의 객체를 공유하므로, 상태가 유지되도록(Stateful) 설계하면 안된다.                     
ex) A가 10000원 주문 -> B가 20000원 주문 -> A가 주문금액 조회 시 상태 유지로 인해 자신의 주문금액인 10000원이 아닌 20000원을 반환 받는 문제가 발생            

### [#2-4 스프링 컨테이너와 싱글톤 2](https://github.com/HunSeongPark/spring-core/commit/d13210e445449ec3d4d8ae0974b276ebeac4efce)                            
- 스프링 컨테이너는 `@Configuration` 어노테이션이 붙은 클래스에 바이트코드를 조작하는 CGLIB(Byte Code Generation Library) 기술을 통해 싱글톤을 보장한다.
- 실제로 스프링 컨테이너는 `@Configuration` 어노테이션이 붙은 클래스를 상속받은 임의의 클래스를 만들고, CGLIB 기술을 통해 다음과 같은 흐름의 로직을 추가하고 그 클래스를 빈으로 등록한 것이다.
```java
if (해당 객체가 스프링 컨테이너에 등록되어 있다면) {
    return 스프링 컨테이너에서 해당 객체를 찾아 반환
} else {
    return 기존 로직을 호출하여 새로운 객체를 생성하고 스프링 컨테이너에 등록 후 반환
}
```

내가 만든 AppConfig.class가 아닌 AppConfig를 상속받은 임의의 다른 클래스가 등록되어 있음
```java
bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$ae12a2ec              
```

- 이러한 원리를 통해 알 수 있는 것 : `@Configuration` 어노테이션을 적용하지 않고 `@Bean` 어노테이션 만으로 빈을 등록 시 순수하게 만든 클래스가 등록되고, CGLIB 기술이 적용되지 않아 싱글톤이 보장되지 않는다.
