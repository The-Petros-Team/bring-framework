## <span style="color:green">Petros Bring Framework</span>
Bring Framework is an implementation of the Dependency Injection pattern.
Dependency Injection allows object that depends on one or more another objects to have them injected automatically,
so there is no need for a developer to maintain all the dependencies for a specific object.

Basic functionality:
1. <span style="color:green">_**Component**_</span> - generic stereotype annotation that allows to declare specific class as a bean
2. <span style="color:green">_**Service**_</span> - a bit more specific annotation that allows to declare specific service class as a bean
3. <span style="color:green">_**Repository**_</span> - another specific annotation that allows to declare specific repository/database-related class as a bean
4. <span style="color:green">_**Configuration**_</span> - a class marked via this annotation allows to create Java-based configuration classes that stores bean candidates as methods marked via _**Bean**_ annotation
5. <span style="color:green">_**Bean**_</span> - can be put only on methods in a configuration class marked via _**Configuration**_ annotation
6. <span style="color:green">_**Autowired**_</span> - enables dependency injection mechanism for the fields marked via this annotation (only field injection mechanism is supported currently)

To start using Bring Framework please ask Petros Team for username and password as the jar file is hosted in private repository

When all the configuration is ready run _**mvn clean install**_ command to download the dependencies.
And now you are ready to start!

To declare your class as a component you could use one of the following code snippet:
```
@Component
public class SuperFastDeliveryService implements DeliveryService {

    // implementation is omitted

}
```

You can declare your class as a service:
```
@Service
public class BasicUserService implements UserService {

    // implementation is omitted

}
```

It's also possible to declare your class as repository in case it interacts via database:
```
@Repository
public class BasicUserRepository implements UserRepository {

    // implementation is omitted

}
```

Bring also allows you to declare your dependencies as a Java configuration:
```
@Configuration
public class AppConfig {

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }
}
```

Moreover, you can use autowiring mechanism tao inject your dependencies into a class that is registered as a bean:
```
@Service
public class VisaPaymentService implements PaymentService {

    @Autowired
    private UserService userService;

    // implementation is omitted

}
```

If you need to additionally configure your bean before injection mechanism applied to all beans, you can implement
_**BeanPostProcessor**_ interface:
```
public class CustomBeanPostProcessor implements BeanPostProcessor {

    BeanReference postProcessBeforeInitialization(BeanReference beanReference) {
        // implement your logic here
        return beanReference; // you must return the same bean reference received on input
    }

    BeanReference postProcessAfterInitialization(BeanReference beanReference) {
        // implement your logic here
        return beanReference; // you must return the same bean reference received on input
    }

    int getOrder() {
        return 10; // specify an order of execution
    }
}
```
The order of bean post processors are predefined for Bring-specific post processors implementations. By default your
custom bean post processor will be linked to the end of the post processing chain. In case you would like to run it's
logic in a specific place just override getOrder() method.

After all configuration steps are done, it's time to create an application container. You just have to create it and
provide a package as a parameter using the following code snippet:
```
public class AppRunner {
    public static void main(String[] args) {
        ApplicationContext context = ApplicationContextContainer.create("specify.your.package.here");
        UserService userService = context.getBean(UserService.class);
        // ...
    }
}
```

Don't hesitate to help us with improving of our project. It's opensource and free to use. We're waiting for your contribution 😜
https://github.com/The-Petros-Team/bring-framework
