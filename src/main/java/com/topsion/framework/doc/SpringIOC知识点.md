### 什么是IoC
<img src="./png/img.png" alt="image" style="zoom:50%;" />

如图，一个“正常”的调用过程是由调用者创建Bean，进行调用IoC则是由框架创建对象注入给调用者。

### DI(Dependency Injection)和IoC(Inversion of Control)的关系

通过上边概念的理解，那么DI依赖注入和IoC的关系就很好区别了，IoC控制反转是通过DI依赖注入的方式实现的。所以说Matin Fowler 引入了依赖注入（DI）来更名IoC。

### Spring三种属性注入的方式
**Field注入，Setter注入， 构造器（Constructor）注入**

1. Field 注入是指我们给 Bean 里面某个变量赋值。
2. Setter 注入是提供了一个 setter 方法，调用 setXXX() 来注入值。
3. constructor 就是在构造器 / 构造函数里传入参数来进行注入。

#### xml配置声明Setter注入
```xml
<beans>
    <bean id="aservice" class="com.minis.test.AServiceImpl">
        <property type="String" name="property1" value="Hello World!"/>
    </bean>
</beans>
```
> 标签下引入了 标签，它又包含了 type、name 和 value，分别对应属性类型、属性名称以及赋值。你可以看一下这个 Bean 的代码。

对应java代码

```java
public class AServiceImpl {
    private String property1;

    public void setProperty1(String property1) {
        this.property1 = property1;
    }
}
```

#### 配置声明构造器注入

```xml
<beans>
    <bean id="aservice" class="com.minis.test.AServiceImpl">
        <constructor-arg type="String" name="name" value="abc"/>
        <constructor-arg type="int" name="level" value="3"/>
    </bean>
</beans>
```

> 与 Setter 注入类似，我们只是把 标签换成了 标签。

对应java代码
```java
public class AServiceImpl {
  
  private String name;
  private int level;

  public AServiceImpl(String name, int level) {
    this.name = name;
    this.level = level;
  }
}
```

> 注入操作的本质，就是给Bean的各个属性进行赋值，具体方式取决于实际情况，哪一种更加便捷就选择哪一种，
> 如果构造器注入满足不了对域的赋值，哪就可以和Setter搭配试用

### 依赖注入
> IoC技术代码核心是通过Java的反射机制调用构造器，以及Setter方法，在调用过程中根据具体类型
> 把属性值作为一个参数赋值进去。这也是所有框架实现IoC时的思路。**反射技术是IoC容器赖以工作的基础。**

如果属性本身是一个Bean，就牵扯到了Bean之间依赖的情况。**如何用Value这样一个简单的值表示某个对象中所有的域**Spring在标签里增加了**ref（引用）属性**记录了需要引用另外一个Bean。如下配置文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans>
    <bean id="basebaseservice" class="com.minis.test.BaseBaseService">
        <property type="com.minis.test.AServiceImpl" name="as" ref="aservice" />
    </bean>
    <bean id="aservice" class="com.minis.test.AServiceImpl">
        <constructor-arg type="String" name="name" value="abc"/>
        <constructor-arg type="int" name="level" value="3"/>
        <property type="String" name="property1" value="Someone says"/>
        <property type="String" name="property2" value="Hello World!"/>
        <property type="com.minis.test.BaseService" name="ref1" ref="baseservice"/>
    </bean>
    <bean id="baseservice" class="com.minis.test.BaseService">
        <!--引用了basebaseservice Bean-->
        <property type="com.minis.test.BaseBaseService" name="bbs" ref="basebaseservice" />
    </bean>
</beans>
```

### 循环依赖
如图

<img src="https://cdn.jsdelivr.net/gh/wenPKtalk/pictures@master/blog/20230822/23_08/image-20230822230804126.png" alt="image-20230822230804126" style="zoom:50%;" />

引入了一个**earlySingletonObjects**用来存放毛坯实例（属性未被赋值的实例）。在注入引用属性过程时从earlySingletonObjects先找到毛坯实例注入进去。

代码反应在：getBean(),createBean()和doCreateBean()中。

> createBean() 方法中调用了一个 doCreateBean(bd) 方法，
> 专门负责创建早期的毛胚实例。
> 毛胚实例创建好后会放在 earlySingletonObjects 结构中，
> 然后 createBean() 方法再调用 handleProperties() 补齐这些 property 的值。
> 在 getBean() 方法中，首先要判断有没有已经创建好的 bean，有的话直接取出来，如果没有就检查 earlySingletonObjects 中有没有相应的毛胚 Bean，有的话直接取出来，没有的话就去创建，并且会根据 Bean 之间的依赖关系把相关的 Bean 全部创建好。
> 很多资料把这个过程叫做 bean 的“三级缓存”，
> 这个术语来自于 Spring 源代码中的程序注释。实际上我们弄清楚了这个 getBean() 的过程后就会知道这段注释并不是很恰当。
> 只不过这是 Spring 发明人自己写下的注释，大家也都这么称呼而已。

#### 包装方法refresh()

在spring中，Bean是结合在一起同时创建完毕的，为了减少内部复杂性，Spring对外提供了一个包装方法**refresh()**。该方法对所有的Bean调用了一次getBean(),利用getBean()方法中的createBean()创建Bean的实例，这样就可以只用一个方法把容器中所有的Bean的实例创建出来。

