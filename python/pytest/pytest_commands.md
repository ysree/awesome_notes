# Pytest Commands Guide

**Date:** September 21, 2025  
**Author:** Grok (xAI)  
**Time:** 11:17 AM IST  

This guide provides a comprehensive overview of commonly used **pytest commands**, their purposes, and examples, tailored to the context of a pytest project with async testing, multiple `conftest.py` files, and multiple `.ini` files, as described in the referenced pytest guide. The examples reflect the guide’s structure, including async tests, fixtures, markers, and plugins (`pytest-asyncio`, `pytest-mock`, `aiofiles`). Commands are grouped by functionality, with explanations and outputs based on the provided project setup.

---

## Project Context
The examples assume the following directory structure from the pytest guide:

```
project/
├── pytest.ini
├── conftest.py
├── src/
│   └── api.py
├── tests/
│   ├── conftest.py
│   ├── unit/
│   │   ├── pytest.ini
│   │   ├── conftest.py
│   │   ├── test_math.py
│   │   └── test_string.py
│   ├── integration/
│   │   ├── pytest.ini
│   │   ├── conftest.py
│   │   └── test_api.py
```

- **`pytest.ini`**: Configures `asyncio_mode = auto`, markers (`slow`, `integration`, `async_test`, `custom_marker`), and `addopts = -v -s`.
- **`conftest.py`**: Defines fixtures like `async_db_connection` (session scope) and hooks (e.g., marker application).
- **`src/api.py`**: Contains `get_user_data` (sync) and `fetch_user_data` (async) functions.
- **Tests**: Include async tests with fixtures (`async_db_connection`, `test_config`, `unit_data`, `integration_client`), mocks, and markers.
- **Multiple `conftest.py`**: Root, tests, unit, and integration levels provide hierarchical fixtures.
- **Multiple `pytest.ini`**: Root, unit, and integration levels configure suite-specific settings (e.g., coverage for unit tests, marker filtering for integration tests).

All commands are executed in a Python 3.7+ environment with `pytest`, `pytest-asyncio`, `pytest-mock`, and `aiofiles` installed.

---

## Pytest Commands

### 1. Basic Test Execution Commands
These commands run tests with default or customized behavior.

#### `pytest`
- **Purpose**: Run all tests in the current directory and subdirectories, following discovery rules (`test_*.py`, `*_test.py`, `test_*` functions).
- **Explanation**: Discovers and executes tests using settings from the closest `pytest.ini` (e.g., `project/pytest.ini` in the root). Applies fixtures from multiple `conftest.py` files (root, tests, unit, integration) and respects markers (e.g., skips integration tests via `tests/integration/conftest.py` hook).
- **Example**:
  ```bash
  pytest
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items
  tests/unit/test_math.py::test_async_add PASSED                           [ 33%]
  tests/unit/test_string.py::test_async_concat PASSED                      [ 66%]
  tests/integration/test_api.py::test_integration_api SKIPPED (Integration test; run with -m integration) [100%]
  ==================== 2 passed, 1 skipped in 0.22s =====================
  ```
- **Note**: Uses `project/pytest.ini` for markers and `asyncio_mode`. Integration tests are skipped due to the hook in `tests/integration/conftest.py`.

#### `pytest path/to/directory`
- **Purpose**: Run tests in a specific directory.
- **Explanation**: Limits discovery to the specified directory, using the closest `.ini` file (e.g., `tests/unit/pytest.ini` for unit tests). Useful for isolating unit or integration suites.
- **Example**:
  ```bash
  pytest tests/unit
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 2 items
  tests/unit/test_math.py::test_async_add 
  Unit Setup: Mock DB connection
  Tests Setup: Loading test config
  Unit Setup: Preparing unit data
  .PASSED
  Unit Teardown: Cleaning unit data
  Tests Teardown: Unloading test config
  tests/unit/test_string.py::test_async_concat 
  Tests Setup: Loading test config
  Unit Setup: Preparing unit data
  .PASSED
  Unit Teardown: Cleaning unit data
  Tests Teardown: Unloading test config
  Unit Teardown: Closing mock DB connection
  ============================== 2 passed in 0.22s ==============================
  ```
- **Note**: Uses `tests/unit/pytest.ini` (adds coverage) and `tests/unit/conftest.py` (mock DB).

#### `pytest path/to/file.py`
- **Purpose**: Run a single test file.
- **Explanation**: Executes all tests in the specified file, using local `conftest.py` (e.g., `tests/unit/conftest.py`) and the closest `.ini` file.
- **Example**:
  ```bash
  pytest tests/unit/test_math.py
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  tests/unit/test_math.py::test_async_add 
  Unit Setup: Mock DB connection
  Tests Setup: Loading test config
  Unit Setup: Preparing unit data
  .PASSED
  Unit Teardown: Cleaning unit data
  Tests Teardown: Unloading test config
  Unit Teardown: Closing mock DB connection
  ============================== 1 passed in 0.11s ==============================
  ```

#### `pytest -v`
- **Purpose**: Run tests with verbose output.
- **Explanation**: Displays detailed test names and results (PASS, FAIL, SKIP). Included in `addopts` in `project/pytest.ini` for detailed output.
- **Example**:
  ```bash
  pytest -v
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items
  tests/unit/test_math.py::test_async_add PASSED                           [ 33%]
  tests/unit/test_string.py::test_async_concat PASSED                      [ 66%]
  tests/integration/test_api.py::test_integration_api SKIPPED (Integration test; run with -m integration) [100%]
  ==================== 2 passed, 1 skipped in 0.22s =====================
  ```

#### `pytest -s`
- **Purpose**: Disable output capturing to show print statements.
- **Explanation**: Displays print statements from tests and fixtures (e.g., `async_db_connection` setup/teardown in the guide). Included in `addopts` in `project/pytest.ini`.
- **Example**:
  ```bash
  pytest tests/unit/test_math.py -s
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  Unit Setup: Mock DB connection
  Tests Setup: Loading test config
  Unit Setup: Preparing unit data
  tests/unit/test_math.py .
  Unit Teardown: Cleaning unit data
  Tests Teardown: Unloading test config
  Unit Teardown: Closing mock DB connection
  ============================== 1 passed in 0.11s ==============================
  ```

### 2. Filtering and Selecting Tests
These commands filter tests by name, marker, or specific test methods.

#### `pytest -k "expression"`
- **Purpose**: Filter tests by a keyword expression matching test names or paths.
- **Explanation**: Matches test function/class names or file paths. In the guide, used to run specific tests like `test_add` (Section 1).
- **Example**:
  ```bash
  pytest -k "add" -v
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items / 2 deselected / 1 selected
  tests/unit/test_math.py::test_async_add PASSED                           [100%]
  ==================== 1 passed, 2 deselected in 0.11s ===================
  ```
- **Note**: Selects tests containing “add” (e.g., `test_async_add`).

#### `pytest -m marker`
- **Purpose**: Run tests with a specific marker.
- **Explanation**: Filters by markers defined in `pytest.ini` (e.g., `unit`, `integration`, `slow`). In the guide, integration tests are skipped unless `-m integration` is used (Section 9).
- **Example**:
  ```bash
  pytest -m integration -v
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items / 2 deselected / 1 selected
  tests/integration/test_api.py::test_integration_api 
  Root Setup: Connecting to async DB
  Tests Setup: Loading test config
  Integration Setup: Starting client
  PASSED
  Integration Teardown: Stopping client
  Tests Teardown: Unloading test config
  Root Teardown: Closing async DB connection
  ==================== 1 passed, 2 deselected in 0.11s ===================
  ```
- **Note**: Uses `tests/integration/pytest.ini` if run from `tests/integration`, focusing on `integration` marker.

#### `pytest -m "expression"`
- **Purpose**: Run tests with combined marker expressions (e.g., `and`, `or`, `not`).
- **Explanation**: Enables complex filtering, as shown in the guide’s combined markers example (Section 9).
- **Example**:
  ```bash
  pytest -m "unit and async_test" -v
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items / 1 deselected / 2 selected
  tests/unit/test_math.py::test_async_add PASSED                           [ 50%]
  tests/unit/test_string.py::test_async_concat PASSED                      [100%]
  ==================== 2 passed, 1 deselected in 0.22s ===================
  ```

#### `pytest path/to/file.py::TestClass::test_method`
- **Purpose**: Run a specific test method in a class or function.
- **Explanation**: Targets a single test, useful for debugging. Modify `test_math.py` to include a class for this example:
  ```python
  class TestMath:
      async def test_async_add(self, async_db_connection, test_config, unit_data):
          await asyncio.sleep(0.1)
          assert async_db_connection["connection"] == "unit_mock_db"
          assert 1 + 1 == 2
  ```
- **Example**:
  ```bash
  pytest tests/unit/test_math.py::TestMath::test_async_add -v
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  tests/unit/test_math.py::TestMath::test_async_add PASSED                 [100%]
  ============================== 1 passed in 0.11s ==============================
  ```

### 3. Debugging and Inspection Commands
These commands aid in debugging or inspecting the test suite.

#### `pytest --collect-only`
- **Purpose**: List all tests without running them.
- **Explanation**: Verifies test discovery, showing which tests are collected and which `conftest.py`/`pytest.ini` files are used.
- **Example**:
  ```bash
  pytest --collect-only
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items
  <Module tests/unit/test_math.py>
    <Function test_async_add>
  <Module tests/unit/test_string.py>
    <Function test_async_concat>
  <Module tests/integration/test_api.py>
    <Function test_integration_api>
  =========================== 3 tests collected in 0.01s =========================
  ```

#### `pytest --pdb`
- **Purpose**: Launch the Python debugger (`pdb`) on test failures.
- **Explanation**: Useful for debugging failing async tests (e.g., `test_async_add` in the guide). Modify `test_math.py` to fail:
  ```python
  async def test_async_add(async_db_connection, test_config, unit_data):
      await asyncio.sleep(0.1)
      assert 1 + 1 == 3  # Intentional failure
  ```
- **Example**:
  ```bash
  pytest tests/unit/test_math.py --pdb
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  tests/unit/test_math.py F
  ============================== FAILURES ==============================
  ___________________________ test_async_add ___________________________
  assert 2 == 3
  > import pdb; pdb.set_trace()
  ```

#### `pytest --tb=style`
- **Purpose**: Control traceback style (`auto`, `long`, `short`, `line`, `no`).
- **Explanation**: Adjusts failure report verbosity. The guide suggests `--tb=short` for concise tracebacks (Section 1).
- **Example**:
  ```bash
  pytest tests/unit/test_math.py --tb=short
  ```
- **Output** (with failure):
  ```
  ============================= test session starts ==============================
  collected 1 item
  tests/unit/test_math.py::test_async_add FAILED                           [100%]
  ============================== FAILURES ==============================
  ___________________________ test_async_add ___________________________
  assert 2 == 3
  tests/unit/test_math.py:5: AssertionError
  ========================= short test summary info =========================
  FAILED tests/unit/test_math.py::test_async_add - assert 2 == 3
  ============================== 1 failed in 0.11s ==============================
  ```

#### `pytest --setup-show`
- **Purpose**: Display fixture setup and teardown.
- **Explanation**: Traces fixture execution (e.g., `async_db_connection`, `test_config`, `unit_data`), as used in the guide’s fixture scope examples (Section 2).
- **Example**:
  ```bash
  pytest tests/unit/test_math.py --setup-show
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  tests/unit/test_math.py::test_async_add
      SETUP    S async_db_connection
      SETUP    M test_config
      SETUP    F unit_data
      tests/unit/test_math.py::test_async_add (fixtures used: async_db_connection, test_config, unit_data) PASSED
      TEARDOWN F unit_data
      TEARDOWN M test_config
      TEARDOWN S async_db_connection
  ============================== 1 passed in 0.11s ==============================
  ```

### 4. Coverage and Reporting Commands
These commands generate coverage or test result reports.

#### `pytest --cov=module`
- **Purpose**: Measure code coverage for a module or package.
- **Explanation**: Requires `pytest-cov` plugin. In the guide, `tests/unit/pytest.ini` includes `--cov=src` for unit tests (Section 6).
- **Example**:
  ```bash
  pytest tests/unit --cov=src
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 2 items
  tests/unit/test_math.py PASSED
  tests/unit/test_string.py PASSED
  ---------- coverage: platform linux, python 3.x.y ----------
  Name            Stmts   Miss  Cover
  -----------------------------------
  src/api.py         10      2    80%
  -----------------------------------
  TOTAL              10      2    80%
  ============================== 2 passed in 0.22s ==============================
  ```

#### `pytest --cov-report=html`
- **Purpose**: Generate an HTML coverage report.
- **Explanation**: Creates a `htmlcov/` directory with detailed coverage. Used in `tests/unit/pytest.ini` in the guide.
- **Example**:
  ```bash
  pytest tests/unit --cov=src --cov-report=html
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 2 items
  tests/unit/test_math.py PASSED
  tests/unit/test_string.py PASSED
  ---------- coverage: platform linux, python 3.x.y ----------
  Coverage HTML written to dir htmlcov
  ============================== 2 passed in 0.22s ==============================
  ```

#### `pytest --junitxml=path`
- **Purpose**: Generate a JUnit XML report for CI/CD integration.
- **Explanation**: Useful for tools like Jenkins or GitHub Actions to parse test results.
- **Example**:
  ```bash
  pytest --junitxml=report.xml
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items
  tests/unit/test_math.py PASSED
  tests/unit/test_string.py PASSED
  tests/integration/test_api.py SKIPPED
  generated xml file: report.xml
  ==================== 2 passed, 1 skipped in 0.22s =====================
  ```

### 5. Async-Specific Commands
These commands support async testing, as used in the guide with `pytest-asyncio` (Section 4).

#### `pytest --asyncio-mode=auto`
- **Purpose**: Enable automatic detection of async tests.
- **Explanation**: Allows `async def` tests to run without explicit `@pytest.mark.asyncio`, as configured in the guide’s `pytest.ini`.
- **Example**:
  ```bash
  pytest tests/unit/test_math.py --asyncio-mode=auto
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  tests/unit/test_math.py::test_async_add PASSED
  ============================== 1 passed in 0.11s ==============================
  ```

#### `pytest --asyncio-debug`
- **Purpose**: Enable debug logging for asyncio event loops.
- **Explanation**: Helps debug async issues (e.g., unclosed loops in `async_db_connection` fixture).
- **Example**:
  ```bash
  pytest tests/unit/test_math.py --asyncio-debug
  ```
- **Output**: Similar to standard run but includes asyncio debug logs if issues occur.

### 6. Configuration and Customization Commands
These commands interact with `pytest.ini` or `conftest.py` settings.

#### `pytest --ini=path/to/pytest.ini`
- **Purpose**: Specify a custom `.ini` file.
- **Explanation**: Forces a specific configuration file, overriding default lookup. Useful with multiple `.ini` files (e.g., `tests/unit/pytest.ini` for coverage).
- **Example**:
  ```bash
  pytest --ini=tests/unit/pytest.ini
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 2 items
  tests/unit/test_math.py PASSED
  tests/unit/test_string.py PASSED
  Coverage HTML written to dir htmlcov
  ============================== 2 passed in 0.22s ==============================
  ```
- **Note**: Uses `tests/unit/pytest.ini`, enabling coverage and excluding integration tests.

#### `pytest --markers`
- **Purpose**: List all available markers.
- **Explanation**: Shows markers from `pytest.ini` (e.g., `slow`, `integration`, `async_test`) and built-in markers (e.g., `skip`, `xfail`).
- **Example**:
  ```bash
  pytest --markers
  ```
- **Output** (partial):
  ```
  @pytest.mark.slow: Tests that run slowly
  @pytest.mark.integration: Tests requiring external resources
  @pytest.mark.async_test: Async-specific tests
  @pytest.mark.custom_marker: Custom filtering for tests
  @pytest.mark.skip(reason=None): skip test
  @pytest.mark.xfail(condition=None, reason=None, ...): mark test as expected to fail
  ...
  ```

#### `pytest --fixtures`
- **Purpose**: List all available fixtures.
- **Explanation**: Shows fixtures from `conftest.py` files (e.g., `async_db_connection`, `test_config`, `unit_data`, `integration_client`) and plugins.
- **Example**:
  ```bash
  pytest --fixtures
  ```
- **Output** (partial):
  ```
  async_db_connection
      project/conftest.py:4: Connecting to async DB (session scope)
  test_config
      tests/conftest.py:4: Loading test config (module scope)
  unit_data
      tests/unit/conftest.py:4: Preparing unit data (function scope)
  integration_client
      tests/integration/conftest.py:4: Starting client (function scope)
  tmp_path
      built-in: A temporary directory path object
  mocker
      pytest-mock: Mocking fixture
  ```

### 7. Parallel and Distributed Testing
These commands use `pytest-xdist` for faster execution.

#### `pytest -n num`
- **Purpose**: Run tests in parallel using multiple processes.
- **Explanation**: Speeds up execution, especially for async tests with delays (e.g., `asyncio.sleep` in the guide). Requires `pytest-xdist`.
- **Example**:
  ```bash
  pytest -n 2
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items
  tests/unit/test_math.py . [ 33%]
  tests/unit/test_string.py . [ 66%]
  tests/integration/test_api.py s [100%]
  ==================== 2 passed, 1 skipped in 0.15s =====================
  ```
- **Note**: Uses two processes, reducing runtime for async delays.

#### `pytest -n auto`
- **Purpose**: Run tests in parallel using all available CPU cores.
- **Explanation**: Optimizes parallel execution based on system resources.
- **Example**:
  ```bash
  pytest -n auto
  ```
- **Output**: Similar to `-n num` but uses all available cores.

### 8. Miscellaneous Commands

#### `pytest --version`
- **Purpose**: Display pytest and plugin versions.
- **Explanation**: Verifies installed versions (e.g., `pytest-asyncio`, `pytest-mock`).
- **Example**:
  ```bash
  pytest --version
  ```
- **Output**:
  ```
  pytest 8.x.y
  plugins: asyncio-x.x.x, mock-x.x.x, cov-x.x.x, xdist-x.x.x
  ```

#### `pytest --help`
- **Purpose**: Show all pytest command-line options and configuration settings.
- **Explanation**: Lists available flags and plugin options.
- **Example**:
  ```bash
  pytest --help
  ```
- **Output** (partial):
  ```
  usage: pytest [options] [file_or_dir] [file_or_dir] [...]
  ...
  -v, --verbose         increase verbosity
  -k EXPRESSION        only run tests which match the given substring expression
  -m MARKEXPR          only run tests matching given mark expression
  --tb=TBSTYLE         traceback print mode (auto/long/short/line/no)
  ...
  ```

#### `pytest --durations=N`
- **Purpose**: Show the N slowest tests or setup/teardown steps.
- **Explanation**: Identifies slow tests (e.g., `test_slow_async` in the guide’s Section 9).
- **Example**:
  ```bash
  pytest -m slow --durations=3
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 3 items / 2 deselected / 1 selected
  tests/markers/test_markers.py::test_slow_async PASSED
  ==================== 1 passed, 2 deselected in 0.31s ===================
  ============================= slowest durations ==============================
  0.20s call     tests/markers/test_markers.py::test_slow_async
  0.10s setup    tests/markers/test_markers.py::test_slow_async
  0.01s teardown tests/markers/test_markers.py::test_slow_async
  ```

### Integration with Multiple `conftest.py` and `.ini` Files
- **Multiple `conftest.py` Files**: Commands like `pytest tests/unit` load `tests/unit/conftest.py` (mock DB), `tests/conftest.py` (test config), and `project/conftest.py` (root hooks). Similarly, `pytest tests/integration` loads `tests/integration/conftest.py` (integration client) and parent `conftest.py` files.
- **Multiple `.ini` Files**: Commands like `pytest --ini=tests/unit/pytest.ini` force unit-specific settings (e.g., coverage). Running from `tests/integration` uses `tests/integration/pytest.ini`, limiting to integration tests.
- **Example**:
  ```bash
  cd tests/integration
  pytest -m integration
  ```
- **Output**:
  ```
  ============================= test session starts ==============================
  collected 1 item
  test_api.py::test_integration_api PASSED
  ============================== 1 passed in 0.11s ==============================
  ```
- **Note**: Uses `tests/integration/pytest.ini` and `tests/integration/conftest.py`.

### Tips for Using Pytest Commands
- **Combine Flags**: Use `pytest -v -s -k "add" -m unit` for verbose, printing, and filtered tests.
- **Debug Async Tests**: Use `-s --asyncio-debug` for async issues (e.g., `async_db_connection`).
- **Check Configuration**: Use `--collect-only` to verify which `.ini` file is active.
- **Optimize Performance**: Use `-n auto` with `pytest-xdist` for large suites.
- **CI/CD Integration**: Combine `--junitxml` and `--cov-report=xml` for pipelines.

### Common Issues and Solutions
- **Tests Not Found**: Verify `python_files` and `python_functions` in `pytest.ini` (e.g., `test_*.py`, `test_*`).
- **Marker Warnings**: Ensure markers are registered in the active `.ini` file (e.g., `markers = unit: Unit tests`).
- **Async Test Failures**: Confirm `asyncio_mode = auto` in `.ini` or use `@pytest.mark.asyncio`.
- **Wrong `.ini` File**: Use `--ini=path` or run from the correct directory.

### Further Assistance
- For additional examples or specific command combinations, request further details.
- To generate a chart of test pass/fail counts or durations, provide data or request a specific visualization.