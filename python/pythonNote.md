--------------------------------------------

Python concepts to read 

--------------------------------------------------

Generator - https://www.youtube.com/playlist?list=PLsyeobzWxl7poL9JTVyndKe62ieoN-MZ3
    >>> def test():
    ...     i = 1
    ...     while i <= 100:
    ...         yield i
    ...         i+=1
    ...
    >>>
    >>> itr = test()
    >>> next(itr)
    >>> for i in itr:
    ...     next(itr)


Iterator
    >>> a = [1,2,3,4]
    >>> itr = iter(a)
    >>> next(itr)
    1
    >>> itr.__next__()
    2

    >>> a = [1,2,3,4]
    >>> itr = a.__iter__()
    >>> itr.__next__()
    1
    >>> next(itr)
    2

What are *args and **kwargs?
    *args → Pass variable number of positional arguments to a function.
    **kwargs → Pass variable number of keyword arguments (key=value) to a function.

collections - ordereddict ,defaultdict ,deque 
OrderedDict
    - OrderedDict is a dictionary that remembers the order of keys, providing predictable iteration and additional order-manipulation methods.
    - defaultdict is a dictionary that provides a default value for missing keys, avoiding KeyError exceptions.

    eg. 
    from collections import OrderedDict

    # Create an OrderedDict
    od = OrderedDict()
    od['apple'] = 10
    od['banana'] = 20
    od['cherry'] = 30

    # Iterating preserves insertion order
    for key, value in od.items():
        print(key, value)

    # OrderedDict specific methods
    od.move_to_end('banana')  # Move 'banana' to the end
    print(list(od.keys()))    # ['apple', 'cherry', 'banana']

    od.popitem(last=False)    # Remove first item
    print(list(od.keys()))    # ['cherry', 'banana']

Defaultdict
    - defaultdict is a subclass of the built-in dict from the collections module.
    - It provides a default value for missing keys automatically, so you don’t get a KeyError.
    - You define a default factory function that provides the default value.

    EG. 
        >>> from collections import defaultdict
        ...
        ... # Create a defaultdict with default type int
        ... dd = defaultdict(int)
        ...
        ... dd['apples'] += 10
        ... dd['bananas'] += 5
        ...
        ... print(dd)
        ...
        # Output:
        # defaultdict(<class 'int'>, {'apples': 10, 'bananas': 5})


    EG. # Default value is an empty list
        dd_list = defaultdict(list)

        dd_list['fruits'].append('apple')
        dd_list['fruits'].append('banana')
        dd_list['vegetables'].append('carrot')

        print(dd_list)

        # Output:
        # defaultdict(<class 'list'>, {'fruits': ['apple', 'banana'], 'vegetables': ['carrot']})

    EG. # Counting occurrences of items
        words = ['apple', 'banana', 'apple', 'orange', 'banana']
        count = defaultdict(int)
        for word in words:
            count[word] += 1
        print(count)
        # Output: defaultdict(<class 'int'>, {'apple': 2, 'banana': 2, 'orange': 1})

    EG. # Grouping/Pair items
        pairs = [('a', 1), ('b', 2), ('a', 3)]
        group = defaultdict(list)
        for k, v in pairs:
            group[k].append(v)
        print(group)
        # Output: defaultdict(<class 'list'>, {'a': [1, 3], 'b': [2]})
Deque
    - deque stands for double-ended queue.
    - Supports adding/removing elements from both ends efficiently (O(1) operations).
    - Unlike regular lists, deque is optimized for fast appends and pops from both ends

    Eg.
    from collections import deque
    # Create a deque
    dq = deque([1, 2, 3])

    # Add elements to the right (default)
    dq.append(4)
    print(dq)  # deque([1, 2, 3, 4])

    # Add elements to the left
    dq.appendleft(0)
    print(dq)  # deque([0, 1, 2, 3, 4])

    # Remove from right
    dq.pop()
    print(dq)  # deque([0, 1, 2, 3])

    # Remove from left
    dq.popleft()
    print(dq)  # deque([1, 2, 3])

    # Rotate the deque
    dq.rotate(1)  # Rotate right
    print(dq)  # deque([3, 1, 2])

    dq.rotate(-1)  # Rotate left
    print(dq)  # deque([1, 2, 3])



itertools Notes with Examples
    Lazy Evaluation using itertools: 
			Python 3 emphasizes lazy evaluation, and itertools fits perfectly into this paradigm by providing iterators that evaluate items only when needed.
				Integration with Generators: itertools functions work seamlessly with generator expressions, enhancing their utility in Python 3's iterator-based approach.
				Performance: The functions in itertools are implemented in C, making them highly efficient for iteration tasks.
					Overall, itertools is a powerful module in Python 3 that enhances the language's capabilities for handling iteration and functional programming, making it a valuable tool for developers.
					
					from itertools import groupby
					data = [1, 1, 2, 2, 3, 3]
					for key, group in groupby(data):
						print(key, list(group))
		
		please explore the packages using itertools
			https://www.youtube.com/watch?v=znyTAmoyMlA&list=PL-2EBeDYMIbSfbjfLIRtwAP21rf4QxaYb
		
		Remove all the elements from list - 						fruits.clear()
		Counting Elements: Count the occurrences of an element - 	count = fruits.count("apple")
		Finding the Index: Get the index of an element - 			index = fruits.index("cherry")
		Sorting: Sort the list in ascending order.					fruits.sort()
		Reversing: Reverse the order of the list. 					fruits.reverse()
		Concatenation: Combine two lists							vegetables = ["carrot", "broccoli"] , all_items = fruits + vegetables
		Appending: Add an element to the end of the list			fruits.append("orange")
		Inserting: Insert an element at a specific position			fruits.insert(1, "kiwi")

    itertools is a built-in module in Python that provides fast, memory-efficient tools for creating iterators.

    count(start=0, step=1) → Infinite counter. Example:
    for i in itertools.count(5, 2): if i > 15: break; print(i) 
    → 5 7 9 11 13 15

    cycle(iterable) → Repeats items forever. Example:
    for i, val in zip(range(6), itertools.cycle(['A', 'B', 'C'])): print(val) 
    → A B C A B C

    repeat(object, times) → Repeats the same element. Example:
    list(itertools.repeat("Hi", 3)) 
    → ['Hi', 'Hi', 'Hi']

    accumulate(iterable, func) → Running totals (default sum). Example:
    list(itertools.accumulate([1, 2, 3, 4])) 
    → [1, 3, 6, 10]; with multiplication:
    list(itertools.accumulate([1, 2, 3, 4], operator.mul)) 
    → [1, 2, 6, 24]

    *chain(iterables) → Joins multiple iterables. Example:
    list(itertools.chain([1, 2], ['a', 'b'])) 
    → [1, 2, 'a', 'b']

    compress(data, selectors) → Filters elements using a selector list. Example:
    list(itertools.compress("PYTHON", [1, 0, 1, 0, 1, 0])) 
    → ['P', 'T', 'O']

    dropwhile(predicate, iterable) → Drops elements while condition is true, then returns the rest. Example:
    list(itertools.dropwhile(lambda x: x < 5, [1, 4, 6, 2, 7])) 
    → [6, 2, 7]

    takewhile(predicate, iterable) → Takes elements while condition is true. Example:
    list(itertools.takewhile(lambda x: x < 5, [1, 4, 6, 2, 7])) 
    → [1, 4]

    product(iterables, repeat=1) → Cartesian product (like nested loops). Example:
    list(itertools.product([1, 2], ['a', 'b'])) 
    → [(1, 'a'), (1, 'b'), (2, 'a'), (2, 'b')];
    list(itertools.product([0, 1], repeat=2)) 
    → [(0, 0), (0, 1), (1, 0), (1, 1)]

    permutations(iterable, r=None) → All possible orderings. Example:
    list(itertools.permutations([1, 2, 3], 2)) 
    → [(1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2)]

    combinations(iterable, r) → Unique selections without replacement. Example:
    list(itertools.combinations([1, 2, 3], 2)) 
    → [(1, 2), (1, 3), (2, 3)]

    combinations_with_replacement(iterable, r) 
    → Selections with replacement. Example:
    list(itertools.combinations_with_replacement([1, 2], 2)) 
    → [(1, 1), (1, 2), (2, 2)]

    groupby(iterable, key) → Groups consecutive elements by key. Example:
    for k, g in itertools.groupby([("a", 1), ("a", 2), ("b", 3), ("b", 4)], lambda x: x[0]): print(k, list(g))
    → a [('a', 1), ('a', 2)] and b [('b', 3), ('b', 4)]

classes and oops concepts
    Python OOP (Object-Oriented Programming) Notes
    1. Class and Object
        Class → Blueprint for creating objects (defines attributes & methods).
        Object → Instance of a class (real entity with state and behavior).

        class Car:
            def __init__(self, brand, model):
                self.brand = brand   # attribute
                self.model = model

            def drive(self):        # method
                return f"{self.brand} {self.model} is driving."

        car1 = Car("Tesla", "Model 3")
        print(car1.drive())   # Tesla Model 3 is driving.

    2. Inheritance
        Mechanism to create a new class (child) from an existing class (parent).
        Child class inherits attributes and methods from the parent class.
        class ElectricCar(Car):  # Inherits from Car
            def charge(self):
                return f"{self.brand} {self.model} is charging."

        ecar1 = ElectricCar("Tesla", "Model S")
        print(ecar1.drive())   # Tesla Model S is driving.
        print(ecar1.charge())  # Tesla Model S is charging.

    3. Encapsulation
        Bundling data (attributes) and methods that operate on the data within a class.
        Use of private attributes/methods (prefix with __) to restrict access.
        class BankAccount:
            def __init__(self, balance):
                self.__balance = balance  # private attribute   
            def deposit(self, amount):
                self.__balance += amount
            def get_balance(self):
                return self.__balance

        acc = BankAccount(1000)
        acc.deposit(500)
        print(acc.get_balance())  # 1500    
        # print(acc.__balance)  # Error: AttributeError

    4. Polymorphism
        Ability to use a common interface for different data types.
        Method overriding: Child class provides a specific implementation of a method defined in the parent class.
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

    5. Abstraction
        Hiding complex implementation details and showing only the essential features.
        Use of abstract base classes (ABC) and abstract methods.
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

    6. Special Methods (Magic Methods)
        Special methods with double underscores (__) to define object behavior.
        Examples: __init__, __str__, __repr__, __len__, __add__, etc.
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
        print(p1)          # Point(2, 3)
        p3 = p1 + p2
        print(p3)         # Point(6, 8)

    7. Class vs Instance Variables
        Class Variables → Shared across all instances of the class.
        Instance Variables → Unique to each instance.
        class Dog:
            species = "Canis familiaris"  # class variable
            def __init__(self, name):
                self.name = name            # instance variable
        dog1 = Dog("Buddy")
        dog2 = Dog("Max")
        print(dog1.species)  # Canis familiaris
        print(dog2.species)  # Canis familiaris
        print(dog1.name)     # Buddy
        print(dog2.name)     # Max

    8. Class Methods and Static Methods
        Class Methods → Use @classmethod decorator, takes cls as first parameter, can access class variables.
        Static Methods → Use @staticmethod decorator, does not take self or cls, utility functions.
        class Circle:
            pi = 3.14159  # class variable
            def __init__(self, radius):
                self.radius = radius  # instance variable
            
            def get_area(self):                     # Instance method where self is must
                return self.radius                   

            @classmethod
            def circle_area(cls, radius):           # No Self, cls is must here
                return cls.pi * (radius ** 2)
                
            @staticmethod
            def circumference(radius):              # No self , cls 
                return 2 * Circle.pi * radius

        print(Circle.circle_area(5))  # 78.53975
        print(Circle.circumference(5))  # 31.4159

    9. Property Decorators
        Use @property decorator to define getter methods for attributes.
        Use @<property_name>.setter to define setter methods.
        class Person:
            def __init__(self, name):
                self._name = name  # private attribute
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

    10. Multiple Inheritance
        A class can inherit from multiple parent classes.
        class A:
            def method_a(self):         
                return "Method A"
        class B:
            def method_b(self):
                return "Method B"   
        class C(A, B):  # Inherits from A and B
            def method_c(self):
                return "Method C"
        c = C()
        print(c.method_a())  # Method A
        print(c.method_b())  # Method B
        print(c.method_c())  # Method C 

    11. Method Resolution Order (MRO)
        In multiple inheritance, MRO determines the order in which base classes are searched when executing a method.
        Use ClassName.mro() or help(ClassName) to see the MRO.
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
        print(D.mro())    # [D, B, C, A, object]

    12. Composition vs Inheritance
        Composition → Building complex objects by combining simpler ones (has-a relationship).
        Inheritance → Creating a new class from an existing class (is-a relationship).
        class Engine:
            def start(self):
                return "Engine started"
        class Car:
            def __init__(self):
                self.engine = Engine()  # Composition
            def start(self):    
                return self.engine.start()
        car = Car()
        print(car.start())  # Engine started     

    13. Exception Handling in OOP
        Use try-except blocks to handle exceptions in methods.
        class Calculator:
            def divide(self, a, b):
                try:
                    return a / b
                except ZeroDivisionError:
                    return "Error: Division by zero"
        calc = Calculator()
        print(calc.divide(10, 2))  # 5.0
        print(calc.divide(10, 0))  # Error: Division by zero

    14. Data Classes (Python 3.7+)
        Use @dataclass decorator to automatically generate special methods like __init__, __repr__, and __eq__.
        from dataclasses import dataclass
        @dataclass
        class Point:
            x: int
            y: int  
        p1 = Point(2, 3)
        p2 = Point(2, 3)
        print(p1)          # Point(x=2, y=3)    
        print(p1 == p2)   # True

    15. Slots
        Use __slots__ to restrict dynamic creation of attributes and save memory.
        class Person:
            __slots__ = ['name', 'age']  # restrict attributes
            def __init__(self, name, age):
                self.name = name
                self.age = age      
        p = Person("Alice", 30)
        print(p.name)  # Alice
        # p.address = "123 Street"  # Error: AttributeError 

    16. Metaclasses
        Metaclasses are classes of classes that define how classes behave.
        Use metaclasses to customize class creation.
        class Meta(type):
            def __new__(cls, name, bases, attrs):
                attrs['class_name'] = name
                return super().__new__(cls, name, bases, attrs)
        class MyClass(metaclass=Meta):
            pass
        obj = MyClass()
        print(obj.class_name)  # MyClass

    17. Memory Management and Garbage Collection
        Python uses reference counting and a cyclic garbage collector to manage memory.
        When an object’s reference count drops to zero, it is automatically deallocated.
        Use the gc module to interact with the garbage collector.
        import gc
        gc.collect()  # Manually trigger garbage collection     

    18. Advanced Topics
        - Design Patterns (Singleton, Factory, Observer, etc.)
        - Dependency Injection
        - Mixins
        - Interfaces (using ABC)
        - SOLID Principles
        - UML Diagrams for OOP Design
        - Best Practices for OOP in Python

    19. Best Practices
        - Follow PEP 8 style guide for code readability.
        - Use meaningful class and method names.
        - Keep classes focused on a single responsibility (Single Responsibility Principle).
        - Prefer composition over inheritance when appropriate.
        - Document classes and methods with docstrings.
        - Write unit tests for classes and methods.
        - Use version control (e.g., Git) for code management.
        - Regularly refactor code to improve design and maintainability.    

list comprehension and lambdas

	List comprehension is a concise and expressive way to create lists in Python. It allows you to generate a new list by applying an expression to each item in an existing iterable (such as a list, tuple, or string) and can include optional filtering logic. List comprehensions provide a more readable and efficient way to create lists compared to using traditional loops.
	
	syntax :  [expression for item in iterable if condition] 
	
		expression: The expression that is evaluated and included in the new list.
		item: The variable that takes the value of each element in the iterable.
		iterable: The collection being iterated over.
		condition: (Optional) A filter that determines if the expression should be included in the new list.
		
	Key Points
		Conciseness: List comprehensions allow for more concise and readable code compared to traditional loops.
		Performance: They are often faster than equivalent for-loops because they are optimized for performance.
		Readability: While list comprehensions are concise, they can become difficult to read if overly complex, so it's important to balance readability with conciseness.
		List comprehensions are a powerful feature in Python that can simplify your code and improve performance when generating lists.
		
	1. Basic List Comprehension
	
	numbers = [1, 2, 3, 4, 5]
	squared = [x ** 2 for x in numbers]
	print(squared)  # Output: [1, 4, 9, 16, 25]
	
	2. With a Condition:
	
	numbers = [1, 2, 3, 4, 5]
	even_numbers = [x for x in numbers if x % 2 == 0]
	print(even_numbers)  # Output: [2, 4]
	
	3. Transforming Strings:
	
	words = ["Hello", "World"]
	lowercased = [word.lower() for word in words]
	print(lowercased)  # Output: ['hello', 'world']
	
	
	4. Nested List Comprehension:
	
	matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
	flattened = [num for row in matrix for num in row]
	print(flattened)  # Output: [1, 2, 3, 4, 5, 6, 7, 8, 9]

    # Python List Comprehension + Lambda Examples

    # 1. Basic Example
        nums = [1, 2, 3, 4, 5]
        squares = [x*x for x in nums]
        print("Squares (list comp):", squares)  # [1, 4, 9, 16, 25]

        # Same with lambda + map
        squares_lambda = list(map(lambda x: x*x, nums))
        print("Squares (lambda):", squares_lambda)

    # 2. With Condition (Filtering)
        evens = [x for x in nums if x % 2 == 0]
        print("Evens (list comp):", evens)  # [2, 4]

        # Same with lambda + filter
        evens_lambda = list(filter(lambda x: x % 2 == 0, nums))
        print("Evens (lambda):", evens_lambda)

    # 3. Nested Loops
        pairs = [(x, y) for x in [1, 2] for y in [3, 4]]
        print("Pairs:", pairs)  # [(1, 3), (1, 4), (2, 3), (2, 4)]

    # 4. Applying Functions
        words = ["hello", "python", "world"]
        upper_words = [w.upper() for w in words]
        print("Upper Words (list comp):", upper_words)  # ['HELLO', 'PYTHON', 'WORLD']

        # Same with lambda + map
        upper_words_lambda = list(map(lambda w: w.upper(), words))
        print("Upper Words (lambda):", upper_words_lambda)

    # 5. With if...else
        parity = ["even" if x % 2 == 0 else "odd" for x in nums]
        print("Parity:", parity)  # ['odd', 'even', 'odd', 'even', 'odd']

        # Lambda alternative inside map
        parity_lambda = list(map(lambda x: "even" if x % 2 == 0 else "odd", nums))
        print("Parity (lambda):", parity_lambda)

    # 6. Flattening Lists
        matrix = [[1, 2], [3, 4], [5, 6]]
        flat = [x for row in matrix for x in row]
        print("Flattened:", flat)  # [1, 2, 3, 4, 5, 6]

    # 7. Dictionary Comprehension
        squares_dict = {x: x*x for x in range(1, 4)}
        print("Squares Dict:", squares_dict)  # {1: 1, 2: 4, 3: 9}

        # Same with lambda + dict()
        squares_dict_lambda = dict(map(lambda x: (x, x*x), range(1, 4)))
        print("Squares Dict (lambda):", squares_dict_lambda)

    # 8. Set Comprehension
        unique_letters = {c for c in "hello"}
        print("Unique Letters:", unique_letters)  # {'h', 'e', 'l', 'o'}

        # Same with lambda + set()
        unique_letters_lambda = set(map(lambda c: c, "hello"))
        print("Unique Letters (lambda):", unique_letters_lambda)

lamda functions 
	In Python, a lambda function is a small anonymous function that is defined using the lambda keyword. It can have any number of input parameters but only one expression. The expression is evaluated and returned when the lambda function is called. Lambda functions are often used for short, throwaway functions that are not intended to be reused elsewhere.
	
	syntax :     lambda arguments: expression 
		arguments: A comma-separated list of parameters.
		expression: A single expression that is evaluated and returned.
	
	1. Basic Lambda Function:
		
		add = lambda x, y: x + y
		print(add(2, 3))  # Output: 5
	
	2. Using Lambda with map() 
	
		numbers = [1, 2, 3, 4, 5]
		squared = map(lambda x: x ** 2, numbers)
		print(list(squared))  # Output: [1, 4, 9, 16, 25]
		
	3. Using Lambda with filter()
	
		numbers = [1, 2, 3, 4, 5]
		even_numbers = filter(lambda x: x % 2 == 0, numbers)
		print(list(even_numbers))  # Output: [2, 4]
		
	4. Using Lambda with sorted()
		
		pairs = [(1, 'one'), (2, 'two'), (3, 'three')]
		sorted_pairs = sorted(pairs, key=lambda pair: pair[1])
		print(sorted_pairs)  # Output: [(1, 'one'), (3, 'three'), (2, 'two')]

map function 
	The map() function in Python is a built-in function that applies a specified function to each item in an iterable (such as a list or tuple) and returns a map object (an iterator) with the results. It is a convenient way to transform data by applying a function to each element in a collection.
	
	syntax :  map(function, iterable, ...)
	
		function: The function to apply to each item in the iterable.
		iterable: One or more iterables. If multiple iterables are provided, the function must take that many arguments and is applied to the items from all iterables in parallel.
		
	Key Points
		Returns an Iterator: The map() function returns an iterator, so you often need to convert it to a list or another collection type to see the results.
		Lazy Evaluation: Since it returns an iterator, the function is not applied until you iterate over the map object.
		Multiple Iterables: You can pass multiple iterables to map(), and the function will be applied to corresponding items from each iterable.
	
		
	1. Basic Usage with a Single Iterable: using lambda
	
		numbers = [1, 2, 3, 4, 5]
		squared = map(lambda x: x ** 2, numbers)
		print(list(squared))  # Output: [1, 4, 9, 16, 25]
		
	2. Using a Named Function
	
		def square(x):
			return x ** 2

		numbers = [1, 2, 3, 4, 5]
		squared = map(square, numbers)
		print(list(squared))  # Output: [1, 4, 9, 16, 25]
		
	3. Using Multiple Iterables: 
		
		numbers1 = [1, 2, 3]
		numbers2 = [4, 5, 6]
		summed = map(lambda x, y: x + y, numbers1, numbers2)
		print(list(summed))  # Output: [5, 7, 9]
		
		When multiple iterables are passed, map() applies the function to corresponding items from all iterables. The iteration stops when the shortest iterable is exhausted.
		
	4. How does the map() function differ from a list comprehension? 
		map() applies a function to each element in an iterable, while list comprehensions offer more flexibility, allowing for conditional logic and transformations without explicitly defining a function.
	
	5. How does map() differ from the reduce() function?
		map() applies a function to each item in an iterable, producing a new iterable of results. reduce() applies a function cumulatively to the items, reducing the iterable to a single value.
		
	6. Explain the concept of lazy evaluation in the context of map()?
		map() returns an iterator, meaning the function is not applied until you explicitly iterate over the results. This is known as lazy evaluation.
	
	7. How would you handle exceptions in a function used with map()?
		You can handle exceptions within the function itself using try-except blocks to ensure that errors are managed gracefully.
		
	8. What are the advantages of using map() over a for-loop in terms of performance and readability?
		map() can be more concise and readable, especially for simple transformations. It may also be faster for large datasets due to its internal optimizations.

	9. How does the performance of map() compare to list comprehensions?
		List comprehensions are often faster for simple transformations due to their direct list construction, while map() is more memory efficient as it returns an iterator.
	
	10. Real-world Scenarios : How can map() be used in data preprocessing tasks?
		map() can be used to apply transformations like normalization, scaling, or encoding to datasets in preparation for analysis or machine learning.

	11. Real-world Scenarios : Can you provide an example where using map() would significantly simplify the code?
		words = ["Hello", "World"]
		lowercased = map(str.lower, words)
		print(list(lowercased))  # Output: ['hello', 'world']
		

filter function 	
	The filter() function in Python is a built-in function used to construct an iterator from elements of an iterable for which a function returns True. It is commonly used to filter out elements that do not meet a certain condition.
	
	syntax :   filter(function, iterable)
	
		function: A function that returns a boolean value. It is applied to each element of the iterable.
		iterable: The iterable to be filtered. 
		
	Key Points
		Returns an Iterator: filter() returns an iterator, so you often need to convert it to a list or another collection type to see the results.
		Lazy Evaluation: Like map(), filter() is evaluated lazily, meaning the filtering function is applied only when you iterate over the results.
		Use Cases: Ideal for extracting elements from an iterable that meet specific criteria, such as filtering numbers, strings, or objects based on attributes.
		The filter() function is a powerful tool for selecting elements from a collection based on a condition, providing a clean and efficient way to perform filtering operations.
	
	Examples 
	
	1. Filtering Even Numbers from a List 
	
		numbers = [1, 2, 3, 4, 5, 6]
		even_numbers = filter(lambda x: x % 2 == 0, numbers)
		print(list(even_numbers))  # Output: [2, 4, 6]
		
		above function is same for list comprehension :  [ number for number in numbers if number % 2 == 0 ]
		
	2. Using a Named Function
	
		def is_even(x):
			return x % 2 == 0

		numbers = [1, 2, 3, 4, 5, 6]
		even_numbers = filter(is_even, numbers)
		print(list(even_numbers))  # Output: [2, 4, 6]
		
	3. Filtering Strings Based on Length
	
		words = ["apple", "banana", "kiwi", "pear"]
		long_words = filter(lambda word: len(word) > 4, words)
		print(list(long_words))  # Output: ['apple', 'banana']

sorted function 
	The sorted() function in Python is a built-in function that returns a new sorted list from the elements of any iterable. It does not modify the original iterable but instead creates a new sorted list.
	
	syntax : sorted(iterable, key=None, reverse=False) 
	
		iterable: The collection to be sorted, such as a list, tuple, or string.
		key: A function that serves as a key for the sort comparison. It's optional and allows for custom sorting logic.
		reverse: A boolean value. If set to True, the list elements are sorted as if each comparison were reversed (descending order).
		
	Key Points
		Non-Destructive: sorted() returns a new list and does not modify the original iterable.
		Custom Sorting: The key parameter allows for custom sorting logic, such as sorting by length, specific attributes, or complex expressions.
		Flexibility: Works with any iterable and supports both ascending and descending order sorting.
		The sorted() function is versatile and powerful, providing a simple way to sort data in Python according to various criteria.
		
	1. Sorting a List of Numbers
	
		numbers = [5, 2, 9, 1, 5, 6]
		sorted_numbers = sorted(numbers)
		print(sorted_numbers)  # Output: [1, 2, 5, 5, 6, 9]
	
	2. Sorting a List of Strings
	
		words = ["banana", "apple", "cherry"]
		sorted_words = sorted(words)
		print(sorted_words)  # Output: ['apple', 'banana', 'cherry']
		
	3. Sorting with a Key Function
	
		words = ["banana", "apple", "cherry"]
		sorted_words = sorted(words, key=len)
		print(sorted_words)  # Output: ['apple', 'banana', 'cherry'] (sorted by length)
		
	4. Sorting in Reverse Order
	
		numbers = [5, 2, 9, 1, 5, 6]
		sorted_numbers = sorted(numbers, reverse=True)
		print(sorted_numbers)  # Output: [9, 6, 5, 5, 2, 1]
		
	5. Sorting a List of Tuples
	
		pairs = [(1, 'one'), (3, 'three'), (2, 'two')]
		sorted_pairs = sorted(pairs, key=lambda pair: pair[1])
		print(sorted_pairs)  # Output: [(1, 'one'), (3, 'three'), (2, 'two')] (sorted by second element)

decorators
    # Python Decorator Examples

    from functools import wraps
    import time

    # 1. Basic Decorator
        def my_decorator(func):   # parameter func is important here in main def method
            def wrapper():
                print("Before function runs")
                func()
                print("After function runs")
            return wrapper

        @my_decorator
        def say_hello():
            print("Hello!")

        say_hello()

        Output:
        Before function runs
        Hello!
        After function runs

    # 2. Decorator with Arguments
        def repeat_decorator(func):
            def wrapper(*args, **kwargs):   # *args, **kwargs are important to pass inside the nested method
                print("Repeating twice:")
                func(*args, **kwargs)       # *args, **kwargs are important to pass
                func(*args, **kwargs)
            return wrapper

        @repeat_decorator
        def greet(name):
            print(f"Hello, {name}!")

        greet("Alice")

        Output:
        Repeating twice:
        Hello, Alice!
        Hello, Alice!

    # 3. Chaining Multiple Decorators
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

        print(get_text())  
        
        Output:
        # <b><i>Hello</i></b>
        

    # 4. Using functools.wraps to preserve metadata
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
        

    # 5. Practical Example: Timer Decorator
        >>> import time
        ...
        ... def timer(func):
        ...     def wrapper(*args, **kwargs):
        ...         start = time.time()
        ...         result = func(*args, **kwargs)
        ...         end = time.time()
        ...         print(f"{func.__name__} took {end - start:.5f} seconds")
        ...         return result
        ...     return wrapper
        ...
        >>> @timer
        ... def slow_function():
        ...     time.sleep(1)
        ...
        >>> slow_function()
        slow_function took 1.00552 seconds


Python Multithreading
    # Python Multithreading Examples

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

        Output:
        Thread A starting
        Thread B starting
        Thread A finished
        Thread B finished
        All threads completed

        # --------------------------------------------------

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
        
        Output:
        Thread-1 is running
        Thread-1 finished

        # --------------------------------------------------

        print("\n=== 3. Synchronization with Lock ===")
        lock = threading.Lock()
        counter = 0

        def increment():
            global counter
            for _ in range(100000):
                with lock:  # safe access
                    counter += 1

        t1 = threading.Thread(target=increment)
        t2 = threading.Thread(target=increment)

        t1.start(); t2.start()
        t1.join(); t2.join()

        print("Counter (should be 200000):", counter)

        Output:
        Counter (should be 200000): 200000

        # --------------------------------------------------

        print("\n=== 4. Daemon Thread Example ===")
        def background_task():
            while True:
                print("Background running...")
                time.sleep(1)

        daemon_thread = threading.Thread(target=background_task, daemon=True)
        daemon_thread.start()
        time.sleep(3)  # main runs for 3 sec
        print("Main program ends (daemon dies)\n")

        Output:
        Background running...
        Background running...
        Background running...
        Main program ends (daemon dies)

        # --------------------------------------------------

        print("\n=== 5. ThreadPoolExecutor Example ===")
        def square(n):
            time.sleep(0.5)
            return n*n

        with ThreadPoolExecutor(max_workers=3) as executor:
            results = list(executor.map(square, [1, 2, 3, 4, 5]))
            print("Squares from thread pool:", results)

        Output:
        Squares from thread pool: [1, 4, 9, 16, 25]

Naming conventions Not rigid, but:

		○ Modules and packages all lower case.
		○ Globals and constants Upper case.
		○ Classes Bumpy caps with initial upper.
		○ Methods and functions All lower case with words separated by underscores.
		○ Local variables Lower case (with underscore between words) or bumpy caps with initial lower or your choice.
		○ Good advice Follow the conventions used in the code on which you are working.
		
Special name classes Single and double underscores. 
		
	Single Leading Underscore (_var) : 	
		
		This is a convention to indicate that a variable or method is intended for internal use only. It's a hint to other programmers that it should be treated as "private" and not accessed directly from outside the class. 
	
		class MyClass:
   
			def __init__(self):
				self._internal_value = 42  # Intended as a private attribute
				
	Single Trailing Underscore (var_):
	
		Used to avoid naming conflicts with Python keywords.
		
		class MyClass:
			def __init__(self, class_):
				self.class_ = class_  # Avoids conflict with the keyword 'class'
				
	
	Double Leading Underscore (__var) :
		This triggers name mangling, where the interpreter changes the name of the variable in a way that makes it harder to create subclasses that accidentally override the private attributes and methods.  The name is changed to _ClassName__var to prevent accidental access. 
		
		class MyClass:
			def __init__(self):
				self.__private_value = 42  # Name mangled to _MyClass__private_value
				
	Double Leading and Trailing Underscore (__var__) :
		These are reserved for special use in the language. Such names are often referred to as "dunder" (double underscore) names. They are used for special methods and variables that have special meanings, like __init__, __str__, __repr__, etc.
		
		
		class MyClass:
			def __init__(self):
				pass

			def __str__(self):
				return "MyClass instance"
				
Doc Strings 
	Docstrings in Python are a way to document your code. They are string literals that appear right after the definition of a function, method, class, or module, and they are used to describe what the code does. Here's how they work:
	
	Function Docstring:
		usage : print(add.__doc__) 
		Example : 
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
				
	Class Docstring:
		
		usage : print(MyClass.__doc__)
		
		Example : 
		
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
	
	
	
# Python Operators

	Python provides a variety of operators that allow you to perform operations on variables and values. Here's an overview of the main types of operators in Python, along with examples:

	## 1. Arithmetic Operators

		- **Addition (`+`)**: Adds two operands.  result = 5 + 3  # 8
		- **Subtraction (`-`)**: Subtracts the second operand from the first.  result = 5 - 3  # 2
		- **Multiplication (`*`)**: Multiplies two operands.  result = 5 * 3  # 15
		- **Division (`/`)**: Divides the first operand by the second.  result = 5 / 3  # 1.6666...
		- **Floor Division (`//`)**: Divides and returns the largest integer less than or equal to the result.  result = 5 // 3  # 1
  		- **Modulus (`%`)**: Returns the remainder of the division.   result = 5 % 3  # 2
		- **Exponentiation (`**`)**: Raises the first operand to the power of the second.  result = 5 ** 3  # 125
  
	## 2. Comparison Operators
		- **Equal (`==`)**: Checks if two operands are equal. result = (5 == 3)  # False
		- **Not Equal (`!=`)**: Checks if two operands are not equal.  result = (5 != 3)  # True
		- **Greater Than (`>`)**: Checks if the left operand is greater than the right. result = (5 > 3)  # True
		- **Less Than (`<`)**: Checks if the left operand is less than the right. result = (5 < 3)  # False
		- **Greater Than or Equal To (`>=`)**: Checks if the left operand is greater than or equal to the right.  result = (5 >= 3)  # True
		- **Less Than or Equal To (`<=`)**: Checks if the left operand is less than or equal to the right. result = (5 <= 3)  # False
  
	## 3. Logical Operators
		- **Logical AND (`and`)**: Returns True if both operands are true. result = (5 > 3) and (3 > 1)  # True
		- **Logical OR (`or`)**: Returns True if at least one operand is true. result = (5 > 3) or (3 < 1)  # True
		- **Logical NOT (`not`)**: Reverses the logical state of its operand. result = not (5 > 3)  # False

	## 4. Assignment Operators

		- **Assign (`=`)**: Assigns a value to a variable. x = 5
		- **Add and Assign (`+=`)**: Adds and assigns a value.              x += 3  # x = x + 3
		- **Subtract and Assign (`-=`)**: Subtracts and assigns a value.    x -= 3  # x = x - 3
		- **Multiply and Assign (`*=`)**: Multiplies and assigns a value.   x *= 3  # x = x * 3
		- **Divide and Assign (`/=`)**: Divides and assigns a value.        x /= 3  # x = x / 3 
		- **Modulus and Assign (`%=`)**: Modulus and assigns a value.       x %= 3  # x = x % 3
		- **Exponent and Assign (`**=`)**: Exponentiates and assigns a value. x **= 3  # x = x ** 3
		- **Floor Divide and Assign (`//=`)**: Floor divides and assigns a value. x //= 3  # x = x // 3
	
	## 5. Bitwise Operators

		- **AND (`&`)**: Performs a bitwise AND operation.  result = 5 & 3  # 1
		- **OR (`|`)**: Performs a bitwise OR operation.    result = 5 | 3  # 7
		- **XOR (`^`)**: Performs a bitwise XOR operation.  result = 5 ^ 3  # 6
		- **NOT (`~`)**: Performs a bitwise NOT operation.  result = ~5  # -6
		- **Left Shift (`<<`)**: Shifts bits to the left.   result = 5 << 1  # 10
		- **Right Shift (`>>`)**: Shifts bits to the right. result = 5 >> 1  # 2
		
	## 6. Difference between != and <> 
		Python 3 Compatibility: The <> operator is not supported in Python 3. You must use != for checking inequality in Python 3.
		Recommendation: Always use != for inequality checks to ensure compatibility with Python 3 and beyond.

		If you're working with Python 3, you should use != for all inequality comparisons. If you encounter <>, it is likely in older Python 2 code, which should be updated to use != for modern compatibility.
	
	## 7. is , not is  operator 
	
		The is operator in Python is used to compare the identity of two objects. It checks whether two variables point to the same object in memory, rather than comparing the values of the objects themselves.
		
		The is operator is particularly useful when you need to ensure that two variables reference the exact same object, such as when checking for singleton instances or for None.
		
		While both is and == will work for checking if a variable is None, using is is the preferred and more idiomatic approach in Python. It ensures that you're checking for the identity of None, which aligns with Python's design philosophy and best practices. The is operator can be slightly faster than == because it doesn't involve a method call to check equality. 
		
		
		7.1 Comparing Identical Objects:
			a = [1, 2, 3]
			b = a
			print(a is b)  # True, because b is a reference to the same list object as a
			
		7.2 Comparing Different Objects with the Same Value:
			a = [1, 2, 3]
			b = [1, 2, 3]
			print(a is b)  # False, because a and b are different objects, even though they have the same content
			
		7.3 Using is with Immutable Types
			a = 1000
			b = 1000
			print(a is b)  # False, because integers with value > 256 are not cached and are different objects
			
		7.4 Using is with None
			a = None
			print(a is None)  # True, commonly used to check if a variable is None
  
	## 8. in . not in  operator
		
		The in operator in Python is used to check for membership within an iterable, such as a list, tuple, string, set, or dictionary. It returns True if the specified element is found in the iterable, and False otherwise.
		
		Syntax: element in iterable
		
		8.1 Checking in a List 
		
			fruits = ["apple", "banana", "cherry"]
			print("banana" in fruits)  # True
			print("orange" in fruits)  # False
		
		8.2 Checking in a String: 
		
			text = "Hello, World!"
			print("Hello" in text)  # True
			print("Python" in text)  # False
			
			For checking if a substring exists within a string, it's recommended to use the in operator due to its simplicity and readability. The __contains__ method is more relevant when implementing custom classes that need specific behavior for membership tests.
			
		
		8.3 Checking in a Tuple 
		
			numbers = (1, 2, 3, 4, 5)
			print(3 in numbers)  # True
			print(6 in numbers)  # False
		
		8.4 Checking in a Set
		
			unique_numbers = {1, 2, 3, 4, 5}
			print(4 in unique_numbers)  # True
			print(10 in unique_numbers)  # False
		
		8.5 Checking Keys in a Dictionary , The in operator is used to check if a key exists in a dictionary directly.
		
			person = {"name": "Alice", "age": 30}
			print("name" in person)  # True
			print("address" in person)  # False
			
			
			checking key exists or not can be done with alternative way as below 
			
			person = {"name": "Alice", "age": 30}
			print("name" in person.keys())  # True
			print("address" in person.keys())  # False
			
			Using keys() with in is slightly less efficient than using in directly, as it involves creating a view object. However, the difference is usually negligible for most practical purposes. For checking if a key exists in a dictionary, it's recommended to use the in operator directly for simplicity and efficiency. The keys() method is more appropriate when you need to work with the keys as a separate collection.
  
	These operators allow you to perform a wide range of operations on data in Python.

How to use the timeit for measuring the time taken for function execution 
	
	import timeit

	# Original code using a for-loop
	def filter_even_numbers_loop():
		numbers = range(0,10000000)
		even_numbers = []
		for number in numbers:
			if number % 2 == 0:
				even_numbers.append(number)
		return even_numbers

	# Refactored code using list comprehension
	def filter_even_numbers_comprehension():
		numbers = range(0,10000000)
		even_numbers = [number for number in numbers if number % 2 == 0]
		return even_numbers


	# Timing the execution of both functions
	loop_time = timeit.timeit(filter_even_numbers_loop, number=10)
	comprehension_time = timeit.timeit(filter_even_numbers_comprehension, number=10)

	print(f"For-loop time: {loop_time}")
	print(f"List comprehension time: {comprehension_time}")

How to use the cProfile for measuring the performance 

	import cProfile

	# Original code using a for-loop
	def filter_even_numbers_loop():
		numbers = range(0,10000000)
		even_numbers = []
		for number in numbers:
			if number % 2 == 0:
				even_numbers.append(number)
		return even_numbers

	# Refactored code using list comprehension
	def filter_even_numbers_comprehension():
		numbers = range(0,10000000)
		even_numbers = [number for number in numbers if number % 2 == 0]
		return even_numbers

	# Profile the execution of both functions using cProfile
	def profile_functions():
		print("Profiling filter_even_numbers_loop:")
		cProfile.run('filter_even_numbers_loop()')
		print("\nProfiling filter_even_numbers_comprehension:")
		cProfile.run('filter_even_numbers_comprehension()')

	profile_functions()
	
In python what are the different kinds of iterables ? - In Python, an iterable is any object that can return its elements one at a time, allowing it to be looped over in a for loop. Here are the different types of iterables commonly used in Python 

	Basic data types like Strings , List , Set , Tuple , Dictionary 
	Some of the objects like file , range , zip , collections , enumerates etc 
	other complex like Generators 

range vs xrange functions 
	In Python 2, there were two functions for generating sequences of numbers: range() and xrange(). However, in Python 3, xrange() has been removed, and range() has been optimized to behave like xrange()
	
Tuples and lists 
	List -- 
		A list is a dynamic array/sequence. It is ordered and indexable. A list is mutable.
		
		List constructors: [], list().
		
		Use remove(value) to remove by value and pop(index) to remove by index. Example: fruits.remove("banana"), fruits.pop(2).
		
		Slicing extracts a portion of a list using the syntax list[start:stop:step]. To reverse a list, use list[::-1].
		
		Use sort(reverse=True) or sorted(list, reverse=True). Example: fruits.sort(reverse=True).
		
		A list comprehension is a concise way to create lists using a single line of code with a for-loop and optional conditions. It is more readable and often faster than a traditional for-loop. 
			List comprehensions are generally faster due to their optimized implementation in C and reduced overhead compared to for-loops. 
			Use list comprehensions, generators for lazy evaluation, and built-in functions like map() and filter() for efficiency.
			
				numbers = [1, 2, 3, 4, 5]
				even_squares = [x ** 2 for x in numbers if x % 2 == 0]
				
		How can you remove duplicates from a list while preserving the order of elements?
			Use a set to track seen elements and a list comprehension to filter. Example 
			
			seen = set()
			unique = [x for x in fruits if not (x in seen or seen.add(x))]
			
		How do you merge two lists into a list of tuples, pairing elements with the same index?
			list1 = [1, 2, 3]
			list2 = ['a', 'b', 'c']
			merged = list(zip(list1, list2))
			
		How would you use a list to implement a stack or a queue in Python?
			Use append() and pop() for a stack (LIFO). pop() removes the last elements in the list 
			append() and pop(0) for a queue (FIFO).
		
		What happens if you try to access an index that is out of range in a list ? 
			An IndexError is raised.
			
		How can you avoid common mistakes when modifying a list while iterating over it?
			Iterate over a copy of the list or use list comprehensions to avoid modifying the list during iteration.
			
		Confusing append() with extend()
			Mistake: Using append() to add multiple elements results in a nested list.
			Solution: Use extend() to add elements from an iterable.
			
			
				# Mistake
				fruits = ["apple", "banana"]
				fruits.append(["cherry", "date"])  # Results in ['apple', 'banana', ['cherry', 'date']]

				# Solution
				fruits = ["apple", "banana"]
				fruits.extend(["cherry", "date"])  # Results in ['apple', 'banana', 'cherry', 'date']
				
				
		Using Mutable Default Arguments:
			Mistake: Using a mutable object like a list as a default argument can lead to unexpected behavior.
			Solution: Use None as the default value and initialize inside the function.
			
				# Mistake
				def add_item(item, items=[]):
					items.append(item)
					return items

				# Solution
				def add_item(item, items=None):
					if items is None:
						items = []
					items.append(item)
					return items
			
		Assuming List Copying with =
			Mistake: Using = to copy a list creates a reference, not a new list.
			Solution: Use list.copy(), slicing, or list() to create a copy.
				# Mistake
				original = [1, 2, 3]
				copy = original
				copy.append(4)  # Affects both 'original' and 'copy'

				# Solution
				original = [1, 2, 3]
				copy = original.copy()
				copy.append(4)  # Only affects 'copy'
				
		In Python, copying an object can be done in two main ways: shallow copy and deep copy. Understanding the difference between these two types of copies is crucial when working with mutable objects like lists, especially when they contain nested structures.
			Shallow Copy
				Definition: A shallow copy creates a new object, but it only copies references to the objects contained within the original object. This means that the outer structure is copied, but the inner objects are shared between the original and the copy.
				
				How to Create: You can create a shallow copy using the copy() method, copy.copy(), or slicing for lists
				
						import copy
						original_list = [[1, 2, 3], [4, 5, 6]]
						shallow_copied_list = copy.copy(original_list)
						
					Behavior: Modifying the outer structure (e.g., adding or removing elements) in the shallow copy does not affect the original. However, modifying the inner objects (e.g., changing an element in a nested list) will affect both the original and the shallow copy because they share the same references.
					
							original_list = [[1, 2, 3], [4, 5, 6]]
							shallow_copied_list = original_list[:]

							shallow_copied_list[0][0] = 99
							print(original_list)  # Output: [[99, 2, 3], [4, 5, 6]]
					
			Deep Copy
				Definition: A deep copy creates a new object and recursively copies all objects found within the original object. This results in a completely independent copy, with no shared references between the original and the copy.
				
				How to Create: You can create a deep copy using the copy.deepcopy() function
				
					import copy
					original_list = [[1, 2, 3], [4, 5, 6]]
					deep_copied_list = copy.deepcopy(original_list)
					
					Behavior: Modifying either the outer structure or the inner objects in the deep copy does not affect the original. Each element is a completely new object.
					
						original_list = [[1, 2, 3], [4, 5, 6]]
						deep_copied_list = copy.deepcopy(original_list)

						deep_copied_list[0][0] = 99
						print(original_list)  # Output: [[1, 2, 3], [4, 5, 6]]
						
			Key Differences
				Shared References: Shallow copies share references to the inner objects, while deep copies do not.
				Independence: Deep copies are fully independent of the original, whereas shallow copies are only partially independent.
				Performance: Deep copies are more memory-intensive and slower because they duplicate all nested objects.
			When to Use
				Shallow Copy: Use when you only need to copy the outer structure, and the inner objects will not be modified.
				Deep Copy: Use when you need a completely independent copy of the entire object, including all nested objects.
		
		Use deque for Queue Operations
			If you need to perform frequent insertions or deletions at both ends of a list, consider using collections.deque, which is optimized for such operations.
			
			
				from collections import deque
				queue = deque([1, 2, 3])
				queue.appendleft(0)
				queue.pop()
				
	Tuples --
		A tuple is a sequence. A tuple is immutable.
		Tuple constructors: (), but really a comma; also tuple().
		To construct a tuple with a single element, use (x,); a tuple with a single element requires a comma.

strings 
	In Python, strings are a fundamental data type used to represent text. They are sequences of characters and are immutable, meaning once a string is created, it cannot be modified. Here's an overview of Python strings and some common operations you can perform on them:
	
	Creating Strings
		Single Quotes: 'Hello' . Useful when the string contains double quotes. Example: 'He said, "Hello, World!"'
		Double Quotes: "World" . Useful when the string contains single quotes. Example: "It's a beautiful day!"
		Triple Quotes: '''This is a multi-line string''' or """This is a multi-line string"""
		
		While both single and double quotes can be used interchangeably in Python, the choice often comes down to readability, consistency, and personal or team preferences. In practice, many developers choose one style for general use and switch to the other when the string content makes it more convenient. The most important aspect is to maintain consistency within your codebase.
		
		How can you check if a substring exists within a string ? Use the in operator. Example: 'Hello' in 'Hello, World' returns True.
		How can you use regular expressions to search for patterns in strings ? Use the re module functions like re.search(), re.match(), and re.findall() ?
		Use regular expressions with a pattern that matches email formats. Example: re.findall(r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b', text).
		
		codecs Module
			Purpose: Provides a way to encode and decode data, especially for handling different text encodings.
			Features: Functions for reading and writing files with specific encodings.
				
				import codecs
				with codecs.open('file.txt', 'r', encoding='utf-8') as f:
				text = f.read()
dictionary
	A dictionary in Python is a built-in data structure that stores data in key-value pairs. It is one of the most versatile and widely used data structures in Python, allowing for efficient 	data retrieval and manipulation.

	
	In Python, items() and iteritems() are methods used to iterate over the key-value pairs of a dictionary. However, there is an important distinction between them, particularly in the context of Python 2 and Python 3:
		Python Version: iteritems() is specific to Python 2 and does not exist in Python 3. In Python 3, items() provides similar functionality with better memory efficiency.
		
		Python 2: items() returns a list, while iteritems() returns an iterator.
		Python 3: items() returns a view object, which is iterable and memory-efficient.
		
		my_dict = {'a': 1, 'b': 2}
		for name,  value in my_dict.items():
			print(name, value)
			
			
	can be referred as below intresting 	
		values = {'vegetable': 'chard', 'fruit': 'nectarine'}
		output = 'I love %(vegetable)s and I love %(fruit)s.' % values
		print(output)
		
	Removing Key-Value Pairs: Use del or pop()
		del person["city"]
		email = person.pop("email")
		
	keys(): Returns a view object of all keys :  	 								keys = person.keys()
	values(): Returns a view object of all values : 								values = person.values()
	items(): Returns a view object of key-value pairs.								items = person.items()
	get(): Safely access a value, with an optional default 							age = person.get("age", "Unknown")   or age = person["age"]
	update(): Update the dictionary with another dictionary or key-value pairs		person.update({"name": "Bob", "age": 35})
	
	get(key[, default]):
			Returns the value for the specified key if the key is in the dictionary; otherwise, it returns the default value. if default is not there then None will be return 
			Example: value = my_dict.get('key', 'default_value')
			
	update([other]):
			Updates the dictionary with elements from another dictionary object or from an iterable of key-value pairs.
			Example: my_dict.update({'new_key': 'new_value'})
	
	setdefault(key[, default]):
			Returns the value of the specified key. If the key does not exist, inserts the key with the specified default value.
			Example: value = my_dict.setdefault('key', 'default_value')	
	
	pop(key[, default]):
			Removes the specified key and returns the corresponding value. If the key is not found, the default value is returned if provided; otherwise, a KeyError is raised.
			Example: value = my_dict.pop('key', 'default_value')
			
	popitem():
			Removes and returns a (key, value) pair from the dictionary. Pairs are returned in LIFO (last-in, first-out) order.
			Example: key, value = my_dict.popitem()
		
	dict.fromkeys(seq[, value]):
			Creates a new dictionary with keys from seq and values set to value.
			Example: new_dict = dict.fromkeys(['a', 'b', 'c'], 0)
			
	len(dict):
			Returns the number of items (key-value pairs) in the dictionary.
			Example: length = len(my_dict)
	in Operator:
			Checks if a key exists in the dictionary.
			Example: if 'key' in my_dict:
	
	copy(): shallow copy ( not recommended modifying a mutable object (like a list) in the shallow copy affects the original dictionary because both dictionaries reference the same list object.) 
			Returns a shallow copy of the dictionary.
			Example: new_dict = my_dict.copy()
			
	shallow copy vs deep copy 
	
		In Python, when working with dictionaries (or other complex data structures), it's important to understand the difference between shallow copies and deep copies. This distinction affects how nested objects within the dictionary are handled during the copying process.
		
		Shallow Copy
			Definition: A shallow copy of a dictionary creates a new dictionary object, but it does not create copies of the objects that the dictionary references. Instead, it copies references to those objects.
			
			Method: You can create a shallow copy using the copy() method or the dict() constructor.
			
			Example : [ modifying a mutable object (like a list) in the shallow copy affects the original dictionary because both dictionaries reference the same list object. ] 
					import copy

					original = {'numbers': [1, 2, 3], 'letters': ['a', 'b', 'c']}
					shallow_copy = original.copy()

					# Modifying the shallow copy
					shallow_copy['numbers'].append(4)

					print(original['numbers'])  # Output: [1, 2, 3, 4]
	

		Deep Copy
			Definition: A deep copy creates a new dictionary and recursively copies all objects found within the original dictionary. This means that all nested objects are also copied, not just their references.
			
			Method: You can create a deep copy using the deepcopy() function from the copy module.
			
			Example :
					import copy

					original = {'numbers': [1, 2, 3], 'letters': ['a', 'b', 'c']}
					deep_copy = copy.deepcopy(original)

					# Modifying the deep copy
					deep_copy['numbers'].append(4)

					print(original['numbers'])  # Output: [1, 2, 3]
					
		When to Use
			Shallow Copy: Use when you need a new dictionary object but are okay with shared references to the nested objects. It's faster and uses less memory.
			Deep Copy: Use when you need complete independence from the original dictionary, including all nested objects. It's safer for complex data structures but more resource-intensive.
			
			
	common mistake : Modifying a Dictionary While Iterating:
		Mistake: Changing the size of a dictionary (adding or removing keys) while iterating over it, which can lead to runtime errors.
		Solution: Iterate over a copy of the dictionary's keys or use dictionary comprehensions.
		
				# Mistake
				# for key in my_dict:
				#     del my_dict[key]  # Raises RuntimeError

				# Solution
				for key in list(my_dict.keys()):
					del my_dict[key]
					
	common mistake : copy() vs deepcopy() , Confusing copy() with deepcopy():
		Mistake: Assuming copy() creates a deep copy of a dictionary, leading to shared references in nested structures.
		Solution: Use copy.deepcopy() for deep copies when needed.
				
				import copy
				original = {'numbers': [1, 2, 3]}
				shallow_copy = original.copy()
				deep_copy = copy.deepcopy(original)
	
	common mistake : Misusing update() Method
		Mistake: Assuming update() only adds new keys, but it also overwrites existing keys.
		Solution: Be aware that update() modifies existing keys.
		
			my_dict.update({'key': 'new_value'})  # Updates existing 'key'

	Use immutable and hashable types like strings and integers as keys. Avoid using complex or custom objects unless necessary, as they can slow down hash calculations.
	
	Preallocate Memory with dict.fromkeys()
		Tip: When initializing a dictionary with known keys, use dict.fromkeys() to preallocate memory efficiently.
			keys = ['a', 'b', 'c']
			my_dict = dict.fromkeys(keys, 0)
			
	Avoid Repeated Key Lookups
		Minimize repeated accesses to the same key by storing the value in a variable if it's needed multiple times.
		# avoid below type accessing the same key multiple times 
		if my_dict['key'] > 10 :
			print(my_dict['key']) 
		
		# Instead of repeatedly accessing the key
		value = my_dict['key']
		if value > 10:
			print(value)
			
	** Use Generators for Large Data 
		 When processing large datasets, use generators to avoid loading all data into memory at once.
		 
		 def data_generator():
			for item in large_data_source:
				yield item
				
				
	** good example 
			dict_comp = {x:chr(65+x) for x in range(1, 11)}	
			
	** iterkeys(), itervalues(), and iteritems() 
		n Python 3, the iterkeys(), itervalues(), and iteritems() methods were removed, and the keys(), values(), and items() methods were updated to return view objects instead of lists. These view objects are more memory-efficient and behave similarly to the iterators in Python 2.
		
		Modern Python (Python 3) Approach
			dict.keys(): Now returns a view object that is memory-efficient and reflects changes to the dictionary.
			dict.values(): Returns a view object over the dictionary's values.
			dict.items(): Returns a view object over the dictionary's key-value pairs.
	
	** has_key() vs in operator 
		With the transition from Python 2 to Python 3, the in operator became the standard approach for checking key existence in dictionaries. It is preferred for its simplicity and consistency across different Python data structures. If you are maintaining or updating older Python code, replacing has_key() with the in operator is a necessary step for compatibility with Python 3.
				
yeild functionality 
	In Python, yield is a keyword used in functions to turn them into generators. Generators are a type of iterable, like lists or tuples, but unlike lists, they do not store their contents in memory. Instead, they generate items on-the-fly and are thus more memory-efficient, especially for large datasets
	
	When a function contains the yield keyword, it becomes a generator function. Instead of returning a single value and terminating, a generator function can yield multiple values, one at a time, pausing after each yield statement and resuming from there the next time it's called.
	
	Key Characteristics of Generators
		Lazy Evaluation: Generators produce items only as needed, which can save memory and improve performance.
		State Retention: Generators maintain their state between successive calls, allowing complex iteration logic without global variables.
		Single Iteration: Generators can be iterated over only once. To iterate again, you need to recreate the generator.
		
	
			def count_up_to(max):
				count = 1
				while count <= max:
					yield count
					count += 1

			# Using the generator
			counter = count_up_to(5)
			for number in counter:
				print(number)
				
		In this example, count_up_to is a generator function that yields numbers from 1 to max. Each call to yield produces the next number in the sequence and pauses the function's execution.
		
	Infinite Generator
	
		def infinite_counter():
			count = 0
			while True:
				yield count
				count += 1

		# Using the infinite generator
		counter = infinite_counter()
		for _ in range(5):
			print(next(counter))
			
	This generator function produces an infinite sequence of numbers. The next() function is used to manually iterate over the generator, allowing control over when to stop.
	
Generator Expression
	Generator expressions in Python provide a concise way to create generators. They are similar to list comprehensions, but instead of creating a list and storing all elements in memory, they generate items one at a time and are more memory-efficient. This makes them particularly useful for large datasets or when you need to iterate over data without storing it all at once.

	syntax : (expression for item in iterable if condition)
	
	Generators can also be created using generator expressions, which are similar to list comprehensions but use parentheses instead of square brackets.
		
		squares = (x * x for x in range(5))
		for square in squares:
			print(square)
			

	Comparison with List Comprehensions
		List Comprehensions: Create a list in memory with all elements.  Use when you need to create a list and access its elements multiple times. They are ideal for smaller datasets where memory usage is not a concern.
				squares_list = [x * x for x in range(5)] 
				
		Generator Expressions: Generate items one by one, without storing them all at once. Use for large datasets or when you only need to iterate once. They provide memory efficiency through lazy evaluation.
				squares_gen = (x * x for x in range(5))
		
a file is a context manager: it obeys the context manager protocol. A file has methods __enter__ and __exit__, and the
	__exit__ method automatically closes the file for us. See the section on the
	
	with: statement.


String Formatting: Use f-strings for more readable and efficient string formatting.

In Python, * and ** are used for unpacking and argument passing, but they serve different purposes and are used in different contexts. Here's a breakdown of their uses:
	* Used to unpack a list or tuple into positional arguments when calling a function.
	** Used to unpack key-value pairs from a dictionary into keyword arguments in function calls.
	

Searching Algorithm 
	Linear Search 
	
		Description: A simple search algorithm that checks each element in the list sequentially until the desired element is found or the list ends.
		Complexity: O(n), where n is the number of elements in the list.
		Use Case: Suitable for unsorted lists or when the list is small.

			def linear_search(lst, target):
				for index, element in enumerate(lst):
					if element == target:
						return index
				return -1

			# Example usage:
			numbers = [4, 2, 3, 1, 5]
			index = linear_search(numbers, 3)
			print(index)  # Output: 2
			
	Binary Search
		Description: A more efficient search algorithm that works on sorted lists by repeatedly dividing the search interval in half.
		Complexity: O(log n), where n is the number of elements in the list.
		Use Case: Suitable for large, sorted lists.
		
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

		# Example usage:
		sorted_numbers = [1, 2, 3, 4, 5]
		index = binary_search(sorted_numbers, 3)
		print(index)  # Output: 2
		
python explain positional and keyword argumnets 
	positional arguments can be passed using * 
	keyword arguments can be passed using **
	
			import sys
			import time

			def print_debug_info(target_function):
				def wrapper_function(*args, **kwargs):
					print(f"positiona arguments are {args}")
					print(f"keyword arguments are {kwargs}")
					return target_function(*args, **kwargs)
				return wrapper_function

			@print_debug_info <===== Decorator 
			def perform_taks(msg):
				print(f"Performing taks for {msg}")


			perform_taks("Ravikanth")
	
Different type of methods 
	1. instance methods 
	2. class methods , can be defined using @classmethod decorator The @classmethod decorator defines a method that receives the class as the first argument.
	
		class MyClass:
			count = 0

			@classmethod
			def increment_count(cls):
				cls.count += 1

		# Example usage
		MyClass.increment_count()
		MyClass.increment_count()
		print(MyClass.count)  # Output: 2


	3. static methods  @staticmethod decorator 
		
				class MathOperations:
					@staticmethod
					def add(x, y):
						return x + y

				# Example usage
				result = MathOperations.add(5, 3)
				print(result)  # Output: 8
	
Magic methods are the ones prefixed and suffixed with __ for example __init__ or __repr__ is magic method or dandor method 
	
		class Archer:
			def __init__(self , name , age , arrows):
				self.name = name 
				self.age = age 
				self.arrows = arrows
				
			def shoot(self):
				if self.arrows > 0 :
					self.arrows -= 1 
					print(f"Archar {self.name} shooted , arrows left { self.arrows } .. !")
				else :
					print(f"Archar {self.name} no allows left ")
			
			def __repr__(self):
				return "Archer { self.name } with age { self.age } with arrows { self.arrows }"
				
		
The @property decorator is used to define getter methods in classes, allowing you to access methods like attributes. Below is the example 
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

		# Example usage
		c = Circle(5)
		print(c.radius)  # Output: 5
		c.radius = 10
		print(c.radius)  # Output: 10

The @dataclass decorator simplifies the creation of classes that primarily store data.

recurring functions 

	from functools import lru_cache

	@lru_cache(maxsize=32)
	def fibonacci(n):
		if n < 2:
			return n
		return fibonacci(n-1) + fibonacci(n-2)

	# Example usage
	print(fibonacci(10))  # Output: 55
	 
Frameworks 
    numpy → Core library for numerical computing, arrays, and linear algebra.
    pandas → Data analysis and manipulation with DataFrames.
    matplotlib → Basic plotting and visualization library.
    seaborn → Statistical data visualization (built on matplotlib).
    plotly → Interactive and web-based visualizations.
    scikit-learn → Machine learning library for regression, classification, clustering.
    tensorflow → Deep learning framework from Google.
    pytorch → Deep learning framework widely used in research/production.
    xgboost → Gradient boosting for structured/tabular data.
    flask → Lightweight web framework for APIs and apps.
    django → Full-featured web framework with ORM, templates, admin.
    fastapi → High-performance framework for building APIs.
    requests → Simplifies HTTP requests (GET, POST, PUT, DELETE).
    shutil → High-level file operations (copy, move, delete).
    requests → HTTP library for making requests.
    paramiko → SSH2 protocol library for Python.
    pytest → Powerful and popular testing framework.
    unittest → Built-in testing framework (similar to JUnit).
    logging → Standard library for logging messages.
    json → Standard library for JSON parsing and serialization.
    itertools → Iterators, combinations, permutations utilities.
    argparse → Build command-line interfaces (CLI).

pytests

        import pytest
        import json

        # ----------------------------
        # 1. Basic Test
        # ----------------------------
        def add(a, b):
            return a + b

        def test_add_basic():
            assert add(2, 3) == 5


        # ----------------------------
        # 2. Grouping Multiple Tests
        # ----------------------------
        def multiply(a, b):
            return a * b

        def test_multiply_positive():
            assert multiply(2, 3) == 6

        def test_multiply_zero():
            assert multiply(5, 0) == 0


        # ----------------------------
        # 3. Fixtures
        # ----------------------------
        @pytest.fixture
        def numbers():
            return [1, 2, 3]

        def test_sum(numbers):
            assert sum(numbers) == 6

        def test_max(numbers):
            assert max(numbers) == 3


        # ----------------------------
        # 4. Parametrization
        # ----------------------------
        @pytest.mark.parametrize("a,b,expected", [
            (2, 3, 5),
            (10, -5, 5),
            (0, 0, 0)
        ])
        def test_add_param(a, b, expected):
            assert add(a, b) == expected


        # ----------------------------
        # 5. Exception Testing
        # ----------------------------
        def divide(a, b):
            return a / b

        def test_zero_division():
            with pytest.raises(ZeroDivisionError):
                divide(5, 0)


        # ----------------------------
        # 6. Skipping & XFail
        # ----------------------------
        @pytest.mark.skip(reason="Not implemented yet")
        def test_todo():
            assert False

        @pytest.mark.xfail(reason="Known bug")
        def test_bug():
            assert 1 / 0 == 1


        # ----------------------------
        # 7. Fixture with Scope
        # ----------------------------
        @pytest.fixture(scope="module")
        def db_connection():
            print("\n[SETUP] DB connection")
            yield "db_conn"
            print("\n[TEARDOWN] Close DB connection")

        def test_read(db_connection):
            assert db_connection == "db_conn"

        def test_write(db_connection):
            assert db_connection.startswith("db")


        # ----------------------------
        # 8. Markers
        # ----------------------------
        @pytest.mark.slow
        def test_heavy():
            assert sum(range(100000)) > 0

        @pytest.mark.fast
        def test_light():
            assert 2 + 2 == 4


        # ----------------------------
        # 9. Fixture Using Temp Path
        # ----------------------------
        @pytest.fixture
        def config(tmp_path):
            cfg = tmp_path / "config.json"
            cfg.write_text('{"debug":true}')
            return json.loads(cfg.read_text())

        def test_config(config):
            assert config["debug"] is True


        # ----------------------------
        # 10. Parametrize with Fixture
        # ----------------------------
        @pytest.fixture(params=[1, 2, 3])
        def num(request):
            return request.param

        def test_double(num):
            assert num * 2 in [2, 4, 6]


        # ----------------------------
        # 11. Mocking Example
        # ----------------------------
        def get_user():
            raise Exception("DB not connected")

        def test_mock(mocker):
            mocker.patch("__main__.get_user", return_value="Alice")
            assert get_user() == "Alice"


        # ----------------------------
        # 12. E2E Example: Shopping Cart
        # ----------------------------
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

        -------------
        Confest.py

        import pytest

        # 1. Shared fixture
        @pytest.fixture
        def user():
            return {"name": "Alice", "role": "admin"}

        # 2. Scoped fixture (session scope)
        @pytest.fixture(scope="session")
        def db_connection():
            print("\n[SETUP] Start DB session")
            yield "db_conn"
            print("\n[TEARDOWN] Close DB session")

        # 3. Custom CLI option
        def pytest_addoption(parser):
            parser.addoption("--env", action="store", default="dev")

        @pytest.fixture
        def env(request):
            return request.config.getoption("--env")

        # 4. Mock fixture
        @pytest.fixture
        def mock_user(mocker):
            mocker.patch("app.get_user", return_value="Bob")
            return "Bob"


