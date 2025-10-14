# Table of Contents

## **Creational Patterns**
1. [Singleton Pattern](#1-singleton-pattern)
2. [Factory Method Pattern](#2-factory-method-pattern)
3. [Abstract Factory Pattern](#3-abstract-factory-pattern)
4. [Builder Pattern](#4-builder-pattern)
5. [Prototype Pattern](#5-prototype-pattern)

## **Structural Patterns**
6. [Adapter Pattern](#6-adapter-pattern)
7. [Decorator Pattern](#7-decorator-pattern)
8. [Facade Pattern](#8-facade-pattern)
9. [Proxy Pattern](#9-proxy-pattern)
10. [Composite Pattern](#10-composite-pattern)

## **Behavioral Patterns**
11. [Observer Pattern](#11-observer-pattern)
12. [Strategy Pattern](#12-strategy-pattern)


# Design Patterns in Java

## **Creational Patterns**

---

## **1. Singleton Pattern**

### **Pattern**
Singleton

### **Definition**
Ensures a class has only one instance and provides a global point of access to it.

### **Type of Pattern**
Creational Pattern

### **Structure**
- Private static instance variable
- Private constructor
- Public static getInstance() method

### **Code**
```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {
        // private constructor to prevent instantiation
    }
    
    // Lazy initialization
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    
    // Thread-safe version with double-checked locking
    public static Singleton getThreadSafeInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    
    // Eager initialization
    public static class EagerSingleton {
        private static final EagerSingleton INSTANCE = new EagerSingleton();
        
        private EagerSingleton() {}
        
        public static EagerSingleton getInstance() {
            return INSTANCE;
        }
    }
}
```

### **Usecases**
- Database connections
- Logger classes
- Configuration settings
- Cache implementations
- Thread pools

### **Advantages**
- Controlled access to sole instance
- Reduced namespace pollution
- Permits refinement of operations and representation
- Lazy initialization possible

### **Disadvantages**
- Violates Single Responsibility Principle
- Can be difficult to unit test
- Can introduce hidden dependencies
- Thread safety concerns in multithreaded environments

---

## **2. Factory Method Pattern**

### **Pattern**
Factory Method

### **Definition**
Defines an interface for creating an object, but lets subclasses decide which class to instantiate.

### **Type of Pattern**
Creational Pattern

### **Structure**
- Creator (abstract class/interface)
- ConcreteCreator (implements factory method)
- Product (interface)
- ConcreteProduct (implements Product)

### **Code**
```java
// Product interface
interface Vehicle {
    void manufacture();
}

// Concrete Products
class Car implements Vehicle {
    public void manufacture() {
        System.out.println("Manufacturing Car");
    }
}

class Motorcycle implements Vehicle {
    public void manufacture() {
        System.out.println("Manufacturing Motorcycle");
    }
}

class Truck implements Vehicle {
    public void manufacture() {
        System.out.println("Manufacturing Truck");
    }
}

// Creator
abstract class VehicleFactory {
    // Factory method
    public abstract Vehicle createVehicle();
    
    public void deliverVehicle() {
        Vehicle vehicle = createVehicle();
        vehicle.manufacture();
        System.out.println("Vehicle delivered!");
    }
}

// Concrete Creators
class CarFactory extends VehicleFactory {
    public Vehicle createVehicle() {
        return new Car();
    }
}

class MotorcycleFactory extends VehicleFactory {
    public Vehicle createVehicle() {
        return new Motorcycle();
    }
}

class TruckFactory extends VehicleFactory {
    public Vehicle createVehicle() {
        return new Truck();
    }
}

// Client code
public class FactoryClient {
    public static void main(String[] args) {
        VehicleFactory carFactory = new CarFactory();
        carFactory.deliverVehicle();
        
        VehicleFactory bikeFactory = new MotorcycleFactory();
        bikeFactory.deliverVehicle();
    }
}
```

### **Usecases**
- Framework development
- When a class can't anticipate the class of objects it must create
- When classes delegate responsibility to helper subclasses
- UI toolkit development

### **Advantages**
- Loose coupling between creator and concrete products
- Open/Closed Principle compliance
- Single Responsibility Principle compliance
- Easy to extend with new product types

### **Disadvantages**
- Can lead to too many subclasses
- Increased complexity
- Code may become harder to read

---

## **3. Abstract Factory Pattern**

### **Pattern**
Abstract Factory

### **Definition**
Provides an interface for creating families of related or dependent objects without specifying their concrete classes.

### **Type of Pattern**
Creational Pattern

### **Structure**
- AbstractFactory (interface)
- ConcreteFactory (implements AbstractFactory)
- AbstractProduct (interface)
- ConcreteProduct (implements AbstractProduct)

### **Code**
```java
// Abstract Products
interface Button {
    void paint();
}

interface Checkbox {
    void paint();
}

// Concrete Products for Windows
class WindowsButton implements Button {
    public void paint() {
        System.out.println("Rendering a Windows style button");
    }
}

class WindowsCheckbox implements Checkbox {
    public void paint() {
        System.out.println("Rendering a Windows style checkbox");
    }
}

// Concrete Products for Mac
class MacButton implements Button {
    public void paint() {
        System.out.println("Rendering a Mac style button");
    }
}

class MacCheckbox implements Checkbox {
    public void paint() {
        System.out.println("Rendering a Mac style checkbox");
    }
}

// Abstract Factory
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

// Concrete Factories
class WindowsFactory implements GUIFactory {
    public Button createButton() {
        return new WindowsButton();
    }
    
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}

class MacFactory implements GUIFactory {
    public Button createButton() {
        return new MacButton();
    }
    
    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}

// Client
class Application {
    private Button button;
    private Checkbox checkbox;
    
    public Application(GUIFactory factory) {
        button = factory.createButton();
        checkbox = factory.createCheckbox();
    }
    
    public void paint() {
        button.paint();
        checkbox.paint();
    }
}
```

### **Usecases**
- Cross-platform UI toolkits
- Database abstraction layers
- System configuration families
- Theme systems

### **Advantages**
- Ensures compatibility between products
- Isolates concrete classes
- Makes exchanging product families easy
- Promotes consistency among products

### **Disadvantages**
- Supporting new kinds of products is difficult
- Increased complexity
- Can be overkill for simple object creation

---

## **4. Builder Pattern**

### **Pattern**
Builder

### **Definition**
Separates the construction of a complex object from its representation, allowing the same construction process to create different representations.

### **Type of Pattern**
Creational Pattern

### **Structure**
- Director
- Builder (interface)
- ConcreteBuilder
- Product

### **Code**
```java
// Product
class Computer {
    private String CPU;
    private String RAM;
    private String storage;
    private String graphicsCard;
    
    public Computer(String CPU, String RAM, String storage, String graphicsCard) {
        this.CPU = CPU;
        this.RAM = RAM;
        this.storage = storage;
        this.graphicsCard = graphicsCard;
    }
    
    @Override
    public String toString() {
        return "Computer [CPU=" + CPU + ", RAM=" + RAM + ", Storage=" + storage + 
               ", GraphicsCard=" + graphicsCard + "]";
    }
}

// Builder interface
interface ComputerBuilder {
    void buildCPU();
    void buildRAM();
    void buildStorage();
    void buildGraphicsCard();
    Computer getComputer();
}

// Concrete Builder
class GamingComputerBuilder implements ComputerBuilder {
    private Computer computer;
    
    public GamingComputerBuilder() {
        this.computer = new Computer("", "", "", "");
    }
    
    public void buildCPU() {
        computer = new Computer("Intel i9", computer.RAM, computer.storage, computer.graphicsCard);
    }
    
    public void buildRAM() {
        computer = new Computer(computer.CPU, "32GB DDR4", computer.storage, computer.graphicsCard);
    }
    
    public void buildStorage() {
        computer = new Computer(computer.CPU, computer.RAM, "1TB SSD", computer.graphicsCard);
    }
    
    public void buildGraphicsCard() {
        computer = new Computer(computer.CPU, computer.RAM, computer.storage, "NVIDIA RTX 4080");
    }
    
    public Computer getComputer() {
        return computer;
    }
}

// Director
class ComputerEngineer {
    private ComputerBuilder computerBuilder;
    
    public ComputerEngineer(ComputerBuilder computerBuilder) {
        this.computerBuilder = computerBuilder;
    }
    
    public Computer buildComputer() {
        computerBuilder.buildCPU();
        computerBuilder.buildRAM();
        computerBuilder.buildStorage();
        computerBuilder.buildGraphicsCard();
        return computerBuilder.getComputer();
    }
}

// Simplified Builder (Common approach)
class ComputerSimpleBuilder {
    private String CPU;
    private String RAM;
    private String storage;
    private String graphicsCard;
    
    public ComputerSimpleBuilder setCPU(String CPU) {
        this.CPU = CPU;
        return this;
    }
    
    public ComputerSimpleBuilder setRAM(String RAM) {
        this.RAM = RAM;
        return this;
    }
    
    public ComputerSimpleBuilder setStorage(String storage) {
        this.storage = storage;
        return this;
    }
    
    public ComputerSimpleBuilder setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
        return this;
    }
    
    public Computer build() {
        return new Computer(CPU, RAM, storage, graphicsCard);
    }
}
```

### **Usecases**
- Creating complex objects with many parts
- HTML/XML parsers
- Document builders
- Meal builders in restaurant systems

### **Advantages**
- Fine control over construction process
- Isolates construction code
- Allows varying object representations
- Improves readability for many parameters

### **Disadvantages**
- Increased complexity
- Requires creating separate ConcreteBuilder for each product type
- Can be overkill for simple objects

---

## **5. Prototype Pattern**

### **Pattern**
Prototype

### **Definition**
Creates new objects by copying an existing object (prototype) rather than creating new instances from scratch.

### **Type of Pattern**
Creational Pattern

### **Structure**
- Prototype (interface with clone method)
- ConcretePrototype (implements Prototype)
- Client

### **Code**
```java
// Prototype interface
interface Prototype extends Cloneable {
    Prototype clone() throws CloneNotSupportedException;
}

// Concrete Prototype
class Employee implements Prototype {
    private String name;
    private String role;
    private double salary;
    
    public Employee(String name, String role, double salary) {
        this.name = name;
        this.role = role;
        this.salary = salary;
    }
    
    // Copy constructor
    public Employee(Employee employee) {
        this.name = employee.name;
        this.role = employee.role;
        this.salary = employee.salary;
    }
    
    @Override
    public Prototype clone() throws CloneNotSupportedException {
        return (Employee) super.clone();
    }
    
    // Deep clone method
    public Employee deepClone() {
        return new Employee(this);
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    @Override
    public String toString() {
        return "Employee [name=" + name + ", role=" + role + ", salary=" + salary + "]";
    }
}

// Prototype registry
class PrototypeRegistry {
    private java.util.Map<String, Prototype> prototypes = new java.util.HashMap<>();
    
    public void registerPrototype(String key, Prototype prototype) {
        prototypes.put(key, prototype);
    }
    
    public Prototype getPrototype(String key) throws CloneNotSupportedException {
        return prototypes.get(key).clone();
    }
}

// Client code
public class PrototypeClient {
    public static void main(String[] args) throws CloneNotSupportedException {
        Employee original = new Employee("John Doe", "Developer", 50000);
        
        // Shallow clone
        Employee cloned = (Employee) original.clone();
        cloned.setSalary(60000);
        
        System.out.println("Original: " + original);
        System.out.println("Cloned: " + cloned);
        
        // Using registry
        PrototypeRegistry registry = new PrototypeRegistry();
        registry.registerPrototype("developer", new Employee("Template", "Developer", 50000));
        
        Employee newDeveloper = (Employee) registry.getPrototype("developer");
        newDeveloper.setSalary(55000);
        System.out.println("New Developer: " + newDeveloper);
    }
}
```

### **Usecases**
- Object creation is costly
- When classes are instantiated at runtime
- Database record copying
- Game development for character cloning

### **Advantages**
- Reduced subclassing
- Can add/remove objects at runtime
- Improved performance when object creation is expensive
- Simplified object creation process

### **Disadvantages**
- Complex cloning process for objects with circular references
- Deep vs shallow cloning concerns
- Can be tricky to implement correctly

---

## **Structural Patterns**

---

## **6. Adapter Pattern**

### **Pattern**
Adapter

### **Definition**
Converts the interface of a class into another interface clients expect. Lets classes work together that couldn't otherwise because of incompatible interfaces.

### **Type of Pattern**
Structural Pattern

### **Structure**
- Target (interface)
- Adaptee (existing class)
- Adapter (implements Target)

### **Code**
```java
// Target interface
interface MediaPlayer {
    void play(String audioType, String fileName);
}

// Adaptee - existing functionality
class AdvancedMediaPlayer {
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file: " + fileName);
    }
    
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file: " + fileName);
    }
    
    public void playAvi(String fileName) {
        System.out.println("Playing avi file: " + fileName);
    }
}

// Adapter
class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMusicPlayer;
    
    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc") || 
            audioType.equalsIgnoreCase("mp4") || 
            audioType.equalsIgnoreCase("avi")) {
            advancedMusicPlayer = new AdvancedMediaPlayer();
        }
    }
    
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        } else if (audioType.equalsIgnoreCase("avi")) {
            advancedMusicPlayer.playAvi(fileName);
        }
    }
}

// Client
class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;
    
    public void play(String audioType, String fileName) {
        // Built-in support for mp3
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing mp3 file: " + fileName);
        } 
        // Use adapter for other formats
        else if (audioType.equalsIgnoreCase("vlc") || 
                 audioType.equalsIgnoreCase("mp4") ||
                 audioType.equalsIgnoreCase("avi")) {
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid media format: " + audioType);
        }
    }
}
```

### **Usecases**
- Legacy system integration
- Third-party library integration
- Interface compatibility
- Working with incompatible interfaces

### **Advantages**
- Reusability of existing functionality
- Flexibility in combining classes
- Single Responsibility Principle
- Open/Closed Principle

### **Disadvantages**
- Increased complexity
- Sometimes many adaptations are needed
- Can lead to many small classes

---

## **7. Decorator Pattern**

### **Pattern**
Decorator

### **Definition**
Attaches additional responsibilities to an object dynamically. Provides a flexible alternative to subclassing for extending functionality.

### **Type of Pattern**
Structural Pattern

### **Structure**
- Component (interface)
- ConcreteComponent
- Decorator (abstract class)
- ConcreteDecorator

### **Code**
```java
// Component interface
interface Coffee {
    double getCost();
    String getDescription();
}

// Concrete Component
class SimpleCoffee implements Coffee {
    public double getCost() {
        return 5.0;
    }
    
    public String getDescription() {
        return "Simple coffee";
    }
}

// Decorator
abstract class CoffeeDecorator implements Coffee {
    protected Coffee decoratedCoffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }
    
    public double getCost() {
        return decoratedCoffee.getCost();
    }
    
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }
}

// Concrete Decorators
class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double getCost() {
        return super.getCost() + 1.5;
    }
    
    public String getDescription() {
        return super.getDescription() + ", milk";
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double getCost() {
        return super.getCost() + 0.5;
    }
    
    public String getDescription() {
        return super.getDescription() + ", sugar";
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double getCost() {
        return super.getCost() + 2.0;
    }
    
    public String getDescription() {
        return super.getDescription() + ", whipped cream";
    }
}

class CaramelDecorator extends CoffeeDecorator {
    public CaramelDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double getCost() {
        return super.getCost() + 1.8;
    }
    
    public String getDescription() {
        return super.getDescription() + ", caramel";
    }
}

// Client code
public class DecoratorClient {
    public static void main(String[] args) {
        Coffee myCoffee = new SimpleCoffee();
        System.out.println("Cost: " + myCoffee.getCost() + ", Description: " + myCoffee.getDescription());
        
        // Add milk
        myCoffee = new MilkDecorator(myCoffee);
        System.out.println("Cost: " + myCoffee.getCost() + ", Description: " + myCoffee.getDescription());
        
        // Add sugar
        myCoffee = new SugarDecorator(myCoffee);
        System.out.println("Cost: " + myCoffee.getCost() + ", Description: " + myCoffee.getDescription());
        
        // Add whipped cream
        myCoffee = new WhippedCreamDecorator(myCoffee);
        System.out.println("Cost: " + myCoffee.getCost() + ", Description: " + myCoffee.getDescription());
        
        // Add caramel
        myCoffee = new CaramelDecorator(myCoffee);
        System.out.println("Cost: " + myCoffee.getCost() + ", Description: " + myCoffee.getDescription());
    }
}
```

### **Usecases**
- Java I/O streams (BufferedReader, FileReader, etc.)
- GUI components
- Middleware systems
- Web servers
- Adding features to objects dynamically

### **Advantages**
- More flexible than inheritance
- Responsibilities can be added/removed at runtime
- Avoids feature-laden classes high up in hierarchy
- Open/Closed Principle

### **Disadvantages**
- Lots of small objects
- Can complicate instantiation
- Hard to debug and trace
- Can be overused

---

## **8. Facade Pattern**

### **Pattern**
Facade

### **Definition**
Provides a unified interface to a set of interfaces in a subsystem. Defines a higher-level interface that makes the subsystem easier to use.

### **Type of Pattern**
Structural Pattern

### **Structure**
- Facade
- Subsystem classes
- Client

### **Code**
```java
// Subsystem classes
class CPU {
    public void start() {
        System.out.println("CPU is starting");
    }
    
    public void execute() {
        System.out.println("CPU is executing commands");
    }
    
    public void shutdown() {
        System.out.println("CPU is shutting down");
    }
}

class Memory {
    public void load() {
        System.out.println("Memory is loading data");
    }
    
    public void free() {
        System.out.println("Memory is being freed");
    }
}

class HardDrive {
    public void read() {
        System.out.println("Hard Drive is reading data");
    }
    
    public void write() {
        System.out.println("Hard Drive is writing data");
    }
}

// Facade
class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;
    
    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }
    
    public void start() {
        System.out.println("Computer starting...");
        cpu.start();
        memory.load();
        hardDrive.read();
        cpu.execute();
        System.out.println("Computer started successfully");
    }
    
    public void shutdown() {
        System.out.println("Computer shutting down...");
        hardDrive.write();
        memory.free();
        cpu.shutdown();
        System.out.println("Computer shut down successfully");
    }
    
    public void executeProgram() {
        System.out.println("Executing program...");
        memory.load();
        hardDrive.read();
        cpu.execute();
        System.out.println("Program executed successfully");
    }
}

// Client
public class FacadeClient {
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        
        // Simple interface for complex subsystem
        computer.start();
        System.out.println();
        
        computer.executeProgram();
        System.out.println();
        
        computer.shutdown();
    }
}
```

### **Usecases**
- Simplifying complex subsystems
- Layer separation in applications
- API design
- Legacy system wrapping

### **Advantages**
- Shields clients from subsystem complexity
- Promotes weak coupling between subsystems and clients
- Easier to use complex systems

### **Disadvantages**
- Can become a god object (handling too many responsibilities)
- Additional layer of abstraction
- Can limit flexibility for advanced users

---

## **9. Proxy Pattern**

### **Pattern**
Proxy

### **Definition**
Provides a surrogate or placeholder for another object to control access to it.

### **Type of Pattern**
Structural Pattern

### **Structure**
- Subject (interface)
- RealSubject
- Proxy (implements Subject)

### **Code**
```java
// Subject interface
interface Image {
    void display();
    String getFileName();
}

// RealSubject
class RealImage implements Image {
    private String fileName;
    
    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }
    
    private void loadFromDisk() {
        System.out.println("Loading image: " + fileName);
        // Simulate expensive operation
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void display() {
        System.out.println("Displaying image: " + fileName);
    }
    
    public String getFileName() {
        return fileName;
    }
}

// Proxy
class ProxyImage implements Image {
    private RealImage realImage;
    private String fileName;
    
    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
    
    public String getFileName() {
        return fileName;
    }
}

// Virtual Proxy Example
class ImageViewer {
    public static void main(String[] args) {
        Image image1 = new ProxyImage("photo1.jpg");
        Image image2 = new ProxyImage("photo2.jpg");
        
        // Image loaded only when displayed
        image1.display(); // Loading occurs here
        System.out.println();
        
        image1.display(); // Already loaded, no loading
        System.out.println();
        
        image2.display(); // Loading occurs here
    }
}

// Protection Proxy Example
interface BankAccount {
    void withdraw(double amount);
    double getBalance();
}

class RealBankAccount implements BankAccount {
    private double balance;
    private String owner;
    
    public RealBankAccount(double balance, String owner) {
        this.balance = balance;
        this.owner = owner;
    }
    
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient funds");
        }
    }
    
    public double getBalance() {
        return balance;
    }
}

class ProtectionProxy implements BankAccount {
    private RealBankAccount realAccount;
    private String userRole;
    
    public ProtectionProxy(double balance, String owner, String userRole) {
        this.realAccount = new RealBankAccount(balance, owner);
        this.userRole = userRole;
    }
    
    public void withdraw(double amount) {
        if ("admin".equals(userRole) || "owner".equals(userRole)) {
            realAccount.withdraw(amount);
        } else {
            System.out.println("Access denied: Insufficient permissions");
        }
    }
    
    public double getBalance() {
        if ("admin".equals(userRole) || "owner".equals(userRole) || "viewer".equals(userRole)) {
            return realAccount.getBalance();
        } else {
            System.out.println("Access denied: Insufficient permissions");
            return 0;
        }
    }
}
```

### **Usecases**
- Virtual proxies (lazy initialization)
- Protection proxies (access control)
- Remote proxies (network communication)
- Caching proxies
- Logging proxies

### **Advantages**
- Controlled access to the real object
- Can add functionality without changing real subject
- Memory efficiency (lazy loading)
- Security enhancement

### **Disadvantages**
- Increased response time
- Additional layer of complexity
- Can introduce overhead

---

## **10. Composite Pattern**

### **Pattern**
Composite

### **Definition**
Composes objects into tree structures to represent part-whole hierarchies. Lets clients treat individual objects and compositions uniformly.

### **Type of Pattern**
Structural Pattern

### **Structure**
- Component (interface)
- Leaf (implements Component)
- Composite (implements Component, contains children)

### **Code**
```java
import java.util.ArrayList;
import java.util.List;

// Component
interface FileSystemComponent {
    void showDetails();
    long getSize();
    String getName();
}

// Leaf
class File implements FileSystemComponent {
    private String name;
    private long size;
    
    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }
    
    public void showDetails() {
        System.out.println("File: " + name + " | Size: " + size + " bytes");
    }
    
    public long getSize() {
        return size;
    }
    
    public String getName() {
        return name;
    }
}

// Composite
class Directory implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> components;
    
    public Directory(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }
    
    public void addComponent(FileSystemComponent component) {
        components.add(component);
    }
    
    public void removeComponent(FileSystemComponent component) {
        components.remove(component);
    }
    
    public void showDetails() {
        System.out.println("Directory: " + name + " | Total Size: " + getSize() + " bytes");
        System.out.println("Contents:");
        for (FileSystemComponent component : components) {
            component.showDetails();
        }
        System.out.println("--- End of " + name + " ---");
    }
    
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent component : components) {
            totalSize += component.getSize();
        }
        return totalSize;
    }
    
    public String getName() {
        return name;
    }
}

// Client code
public class CompositeClient {
    public static void main(String[] args) {
        // Create files
        File file1 = new File("document.txt", 1024);
        File file2 = new File("image.jpg", 2048);
        File file3 = new File("video.mp4", 4096);
        File file4 = new File("readme.txt", 512);
        
        // Create directories
        Directory documents = new Directory("Documents");
        Directory images = new Directory("Images");
        Directory videos = new Directory("Videos");
        Directory root = new Directory("Root");
        
        // Build tree structure
        documents.addComponent(file1);
        documents.addComponent(file4);
        
        images.addComponent(file2);
        
        videos.addComponent(file3);
        
        root.addComponent(documents);
        root.addComponent(images);
        root.addComponent(videos);
        
        // Display structure
        root.showDetails();
        
        // Individual components can also be used
        System.out.println("\nIndividual file:");
        file1.showDetails();
        
        System.out.println("\nDocuments directory:");
        documents.showDetails();
    }
}
```

### **Usecases**
- File systems
- GUI components (containers and widgets)
- Organization hierarchies
- XML/HTML parsing
- Menu systems

### **Advantages**
- Simplifies client code
- Makes it easy to add new component types
- Provides flexibility in structure
- Uniform treatment of individual and composite objects

### **Disadvantages**
- Can make design overly general
- Difficult to restrict certain operations
- Type safety can be compromised
- Can be overly simplistic for complex hierarchies

---

## **Behavioral Patterns**

---

## **11. Observer Pattern**

### **Pattern**
Observer

### **Definition**
Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

### **Type of Pattern**
Behavioral Pattern

### **Structure**
- Subject (interface)
- ConcreteSubject
- Observer (interface)
- ConcreteObserver

### **Code**
```java
import java.util.ArrayList;
import java.util.List;

// Subject interface
interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// Concrete Subject
class WeatherStation implements Subject {
    private List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;
    
    public WeatherStation() {
        observers = new ArrayList<>();
    }
    
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }
    
    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }
    
    private void measurementsChanged() {
        notifyObservers();
    }
    
    // Other weather station methods...
    public float getTemperature() {
        return temperature;
    }
    
    public float getHumidity() {
        return humidity;
    }
    
    public float getPressure() {
        return pressure;
    }
}

// Observer interface
interface Observer {
    void update(float temperature, float humidity, float pressure);
}

// Concrete Observers
class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;
    private Subject weatherData;
    
    public CurrentConditionsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }
    
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }
    
    public void display() {
        System.out.println("Current conditions: " + temperature + "°C and " + humidity + "% humidity");
    }
}

class StatisticsDisplay implements Observer {
    private List<Float> temperatures;
    private Subject weatherData;
    
    public StatisticsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        this.temperatures = new ArrayList<>();
        weatherData.registerObserver(this);
    }
    
    public void update(float temperature, float humidity, float pressure) {
        temperatures.add(temperature);
        display();
    }
    
    public void display() {
        float max = temperatures.stream().max(Float::compare).orElse(0f);
        float min = temperatures.stream().min(Float::compare).orElse(0f);
        float avg = (float) temperatures.stream().mapToDouble(Float::doubleValue).average().orElse(0);
        
        System.out.println("Weather Stats - Avg/Max/Min: " + avg + "/" + max + "/" + min);
    }
}

class ForecastDisplay implements Observer {
    private float lastPressure;
    private float currentPressure;
    private Subject weatherData;
    
    public ForecastDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        this.currentPressure = 29.92f;
        weatherData.registerObserver(this);
    }
    
    public void update(float temperature, float humidity, float pressure) {
        lastPressure = currentPressure;
        currentPressure = pressure;
        display();
    }
    
    public void display() {
        System.out.print("Forecast: ");
        if (currentPressure > lastPressure) {
            System.out.println("Improving weather on the way!");
        } else if (currentPressure == lastPressure) {
            System.out.println("More of the same");
        } else {
            System.out.println("Watch out for cooler, rainy weather");
        }
    }
}

// Client code
public class ObserverClient {
    public static void main(String[] args) {
        WeatherStation weatherStation = new WeatherStation();
        
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherStation);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherStation);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherStation);
        
        // Simulate weather changes
        System.out.println("=== First Update ===");
        weatherStation.setMeasurements(25, 65, 1013);
        System.out.println();
        
        System.out.println("=== Second Update ===");
        weatherStation.setMeasurements(27, 70, 1012);
        System.out.println();
        
        System.out.println("=== Third Update ===");
        weatherStation.setMeasurements(23, 90, 1010);
    }
}
```

### **Usecases**
- Event handling systems
- News subscription
- Stock market updates
- Social media feeds
- MVC architecture
- Message brokers

### **Advantages**
- Loose coupling between subject and observers
- Supports broadcast communication
- Dynamic relationships
- Open/Closed Principle

### **Disadvantages**
- Memory leaks if observers aren't properly removed
- Unexpected updates
- Performance issues with many observers
- Can cause cascading updates

---

## **12. Strategy Pattern**

### **Pattern**
Strategy

### **Definition**
Defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from clients that use it.

### **Type of Pattern**
Behavioral Pattern

### **Structure**
- Strategy (interface)
- ConcreteStrategy (implements Strategy)
- Context (uses Strategy)

### **Code**
```java
import java.util.List;

// Strategy interface
interface SortingStrategy {
    void sort(List<Integer> list);
    String getStrategyName();
}

// Concrete Strategies
class BubbleSort implements SortingStrategy {
    public void sort(List<Integer> list) {
        System.out.println("Sorting using Bubble Sort");
        int n = list.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (list.get(j) > list.get(j+1)) {
                    // Swap
                    int temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                }
            }
        }
    }
    
    public String getStrategyName() {
        return "Bubble Sort";
    }
}

class QuickSort implements SortingStrategy {
    public void sort(List<Integer> list) {
        System.out.println("Sorting using Quick Sort");
        quickSort(list, 0, list.size() - 1);
    }
    
    private void quickSort(List<Integer> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }
    
    private int partition(List<Integer> list, int low, int high) {
        int pivot = list.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (list.get(j) < pivot) {
                i++;
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        int temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
    
    public String getStrategyName() {
        return "Quick Sort";
    }
}

class MergeSort implements SortingStrategy {
    public void sort(List<Integer> list) {
        System.out.println("Sorting using Merge Sort");
        mergeSort(list, 0, list.size() - 1);
    }
    
    private void mergeSort(List<Integer> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }
    
    private void merge(List<Integer> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];
        
        for (int i = 0; i < n1; i++)
            leftArray[i] = list.get(left + i);
        for (int j = 0; j < n2; j++)
            rightArray[j] = list.get(mid + 1 + j);
        
        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                list.set(k, leftArray[i]);
                i++;
            } else {
                list.set(k, rightArray[j]);
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            list.set(k, leftArray[i]);
            i++;
            k++;
        }
        
        while (j < n2) {
            list.set(k, rightArray[j]);
            j++;
            k++;
        }
    }
    
    public String getStrategyName() {
        return "Merge Sort";
    }
}

// Context
class Sorter {
    private SortingStrategy strategy;
    
    public Sorter(SortingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(SortingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void sortList(List<Integer> list) {
        System.out.println("Using strategy: " + strategy.getStrategyName());
        long startTime = System.nanoTime();
        
        strategy.sort(list);
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Sorting completed in " + duration + " ms");
    }
    
    public void displayList(List<Integer> list) {
        System.out.println("Sorted list: " + list);
    }
}

// Payment Strategy Example
interface PaymentStrategy {
    void pay(int amount);
    String getPaymentMethod();
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cvv;
    
    public CreditCardPayment(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }
    
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card ending with " + 
                          cardNumber.substring(cardNumber.length() - 4));
    }
    
    public String getPaymentMethod() {
        return "Credit Card";
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;
    
    public PayPalPayment(String email) {
        this.email = email;
    }
    
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using PayPal: " + email);
    }
    
    public String getPaymentMethod() {
        return "PayPal";
    }
}

class CryptoPayment implements PaymentStrategy {
    private String walletAddress;
    
    public CryptoPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }
    
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Cryptocurrency to wallet: " + 
                          walletAddress.substring(0, 8) + "...");
    }
    
    public String getPaymentMethod() {
        return "Cryptocurrency";
    }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public void checkout(int amount) {
        if (paymentStrategy == null) {
            System.out.println("Please select a payment method");
            return;
        }
        System.out.println("Processing payment with: " + paymentStrategy.getPaymentMethod());
        paymentStrategy.pay(amount);
        System.out.println("Checkout completed successfully!");
    }
}

// Client code
public class StrategyClient {
    public static void main(String[] args) {
        // Sorting Strategy Example
        List<Integer> numbers = List.of(64, 34, 25, 12, 22, 11, 90);
        
        Sorter sorter = new Sorter(new BubbleSort());
        sorter.sortList(new java.util.ArrayList<>(numbers));
        sorter.displayList(new java.util.ArrayList<>(numbers));
        System.out.println();
        
        sorter.setStrategy(new QuickSort());
        sorter.sortList(new java.util.ArrayList<>(numbers));
        sorter.displayList(new java.util.ArrayList<>(numbers));
        System.out.println();
        
        sorter.setStrategy(new MergeSort());
        sorter.sortList(new java.util.ArrayList<>(numbers));
        sorter.displayList(new java.util.ArrayList<>(numbers));
        System.out.println();
        
        // Payment Strategy Example
        ShoppingCart cart = new ShoppingCart();
        
        cart.setPaymentStrategy(new CreditCardPayment("1234567812345678", "123"));
        cart.checkout(100);
        System.out.println();
        
        cart.setPaymentStrategy(new PayPalPayment("user@example.com"));
        cart.checkout(200);
        System.out.println();
        
        cart.setPaymentStrategy(new CryptoPayment("1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"));
        cart.checkout(150);
    }
}
```

### **Usecases**
- Payment processing
- Sorting algorithms
- Compression algorithms
- Navigation systems
- Validation strategies
- Data export formats

### **Advantages**
- Eliminates conditional statements
- Open/Closed Principle compliance
- Easier to test and maintain
- Algorithm reuse across different contexts

### **Disadvantages**
- Clients must be aware of different strategies
- Increased number of objects
- Communication overhead between strategies and context
- Can be overkill for simple conditional logic

---

## **Summary Table**

| Pattern | Type | Purpose | Key Benefit |
|---------|------|---------|-------------|
| Singleton | Creational | Single instance | Controlled access |
| Factory Method | Creational | Object creation | Loose coupling |
| Abstract Factory | Creational | Families of objects | Compatibility |
| Builder | Creational | Complex objects | Step-by-step construction |
| Prototype | Creational | Object cloning | Performance |
| Adapter | Structural | Interface conversion | Compatibility |
| Decorator | Structural | Add responsibilities | Runtime flexibility |
| Facade | Structural | Simplified interface | Complexity hiding |
| Proxy | Structural | Access control | Security/Lazy loading |
| Composite | Structural | Tree structures | Uniform treatment |
| Observer | Behavioral | One-to-many dependency | Automatic notifications |
| Strategy | Behavioral | Algorithm family | Interchangeable behaviors |

These patterns provide proven solutions to common software design problems and promote code reusability, maintainability, and flexibility. Each pattern addresses specific design challenges and can be combined to create robust, scalable applications.


## **Reference**
- [Summary Table](#summary-table)

---

**Quick Navigation:**
- [Back to Top](#design-patterns-in-java)
- [Creational Patterns](#creational-patterns)
- [Structural Patterns](#structural-patterns) 
- [Behavioral Patterns](#behavioral-patterns)
- [Summary Table](#summary-table)
