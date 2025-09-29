# Python Concepts to Read

## Table of Contents

- [Generator](#generator)
- [Iterator](#iterator)
- [What are *args and **kwargs?](#what-are-args-and-kwargs)
- [Collections](#collections)
  - [OrderedDict](#ordereddict)
  - [Defaultdict](#defaultdict)
  - [Deque](#deque)
- [List Operations](#list-operations)
- [Itertools](#itertools)
- [Classes and OOP Concepts](#classes-and-oop-concepts)
  - [Class and Object](#class-and-object)
  - [Inheritance](#inheritance)
  - [Encapsulation](#encapsulation)
  - [Polymorphism](#polymorphism)
  - [Abstraction](#abstraction)
  - [Special Methods (Magic Methods)](#special-methods-magic-methods)
  - [Class vs Instance Variables](#class-vs-instance-variables)
  - [Class Methods and Static Methods](#class-methods-and-static-methods)
  - [Property Decorators](#property-decorators)
  - [Multiple Inheritance](#multiple-inheritance)
  - [Method Resolution Order (MRO)](#method-resolution-order-mro)
  - [Composition vs Inheritance](#composition-vs-inheritance)
  - [Exception Handling in OOP](#exception-handling-in-oop)
  - [Data Classes](#data-classes)
  - [Slots](#slots)
  - [Metaclasses](#metaclasses)
  - [Memory Management and Garbage Collection](#memory-management-and-garbage-collection)
  - [Advanced Topics](#advanced-topics)
  - [Best Practices](#best-practices)
- [List Comprehension and Lambdas](#list-comprehension-and-lambdas)
  - [List Comprehension](#list-comprehension)
  - [Lambda Functions](#lambda-functions)
- [Map Function](#map-function)
- [Filter Function](#filter-function)
- [Sorted Function](#sorted-function)
- [Decorators](#decorators)
- [Python Multithreading](#python-multithreading)
- [Naming Conventions](#naming-conventions)
- [Special Name Classes](#special-name-classes)
- [Doc Strings](#doc-strings)
- [Python Operators](#python-operators)
  - [Arithmetic Operators](#arithmetic-operators)
  - [Comparison Operators](#comparison-operators)
  - [Logical Operators](#logical-operators)
  - [Assignment Operators](#assignment-operators)
  - [Bitwise Operators](#bitwise-operators)
  - [Difference between != and <>](#difference-between--and-)
  - [is, not is Operator](#is-not-is-operator)
  - [in, not in Operator](#in-not-in-operator)
- [Timeit for Measuring Function Execution Time](#timeit-for-measuring-function-execution-time)
- [cProfile for Measuring Performance](#cprofile-for-measuring-performance)
- [Kinds of Iterables](#kinds-of-iterables)
- [Range vs Xrange Functions](#range-vs-xrange-functions)
- [Tuples and Lists](#tuples-and-lists)
  - [List](#list)
  - [Shallow Copy vs Deep Copy](#shallow-copy-vs-deep-copy)
  - [Tuples](#tuples)
- [Strings](#strings)
- [Dictionary](#dictionary)
  - [Shallow Copy vs Deep Copy](#shallow-copy-vs-deep-copy-1)
- [Yield](#yield)
- [Generator Expression](#generator-expression)
- [Searching Algorithms](#searching-algorithms)
  - [Linear Search](#linear-search)
  - [Binary Search](#binary-search)
- [Positional and Keyword Arguments](#positional-and-keyword-arguments)
- [Different Types of Methods](#different-types-of-methods)
  - [Instance Methods](#instance-methods)
  - [Class Methods](#class-methods)
  - [Static Methods](#static-methods)
- [Magic Methods](#magic-methods)
- [Property Decorator](#property-decorator)
- [Recurring Functions](#recurring-functions)
- [Python Slicing](#python-slicing)
- [Frameworks](#frameworks)
- [Pytest](#pytest)

## Generator

A generator is a special type of iterator that allows you to iterate over a sequence of values lazily, generating values one at a time to save memory. Generators are defined using functions with the `yield` keyword or generator expressions.

**Example**:
```python
def test():
    i = 1
    while i <= 100:
        yield i
        i += 1

itr = test()
print(next(itr))  # 1
for i in itr:
    print(next(itr))  # Prints every other number (2, 4, ..., 100)
```

**Note**: The `for i in itr: next(itr)` loop skips every other element because `next(itr)` advances the iterator. To print all numbers, use `for i in itr: print(i)`.

**Resource**: [YouTube Playlist on Generators](https://www.youtube.com/playlist?list=PLsyeobzWxl7poL9JTVyndKe62ieoN-MZ3)

## Iterator

An iterator is an object that implements the `__iter__()` and `__next__()` methods, allowing iteration over a sequence. Any object with these methods can be used in a `for` loop.

**Example**:
```python
a = [1, 2, 3, 4]
itr = iter(a)
print(next(itr))      # 1
print(itr.__next__()) # 2

a = [1, 2, 3, 4]
itr = a.__iter__()
print(itr.__next__()) # 1
print(next(itr))      # 2
```

## What are *args and **kwargs?

- `*args`: Allows a function to accept a variable number of positional arguments, passed as a tuple.
- `**kwargs`: Allows a function to accept a variable number of keyword arguments, passed as a dictionary.

**Example**:
```python
def func(*args, **kwargs):
    print(args)   # Tuple of positional args
    print(kwargs) # Dict of keyword args

func(1, 2, a=3, b=4)
# Output: (1, 2)
#         {'a': 3, 'b': 4}
```

## Collections

The `collections` module provides specialized container datatypes that extend Python's built-in containers.

### OrderedDict

`OrderedDict` is a dictionary that remembers the order of key insertion, useful for predictable iteration and order-specific operations.

**Example**:
```python
from collections import OrderedDict

od = OrderedDict()
od['apple'] = 10
od['banana'] = 20
od['cherry'] = 30

for key, value in od.items():
    print(key, value)
# Output: apple 10
#         banana 20
#         cherry 30

od.move_to_end('banana')  # Move 'banana' to the end
print(list(od.keys()))    # ['apple', 'cherry', 'banana']
od.popitem(last=False)    # Remove first item
print(list(od.keys()))    # ['cherry', 'banana']
```

### Defaultdict

`defaultdict` provides a default value for missing keys, avoiding `KeyError`.

**Example**:
```python
from collections import defaultdict

dd = defaultdict(int)
dd['apples'] += 10
dd['bananas'] += 5
print(dd)  # defaultdict(<class 'int'>, {'apples': 10, 'bananas': 5})

dd_list = defaultdict(list)
dd_list['fruits'].append('apple')
dd_list['fruits'].append('banana')
dd_list['vegetables'].append('carrot')
print(dd_list)  # defaultdict(<class 'list'>, {'fruits': ['apple', 'banana'], 'vegetables': ['carrot']})

# Counting occurrences
words = ['apple', 'banana', 'apple', 'orange', 'banana']
count = defaultdict(int)
for word in words:
    count[word] += 1
print(count)  # defaultdict(<class 'int'>, {'apple': 2, 'banana': 2, 'orange': 1})

# Grouping items
pairs = [('a', 1), ('b', 2), ('a', 3)]
group = defaultdict(list)
for k, v in pairs:
    group[k].append(v)
print(group)  # defaultdict(<class 'list'>, {'a': [1, 3], 'b': [2]})
```

### Deque

`deque` (double-ended queue) supports efficient additions and removals from both ends (O(1) complexity).

**Example**:
```python
from collections import deque

dq = deque([1, 2, 3])
dq.append(4)        # deque([1, 2, 3, 4])
print(dq)
dq.appendleft(0)    # deque([0, 1, 2, 3, 4])
print(dq)
dq.pop()            # deque([0, 1, 2, 3])
print(dq)
dq.popleft()        # deque([1, 2, 3])
print(dq)
dq.rotate(1)        # deque([3, 1, 2])
print(dq)
dq.rotate(-1)       # deque([1, 2, 3])
print(dq)
```

## List Operations

Lists support various operations for manipulation.

**Example**:
```python
fruits = ['apple', 'banana', 'apple', 'orange', 'banana']
fruits.clear()  # Remove all elements
print(fruits)   # []

fruits = ['apple', 'banana', 'apple', 'orange', 'banana']
print(fruits.count("apple"))  # 2
print(fruits.index("orange")) # 3
fruits.sort()                 # ['apple', 'apple', 'banana', 'banana', 'orange']
print(fruits)
fruits.reverse()              # ['orange', 'banana', 'banana', 'apple', 'apple']
print(fruits)

vegetables = ["carrot", "broccoli"]
all_items = fruits + vegetables
print(all_items)  # ['orange', 'banana', 'banana', 'apple', 'apple', 'carrot', 'broccoli']

fruits.append("orange")
print(fruits)  # ['orange', 'banana', 'banana', 'apple', 'apple', 'orange']
fruits.insert(1, "kiwi")
print(fruits)  # ['orange', 'kiwi', 'banana', 'banana', 'apple', 'apple', 'orange']
```

## Itertools

The `itertools` module provides efficient tools for creating iterators, supporting lazy evaluation.

**Resource**: [YouTube Video on Itertools](https://www.youtube.com/watch?v=znyTAmoyMlA&list=PL-2EBeDYMIbSfbjfLIRtwAP21rf4QxaYb)

**Example**:
```python
import itertools
from operator import mul

data = [1, 1, 2, 2, 3, 3]
for key, group in itertools.groupby(data):
    print(key, list(group))
# Output: 1 [1, 1]
#         2 [2, 2]
#         3 [3, 3]

for i in itertools.count(5, 2):
    if i > 15:
        break
    print(i)  # 5, 7, 9, 11, 13, 15

for i, val in zip(range(6), itertools.cycle(['A', 'B', 'C'])):
    print(val)  # A, B, C, A, B, C

print(list(itertools.repeat("Hi", 3)))  # ['Hi', 'Hi', 'Hi']
print(list(itertools.accumulate([1, 2, 3, 4])))  # [1, 3, 6, 10]
print(list(itertools.accumulate([1, 2, 3, 4], mul)))  # [1, 2, 6, 24]
print(list(itertools.chain([1, 2], ['a', 'b'])))  # [1, 2, 'a', 'b']
print(list(itertools.compress("PYTHON", [1, 0, 1, 0, 1, 0])))  # ['P', 'T', 'O']
print(list(itertools.dropwhile(lambda x: x < 5, [1, 4, 6, 2, 7])))  # [6, 2, 7]
print(list(itertools.takewhile(lambda x: x < 5, [1, 4, 6, 2, 7])))  # [1, 4]
print(list(itertools.product([1, 2], ['a', 'b'])))  # [(1, 'a'), (1, 'b'), (2, 'a'), (2, 'b')]
print(list(itertools.product([0, 1], repeat=2)))  # [(0, 0), (0, 1), (1, 0), (1, 1)]
print(list(itertools.permutations([1, 2, 3], 2)))  # [(1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2)]
print(list(itertools.combinations([1, 2, 3], 2)))  # [(1, 2), (1, 3), (2, 3)]
print(list(itertools.combinations_with_replacement([1, 2], 2)))  # [(1, 1), (1, 2), (2, 2)]

for k, g in itertools.groupby([("a", 1), ("a", 2), ("b", 3), ("b", 4)], lambda x: x[0]):
    print(k, list(g))
# Output: a [('a', 1), ('a', 2)]
#         b [('b', 3), ('b', 4)]
```

## Classes and OOP Concepts

### Class and Object
A class is a blueprint for creating objects, which are instances with attributes and methods.

**Example**:
```python
class Car:
    def __init__(self, brand, model):
        self.brand = brand
        self.model = model

    def drive(self):
        return f"{self.brand} {self.model} is driving."

car1 = Car("Tesla", "Model 3")
print(car1.drive())  # Tesla Model 3 is driving.
```

### Inheritance
Inheritance allows a child class to inherit attributes and methods from a parent class.

**Example**:
```python
class ElectricCar(Car):
    def charge(self):
        return f"{self.brand} {self.model} is charging."

ecar1 = ElectricCar("Tesla", "Model S")
print(ecar1.drive())   # Tesla Model S is driving.
print(ecar1.charge())  # Tesla Model S is charging.
```

### Encapsulation
Encapsulation bundles data and methods, using private attributes (`__`) to restrict access.

**Example**:
```python
class BankAccount:
    def __init__(self, balance):
        self.__balance = balance
    def deposit(self, amount):
        self.__balance += amount
    def get_balance(self):
        return self.__balance

acc = BankAccount(1000)
acc.deposit(500)
print(acc.get_balance())  # 1500
```

### Polymorphism
Polymorphism allows different classes to share a common interface.

**Example**:
```python
class Dog:
    def sound(self):
        return "Woof!"
class Cat:
    def sound(self):
        return "Meow!"

def animal_sound(animal):
    print(animal.sound())

dog = Dog()
cat = Cat()
animal_sound(dog)  # Woof!
animal_sound(cat)  # Meow!
```

### Abstraction
Abstraction hides implementation details, exposing only essential features using abstract base classes.

**Example**:
```python
from abc import ABC, abstractmethod
class Shape(ABC):
    @abstractmethod
    def area(self):
        pass

class Rectangle(Shape):
    def __init__(self, width, height):
        self.width = width
        self.height = height
    def area(self):
        return self.width * self.height

rect = Rectangle(5, 10)
print(rect.area())  # 50
```

### Special Methods (Magic Methods)
Special methods (`__method__`) define custom behavior for objects.

**Example**:
```python
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __str__(self):
        return f"Point({self.x}, {self.y})"
    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

p1 = Point(2, 3)
p2 = Point(4, 5)
print(p1)  # Point(2, 3)
p3 = p1 + p2
print(p3)  # Point(6, 8)
```

### Class vs Instance Variables
Class variables are shared across instances; instance variables are unique to each instance.

**Example**:
```python
class Dog:
    species = "Canis familiaris"  # Class variable
    def __init__(self, name):
        self.name = name  # Instance variable

dog1 = Dog("Buddy")
dog2 = Dog("Max")
print(dog1.species)  # Canis familiaris
print(dog2.species)  # Canis familiaris
print(dog1.name)     # Buddy
print(dog2.name)     # Max
```

### Class Methods and Static Methods
- **Class Methods**: Use `@classmethod`, take `cls` as the first parameter.
- **Static Methods**: Use `@staticmethod`, don’t take `self` or `cls`.

**Example**:
```python
class Circle:
    pi = 3.14159
    def __init__(self, radius):
        self.radius = radius
    def get_area(self):
        return self.radius
    @classmethod
    def circle_area(cls, radius):
        return cls.pi * (radius ** 2)
    @staticmethod
    def circumference(radius):
        return 2 * Circle.pi * radius

print(Circle.circle_area(5))    # 78.53975
print(Circle.circumference(5))  # 31.4159
```

### Property Decorators
`@property` defines getter methods; `@<property>.setter` defines setters.

**Example**:
```python
class Person:
    def __init__(self, name):
        self._name = name
    @property
    def name(self):
        return self._name
    @name.setter
    def name(self, value):
        self._name = value

p = Person("Alice")
print(p.name)  # Alice
p.name = "Bob"
print(p.name)  # Bob
```

### Multiple Inheritance
A class can inherit from multiple parent classes.

**Example**:
```python
class A:
    def method_a(self):
        return "Method A"
class B:
    def method_b(self):
        return "Method B"
class C(A, B):
    def method_c(self):
        return "Method C"

c = C()
print(c.method_a())  # Method A
print(c.method_b())  # Method B
print(c.method_c())  # Method C
```

### Method Resolution Order (MRO)
MRO defines the order in which base classes are searched for methods.

**Example**:
```python
class A:
    def method(self):
        return "A"
class B(A):
    def method(self):
        return "B"
class C(A):
    def method(self):
        return "C"
class D(B, C):
    pass

d = D()
print(d.method())  # B
print(D.mro())    # [<class '__main__.D'>, <class '__main__.B'>, <class '__main__.C'>, <class '__main__.A'>, <class 'object'>]
```

### Composition vs Inheritance
Composition uses objects of other classes (has-a), while inheritance extends classes (is-a).

**Example**:
```python
class Engine:
    def start(self):
        return "Engine started"
class Car:
    def __init__(self):
        self.engine = Engine()
    def start(self):
        return self.engine.start()

car = Car()
print(car.start())  # Engine started
```

### Exception Handling in OOP
Handle exceptions within class methods using `try-except`.

**Example**:
```python
class Calculator:
    def divide(self, a, b):
        try:
            return a / b
        except ZeroDivisionError:
            return "Error: Division by zero"

calc = Calculator()
print(calc.divide(10, 2))  # 5.0
print(calc.divide(10, 0))  # Error: Division by zero
```

### Data Classes
`@dataclass` simplifies class creation for data storage.

**Example**:
```python
from dataclasses import dataclass

@dataclass
class Point:
    x: int
    y: int

p1 = Point(2, 3)
p2 = Point(2, 3)
print(p1)        # Point(x=2, y=3)
print(p1 == p2)  # True
```

### Slots
`__slots__` restricts dynamic attributes and saves memory.

**Example**:
```python
class Person:
    __slots__ = ['name', 'age']
    def __init__(self, name, age):
        self.name = name
        self.age = age

p = Person("Alice", 30)
print(p.name)  # Alice
# p.address = "123 Street"  # AttributeError
```

### Metaclasses
Metaclasses define how classes are created.

**Example**:
```python
class Meta(type):
    def __new__(cls, name, bases, attrs):
        attrs['class_name'] = name
        return super().__new__(cls, name, bases, attrs)

class MyClass(metaclass=Meta):
    pass

obj = MyClass()
print(obj.class_name)  # MyClass
```

### Memory Management and Garbage Collection
Python uses reference counting and a cyclic garbage collector.

**Example**:
```python
import gc
gc.collect()  # Trigger garbage collection
```

### Advanced Topics
- Design Patterns (Singleton, Factory, Observer)
- Dependency Injection
- Mixins
- Interfaces (using ABC)
- SOLID Principles
- UML Diagrams
- Best Practices

### Best Practices
- Follow PEP 8 for code style.
- Use meaningful names.
- Keep classes single-responsibility.
- Prefer composition over inheritance.
- Use docstrings for documentation.
- Write unit tests.
- Use version control (Git).
- Refactor regularly.

## List Comprehension and Lambdas

### List Comprehension
List comprehensions provide a concise way to create lists using an expression and optional conditions.

**Syntax**: `[expression for item in iterable if condition]`

**Example**:
```python
nums = [1, 2, 3, 4, 5]
squares = [x*x for x in nums]
print("Squares (list comp):", squares)  # [1, 4, 9, 16, 25]

evens = [x for x in nums if x % 2 == 0]
print("Evens (list comp):", evens)  # [2, 4]

pairs = [(x, y) for x in [1, 2] for y in [3, 4]]
print("Pairs:", pairs)  # [(1, 3), (1, 4), (2, 3), (2, 4)]

words = ["hello", "python", "world"]
upper_words = [w.upper() for w in words]
print("Upper Words (list comp):", upper_words)  # ['HELLO', 'PYTHON', 'WORLD']

parity = ["even" if x % 2 == 0 else "odd" for x in nums]
print("Parity:", parity)  # ['odd', 'even', 'odd', 'even', 'odd']

matrix = [[1, 2], [3, 4], [5, 6]]
flat = [x for row in matrix for x in row]
print("Flattened:", flat)  # [1, 2, 3, 4, 5, 6]

squares_dict = {x: x*x for x in range(1, 4)}
print("Squares Dict:", squares_dict)  # {1: 1, 2: 4, 3: 9}

unique_letters = {c for c in "hello"}
print("Unique Letters:", unique_letters)  # {'h', 'e', 'l', 'o'}
```

### Lambda Functions
Lambda functions are anonymous functions defined with a single expression.

**Syntax**: `lambda arguments: expression`

**Example**:
```python
add = lambda x, y: x + y
print(add(2, 3))  # 5

numbers = [1, 2, 3, 4, 5]
squared = map(lambda x: x ** 2, numbers)
print(list(squared))  # [1, 4, 9, 16, 25]

even_numbers = filter(lambda x: x % 2 == 0, numbers)
print(list(even_numbers))  # [2, 4]

pairs = [(1, 'one'), (2, 'two'), (3, 'three')]
sorted_pairs = sorted(pairs, key=lambda pair: pair[1])
print(sorted_pairs)  # [(1, 'one'), (3, 'three'), (2, 'two')]
```

## Map Function
The `map()` function applies a function to each item in an iterable, returning an iterator.

**Syntax**: `map(function, iterable, ...)`

**Example**:
```python
numbers = [1, 2, 3, 4, 5]
squared = map(lambda x: x ** 2, numbers)
print(list(squared))  # [1, 4, 9, 16, 25]

def square(x):
    return x ** 2
squared = map(square, numbers)
print(list(squared))  # [1, 4, 9, 16, 25]

numbers1 = [1, 2, 3]
numbers2 = [4, 5, 6]
summed = map(lambda x, y: x + y, numbers1, numbers2)
print(list(summed))  # [5, 7, 9]

words = ["Hello", "World"]
lowercased = map(str.lower, words)
print(list(lowercased))  # ['hello', 'world']
```

**Key Points**:
- Returns an iterator (use `list()` to view results).
- Supports multiple iterables for functions with multiple arguments.
- Lazy evaluation improves memory efficiency.
- List comprehensions may be more readable for complex transformations.

## Filter Function
The `filter()` function constructs an iterator from elements of an iterable for which a function returns `True`.

**Syntax**: `filter(function, iterable)`

**Example**:
```python
numbers = [1, 2, 3, 4, 5, 6]
even_numbers = filter(lambda x: x % 2 == 0, numbers)
print(list(even_numbers))  # [2, 4, 6]

def is_even(x):
    return x % 2 == 0
even_numbers = filter(is_even, numbers)
print(list(even_numbers))  # [2, 4, 6]

words = ["apple", "banana", "kiwi", "pear"]
long_words = filter(lambda word: len(word) > 4, words)
print(list(long_words))  # ['apple', 'banana']
```

**Key Points**:
- Returns an iterator.
- Lazy evaluation.
- Equivalent list comprehension: `[x for x in iterable if condition]`.

## Sorted Function
The `sorted()` function returns a new sorted list from an iterable.

**Syntax**: `sorted(iterable, key=None, reverse=False)`

**Example**:
```python
numbers = [5, 2, 9, 1, 5, 6]
sorted_numbers = sorted(numbers)
print(sorted_numbers)  # [1, 2, 5, 5, 6, 9]

words = ["banana", "apple", "cherry"]
sorted_words = sorted(words)
print(sorted_words)  # ['apple', 'banana', 'cherry']

sorted_words = sorted(words, key=len)
print(sorted_words)  # ['apple', 'banana', 'cherry']

sorted_numbers = sorted(numbers, reverse=True)
print(sorted_numbers)  # [9, 6, 5, 5, 2, 1]

pairs = [(1, 'one'), (3, 'three'), (2, 'two')]
sorted_pairs = sorted(pairs, key=lambda pair: pair[1])
print(sorted_pairs)  # [(1, 'one'), (3, 'three'), (2, 'two')]
```

## Decorators
Decorators wrap a function to extend its behavior without modifying it directly.

**Example**:
```python
from functools import wraps
import time

def my_decorator(func):
    def wrapper():
        print("Before function runs")
        func()
        print("After function runs")
    return wrapper

@my_decorator
def say_hello():
    print("Hello!")

say_hello()
# Output: Before function runs
#         Hello!
#         After function runs

def repeat_decorator(func):
    def wrapper(*args, **kwargs):
        print("Repeating twice:")
        func(*args, **kwargs)
        func(*args, **kwargs)
    return wrapper

@repeat_decorator
def greet(name):
    print(f"Hello, {name}!")

greet("Alice")
# Output: Repeating twice:
#         Hello, Alice!
#         Hello, Alice!

def bold(func):
    def wrapper():
        return "<b>" + func() + "</b>"
    return wrapper

def italic(func):
    def wrapper():
        return "<i>" + func() + "</i>"
    return wrapper

@bold
@italic
def get_text():
    return "Hello"

print(get_text())  # <b><i>Hello</i></b>

def log(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__}")
        return func(*args, **kwargs)
    return wrapper

@log
def add(x, y):
    """Adds two numbers"""
    return x + y

print("Add Result:", add(2, 3))
print("Function Name:", add.__name__)
print("Docstring:", add.__doc__)
# Output: Calling add
#         Add Result: 5
#         Function Name: add
#         Docstring: Adds two numbers

def timer(func):
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} took {end - start:.5f} seconds")
        return result
    return wrapper

@timer
def slow_function():
    time.sleep(1)

slow_function()
# Output: slow_function took ~1.00552 seconds
```

## Python Multithreading
Multithreading allows concurrent execution of tasks.

**Example**:
```python
import threading
import time
from concurrent.futures import ThreadPoolExecutor

print("\n=== 1. Basic Threading ===")
def worker(name):
    print(f"Thread {name} starting")
    time.sleep(2)
    print(f"Thread {name} finished")

t1 = threading.Thread(target=worker, args=("A",))
t2 = threading.Thread(target=worker, args=("B",))
t1.start()
t2.start()
t1.join()
t2.join()
print("All threads completed\n")

print("\n=== 2. Thread Class Example ===")
class MyThread(threading.Thread):
    def __init__(self, name):
        super().__init__()
        self.name = name
    def run(self):
        print(f"{self.name} is running")
        time.sleep(1)
        print(f"{self.name} finished")

t = MyThread("Thread-1")
t.start()
t.join()

print("\n=== 3. Synchronization with Lock ===")
lock = threading.Lock()
counter = 0

def increment():
    global counter
    for _ in range(100000):
        with lock:
            counter += 1

t1 = threading.Thread(target=increment)
t2 = threading.Thread(target=increment)
t1.start(); t2.start()
t1.join(); t2.join()
print("Counter (should be 200000):", counter)

print("\n=== 4. Daemon Thread Example ===")
def background_task():
    while True:
        print("Background running...")
        time.sleep(1)

daemon_thread = threading.Thread(target=background_task, daemon=True)
daemon_thread.start()
time.sleep(3)
print("Main program ends (daemon dies)\n")

print("\n=== 5. ThreadPoolExecutor Example ===")
def square(n):
    time.sleep(0.5)
    return n*n

with ThreadPoolExecutor(max_workers=3) as executor:
    results = list(executor.map(square, [1, 2, 3, 4, 5]))
    print("Squares from thread pool:", results)
# Output: Squares from thread pool: [1, 4, 9, 16, 25]
```

## Naming Conventions
- Modules and packages: lowercase (e.g., `my_module`).
- Globals and constants: UPPERCASE (e.g., `MAX_SIZE`).
- Classes: CamelCase with initial capital (e.g., `MyClass`).
- Methods and functions: lowercase_with_underscores (e.g., `my_function`).
- Local variables: lowercase or lowerCamelCase (e.g., `my_var` or `myVar`).
- Follow conventions of the codebase for consistency.

## Special Name Classes
- `_var`: Internal use, treated as private.
- `var_`: Avoids conflicts with Python keywords.
- `__var`: Name mangling to `_ClassName__var` for subclass protection.
- `__var__`: Special "dunder" methods (e.g., `__init__`, `__str__`).

**Example**:
```python
class MyClass:
    def __init__(self):
        self._internal_value = 42
        self.__private_value = 99
    def __str__(self):
        return "MyClass instance"

obj = MyClass()
print(obj._internal_value)  # 42
# print(obj.__private_value)  # AttributeError
print(obj._MyClass__private_value)  # 99 (name mangled)
```

## Doc Strings
Docstrings document functions, methods, classes, or modules.

**Example**:
```python
def add(a, b):
    """
    Adds two numbers and returns the result.

    Parameters:
        a (int or float): The first number.
        b (int or float): The second number.

    Returns:
        int or float: The sum of the two numbers.
    """
    return a + b

class MyClass:
    """
    This is a simple example class.

    Attributes:
        attribute1 (str): Description of attribute1.
        attribute2 (int): Description of attribute2.
    """
    def __init__(self, attribute1, attribute2):
        self.attribute1 = attribute1
        self.attribute2 = attribute2

print(add.__doc__)
print(MyClass.__doc__)
```

## Python Operators

### Arithmetic Operators
- `+`: Addition (e.g., `5 + 3` → 8)
- `-`: Subtraction (e.g., `5 - 3` → 2)
- `*`: Multiplication (e.g., `5 * 3` → 15)
- `/`: Division (e.g., `5 / 3` → 1.666...)
- `//`: Floor Division (e.g., `5 // 3` → 1)
- `%`: Modulus (e.g., `5 % 3` → 2)
- `**`: Exponentiation (e.g., `5 ** 3` → 125)

### Comparison Operators
- `==`: Equal (e.g., `5 == 3` → False)
- `!=`: Not equal (e.g., `5 != 3` → True)
- `>`: Greater than (e.g., `5 > 3` → True)
- `<`: Less than (e.g., `5 < 3` → False)
- `>=`: Greater than or equal (e.g., `5 >= 3` → True)
- `<=`: Less than or equal (e.g., `5 <= 3` → False)

### Logical Operators
- `and`: True if both operands are true (e.g., `(5 > 3) and (3 > 1)` → True)
- `or`: True if at least one operand is true (e.g., `(5 > 3) or (3 < 1)` → True)
- `not`: Reverses logical state (e.g., `not (5 > 3)` → False)

### Assignment Operators
- `=`: Assign (e.g., `x = 5`)
- `+=`: Add and assign (e.g., `x += 3` → `x = x + 3`)
- `-=`: Subtract and assign
- `*=`: Multiply and assign
- `/=`: Divide and assign
- `%=`: Modulus and assign
- `**=`: Exponent and assign
- `//=`: Floor divide and assign

### Bitwise Operators
- `&`: Bitwise AND (e.g., `5 & 3` → 1)
- `|`: Bitwise OR (e.g., `5 | 3` → 7)
- `^`: Bitwise XOR (e.g., `5 ^ 3` → 6)
- `~`: Bitwise NOT (e.g., `~5` → -6)
- `<<`: Left shift (e.g., `5 << 1` → 10)
- `>>`: Right shift (e.g., `5 >> 1` → 2)

### Difference between != and <>
- `!=` is the standard inequality operator in Python 3.
- `<>` was supported in Python 2 but removed in Python 3; use `!=` for compatibility.

### is, not is Operator
The `is` operator checks object identity (same memory location).

**Example**:
```python
a = [1, 2, 3]
b = a
print(a is b)  # True (same object)

a = [1, 2, 3]
b = [1, 2, 3]
print(a is b)  # False (different objects)

a = 1000
b = 1000
print(a is b)  # False (large integers not cached)

a = None
print(a is None)  # True
```

### in, not in Operator
The `in` operator checks for membership in an iterable.

**Example**:
```python
fruits = ["apple", "banana", "cherry"]
print("banana" in fruits)  # True

text = "Hello, World!"
print("Hello" in text)  # True

numbers = (1, 2, 3, 4, 5)
print(3 in numbers)  # True

unique_numbers = {1, 2, 3, 4, 5}
print(4 in unique_numbers)  # True

person = {"name": "Alice", "age": 30}
print("name" in person)  # True
print("name" in person.keys())  # True
```

## Timeit for Measuring Function Execution Time
The `timeit` module measures the execution time of small code snippets.

**Example**:
```python
import timeit

def filter_even_numbers_loop():
    numbers = range(0, 10000000)
    even_numbers = []
    for number in numbers:
        if number % 2 == 0:
            even_numbers.append(number)
    return even_numbers

def filter_even_numbers_comprehension():
    numbers = range(0, 10000000)
    even_numbers = [number for number in numbers if number % 2 == 0]
    return even_numbers

loop_time = timeit.timeit(filter_even_numbers_loop, number=10)
comprehension_time = timeit.timeit(filter_even_numbers_comprehension, number=10)
print(f"For-loop time: {loop_time}")
print(f"List comprehension time: {comprehension_time}")
```

## cProfile for Measuring Performance
The `cProfile` module profiles code to analyze performance.

**Example**:
```python
import cProfile

def filter_even_numbers_loop():
    numbers = range(0, 10000000)
    even_numbers = []
    for number in numbers:
        if number % 2 == 0:
            even_numbers.append(number)
    return even_numbers

def filter_even_numbers_comprehension():
    numbers = range(0, 10000000)
    even_numbers = [number for number in numbers if number % 2 == 0]
    return even_numbers

def profile_functions():
    print("Profiling filter_even_numbers_loop:")
    cProfile.run('filter_even_numbers_loop()')
    print("\nProfiling filter_even_numbers_comprehension:")
    cProfile.run('filter_even_numbers_comprehension()')

profile_functions()
```

## Kinds of Iterables
Iterables are objects that can be looped over, including:
- Basic types: Strings, Lists, Sets, Tuples, Dictionaries
- Other objects: Files, `range`, `zip`, `collections`, `enumerate`
- Complex types: Generators

## Range vs Xrange Functions
- Python 2: `range()` returns a list; `xrange()` returns an iterator.
- Python 3: `xrange()` is removed; `range()` behaves like `xrange()` (returns an iterator).

## Tuples and Lists

### List
Lists are mutable, ordered sequences.

**Example**:
```python
numbers = [1, 2, 3, 4, 5]
even_squares = [x ** 2 for x in numbers if x % 2 == 0]
print(even_squares)  # [4, 16]

seen = set()
unique = [x for x in ['apple', 'banana', 'apple'] if not (x in seen or seen.add(x))]
print(unique)  # ['apple', 'banana']

list1 = [1, 2, 3]
list2 = ['a', 'b', 'c']
merged = list(zip(list1, list2))
print(merged)  # [(1, 'a'), (2, 'b'), (3, 'c')]
```

**Common Mistakes**:
- **Modifying while iterating**: Iterate over a copy (`list(lst)`) or use comprehensions.
- **Confusing `append` vs `extend`**:
  ```python
  fruits = ["apple", "banana"]
  fruits.append(["cherry", "date"])  # ['apple', 'banana', ['cherry', 'date']]
  fruits = ["apple", "banana"]
  fruits.extend(["cherry", "date"])  # ['apple', 'banana', 'cherry', 'date']
  ```
- **Mutable default arguments**:
  ```python
  def add_item(item, items=None):
      if items is None:
          items = []
      items.append(item)
      return items
  ```

### Shallow Copy vs Deep Copy
- **Shallow Copy**: Copies outer structure, shares nested objects.
- **Deep Copy**: Copies all objects recursively.

**Example**:
```python
import copy

original_list = [[1, 2, 3], [4, 5, 6]]
shallow_copied_list = copy.copy(original_list)
shallow_copied_list[0][0] = 99
print(original_list)  # [[99, 2, 3], [4, 5, 6]]

deep_copied_list = copy.deepcopy(original_list)
deep_copied_list[0][0] = 88
print(original_list)  # [[99, 2, 3], [4, 5, 6]]
```

### Tuples
Tuples are immutable sequences. Use `(x,)` for single-element tuples.

**Example**:
```python
t = (1, 2, 3)
print(t[0])  # 1
# t[0] = 4  # TypeError (immutable)
```

## Strings
Strings are immutable sequences of characters.

**Example**:
```python
s = 'Hello'
print('Hello' in 'Hello, World!')  # True

import re
print(re.findall(r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b', 'test@example.com'))  # ['test@example.com']

import codecs
with codecs.open('file.txt', 'r', encoding='utf-8') as f:
    text = f.read()
```

## Dictionary
Dictionaries store key-value pairs, with keys being immutable and hashable.

**Example**:
```python
my_dict = {'a': 1, 'b': 2}
for name, value in my_dict.items():
    print(name, value)  # a 1, b 2

values = {'vegetable': 'chard', 'fruit': 'nectarine'}
output = 'I love %(vegetable)s and I love %(fruit)s.' % values
print(output)  # I love chard and I love nectarine.

del my_dict['a']
value = my_dict.pop('b')
print(my_dict.get('c', 'Unknown'))  # Unknown
my_dict.update({'b': 2, 'c': 3})
```

### Shallow Copy vs Deep Copy
- **Shallow Copy**: Copies references to nested objects.
- **Deep Copy**: Copies all nested objects.

**Example**:
```python
import copy

original = {'numbers': [1, 2, 3], 'letters': ['a', 'b', 'c']}
shallow_copy = original.copy()
shallow_copy['numbers'].append(4)
print(original['numbers'])  # [1, 2, 3, 4]

deep_copy = copy.deepcopy(original)
deep_copy['numbers'].append(5)
print(original['numbers'])  # [1, 2, 3, 4]
```

**Best Practices**:
- Use `dict.fromkeys()` for initialization: `dict.fromkeys(['a', 'b'], 0)`
- Avoid repeated key lookups: Store values in variables.
- Use generators for large data: `def data_generator(): yield item`

## Yield
The `yield` keyword creates generators, producing values lazily.

**Example**:
```python
def count_up_to(max):
    count = 1
    while count <= max:
        yield count
        count += 1

counter = count_up_to(5)
for number in counter:
    print(number)  # 1, 2, 3, 4, 5

def infinite_counter():
    count = 0
    while True:
        yield count
        count += 1

counter = infinite_counter()
for _ in range(5):
    print(next(counter))  # 0, 1, 2, 3, 4
```

## Generator Expression
Generator expressions are like list comprehensions but use parentheses for lazy evaluation.

**Example**:
```python
squares = (x * x for x in range(5))
for square in squares:
    print(square)  # 0, 1, 4, 9, 16
```

## Searching Algorithms

### Linear Search
Sequential search with O(n) complexity.

**Example**:
```python
def linear_search(lst, target):
    for index, element in enumerate(lst):
        if element == target:
            return index
    return -1

numbers = [4, 2, 3, 1, 5]
print(linear_search(numbers, 3))  # 2
```

### Binary Search
Efficient search on sorted lists with O(log n) complexity.

**Example**:
```python
def binary_search(lst, target):
    low, high = 0, len(lst) - 1
    while low <= high:
        mid = (low + high) // 2
        if lst[mid] == target:
            return mid
        elif lst[mid] < target:
            low = mid + 1
        else:
            high = mid - 1
    return -1

sorted_numbers = [1, 2, 3, 4, 5]
print(binary_search(sorted_numbers, 3))  # 2
```

## Positional and Keyword Arguments
- Positional arguments: Passed in order, unpacked with `*`.
- Keyword arguments: Passed by name, unpacked with `**`.

**Example**:
```python
import sys
import time

def print_debug_info(target_function):
    def wrapper_function(*args, **kwargs):
        print(f"positional arguments are {args}")
        print(f"keyword arguments are {kwargs}")
        return target_function(*args, **kwargs)
    return wrapper_function

@print_debug_info
def perform_task(msg):
    print(f"Performing task for {msg}")

perform_task("ysree")
# Output: positional arguments are ('ysree',)
#         keyword arguments are {}
#         Performing task for ysree
```

## Different Types of Methods

### Instance Methods
Operate on instance data, take `self` as the first parameter.

**Example**:
```python
class MyClass:
    def instance_method(self):
        return "Instance method"

obj = MyClass()
print(obj.instance_method())  # Instance method
```

### Class Methods
Operate on the class, take `cls` as the first parameter, use `@classmethod`.

**Example**:
```python
class MyClass:
    count = 0
    @classmethod
    def increment_count(cls):
        cls.count += 1

MyClass.increment_count()
print(MyClass.count)  # 1
```

### Static Methods
Utility methods, don’t take `self` or `cls`, use `@staticmethod`.

**Example**:
```python
class MathOperations:
    @staticmethod
    def add(x, y):
        return x + y

print(MathOperations.add(5, 3))  # 8
```

## Magic Methods
Magic methods (e.g., `__init__`, `__repr__`) define special behavior.

**Example**:
```python
class Archer:
    def __init__(self, name, age, arrows):
        self.name = name
        self.age = age
        self.arrows = arrows
    def shoot(self):
        if self.arrows > 0:
            self.arrows -= 1
            print(f"Archer {self.name} shot, arrows left {self.arrows}!")
        else:
            print(f"Archer {self.name} has no arrows left")
    def __repr__(self):
        return f"Archer {self.name} with age {self.age} with arrows {self.arrows}"

archer = Archer("Legolas", 2931, 3)
archer.shoot()  # Archer Legolas shot, arrows left 2!
print(archer)   # Archer Legolas with age 2931 with arrows 2
```

## Property Decorator
`@property` allows methods to be accessed like attributes.

**Example**:
```python
class Circle:
    def __init__(self, radius):
        self._radius = radius
    @property
    def radius(self):
        return self._radius
    @radius.setter
    def radius(self, value):
        if value < 0:
            raise ValueError("Radius cannot be negative")
        self._radius = value

c = Circle(5)
print(c.radius)  # 5
c.radius = 10
print(c.radius)  # 10
```

## Recurring Functions
The `@lru_cache` decorator caches results of recursive functions for efficiency.

**Example**:
```python
from functools import lru_cache

@lru_cache(maxsize=32)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

print(fibonacci(10))  # 55
```

## Python Slicing

Python slicing is a powerful feature that allows you to extract a portion of a sequence (like lists, tuples, strings, or arrays) using a concise syntax. It works on any iterable that supports indexing, such as strings, lists, tuples, and NumPy arrays. Slicing is zero-indexed, meaning the first element is at index 0.

### Basic Slicing Syntax

The general syntax for slicing is:
```python
sequence[start:stop:step]
```

- **`start`**: Starting index (inclusive). Defaults to 0 if omitted.
- **`stop`**: Ending index (exclusive). Defaults to the sequence length if omitted.
- **`step`**: Increment between elements. Defaults to 1 if omitted. Can be positive (forward) or negative (backward).

**Key Points**:
- If `start` is omitted, it defaults to the beginning (0).
- If `stop` is omitted, it defaults to the end of the sequence.
- Negative indices count from the end (-1 is the last element).
- Slicing creates a **shallow copy** of the selected elements (for mutable sequences like lists).
- Out-of-bounds indices don't raise errors; they are handled gracefully (e.g., slicing beyond the end returns an empty sequence).

### Examples

#### 1. Basic Slicing on Lists
Lists are mutable sequences, and slicing extracts a sublist.

```python
# Example list
my_list = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

# Slice from index 2 to 5 (exclusive of 5)
print(my_list[2:5])  # [2, 3, 4]

# Slice from start to index 3
print(my_list[:3])   # [0, 1, 2]

# Slice from index 5 to end
print(my_list[5:])   # [5, 6, 7, 8, 9]

# Full copy of the list
print(my_list[:])    # [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```

#### 2. Slicing with Step
The `step` parameter skips elements.

```python
my_list = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

# Every second element from start to end
print(my_list[::2])  # [0, 2, 4, 6, 8]

# Every second element starting from index 1
print(my_list[1::2]) # [1, 3, 5, 7, 9]

# Reverse the list (step = -1)
print(my_list[::-1]) # [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]

# Every third element in reverse
print(my_list[::-3]) # [9, 6, 3, 0]
```

#### 3. Negative Indices and Reverse Slicing
Negative indices start from the end of the sequence.

```python
my_list = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

# Last three elements
print(my_list[-3:])  # [7, 8, 9]

# From second last to fourth last (inclusive)
print(my_list[-5:-2]) # [5, 6, 7]

# Reverse from index 3 to start
print(my_list[3::-1]) # [3, 2, 1, 0]

# Elements from end to start with step 2
print(my_list[-1::-2]) # [9, 7, 5, 3, 1]
```

#### 4. Slicing Strings
Strings are immutable sequences, so slicing returns a new string.

```python
text = "Hello, World!"

# First 5 characters
print(text[:5])      # "Hello"

# From index 7 to end
print(text[7:])      # "World!"

# Every second character
print(text[::2])     # "Hlo ol!"

# Reverse the string
print(text[::-1])    # "!dlroW ,olleH"

# Last 5 characters
print(text[-5:])     # "orld!"
```

#### 5. Slicing Tuples
Tuples are immutable, similar to lists but unchangeable.

```python
my_tuple = (0, 1, 2, 3, 4, 5)

# Slice like a list
print(my_tuple[1:4]) # (1, 2, 3)

# Reverse
print(my_tuple[::-1]) # (5, 4, 3, 2, 1, 0)
```

#### 6. Advanced: Slicing with Out-of-Bounds
Slicing handles invalid indices without errors.

```python
my_list = [0, 1, 2, 3]

# Start beyond end (empty)
print(my_list[10:])  # []

# Stop before start (empty)
print(my_list[3:1])  # []

# Negative step with invalid range
print(my_list[1:3:-1]) # []
```

#### 7. Assigning to Slices (Mutable Sequences Only)
For lists (mutable), you can assign values to slices.

```python
my_list = [0, 1, 2, 3, 4]

# Replace a slice
my_list[1:3] = [10, 20]
print(my_list)  # [0, 10, 20, 3, 4]

# Insert by assigning to empty slice
my_list[1:1] = [99]  # Inserts at index 1
print(my_list)  # [0, 99, 10, 20, 3, 4]

# Delete a slice
del my_list[2:4]
print(my_list)  # [0, 99, 3, 4]

# Extend with step (replace every other)
my_list[::2] = [100, 200, 300]
print(my_list)  # [100, 99, 200, 4, 300]
```

**Note**: Assignment requires the right-hand side length to match the slice length when using step ≠ 1, or it may raise a `ValueError`.

#### 8. Slicing NumPy Arrays
NumPy arrays support advanced slicing, including boolean and multi-dimensional.

```python
import numpy as np

arr = np.array([0, 1, 2, 3, 4, 5])

# Basic slicing
print(arr[1:4])  # [1 2 3]

# Boolean slicing
print(arr[arr > 2])  # [3 4 5]

# 2D array slicing
arr_2d = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
print(arr_2d[1:, 1:])  # [[5 6] [8 9]]
```

### Best Practices and Common Pitfalls

- **Shallow Copies**: `my_list[:]` creates a shallow copy. For deep copies (nested objects), use `copy.deepcopy()`.
- **Step Pitfalls**: Negative step with positive range can yield empty results (e.g., `[3:1:-1]` works, but `[1:3:-1]` doesn't).
- **Performance**: Slicing is O(k) where k is the slice length, efficient for large sequences.
- **Immutables**: Can't assign to slices of strings or tuples (raises `TypeError`).
- **Use Cases**: Use slicing for data extraction, reversing sequences, or creating views in NumPy for memory efficiency.

### Integration with Other Python Concepts
Slicing integrates well with list comprehensions for filtering or transforming data:
```python
numbers = [1, 2, 3, 4, 5]
evens = [x for x in numbers[::2]]  # [1, 3, 5]
```

For Kubernetes RBAC automation (from your previous context), slicing can extract subsets of user lists or role definitions from YAML configs processed in Python.

## Frameworks
- `numpy`: Numerical computing, arrays, linear algebra.
- `pandas`: Data analysis with DataFrames.
- `matplotlib`: Plotting and visualization.
- `seaborn`: Statistical visualization.
- `plotly`: Interactive visualizations.
- `scikit-learn`: Machine learning.
- `tensorflow`, `pytorch`: Deep learning.
- `xgboost`: Gradient boosting.
- `flask`, `django`, `fastapi`: Web frameworks.
- `requests`, `paramiko`: HTTP and SSH.
- `pytest`, `unittest`: Testing.
- `logging`, `json`, `itertools`, `argparse`: Utilities.

## Pytest
`pytest` is a powerful testing framework.

**Example**:
```python
import pytest
import json

def add(a, b):
    return a + b

def test_add_basic():
    assert add(2, 3) == 5

def multiply(a, b):
    return a * b

def test_multiply_positive():
    assert multiply(2, 3) == 6

def test_multiply_zero():
    assert multiply(5, 0) == 0

@pytest.fixture
def numbers():
    return [1, 2, 3]

def test_sum(numbers):
    assert sum(numbers) == 6

def test_max(numbers):
    assert max(numbers) == 3

@pytest.mark.parametrize("a,b,expected", [
    (2, 3, 5),
    (10, -5, 5),
    (0, 0, 0)
])
def test_add_param(a, b, expected):
    assert add(a, b) == expected

def divide(a, b):
    return a / b

def test_zero_division():
    with pytest.raises(ZeroDivisionError):
        divide(5, 0)

@pytest.mark.skip(reason="Not implemented yet")
def test_todo():
    assert False

@pytest.mark.xfail(reason="Known bug")
def test_bug():
    assert 1 / 0 == 1

@pytest.fixture(scope="module")
def db_connection():
    print("\n[SETUP] DB connection")
    yield "db_conn"
    print("\n[TEARDOWN] Close DB connection")

def test_read(db_connection):
    assert db_connection == "db_conn"

def test_write(db_connection):
    assert db_connection.startswith("db")

@pytest.mark.slow
def test_heavy():
    assert sum(range(100000)) > 0

@pytest.mark.fast
def test_light():
    assert 2 + 2 == 4

@pytest.fixture
def config(tmp_path):
    cfg = tmp_path / "config.json"
    cfg.write_text('{"debug":true}')
    return json.loads(cfg.read_text())

def test_config(config):
    assert config["debug"] is True

@pytest.fixture(params=[1, 2, 3])
def num(request):
    return request.param

def test_double(num):
    assert num * 2 in [2, 4, 6]

def get_user():
    raise Exception("DB not connected")

def test_mock(mocker):
    mocker.patch("__main__.get_user", return_value="Alice")
    assert get_user() == "Alice"

class Cart:
    def __init__(self):
        self.items = []
    def add(self, item):
        self.items.append(item)
    def total(self):
        return len(self.items)

@pytest.fixture
def cart():
    return Cart()

def test_cart_add(cart):
    cart.add("apple")
    assert cart.total() == 1

@pytest.mark.parametrize("item,count", [
    ("banana", 1), ("mango", 1)
])
def test_cart_param(cart, item, count):
    cart.add(item)
    assert cart.total() == count
```

**Conftest.py**:
```python
import pytest

@pytest.fixture
def user():
    return {"name": "Alice", "role": "admin"}

@pytest.fixture(scope="session")
def db_connection():
    print("\n[SETUP] Start DB session")
    yield "db_conn"
    print("\n[TEARDOWN] Close DB session")

def pytest_addoption(parser):
    parser.addoption("--env", action="store", default="dev")

@pytest.fixture
def env(request):
    return request.config.getoption("--env")

@pytest.fixture
def mock_user(mocker):
    mocker.patch("app.get_user", return_value="Bob")
    return "Bob"
```