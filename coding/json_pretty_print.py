# Input JSON string
json_str = '{"name": "bob", "age" : 25, "university": "Georgia Institute", "academic-info": { "semesters-gpa": [ 3.75, 3.50, 3.75 ],"course": [ "RelevantCoursework", "AdvancedOS", "DatabaseSystems" ], "major": "computer-science" } }'

# Function to pretty print JSON manually (no json library)
def pretty_print_json(s):
    indent = 0
    result = ""
    in_string = False

    for i, char in enumerate(s):
        if char == '"':
            # Detect quote, but handle escaped quotes inside strings
            if i > 0 and s[i - 1] != '\\':
                in_string = not in_string
            result += char
        elif not in_string:
            if char in ['{', '[']:
                result += char + "\n" + " " * (indent + 4)
                indent += 4
            elif char in ['}', ']']:
                indent -= 4
                result += "\n" + " " * indent + char
            elif char == ',':
                result += char + "\n" + " " * indent
            elif char == ':':
                result += ": "
            elif char in [' ', '\n', '\t']:
                # Skip unnecessary spaces outside strings
                continue
            else:
                result += char
        else:
            result += char

    return result

# Pretty print
print(pretty_print_json(json_str))
