# spring-core               
스프링 핵심원리 강의 내용정리                    
강의 : [스프링 핵심 원리 - 기본편, 김영한 강사님](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/dashboard)                            

## README 정리 방식                 
실습 내용 중 중요하다 생각하는 부분 / 새롭게 알게 된 부분에 대한 내용을 기록       
* 이론과 관련한 부분은 pdf 기반으로 공부. 해당 repository에는 실습 관련 개인 공부용 정리             
* 해당 이론과 관련한 실습 코드는 커밋 링크                        

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

## #3 @ComponentScan / @Autowired                   
### [#3-1 @ComponentScan을 통한 빈 자동등록](https://github.com/HunSeongPark/spring-core/commit/d13210e445449ec3d4d8ae0974b276ebeac4efce)                   
- 설정 정보에 `@ComponentScan` 어노테이션을 붙여주게 되면 `@Component` 어노테이션이 붙은 클래스를 스캔해 자동으로 스프링 빈으로 등록해준다.
- 기존에 `@Configuration` 어노테이션과 `@Bean` 어노테이션을 통해 수동으로 빈을 등록해주는 설정 정보인 AppConfig 클래스와 달리 아래처럼 아무 메서드 없이도 `@Component` 어노테이션이 붙은 클래스는 빈 등록이 이루어진다.
```java
@Configuration
@ComponentScan
public class AutoAppConfig {
    // 아무 메서드도 존재하지 않는다.
}
```
이로 인해 해당 테스트는 정상적으로 통과한다.                   
```java
@Test
void basicScan() {
   ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
   MemberService memberService = ac.getBean(MemberService.class);
   // MemberService 클래스에 @Component 어노테이션을 붙였으므로 컴포넌트스캔에 의해 Bean 등록이 자동으로 이루어짐 (OK)
   Assertions.assertThat(memberService).isInstanceOf(MemberService.class);
}
```
- `@Component`가 붙은 클래스의 빈 등록 시 기본 이름은 클래스명의 맨 앞글자를 소문자로 바꾼 이름을 사용한다. ex) MemberService -> memberService
- 만약 스프링 빈의 이름을 바꾸고 싶다면 `@Component("memberServiceNew")`와 같이 바꾸면 된다.
- `@ComponentScan(basePackages="hello.core")`와 같이 스캔을 수행할 패키지의 위치를 지정할 수 있다. 이 경우 hello.core 패키지 포함 하위 패키지를 스캔한다. 지정하지 않을 시 기본 스캔 패키지 위치는 해당 컴포넌트스캔 클래스가 존재하는 패키지이다.
- !! 권장하는 방법 : 패키지의 위치를 위와같이 명시적으로 지정하지 않고, 프로젝트 루트 위치에 컴포넌트 스캔 클래스를 두어 프로젝트 패키지를 basePackage로 지정한다.              
- `@Controller, @Service, @Repository, @Configuration` 어노테이션도 `@Component`를 포함하고 있어 컴포넌트스캔 대상이 된다.

### [#3-2 @Autowired를 통한 의존관계 자동 주입](https://github.com/HunSeongPark/spring-core/commit/d13210e445449ec3d4d8ae0974b276ebeac4efce)  
- 다음과 같이 생성자, setter에 `@Autowired` 어노테이션을 붙이면 스프링 컨테이너가 자동으로 컨테이너에 존재하는 해당 타입의 스프링 빈을 찾아서 주입한다.
```java
@Component
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired // 자동으로 스프링 컨테이너 내의 MemberRepository 빈을 찾아 의존성 주입
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

```
- 생성자, setter, 필드, 메서드 주입에서 `@Autowired` 어노테이션을 통한 의존관계 자동 주입이 가능하다.
- 일반적으로 생성자 주입은 불변, 필수 의존관계에 사용 / setter 주입은 선택, 변경 가능성이 있는 의존관계에 사용한다. 그 외 필드, 메서드 주입은 권장하지 않는다.                   
필드 주입 : 외부에서 해당 값에 대한 변경이 불가능하므로 테스트가 힘들다.                  
메서드 주입 : 생성자 주입을 주로 사용한다.               
- ! 생성자 주입에서 `@Autowired` 사용 시 생성자가 하나만 있을 경우 해당 어노테이션의 생략이 가능하다.
- 주입할 스프링 빈이 컨테이너에 존재하지 않더라도 동작해야 할 경우, `@Autowired(required=false)`를 통해 옵셔널하게 처리할 수 있다. 이 때 스프링 빈이 존재하지 않아 의존성 자동 주입이 불가능 할 경우 처리하는 방식에는 크게 3가지가 있다.
```java
@Autowired(required = false)
public void setNoBean1(Member member) {
    // 자동 주입할 대상이 없으므로 setter 메서드 자체가 호출되지 않는다.
}

@Autowired(required = false)
public void setNoBean2(@Nullable Member member) {
    // 자동 주입 대상이 없으면 null이 member에 들어온다.
}

@Autowired(required = false)
public void setNoBean3(Optional<Member> member) {
    // 자동 주입 대상이 없으면 Optional.empty가 member에 들어온다.
}
```
- 주로 생성자 주입 방식을 택한다. 대부분의 의존관계 주입은 애플리케이션 종료 시점까지 변하지 않아야 하는 경우가 많아 생성 시점에 딱 1번만 호출이 보장되고 final 키워드를 사용할 수 있는 생성자 주입을 많이 사용한다(불변). 또한 프레임워크 없이 순수 자바코드를 통한 유닛테스트에서 의존관계 주입이 누락되었을 때 생성자 주입의 경우 컴파일 오류를 띄워준다(누락방지).
- `@Autowired` 어노테이션을 통한 의존관계 자동주입 시 빈이 충돌하는 경우 다음과 같은 3가지 해결 방법이 있다.
1. `@Autowired` 어노테이션은 타입 매칭에서 여러 빈이 존재할 경우 필드 이름, 파라미터 이름으로 추가 매칭을 시도한다. 빈이 충돌할 경우 필드 명을 주입하고자 하는 빈의 이름으로 변경한다.
2. `@Qualifier("")` 어노테이션을 통해 추가 구분자를 붙여준다.
```java
@Component
@Qualifier("rateDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {}

@Autowired
public DiscountPolicy setDiscountPolicy(@Qualifier("rateDiscountPolicy") DiscountPolicy discountPolicy) {
    ...
}
```
3. `@Primary` 어노테이션을 붙여 충돌하는 빈 중 우선순위(우선권을 가지고 매칭되는 빈)를 설정한다.                           

## #4 스프링 Bean의 생명주기와 콜백                         
### [#4-1 Bean의 생명주기와 초기화, 종료 작업](https://github.com/HunSeongPark/spring-core/commit/c7140cd4df55750c1701c020d3fd4158a100c512)              
- DB 커넥션 풀이나 네트워크 소켓과 같이 애플리케이션 시작 시점에 초기화 메소드를 수행하고, 애플리케이션 종료 시점에 종료 메소드를 수행하는 것이 필요할 때가 많이 있다.
- 스프링 Bean은 `객체 생성 -> 의존관계 주입`의 과정을 거친다.
- 따라서 스프링에서 지원하는 다양한 방식의 콜백을 통해 다음과 같은 lifecycle을 가지게 할 수 있다.                               
`스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> (초기화 콜백) -> 사용 -> (소멸전 콜백) -> 종료`
- 크게 3가지 방법이 존재하는데, 아래에서 아라보자.                  
 
### [#4-2 스프링에서 지원하는 3가지 방식의 lifecycle callback](https://github.com/HunSeongPark/spring-core/commit/e5c5b47eec199b7b3276b8aca306e3810ee0c434)
! 참고 : 객체의 생성과 초기화를 분리하자. 생성자는 파라미터를 받고 메모리를 할당해 객체를 생성하는 책임만을 가지게하고, 생성된 값을 활용해 외부 커넥션 연결 등 무거운 초기화 작업을 수행하는 것은 초기화 메서드에게 넘기는 것이 좋다.                   
1. InitializingBean, DisposableBean 인터페이스를 구현
다음과 같이 InitializingBean, DisposableBean 인터페이스를 구현하게 되면 자동으로 의존관계 주입 후 afterPropertiesSet() 메서드가, 소멸 전 destroy() 메서드가 호출된다.
```java
public class InterfaceNetworkClient implements InitializingBean, DisposableBean {

    // 객체 생성, 의존관계 주입 완료 후 호출
    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결 메시지");
    }

    // 빈이 소멸되기 직전 호출
    @Override
    public void destroy() throws Exception {
        disconnect();
    }
}
```
단점 : 해당 인터페이스가 스프링 전용 인터페이스이므로 bean이 스프링 코드에 의존한다. 또한 override를 통해 콜백 메서드를 구현하므로 메서드 이름을 바꿀 수 없다. 코드를 직접 수정하여야 하므로 외부 라이브러리에 적용할 수 없다. 후술할 더 나은 방법들이 존재하므로 거의 사용하지 않는다.                     

2. @Bean(initMethod="", destroyMethod="")를 통해 초기화, 종료 메서드 이름을 지정한다
다음과 같이 Bean 어노테이션에 initMethod, destroyMethod 이름을 지정하면 각각 초기화 메서드, 종료 메서드로써 생명주기에 맞게 호출된다.
```java
@Configuration
    static class LifeCycleConfig {

        @Bean(initMethod="init", destroyMethod="close")
        public ConfigNetworkClient configNetworkClient() {
            ConfigNetworkClient networkClient = new ConfigNetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
```                 
해당 방법은 위 인터페이스 방식과 달리 메서드 이름을 자유롭게 줄 수 있고, bean이 스프링 코드에 의존하지 않는다는 장점이 있다. 또한, 코드를 수정하는 것이 아니라 설정 정보를 사용하므로 외부 라이브러리에도 적용 할 수 있다.                                   
! destoryMethod의 경우 기본값이 `(inferred)`으로 되어있다(추론). 대부분의 라이브러리는 종료 메서드 이름으로 `close`, `shutdown`을 가지므로 destoryMethod를 지정하지 않으면 추론 기능에 의해 `close`, `shutdown`이라는 이름의 메서드를 소멸 직전에 호출해준다.                     

3. @PostConstruct / @PreDestroy 어노테이션을 사용한다.                
다음과 같이 초기화, 종료 메서드에 각각 @PostConstruct, @PreDestroy 어노테이션을 지정하면 각각 초기화 메서드, 종료 메서드로써 생명주기에 맞게 호출된다.              
```java
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
```
가장 편리하게 콜백 메서드를 지정할 수 있는 방법으로, 최신 스프링에서 가장 권장하는 방법이기도 하다.                           
해당 어노테이션의 패키지는 `javax.annotation`으로, 스프링에 종속적인 기술이 아닌 JSR-250이라는 자바표준에 해당한다.                        
유일한 단점으로는 코드를 수정해야 하므로 외부 라이브러리에 적용할 수 없다는 것이다. 외부 라이브러리의 콜백메서드는 2번째 방법인 @Bean의 기능을 사용하고, 이외의 콜백 메서드는 해당 어노테이션을 권장한다.                                   
