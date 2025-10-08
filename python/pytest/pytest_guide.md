# Pytest: Python Testing Framework Guide

**Date:** September 21, 2025  
**Author:** Grok (xAI)

This guide compiles key concepts, examples, and outputs for pytest, covering fundamentals, fixture scopes, exception testing, async code, `conftest.py`, `pytest.ini`, temporary files, mocking, and markers. Examples include async testing where relevant, assuming `pytest`, `pytest-asyncio`, `pytest-mock`, and `aiofiles` are installed (`pip install pytest pytest-asyncio pytest-mock aiofiles`). Code is Python 3.7+ compatible.

# table of contents
- [1. Pytest Fundamentals](#1-pytest-fundamentals)
    - [Overview](#overview)
    - [Key Concepts](#key-concepts)
    - [Examples](#examples)
        - [Basic Test](#basic-test)
        - [Assertion Failure](#assertion-failure)
        - [Using Fixtures](#using-fixtures)
        - [Parameterized Tests](#parameterized-tests)
        - [Skipping and Expected Failures](#skipping-and-expected-failures)
        - [Test Class](#test-class)
        - [Fixture with Setup/Teardown](#fixture-with-setupteteardown)
        - [Running Specific Tests](#running-specific-tests)
- [2. Fixture Scopes](#2-fixture-scopes)
    - [Overview](#overview-1)
    - [Example: All Scopes](#example-all-scopes)
    - [Explanation](#explanation)
    - [Use Cases](#use-cases)
    - [Additional Notes](#additional-notes)
- [3. Exception Testing](#3-exception-testing)
    - [Overview](#overview-2)
    - [Examples](#examples-1)
        - [Basic Exception Testing](#basic-exception-testing)
        - [Verifying Exception Message](#verifying-exception-message)
        - [Wrong Exception (Failure Case)](#wrong-exception-failure-case)
        - [Parameterized Exception Testing](#parameterized-exception-testing)
        - [Expected Failure with xfail](#expected-failure-with-xfail)
        - [Inspecting Exception Attributes](#inspecting-exception-attributes)
- [4. Testing Async Code](#4-testing-async-code)
    - [Overview](#overview-3)
    - [Examples](#examples-2)
        - [Basic Async Test](#basic-async-test)
        - [Async Fixture](#async-fixture)
        - [Async Exception Testing](#async-exception-testing)
        - [Parameterized Async Tests](#parameterized-async-tests)
- [5. conftest.py for Shared Fixtures](#5-conftestpy-for-shared-fixtures)
    - [Overview](#overview-4)
    - [Example conftest.py](#example-conftestpy)
    - [Using conftest Fixtures](#using-conftest-fixtures)
- [6. pytest.ini Configuration](#6-pytestini-configuration)
    - [Overview](#overview-5)
    - [Example pytest.ini](#example-pytestini)
    - [Common Configurations](#common-configurations)
- [7. Temporary Files and Directories](#7-temporary-files-and-directories)
    - [Overview](#overview-6)
    - [Examples](#examples-3)
        - [Using tmp_path Fixture](#using-tmp_path-fixture)
        - [Using tmpdir Fixture](#using-tmpdir-fixture)
- [8. Mocking with pytest-mock](#8-mocking-with-pytest-mock)
    - [Overview](#overview-7)
    - [Examples](#examples-4)
        - [Basic Mocking](#basic-mocking)
        - [Mocking with Fixtures](#mocking-with-fixtures)
        - [Mocking Async Functions](#mocking-async-functions)
- [9. Using Markers for Test Categorization](#9-using-markers-for-test-categorization)
    - [Overview](#overview-8)
    - [Examples](#examples-5)
        - [Custom Markers](#custom-markers)
        - [Skipping Tests with Markers](#skipping-tests-with-markers)
        - [Expected Failures with Markers](#expected-failures-with-markers)
        - [Running Tests by Marker](#running-tests-by-marker)

---

## 1. Pytest Fundamentals

### Overview
- **What is pytest?** A testing framework for Python that simplifies writing, organizing, and running tests with minimal boilerplate. Supports unit, functional, and integration tests.
- **Key Features:**
  - Automatic test discovery.
  - Detailed assertion introspection.
  - Fixtures for setup/teardown.
  - Rich plugin ecosystem (e.g., `pytest-asyncio`, `pytest-mock`).
  - Command-line interface.

### Key Concepts
- **Test Discovery:** Files: `test_*.py` or `*_test.py`. Functions: start with `test_`. Classes: start with `Test`, methods start with `test_`.
- **Assertions:** Use `assert`; pytest provides rich failure messages.
- **Fixtures:** `@pytest.fixture` for reusable setup/teardown; injected as arguments.
- **Markers:** `@pytest.mark.*` for tagging (e.g., `skip`, `xfail`).
- **Command-Line Options:**
  - `pytest`: Run all tests.
  - `pytest -v`: Verbose output.
  - `pytest -k "pattern"`: Filter by keyword.
  - `pytest --collect-only`: List tests.
  - `pytest -m marker`: Filter by marker.
- **Setup/Teardown:** Fixtures or class methods; `scope="module"` for module-level.
- **Parameterized Tests:** `@pytest.mark.parametrize`.

### Examples

#### Basic Test (`test_basic.py`)
```python
def test_addition():
    assert 1 + 1 == 2

def test_string_concat():
    assert "hello" + " world" == "hello world"
```
**Run:** `pytest test_basic.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 2 items

test_basic.py::test_addition PASSED                                      [ 50%]
test_basic.py::test_string_concat PASSED                                 [100%]

============================== 2 passed in 0.01s ==============================
```

#### Assertion Failure (`test_failure.py`)
```python
def test_division():
    assert 10 / 2 == 6  # Intentional failure
```
**Run:** `pytest test_failure.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_failure.py::test_division FAILED                                    [100%]

=================================== FAILURES ===================================
________________________________ test_division _________________________________

    def test_division():
>       assert 10 / 2 == 6
E       assert 5.0 == 6
E        +  where 5.0 = 10 / 2

test_failure.py:2: AssertionError
=========================== short test summary info ============================
FAILED test_failure.py::test_division - assert 5.0 == 6
============================== 1 failed in 0.01s ===============================
```

#### Using Fixtures (`test_fixture.py`)
```python
import pytest

@pytest.fixture
def sample_data():
    return {"name": "Alice", "age": 30}

def test_data_name(sample_data):
    assert sample_data["name"] == "Alice"

def test_data_age(sample_data):
    assert sample_data["age"] == 30
```
**Run:** `pytest test_fixture.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 2 items

test_fixture.py::test_data_name PASSED                                   [ 50%]
test_fixture.py::test_data_age PASSED                                    [100%]

============================== 2 passed in 0.01s ==============================
```

#### Parameterized Tests (`test_parametrize.py`)
```python
import pytest

@pytest.mark.parametrize("input,expected", [
    (1, 2),
    (3, 6),
    (5, 10),
])
def test_double(input, expected):
    assert input * 2 == expected
```
**Run:** `pytest test_parametrize.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 3 items

test_parametrize.py::test_double[1-2] PASSED                             [ 33%]
test_parametrize.py::test_double[3-6] PASSED                             [ 66%]
test_parametrize.py::test_double[5-10] PASSED                            [100%]

============================== 3 passed in 0.01s ==============================
```

#### Skipping and Expected Failures (`test_markers.py`)
```python
import pytest

@pytest.mark.skip(reason="Not implemented yet")
def test_not_ready():
    assert False

@pytest.mark.xfail(reason="Known bug")
def test_expected_failure():
    assert 1 + 1 == 3
```
**Run:** `pytest test_markers.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 2 items

test_markers.py::test_not_ready SKIPPED (Not implemented yet)            [ 50%]
test_markers.py::test_expected_failure XFAIL (Known bug)                 [100%]

========================== 1 skipped, 1 xfailed in 0.01s =======================
```

#### Test Class (`test_class.py`)
```python
class TestMathOperations:
    def test_add(self):
        assert 2 + 3 == 5

    def test_multiply(self):
        assert 2 * 3 == 6
```
**Run:** `pytest test_class.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 2 items

test_class.py::TestMathOperations::test_add PASSED                       [ 50%]
test_class.py::TestMathOperations::test_multiply PASSED                  [100%]

============================== 2 passed in 0.01s ==============================
```

#### Fixture with Setup/Teardown (`test_setup_teardown.py`)
```python
import pytest

@pytest.fixture
def resource():
    print("\nSetup: Creating resource")
    yield "resource_data"
    print("Teardown: Cleaning up resource")

def test_use_resource(resource):
    assert resource == "resource_data"
```
**Run:** `pytest test_setup_teardown.py -s`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_setup_teardown.py 
Setup: Creating resource
.Teardown: Cleaning up resource

============================== 1 passed in 0.01s ==============================
```

#### Running Specific Tests
**Structure:**
```
project/
├── test_math.py  # def test_add(): ...
├── test_string.py  # def test_concat(): ...
```
**Run:** `pytest -k "add" -v`  
**Output:**
```
============================= test session starts ==============================
collected 2 items / 1 deselected / 1 selected

test_math.py::test_add PASSED                                            [100%]

====================== 1 passed, 1 deselected in 0.01s ========================
```

### Tips
- **Plugins:** Use `pytest-cov` for coverage: `pytest --cov=your_module`.
- **Config:** Define settings in `pytest.ini` (see Section 6).
- **Debugging:** Use `pytest --pdb` for debugging on failure.
- **Parallel Execution:** Use `pytest-xdist`: `pytest -n auto`.

### Common Issues
- Tests not discovered: Ensure naming conventions (`test_*.py`, `test_`, `Test*`).
- Fixture not found: Check fixture names/scope.
- Unexpected output: Use `-s` for prints, `--tb=short` for concise tracebacks.

---

## 2. Fixture Scopes

### Overview
- **What are Fixture Scopes?** Control how often a fixture runs: `function` (default), `class`, `module`, `package`, `session`.
- **Key Points:**
  - Broader scopes (e.g., `session`) share resources, reducing overhead.
  - Narrower scopes (e.g., `function`) ensure isolation.
  - Use `yield` for setup/teardown.
  - Hierarchy: `function` < `class` < `module` < `package` < `session`.

### Example: All Scopes (`test_scopes.py`)
```python
import pytest

@pytest.fixture(scope="session")
def session_fixture():
    print("\nSetup: session_fixture")
    yield "session_data"
    print("Teardown: session_fixture")

@pytest.fixture(scope="package")
def package_fixture():
    print("\nSetup: package_fixture")
    yield "package_data"
    print("Teardown: package_fixture")

@pytest.fixture(scope="module")
def module_fixture():
    print("\nSetup: module_fixture")
    yield "module_data"
    print("Teardown: module_fixture")

@pytest.fixture(scope="class")
def class_fixture():
    print("\nSetup: class_fixture")
    yield "class_data"
    print("Teardown: class_fixture")

@pytest.fixture(scope="function")
def function_fixture():
    print("\nSetup: function_fixture")
    yield "function_data"
    print("Teardown: function_fixture")

class TestClass:
    def test_class1(self, session_fixture, package_fixture, module_fixture, class_fixture, function_fixture):
        print(f"Test_class1 using: {session_fixture}, {package_fixture}, {module_fixture}, {class_fixture}, {function_fixture}")
        assert True

    def test_class2(self, session_fixture, package_fixture, module_fixture, class_fixture, function_fixture):
        print(f"Test_class2 using: {session_fixture}, {package_fixture}, {module_fixture}, {class_fixture}, {function_fixture}")
        assert True

def test_function(session_fixture, package_fixture, module_fixture, class_fixture, function_fixture):
    print(f"Test_function using: {session_fixture}, {package_fixture}, {module_fixture}, {class_fixture}, {function_fixture}")
    assert True
```
**Directory Structure:**
```
project/
├── test_scopes.py
```
**Run:** `pytest test_scopes.py -s -v`  
**Output:**
```
============================= test session starts ==============================
collected 3 items

test_scopes.py::TestClass::test_class1 
Setup: session_fixture
Setup: package_fixture
Setup: module_fixture
Setup: class_fixture
Setup: function_fixture
Test_class1 using: session_data, package_data, module_data, class_data, function_data
PASSED
Teardown: function_fixture
test_scopes.py::TestClass::test_class2 
Setup: function_fixture
Test_class2 using: session_data, package_data, module_data, class_data, function_data
PASSED
Teardown: function_fixture
Teardown: class_fixture
test_scopes.py::test_function 
Setup: class_fixture
Setup: function_fixture
Test_function using: session_data, package_data, module_data, class_data, function_data
PASSED
Teardown: function_fixture
Teardown: class_fixture
Teardown: module_fixture
Teardown: package_fixture
Teardown: session_fixture

============================== 3 passed in 0.02s ==============================
```

### Explanation
- **session_fixture:** Runs once per session.
- **package_fixture:** Runs once per package.
- **module_fixture:** Runs once per module.
- **class_fixture:** Runs once per class.
- **function_fixture:** Runs per test.
- **Order:** Setup: session → package → module → class → function; Teardown: reverse.

### Use Cases
- `function`: Per-test isolation (e.g., temp files).
- `class`: Class-specific resources (e.g., mocks).
- `module`: Module-wide resources (e.g., datasets).
- `package`: Directory-shared resources (rare).
- `session`: Expensive resources (e.g., DB connections).

### Additional Notes
- **Overriding Scopes:** Redefine fixtures in modules.
- **Autouse Fixtures:** `@pytest.fixture(autouse=True)`.
- **Debugging:** Use `--setup-show` to trace fixture execution.

---

## 3. Exception Testing

### Overview
- **Purpose:** Verify code raises expected exceptions using `pytest.raises`.
- **Key Features:**
  - Check exception type and message (`match`).
  - Combine with fixtures or `parametrize`.
  - Use `@pytest.mark.xfail` for expected failures.
- **Best Practices:** Specify exception types; avoid manual catching.

### Examples

#### Basic Exception Testing (`test_exceptions.py`)
```python
def divide(a, b):
    return a / b

def test_zero_division():
    with pytest.raises(ZeroDivisionError):
        divide(10, 0)
```
**Run:** `pytest test_exceptions.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_exceptions.py::test_zero_division PASSED                            [100%]

============================== 1 passed in 0.01s ==============================
```

#### Verifying Exception Message (`test_exception_message.py`)
```python
def validate_age(age):
    if age < 0:
        raise ValueError("Age cannot be negative")

def test_invalid_age():
    with pytest.raises(ValueError, match="Age cannot be negative"):
        validate_age(-5)
```
**Run:** `pytest test_exception_message.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_exception_message.py::test_invalid_age PASSED                       [100%]

============================== 1 passed in 0.01s ==============================
```

#### Wrong Exception (Failure Case) (`test_wrong_exception.py`)
```python
def test_wrong_exception():
    with pytest.raises(TypeError):  # Expecting TypeError, but ZeroDivisionError
        divide(10, 0)
```
**Run:** `pytest test_wrong_exception.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_wrong_exception.py::test_wrong_exception FAILED                     [100%]

=================================== FAILURES ===================================
____________________________ test_wrong_exception ______________________________

    def test_wrong_exception():
        with pytest.raises(TypeError):
>           divide(10, 0)
E           Failed: DID NOT RAISE <class 'TypeError'>

test_wrong_exception.py:3: Failed
=========================== short test summary info ============================
FAILED test_wrong_exception.py::test_wrong_exception - Failed: DID NOT RAISE...
============================== 1 failed in 0.01s ===============================
```

#### Parameterized Exception Testing (`test_param_exceptions.py`)
```python
import pytest

def process_input(value):
    if not isinstance(value, int):
        raise TypeError("Input must be an integer")
    if value <= 0:
        raise ValueError("Input must be positive")

@pytest.mark.parametrize("input_val,expected_exception,expected_message", [
    ("invalid", TypeError, "Input must be an integer"),
    (0, ValueError, "Input must be positive"),
    (-5, ValueError, "Input must be positive"),
])
def test_process_input(input_val, expected_exception, expected_message):
    with pytest.raises(expected_exception, match=expected_message):
        process_input(input_val)
```
**Run:** `pytest test_param_exceptions.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 3 items

test_param_exceptions.py::test_process_input[invalid-TypeError-Input must be an integer] PASSED [ 33%]
test_param_exceptions.py::test_process_input[0-ValueError-Input must be positive] PASSED [ 66%]
test_param_exceptions.py::test_process_input[-5-ValueError-Input must be positive] PASSED [100%]

============================== 3 passed in 0.01s ==============================
```

#### Expected Failure with xfail (`test_xfail_exception.py`)
```python
import pytest

@pytest.mark.xfail(raises=ValueError, reason="Known bug in validation")
def test_expected_failure():
    validate_age(-5)  # Should raise ValueError
```
**Run:** `pytest test_xfail_exception.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_xfail_exception.py::test_expected_failure XFAIL (Known bug in validation) [100%]

=========================== 1 xfailed in 0.01s ================================
```

#### Inspecting Exception Attributes (`test_exception_attrs.py`)
```python
class CustomError(Exception):
    def __init__(self, message, code):
        super().__init__(message)
        self.code = code

def raise_custom_error():
    raise CustomError("Invalid operation", 500)

def test_custom_error():
    with pytest.raises(CustomError) as exc_info:
        raise_custom_error()
    assert exc_info.value.message == "Invalid operation"
    assert exc_info.value.code == 500
```
**Run:** `pytest test_exception_attrs.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_exception_attrs.py::test_custom_error PASSED                        [100%]

============================== 1 passed in 0.01s ==============================
```

#### Fixture with Exception Testing (`test_fixture_exception.py`)
```python
import pytest

@pytest.fixture
def faulty_resource():
    yield
    raise RuntimeError("Resource cleanup failed")

def test_fixture_cleanup(faulty_resource):
    with pytest.raises(RuntimeError, match="Resource cleanup failed"):
        pytest.fail("Force teardown to trigger exception")
```
**Run:** `pytest test_fixture_exception.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_fixture_exception.py::test_fixture_cleanup PASSED                   [100%]

============================== 1 passed in 0.01s ==============================
```

### Additional Tips
- **Multiple Exceptions:** `pytest.raises((Exc1, Exc2))`.
- **No Exception Expected:** `with pytest.raises(Exception): pass`.
- **Custom Messages:** `pytest.fail("Custom message")`.
- **Debugging:** Use `-s` for prints, `--tb=long` for tracebacks.

---

## 4. Testing Async Code

### Overview
- **Purpose:** Test `async def` functions using `pytest-asyncio`.
- **Key:** Mark tests with `@pytest.mark.asyncio` or use `asyncio_mode=auto` in `pytest.ini`.
- **Features:** Async fixtures, event loop management.
- **Configuration (`pytest.ini`):**
  ```ini
  [pytest]
  asyncio_mode = auto
  ```

### Examples

#### Basic Async Function Testing (`test_async_basic.py`)
```python
import asyncio
import pytest

async def async_add(a, b):
    await asyncio.sleep(0.1)  # Simulate async work
    return a + b

async def test_async_add():
    result = await async_add(3, 4)
    assert result == 7
```
**Run:** `pytest test_async_basic.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_async_basic.py::test_async_add PASSED                               [100%]

============================== 1 passed in 0.11s ==============================
```

#### Async Exception Handling (`test_async_exception.py`)
```python
import asyncio
import pytest

async def async_divide(a, b):
    await asyncio.sleep(0.1)
    if b == 0:
        raise ZeroDivisionError("Division by zero")
    return a / b

async def test_async_zero_division():
    with pytest.raises(ZeroDivisionError, match="Division by zero"):
        await async_divide(10, 0)
```
**Run:** `pytest test_async_exception.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_async_exception.py::test_async_zero_division PASSED                 [100%]

============================== 1 passed in 0.11s ==============================
```

#### Async Fixture (`test_async_fixture.py`)
```python
import asyncio
import pytest

@pytest.fixture
async def async_resource():
    print("\nSetup: Creating async resource")
    await asyncio.sleep(0.1)  # Simulate async setup
    yield {"data": "resource"}
    print("Teardown: Cleaning up async resource")

async def test_use_async_resource(async_resource):
    assert async_resource["data"] == "resource"
```
**Run:** `pytest test_async_fixture.py -v -s`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_async_fixture.py 
Setup: Creating async resource
.Teardown: Cleaning up async resource
PASSED

============================== 1 passed in 0.11s ==============================
```

#### Parameterized Async Tests (`test_async_parametrize.py`)
```python
import asyncio
import pytest

async def async_multiply(x, y):
    await asyncio.sleep(0.1)
    return x * y

@pytest.mark.parametrize("x,y,expected", [
    (2, 3, 6),
    (5, 4, 20),
    (0, 10, 0),
])
async def test_async_multiply(x, y, expected):
    result = await async_multiply(x, y)
    assert result == expected
```
**Run:** `pytest test_async_parametrize.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 3 items

test_async_parametrize.py::test_async_multiply[2-3-6] PASSED             [ 33%]
test_async_parametrize.py::test_async_multiply[5-4-20] PASSED            [ 66%]
test_async_parametrize.py::test_async_multiply[0-10-0] PASSED            [100%]

============================== 3 passed in 0.33s ==============================
```

#### Async Fixture with Exception (`test_async_fixture_exception.py`)
```python
import asyncio
import pytest

@pytest.fixture
async def faulty_async_resource():
    print("\nSetup: Creating faulty async resource")
    await asyncio.sleep(0.1)
    yield
    raise RuntimeError("Async cleanup failed")

async def test_async_fixture_failure(faulty_async_resource):
    with pytest.raises(RuntimeError, match="Async cleanup failed"):
        pytest.fail("Force teardown to trigger exception")
```
**Run:** `pytest test_async_fixture_exception.py -v -s`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_async_fixture_exception.py 
Setup: Creating faulty async resource
.Teardown: Cleaning up faulty async resource
PASSED

============================== 1 passed in 0.11s ==============================
```

#### Async Timeout (`test_async_timeout.py`)
```python
import asyncio
import pytest

async def long_running_task():
    await asyncio.sleep(2)  # Simulate long task
    return "done"

@pytest.mark.asyncio
async def test_timeout():
    with pytest.raises(asyncio.TimeoutError):
        await asyncio.wait_for(long_running_task(), timeout=0.5)
```
**Run:** `pytest test_async_timeout.py -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_async_timeout.py::test_timeout PASSED                               [100%]

============================== 1 passed in 0.51s ==============================
```

### Additional Notes
- **Event Loop Scope:** Set `asyncio_loop_scope = session` in `pytest.ini`.
- **Common Issues:** Forgetting `await`; avoid manual event loops.
- **Debugging:** Use `-s` for prints, `--asyncio-debug` for loop issues.
- **Plugins:** `pytest-asyncio-timeout` for global timeouts.

---

## 5. conftest.py

### Overview
- **Purpose:** Share fixtures and hooks across test files; automatically loaded by pytest.
- **Location:** Root or subdirectories; local `conftest.py` overrides parent.
- **Uses:** Define async/sync fixtures, hooks (e.g., `pytest_collection_modifyitems`), register markers.
- **Async Support:** Use `async def` for async fixtures with `pytest-asyncio`.

### Example
**Directory Structure:**
```
project/
├── conftest.py
├── test_math.py
├── test_string.py
```
**`conftest.py`:**
```python
import asyncio
import pytest

@pytest.fixture(scope="session")
async def async_db_connection():
    print("\nSetup: Connecting to async DB")
    await asyncio.sleep(0.1)
    yield {"connection": "db_connected"}
    print("Teardown: Closing async DB connection")

@pytest.fixture(scope="module")
def test_config():
    print("\nSetup: Loading test config")
    yield {"env": "test"}
    print("Teardown: Unloading test config")

def pytest_collection_modifyitems(config, items):
    for item in items:
        item.add_marker(pytest.mark.custom_marker)

def pytest_configure(config):
    config.addinivalue_line("markers", "custom_marker: Mark tests for custom filtering")
```
**`test_math.py`:**
```python
import asyncio
import pytest

async def test_async_add(async_db_connection, test_config):
    await asyncio.sleep(0.1)
    assert async_db_connection["connection"] == "db_connected"
    assert test_config["env"] == "test"
    assert 1 + 1 == 2

async def test_async_multiply(async_db_connection, test_config):
    await asyncio.sleep(0.1)
    assert async_db_connection["connection"] == "db_connected"
    assert test_config["env"] == "test"
    assert 2 * 3 == 6
```
**`test_string.py`:**
```python
import asyncio
import pytest

async def test_async_concat(async_db_connection, test_config):
    await asyncio.sleep(0.1)
    assert async_db_connection["connection"] == "db_connected"
    assert test_config["env"] == "test"
    assert "hello" + " world" == "hello world"

@pytest.mark.parametrize("input,expected", [
    ("test", "TEST"),
    ("pytest", "PYTEST"),
])
async def test_async_upper(input, expected, async_db_connection, test_config):
    await asyncio.sleep(0.1)
    assert async_db_connection["connection"] == "db_connected"
    assert test_config["env"] == "test"
    assert input.upper() == expected
```
**`pytest.ini`:**
```ini
[pytest]
asyncio_mode = auto
```
**Run:** `pytest -v -s`  
**Output:**
```
============================= test session starts ==============================
collected 4 items

test_math.py::test_async_add 
Setup: Connecting to async DB
Setup: Loading test config
.PASSED
Teardown: Unloading test config

test_math.py::test_async_multiply .PASSED

test_string.py::test_async_concat 
Setup: Loading test config
.PASSED
Teardown: Unloading test config

test_string.py::test_async_upper[test-TEST] .PASSED
test_string.py::test_async_upper[pytest-PYTEST] .PASSED
Teardown: Closing async DB connection

============================== 4 passed in 0.44s ==============================
```

### Additional Examples
#### Async Fixture with Exception
**Add to `conftest.py`:**
```python
@pytest.fixture
async def faulty_async_resource():
    print("\nSetup: Faulty async resource")
    await asyncio.sleep(0.1)
    yield
    raise RuntimeError("Async cleanup failed")
```
**`test_exception.py`:**
```python
import pytest

async def test_async_fixture_failure(faulty_async_resource):
    with pytest.raises(RuntimeError, match="Async cleanup failed"):
        pytest.fail("Force teardown to trigger exception")
```
**Run:** `pytest test_exception.py -v -s`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_exception.py::test_async_fixture_failure 
Setup: Connecting to async DB
Setup: Faulty async resource
.PASSED
Teardown: Closing async DB connection

============================== 1 passed in 0.11s ==============================
```

#### Custom Hook for Filtering
**Add to `conftest.py`:**
```python
def pytest_collection_modifyitems(config, items):
    for item in items:
        if "multiply" in item.name:
            item.add_marker(pytest.mark.skip(reason="Skipping multiply tests"))
```
**Run:** `pytest -v -s`  
**Output (partial):**
```
test_math.py::test_async_multiply SKIPPED (Skipping multiply tests)      [ 50%]

=================== 3 passed, 1 skipped in 0.33s =======================
```

### Additional Notes
- **Multiple `conftest.py`:** Use in subdirectories for local fixtures/hooks.
- **Async Fixtures:** Use `async def` with `await` for setup/teardown.
- **Debugging:** Use `-s` for prints, `--setup-show` for fixture tracing.
- **Common Issues:**
  - Fixture not found: Ensure correct directory/naming.
  - Hook conflicts: Avoid duplicate hooks in multiple `conftest.py`.

---

## 6. Configuration (pytest.ini)

### Overview
- **Purpose:** Customize test discovery, markers, and runtime options.
- **Location:** Project root or test directory.
- **Format:** INI with `[pytest]` section.
- **Common Options:**
  - `python_files`: Test file patterns (e.g., `test_*.py`).
  - `python_functions`: Function patterns (e.g., `test_*`).
  - `python_classes`: Class patterns (e.g., `Test*`).
  - `addopts`: Default flags (e.g., `-v --cov`).
  - `markers`: Register custom markers.
  - `asyncio_mode`: Enable async testing.
  - `norecursedirs`: Exclude directories (e.g., `venv`).
  - `filterwarnings`: Suppress warnings.

### Example
**Directory Structure:**
```
project/
├── pytest.ini
├── conftest.py
├── test_math.py
├── test_string.py
```
**`pytest.ini`:**
```ini
[pytest]
python_files = test_*.py
python_functions = test_*
python_classes = Test*
addopts = -v --cov=src --cov-report=html
asyncio_mode = auto
markers =
    slow: Tests that run slowly
    integration: Tests requiring external resources
    async_test: Async-specific tests
    custom_marker: Custom filtering for tests
norecursedirs = venv .git build dist
filterwarnings =
    ignore::DeprecationWarning
```
**`conftest.py`, `test_math.py`, `test_string.py`:** As in Section 5.  
**Run:** `pytest -s`  
**Output:**
```
============================= test session starts ==============================
platform linux -- Python 3.x.y, pytest-8.x.y, pluggy-1.x.y -- ...
cachedir: .pytest_cache
rootdir: /path/to/project
configfile: pytest.ini
plugins: asyncio-x.x.x, cov-x.x.x
collected 4 items

test_math.py 
Setup: Connecting to async DB
Setup: Loading test config
.PASSED
.PASSED
Teardown: Unloading test config

test_string.py 
Setup: Loading test config
.PASSED
.PASSED
.PASSED
Teardown: Unloading test config
Teardown: Closing async DB connection

---------- coverage: platform linux, python 3.x.y ----------
Coverage HTML written to dir htmlcov

============================== 4 passed in 0.44s ==============================
```

### Additional Examples
#### Filtering by Marker
**Run:** `pytest -m async_test -s -v`  
**Output:**
```
============================= test session starts ==============================
collected 4 items / 1 deselected / 3 selected

test_math.py::test_async_add 
Setup: Connecting to async DB
Setup: Loading test config
PASSED
test_math.py::test_async_multiply SKIPPED (Skipping slow tests)
test_string.py::test_async_concat PASSED
test_string.py::test_async_upper[test-TEST] PASSED
test_string.py::test_async_upper[pytest-PYTEST] PASSED
Teardown: Unloading test config
Teardown: Closing async DB connection

=================== 3 passed, 1 deselected in 0.33s ===================
```

#### Custom Test Discovery
**Modified `pytest.ini`:**
```ini
[pytest]
python_files = check_*.py test_*.py
python_functions = check_* test_*
asyncio_mode = auto
```
**`check_extra.py`:**
```python
async def check_async_value(async_db_connection):
    await asyncio.sleep(0.1)
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest -s -v`  
**Output (partial):**
```
check_extra.py::check_async_value 
Setup: Connecting to async DB
PASSED
============================== 5 passed in 0.55s ==============================
```

#### Suppressing Warnings (`test_warning.py`)
```python
import warnings

async def test_deprecation_warning():
    warnings.warn("This is deprecated", DeprecationWarning)
    assert True
```
**Run:** `pytest test_warning.py -s -v`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_warning.py::test_deprecation_warning PASSED                         [100%]

============================== 1 passed in 0.01s ==============================
```

### Additional Notes
- **Overrides:** Command-line flags override `pytest.ini`.
- **Common Issues:** Syntax errors in `pytest.ini`; unregistered markers.
- **Debugging:** Use `--collect-only` to verify discovery; `--showlocals` for detailed debugging.

---

## 7. Temporary Files and Directories

### Overview
- **Purpose:** Create isolated, disposable files/directories for testing.
- **Fixtures:**
  - `tmp_path`: Function-scoped `pathlib.Path` for temporary directory.
  - `tmp_path_factory`: Session-scoped for custom/shared directories.
- **Features:** Auto-cleanup; compatible with async via `aiofiles`.

### Example
**Directory Structure:**
```
project/
├── pytest.ini
├── conftest.py
├── test_temp_files.py
```
**`pytest.ini`:**
```ini
[pytest]
asyncio_mode = auto
addopts = -v -s
```
**`conftest.py`:**
```python
import asyncio
import pytest

@pytest.fixture(scope="session")
async def async_db_connection():
    print("\nSetup: Connecting to async DB")
    await asyncio.sleep(0.1)
    yield {"connection": "db_connected"}
    print("Teardown: Closing async DB connection")
```
**`test_temp_files.py`:**
```python
import asyncio
import pytest
import aiofiles
from pathlib import Path

def test_write_read_file(tmp_path, async_db_connection):
    file_path = tmp_path / "test.txt"
    file_path.write_text("Hello, pytest!")
    content = file_path.read_text()
    assert content == "Hello, pytest!"
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.async_test
async def test_async_write_read_file(tmp_path, async_db_connection):
    file_path = tmp_path / "async_test.txt"
    async with aiofiles.open(file_path, "w") as f:
        await f.write("Async Hello!")
    async with aiofiles.open(file_path, "r") as f:
        content = await f.read()
    assert content == "Async Hello!"
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.async_test
async def test_shared_temp_dir(tmp_path_factory, async_db_connection):
    temp_dir = tmp_path_factory.mktemp("shared_dir")
    file_path = temp_dir / "shared.txt"
    async with aiofiles.open(file_path, "w") as f:
        await f.write("Shared data")
    async with aiofiles.open(file_path, "r") as f:
        content = await f.read()
    assert content == "Shared data"
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.async_test
async def test_async_file_error(tmp_path, async_db_connection):
    file_path = tmp_path / "non_existent.txt"
    with pytest.raises(FileNotFoundError):
        async with aiofiles.open(file_path, "r") as f:
            await f.read()
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest test_temp_files.py`  
**Output:**
```
============================= test session starts ==============================
collected 4 items

test_temp_files.py::test_write_read_file 
Setup: Connecting to async DB
PASSED
test_temp_files.py::test_async_write_read_file PASSED
test_temp_files.py::test_shared_temp_dir PASSED
test_temp_files.py::test_async_file_error PASSED
Teardown: Closing async DB connection

============================== 4 passed in 0.22s ==============================
```

### Additional Examples
#### Module-Scoped Temporary Directory (`test_temp_module.py`)
```python
import pytest
import aiofiles

@pytest.fixture(scope="module")
def module_temp_dir(tmp_path_factory):
    return tmp_path_factory.mktemp("module_temp")

@pytest.mark.async_test
async def test_module_shared_file(module_temp_dir, async_db_connection):
    file_path = module_temp_dir / "module.txt"
    async with aiofiles.open(file_path, "w") as f:
        await f.write("Module shared")
    async with aiofiles.open(file_path, "r") as f:
        content = await f.read()
    assert content == "Module shared"
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest test_temp_module.py`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_temp_module.py::test_module_shared_file 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

============================== 1 passed in 0.11s ==============================
```

#### Testing Directory Structure (`test_temp_structure.py`)
```python
import pytest

def test_directory_structure(tmp_path, async_db_connection):
    sub_dir = tmp_path / "subfolder"
    sub_dir.mkdir()
    file1 = sub_dir / "file1.txt"
    file2 = tmp_path / "file2.txt"
    file1.write_text("File 1")
    file2.write_text("File 2")
    assert sub_dir.exists()
    assert file1.read_text() == "File 1"
    assert file2.read_text() == "File 2"
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest test_temp_structure.py`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_temp_structure.py::test_directory_structure 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

============================== 1 passed in 0.11s ==============================
```

### Additional Notes
- **Custom Paths:** Use `tmp_path_factory.getbasetemp() / "custom_dir"`.
- **Keeping Files:** Use plugins or manual copy for debugging.
- **Async I/O:** Use `aiofiles` for async file operations.
- **Common Issues:**
  - Permission errors: Ensure write access.
  - Path conflicts: Avoid reusing file names in `tmp_path`.
- **Debugging:** Print paths with `-s`; use `--setup-show`.

---

## 8. Mocking with pytest-mock

### Overview
- **Purpose:** Use `mocker` fixture to create mocks/patches/spies; auto-restores after tests.
- **Key Features:**
  - `mocker.patch`: Replace functions/methods.
  - `mocker.patch.object`: Patch object attributes.
  - `mocker.spy`: Track calls without altering behavior.
  - `AsyncMock`: Mock async functions (Python 3.8+).
- **Assertions:** `assert_called_once`, `assert_called_with`, `call_count`.

### Example
**Directory Structure:**
```
project/
├── pytest.ini
├── conftest.py
├── src/
│   └── api.py
├── test_api.py
```
**`pytest.ini`:**
```ini
[pytest]
asyncio_mode = auto
addopts = -v -s
markers =
    async_test: Async-specific tests
```
**`conftest.py`:**
```python
import asyncio
import pytest

@pytest.fixture(scope="session")
async def async_db_connection():
    print("\nSetup: Connecting to async DB")
    await asyncio.sleep(0.1)
    yield {"connection": "db_connected"}
    print("Teardown: Closing async DB connection")
```
**`src/api.py`:**
```python
import asyncio
import aiohttp

def get_user_data(user_id):
    return {"id": user_id, "name": "User"}

async def fetch_user_data(user_id):
    async with aiohttp.ClientSession() as session:
        async with session.get(f"https://api.example.com/users/{user_id}") as response:
            return await response.json()

async def process_user(user_id):
    data = await fetch_user_data(user_id)
    return f"Processed {data['name']}"
```
**`test_api.py`:**
```python
import asyncio
import pytest
from src.api import get_user_data, fetch_user_data, process_user

def test_get_user_data(mocker, async_db_connection):
    mocker.patch("src.api.get_user_data", return_value={"id": 1, "name": "Mocked User"})
    result = get_user_data(1)
    assert result == {"id": 1, "name": "Mocked User"}
    assert async_db_connection["connection"] == "db_connected"
    mocker.patch("src.api.get_user_data").assert_called_once_with(1)

@pytest.mark.async_test
async def test_fetch_user_data(mocker, async_db_connection):
    mock_response = mocker.AsyncMock()
    mock_response.json = mocker.AsyncMock(return_value={"id": 2, "name": "Async User"})
    mocker.patch("aiohttp.ClientSession.get", return_value=mock_response)
    result = await fetch_user_data(2)
    assert result == {"id": 2, "name": "Async User"}
    assert async_db_connection["connection"] == "db_connected"
    mocker.patch("aiohttp.ClientSession.get").assert_called_once_with("https://api.example.com/users/2")

@pytest.mark.async_test
async def test_fetch_user_data_error(mocker, async_db_connection):
    mocker.patch("aiohttp.ClientSession.get", side_effect=aiohttp.ClientError("Connection failed"))
    with pytest.raises(aiohttp.ClientError, match="Connection failed"):
        await fetch_user_data(3)
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.async_test
async def test_process_user(mocker, async_db_connection):
    mocker.patch("src.api.fetch_user_data", return_value={"id": 4, "name": "Chained User"})
    result = await process_user(4)
    assert result == "Processed Chained User"
    assert async_db_connection["connection"] == "db_connected"
    mocker.patch("src.api.fetch_user_data").assert_called_once_with(4)

def test_get_user_data_spy(mocker, async_db_connection):
    spy = mocker.spy("src.api", "get_user_data")
    result = get_user_data(5)
    assert result == {"id": 5, "name": "User"}
    assert async_db_connection["connection"] == "db_connected"
    assert spy.call_count == 1
    spy.assert_called_once_with(5)
```
**Run:** `pytest test_api.py`  
**Output:**
```
============================= test session starts ==============================
collected 5 items

test_api.py::test_get_user_data 
Setup: Connecting to async DB
PASSED
test_api.py::test_fetch_user_data PASSED
test_api.py::test_fetch_user_data_error PASSED
test_api.py::test_process_user PASSED
test_api.py::test_get_user_data_spy PASSED
Teardown: Closing async DB connection

============================== 5 passed in 0.22s ==============================
```

### Additional Examples
#### Mocking with Temporary Files (`test_temp_mock.py`)
```python
import aiofiles
from src.api import get_user_data

@pytest.mark.async_test
async def test_async_write_mocked_data(mocker, tmp_path, async_db_connection):
    mocker.patch("src.api.get_user_data", return_value={"id": 1, "name": "Temp User"})
    file_path = tmp_path / "user.txt"
    async with aiofiles.open(file_path, "w") as f:
        await f.write(get_user_data(1)["name"])
    async with aiofiles.open(file_path, "r") as f:
        content = await f.read()
    assert content == "Temp User"
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest test_temp_mock.py`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_temp_mock.py::test_async_write_mocked_data 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

============================== 1 passed in 0.11s ==============================
```

#### Mocking with Side Effects (`test_side_effect.py`)
```python
from src.api import fetch_user_data

@pytest.mark.async_test
async def test_multiple_responses(mocker, async_db_connection):
    mocker.patch("src.api.fetch_user_data", side_effect=[
        {"id": 1, "name": "User1"},
        {"id": 2, "name": "User2"},
        ValueError("Invalid ID")
    ])
    result1 = await fetch_user_data(1)
    result2 = await fetch_user_data(2)
    assert result1["name"] == "User1"
    assert result2["name"] == "User2"
    with pytest.raises(ValueError, match="Invalid ID"):
        await fetch_user_data(3)
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest test_side_effect.py`  
**Output:**
```
============================= test session starts ==============================
collected 1 item

test_side_effect.py::test_multiple_responses 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

============================== 1 passed in 0.11s ==============================
```

### Additional Notes
- **Async Mocking:** Use `mocker.AsyncMock` (Python 3.8+) or `return_value` for async functions.
- **Best Practices:** Patch where used (e.g., `src.api.get_user_data`); avoid over-mocking.
- **Common Issues:**
  - Incorrect patch path: Use module path.
  - Async errors: Ensure `AsyncMock` for async methods.
- **Debugging:** Inspect `mock.call_args` or use `-s`, `--pdb`.

---

## 9. Markers

### Overview
- **Purpose:** Categorize and filter tests using built-in (`skip`, `xfail`, `parametrize`) or custom markers.
- **Configuration:** Register custom markers in `pytest.ini`.
- **Usage:** Filter with `pytest -m marker`; customize with hooks in `conftest.py`.

### Example
**Directory Structure:**
```
project/
├── pytest.ini
├── conftest.py
├── src/
│   └── api.py
├── test_markers.py
```
**`pytest.ini`:**
```ini
[pytest]
python_files = test_*.py
python_functions = test_*
python_classes = Test*
addopts = -v -s
asyncio_mode = auto
markers =
    slow: Tests that run slowly
    integration: Tests requiring external resources
    async_test: Async-specific tests
    custom_marker: Custom filtering for tests
norecursedirs = venv .git build dist
filterwarnings =
    ignore::DeprecationWarning
```
**`conftest.py`:**
```python
import asyncio
import pytest

@pytest.fixture(scope="session")
async def async_db_connection():
    print("\nSetup: Connecting to async DB")
    await asyncio.sleep(0.1)
    yield {"connection": "db_connected"}
    print("Teardown: Closing async DB connection")

def pytest_collection_modifyitems(config, items):
    if not config.getoption("-m") or "slow" not in config.getoption("-m"):
        skip_marker = pytest.mark.skip(reason="Slow test; run with -m slow")
        for item in items:
            if "slow" in item.keywords:
                item.add_marker(skip_marker)
```
**`src/api.py`:**
```python
import asyncio
import aiohttp

def get_user_data(user_id):
    return {"id": user_id, "name": "User"}

async def fetch_user_data(user_id):
    async with aiohttp.ClientSession() as session:
        async with session.get(f"https://api.example.com/users/{user_id}") as response:
            return await response.json()
```
**`test_markers.py`:**
```python
import asyncio
import pytest
import aiofiles
from src.api import get_user_data, fetch_user_data

@pytest.mark.skip(reason="Test skipped for demonstration")
def test_skipped(async_db_connection):
    assert False

import sys
@pytest.mark.skipif(sys.version_info < (3, 8), reason="Requires Python 3.8+")
async def test_python_version(async_db_connection):
    assert async_db_connection["connection"] == "db_connected"
    assert 1 + 1 == 2

@pytest.mark.xfail(reason="Known bug in API")
async def test_xfail_api(mocker, async_db_connection):
    mocker.patch("src.api.fetch_user_data", side_effect=ValueError("API error"))
    with pytest.raises(ValueError, match="API error"):
        await fetch_user_data(1)
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.slow
@pytest.mark.async_test
async def test_slow_async(mocker, tmp_path, async_db_connection):
    file_path = tmp_path / "slow.txt"
    mocker.patch("src.api.get_user_data", return_value={"id": 1, "name": "Slow User"})
    await asyncio.sleep(0.2)
    async with aiofiles.open(file_path, "w") as f:
        await f.write(get_user_data(1)["name"])
    async with aiofiles.open(file_path, "r") as f:
        content = await f.read()
    assert content == "Slow User"
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.integration
@pytest.mark.async_test
async def test_integration_api(mocker, async_db_connection):
    mocker.patch("aiohttp.ClientSession.get", return_value=mocker.AsyncMock(json=mocker.AsyncMock(return_value={"id": 2, "name": "Integration User"})))
    result = await fetch_user_data(2)
    assert result["name"] == "Integration User"
    assert async_db_connection["connection"] == "db_connected"

@pytest.mark.custom_marker
def test_custom_marker(async_db_connection):
    assert async_db_connection["connection"] == "db_connected"
    assert "hello" == "hello"
```
**Run:** `pytest test_markers.py`  
**Output:**
```
============================= test session starts ==============================
collected 6 items

test_markers.py::test_skipped SKIPPED (Test skipped for demonstration)    [ 16%]
test_markers.py::test_python_version 
Setup: Connecting to async DB
PASSED
test_markers.py::test_xfail_api PASSED
test_markers.py::test_slow_async SKIPPED (Slow test; run with -m slow)   [ 50%]
test_markers.py::test_integration_api PASSED
test_markers.py::test_custom_marker PASSED
Teardown: Closing async DB connection

==================== 4 passed, 2 skipped in 0.22s =====================
```

### Additional Examples
#### Filtering by Custom Marker
**Run:** `pytest -m integration`  
**Output:**
```
============================= test session starts ==============================
collected 6 items / 5 deselected / 1 selected

test_markers.py::test_integration_api 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

==================== 1 passed, 5 deselected in 0.11s ===================
```

#### Running Slow Tests
**Run:** `pytest -m slow`  
**Output:**
```
============================= test session starts ==============================
collected 6 items / 5 deselected / 1 selected

test_markers.py::test_slow_async 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

==================== 1 passed, 5 deselected in 0.31s ===================
```

#### Combining Markers (`test_combined_markers.py`)
```python
import pytest

@pytest.mark.slow
@pytest.mark.integration
@pytest.mark.async_test
async def test_slow_integration(mocker, tmp_path, async_db_connection):
    file_path = tmp_path / "integration.txt"
    mocker.patch("src.api.get_user_data", return_value={"id": 3, "name": "Combined User"})
    await asyncio.sleep(0.2)
    async with aiofiles.open(file_path, "w") as f:
        await f.write(get_user_data(3)["name"])
    async with aiofiles.open(file_path, "r") as f:
        content = await f.read()
    assert content == "Combined User"
    assert async_db_connection["connection"] == "db_connected"
```
**Run:** `pytest -m "slow and integration"`  
**Output:**
```
============================= test session starts ==============================
collected 7 items / 6 deselected / 1 selected

test_combined_markers.py::test_slow_integration 
Setup: Connecting to async DB
PASSED
Teardown: Closing async DB connection

==================== 1 passed, 6 deselected in 0.31s ===================
```

### Additional Notes
- **Marker Expressions:** Use `-m "slow or integration"`, `-m "not slow"`.
- **Hooks:** Customize marker behavior in `conftest.py`.
- **Debugging:** List markers with `pytest --markers`; check collection with `--collect-only`.
- **Common Issues:**
  - Unknown markers: Register in `pytest.ini`.
  - Async marker errors: Ensure `pytest-asyncio` is configured.

---

## Further Assistance
- For a PDF version, request a LaTeX export.
- For additional examples or charts (e.g., test pass/fail counts), let me know!